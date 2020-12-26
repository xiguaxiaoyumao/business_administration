/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.extended.inspector;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.extended.behavior.VisibilityBehavior;
import com.alee.extended.tree.ExTreeDataProvider;
import com.alee.extended.tree.WebExTree;
import com.alee.laf.tree.TreeState;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.style.StyleId;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.compare.Filter;
import com.alee.utils.swing.HoverListener;
import com.alee.utils.swing.extensions.KeyEventRunnable;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link WebExTree} representing Swing components structure.
 * It displays and dynamically updates Swing components strcuture for the specified root {@link Component}.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see InterfaceInspector
 * @see WebExTree
 */
public class InterfaceTree extends WebExTree<InterfaceTreeNode>
        implements HoverListener<InterfaceTreeNode>, TreeSelectionListener, Filter<Component>
{
    /**
     * Root {@link Component}.
     * Might be {@code null} if all active windows are being tracked.
     */
    @Nullable
    protected final Component root;

    /**
     * Highlighter for hovered tree element.
     */
    @NotNull
    protected ComponentHighlighter hoverHighlighter;

    /**
     * Highlighters for selected tree elements.
     */
    @NotNull
    protected Map<Component, ComponentHighlighter> selectedHighlighters;

    /**
     * Constructs new interface tree.
     *
     * @param root root component
     */
    public InterfaceTree ( @Nullable final Component root )
    {
        this ( StyleId.auto, root );
    }

    /**
     * Constructs new interface tree.
     *
     * @param id   {@link StyleId}
     * @param root root component
     */
    public InterfaceTree ( @NotNull final StyleId id, @Nullable final Component root )
    {
        super ( id );
        this.root = root;

        // Visual settings
        setVisibleRowCount ( 20 );

        // Custom data provider
        setDataProvider ( createEmptyProvider () );

        // Nodes hover listener
        this.hoverHighlighter = new ComponentHighlighter ();
        addHoverListener ( this );

        // Nodes selection listener
        this.selectedHighlighters = new HashMap<Component, ComponentHighlighter> ( 0 );
        addTreeSelectionListener ( this );

        // Simple selection clearing
        onKeyPress ( Hotkey.ESCAPE, new KeyEventRunnable ()
        {
            @Override
            public void run ( @NotNull final KeyEvent e )
            {
                clearSelection ();
            }
        } );

        // Visibility behavior
        new VisibilityBehavior<InterfaceTree> ( this, true )
        {
            /**
             * Saved {@link TreeState}.
             */
            protected TreeState savedState = null;

            @Override
            protected void displayed ( @NotNull final InterfaceTree tree )
            {
                // Performing update later to allow tree update it's own visibility state
                // Otherwise this might cause update issues whenever tree has itself in it's own structure
                SwingUtilities.invokeLater ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        tree.setDataProvider ( createDataProvider () );
                        tree.setTreeState ( savedState );
                    }
                } );
            }

            @Override
            protected void hidden ( @NotNull final InterfaceTree tree )
            {
                // Performing update later to allow tree update it's own visibility state
                // Otherwise this might cause update issues whenever tree has itself in it's own structure
                SwingUtilities.invokeLater ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        savedState = tree.getTreeState ();
                        tree.setDataProvider ( tree.createEmptyProvider () );
                    }
                } );
            }
        }.install ();
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.interfacetree;
    }

    @Override
    public void setDataProvider ( @NotNull final ExTreeDataProvider dataProvider )
    {
        // Uninstalling node listeners
        // todo Nodes are not properly cleared out of memory
        // todo This is not critical as this is simply a debug tool, but might be worth fixing at some point
        final InterfaceTreeNode rootNode = getNullableRootNode ();
        if ( rootNode != null )
        {
            rootNode.uninstall ();
        }

        // Updating data provider
        super.setDataProvider ( dataProvider );
    }

    /**
     * Returns new dummy {@link InterfaceTreeDataProvider} for empty hidden {@link JLabel}.
     *
     * @return new dummy {@link InterfaceTreeDataProvider} for empty hidden {@link JLabel}
     */
    @NotNull
    protected InterfaceTreeDataProvider createEmptyProvider ()
    {
        return new InterfaceTreeDataProvider ( this, new JLabel () );
    }

    /**
     * Returns new {@link InterfaceTreeDataProvider} for the {@link #root} {@link Component}.
     *
     * @return new {@link InterfaceTreeDataProvider} for the {@link #root} {@link Component}
     */
    @NotNull
    protected InterfaceTreeDataProvider createDataProvider ()
    {
        return new InterfaceTreeDataProvider ( this, root );
    }

    @Override
    public boolean accept ( final Component component )
    {
        final InterfaceTreeDataProvider provider = ( InterfaceTreeDataProvider ) super.getDataProvider ();
        return provider != null && provider.accept ( component );
    }

    @Override
    public void hoverChanged ( @Nullable final InterfaceTreeNode previous, @Nullable final InterfaceTreeNode current )
    {
        // Separating action from the tree hover makes UI more responsive
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                if ( hoverHighlighter.isShowing () )
                {
                    hoverHighlighter.uninstall ();
                }
                final Component currentComponent = current != null ? current.getUserObject () : null;
                if ( currentComponent != null && canHighlight ( currentComponent ) )
                {
                    hoverHighlighter.install ( currentComponent );
                }
            }
        } );
    }

    @Override
    public void valueChanged ( @NotNull final TreeSelectionEvent e )
    {
        // Separating action from the tree selection makes UI more responsive
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                // Selected nodes
                final List<InterfaceTreeNode> selected = getSelectedNodes ();

                // Previous and current highlighters
                final Map<Component, ComponentHighlighter> prevHighlighters = selectedHighlighters;
                selectedHighlighters = new HashMap<Component, ComponentHighlighter> ( selected.size () );

                // Updating displayed highlighters
                for ( final InterfaceTreeNode node : selected )
                {
                    final Component component = node.getUserObject ();
                    if ( component != null )
                    {
                        final ComponentHighlighter prevHighlighter = prevHighlighters.get ( component );
                        if ( prevHighlighter != null )
                        {
                            // Preserving existing highlighter
                            selectedHighlighters.put ( component, prevHighlighter );
                            prevHighlighters.remove ( component );
                        }
                        else if ( canHighlight ( component ) )
                        {
                            // Adding new highlighter
                            final ComponentHighlighter newHighlighter = new ComponentHighlighter ();
                            selectedHighlighters.put ( component, newHighlighter );
                            newHighlighter.install ( component );
                        }
                    }
                }

                // Removing redundant highlighters
                for ( final Map.Entry<Component, ComponentHighlighter> entry : prevHighlighters.entrySet () )
                {
                    entry.getValue ().uninstall ();
                }
            }
        } );
    }

    /**
     * Returns whether or not component can be highlighted.
     *
     * @param component component to be highlighted
     * @return {@code true} if component can be highlighted, {@code false} otherwise
     */
    public boolean canHighlight ( @Nullable final Component component )
    {
        return component != null && component.isShowing () && !( component instanceof Window );
    }

    /**
     * Returns root component.
     *
     * @return root component
     */
    @Nullable
    public Component getRootComponent ()
    {
        final ExTreeDataProvider<InterfaceTreeNode> provider = getDataProvider ();
        return provider != null ? provider.getRoot ().getUserObject () : null;
    }

    /**
     * Sets root component.
     *
     * @param root root component
     */
    public void setRootComponent ( @Nullable final Component root )
    {
        setDataProvider ( new InterfaceTreeDataProvider ( this, root ) );
    }

    /**
     * Navigates tree to the specified component.
     *
     * @param component component to navigate to
     */
    public void navigate ( @Nullable final Component component )
    {
        final InterfaceTreeNode node = component != null ?
                findNode ( Integer.toString ( component.hashCode () ) ) :
                getRootNode ();
        if ( node != null )
        {
            expandNode ( node );
            setSelectedNode ( node );
            scrollToNode ( node, true );
        }
    }

    /**
     * Expands tree to the specified component.
     *
     * @param component component to expand to
     */
    public void expand ( @Nullable final Component component )
    {
        final InterfaceTreeNode node = component != null ?
                findNode ( Integer.toString ( component.hashCode () ) ) :
                getRootNode ();
        if ( node != null )
        {
            expandNode ( node );
        }
    }
}