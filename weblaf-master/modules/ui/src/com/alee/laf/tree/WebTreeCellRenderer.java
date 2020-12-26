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

package com.alee.laf.tree;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.api.ui.*;
import com.alee.extended.label.WebStyledLabel;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.ChildStyleId;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.TextUtils;

import javax.swing.*;
import javax.swing.plaf.TreeUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Default {@link TreeCellRenderer} implementation based on {@link WebStyledLabel}.
 * Unlike {@link javax.swing.tree.DefaultTreeCellRenderer} it has generics for node and tree types.
 * It also contains multiple methods for convenient renderer customization that can be overridden.
 * And since it is based on {@link WebStyledLabel} it retains all of its extra features.
 *
 * @param <N> {@link TreeNode} type
 * @param <C> {@link JTree} type
 * @param <P> {@link TreeNodeParameters} type
 * @author Mikle Garin
 */
public class WebTreeCellRenderer<N extends TreeNode, C extends JTree, P extends TreeNodeParameters<N, C>>
        extends WebStyledLabel implements TreeCellRenderer, Stateful
{
    /**
     * Renderer ID prefix.
     */
    public static final String ID_PREFIX = "WTCR";

    /**
     * Renderer unique ID used to cache tree icons.
     */
    @NotNull
    protected final String id;

    /**
     * Additional renderer decoration states.
     */
    @NotNull
    protected final List<String> states;

    /**
     * Constructs new {@link WebTreeCellRenderer}.
     */
    public WebTreeCellRenderer ()
    {
        super ();
        setName ( "Tree.cellRenderer" );
        id = TextUtils.generateId ( ID_PREFIX );
        states = new ArrayList<String> ( 5 );
    }

    @NotNull
    @Override
    public List<String> getStates ()
    {
        return states;
    }

    /**
     * Updates custom renderer states based on render cycle settings.
     *
     * @param parameters {@link TreeNodeParameters}
     */
    protected void updateStates ( @NotNull final P parameters )
    {
        // Resetting states
        states.clear ();

        // Selection state
        states.add ( parameters.isSelected () ? DecorationState.selected : DecorationState.unselected );

        // Expansion state
        states.add ( parameters.isExpanded () ? DecorationState.expanded : DecorationState.collapsed );

        // Focus state
        if ( parameters.isFocused () )
        {
            states.add ( DecorationState.focused );
        }

        // Leaf state
        if ( parameters.isLeaf () )
        {
            states.add ( DecorationState.leaf );
        }

        // Hover state
        final TreeUI ui = parameters.tree ().getUI ();
        if ( ui instanceof WTreeUI )
        {
            if ( ( ( WTreeUI ) ui ).getHoverRow () == parameters.row () )
            {
                states.add ( DecorationState.hover );
            }
        }

        // Extra states provided by node
        states.addAll ( DecorationUtils.getExtraStates ( parameters.node () ) );
    }

    /**
     * Updates renderer component style identifier.
     *
     * @param parameters {@link TreeNodeParameters}
     */
    protected void updateStyleId ( @NotNull final P parameters )
    {
        StyleId id = null;
        if ( parameters.node () instanceof ChildStyleIdBridge )
        {
            final ChildStyleIdBridge childStyleIdBridge = ( ChildStyleIdBridge ) parameters.node ();
            final ChildStyleId childStyleId = childStyleIdBridge.getChildStyleId ( parameters );
            if ( childStyleId != null )
            {
                id = childStyleId.at ( parameters.tree () );
            }
        }
        else if ( parameters.node () instanceof StyleIdBridge )
        {
            final StyleIdBridge styleIdBridge = ( StyleIdBridge ) parameters.node ();
            final StyleId styleId = styleIdBridge.getStyleId ( parameters );
            if ( styleId != null )
            {
                id = styleId;
            }
        }
        if ( id == null )
        {
            id = StyleId.treeCellRenderer.at ( parameters.tree () );
        }
        setStyleId ( id );
    }

    /**
     * Updating renderer based on the provided settings.
     *
     * @param parameters {@link TreeNodeParameters}
     */
    protected void updateView ( @NotNull final P parameters )
    {
        setEnabled ( enabledForValue ( parameters ) );
        setComponentOrientation ( orientationForValue ( parameters ) );
        setFont ( fontForValue ( parameters ) );
        setForeground ( foregroundForValue ( parameters ) );
        setIcon ( iconForValue ( parameters ) );
        setText ( textForValue ( parameters ) );
    }

    /**
     * Returns whether or not renderer for the specified {@link TreeNode} should be enabled.
     *
     * @param parameters {@link TreeNodeParameters}
     * @return {@code true} if renderer for the specified {@link TreeNode} should be enabled, {@code false} otherwise
     */
    protected boolean enabledForValue ( @NotNull final P parameters )
    {
        return parameters.tree ().isEnabled ();
    }

    /**
     * Returns renderer {@link ComponentOrientation} for the specified {@link TreeNode}.
     *
     * @param parameters {@link TreeNodeParameters}
     * @return renderer {@link ComponentOrientation} for the specified {@link TreeNode}
     */
    @NotNull
    protected ComponentOrientation orientationForValue ( @NotNull final P parameters )
    {
        return parameters.tree ().getComponentOrientation ();
    }

    /**
     * Returns renderer {@link Font} for the specified {@link TreeNode}.
     *
     * @param parameters {@link TreeNodeParameters}
     * @return renderer {@link Font} for the specified {@link TreeNode}
     */
    @Nullable
    protected Font fontForValue ( @NotNull final P parameters )
    {
        return parameters.tree ().getFont ();
    }

    /**
     * Returns renderer foreground color for the specified {@link TreeNode}.
     *
     * @param parameters {@link TreeNodeParameters}
     * @return renderer foreground color for the specified {@link TreeNode}
     */
    @Nullable
    protected Color foregroundForValue ( @NotNull final P parameters )
    {
        final Color foreground;
        if ( parameters.node () instanceof ForegroundBridge )
        {
            final ForegroundBridge foregroundBridge = ( ForegroundBridge ) parameters.node ();
            final Color fg = foregroundBridge.getForeground ( parameters );
            if ( fg != null )
            {
                foreground = fg;
            }
            else
            {
                foreground = parameters.tree ().getForeground ();
            }
        }
        else
        {
            foreground = parameters.tree ().getForeground ();
        }
        return foreground;
    }

    /**
     * Returns renderer icon for the specified {@link TreeNode}.
     *
     * @param parameters {@link TreeNodeParameters}
     * @return renderer icon for the specified {@link TreeNode}
     */
    @Nullable
    protected Icon iconForValue ( @NotNull final P parameters )
    {
        final Icon icon;
        final boolean enabled = enabledForValue ( parameters );
        if ( parameters.node () instanceof IconBridge )
        {
            final IconBridge iconBridge = ( IconBridge ) parameters.node ();
            icon = iconBridge.getIcon ( parameters );
        }
        else
        {
            final boolean root = parameters.tree ().getModel ().getRoot () == parameters.node ();
            if ( root )
            {
                icon = parameters.isExpanded () ? Icons.rootOpen : Icons.root;
            }
            else if ( !parameters.isLeaf () )
            {
                icon = parameters.isExpanded () ? Icons.folderOpen : Icons.folder;
            }
            else
            {
                icon = Icons.leaf;
            }
        }
        return icon == null || enabled ? icon : UIManager.getLookAndFeel ().getDisabledIcon ( this, icon );
    }

    /**
     * Returns renderer text for the specified {@link TreeNode}.
     *
     * @param parameters {@link TreeNodeParameters}
     * @return renderer text for the specified {@link TreeNode}
     */
    @Nullable
    protected String textForValue ( @NotNull final P parameters )
    {
        final String text;
        if ( parameters.node () instanceof TextBridge )
        {
            final TextBridge textBridge = ( TextBridge ) parameters.node ();
            text = textBridge.getText ( parameters );
        }
        else
        {
            text = parameters.tree ().convertValueToText (
                    parameters.node (),
                    parameters.isSelected (),
                    parameters.isExpanded (),
                    parameters.isLeaf (),
                    parameters.row (),
                    parameters.isFocused ()
            );
        }
        return text;
    }

    /**
     * Returns renderer component for the specified {@link TreeNode}.
     * Even though {@link TreeCellRenderer} mentions that it is responsible for rendering DnD drop location - this renderer is not.
     * DnD is handled differently in WebLaF and there are separate tools that do better job at handling DnD operation and its view.
     *
     * @param tree       {@link JTree}
     * @param node       {@link TreeNode}
     * @param isSelected whether or not {@link TreeNode} is selected
     * @param expanded   whether or not {@link TreeNode} is expanded
     * @param leaf       whether or not {@link TreeNode} is leaf
     * @param row        {@link TreeNode} row number
     * @param hasFocus   whether or not {@link TreeNode} has focus
     * @return renderer component for the specified {@link TreeNode}
     */
    @Override
    public Component getTreeCellRendererComponent ( @NotNull final JTree tree, @NotNull final Object node, final boolean isSelected,
                                                    final boolean expanded, final boolean leaf, final int row, final boolean hasFocus )
    {
        // Forming rendering parameters
        final P parameters = getRenderingParameters ( ( C ) tree, ( N ) node, isSelected, expanded, leaf, row, hasFocus );

        // Updating custom states
        updateStates ( parameters );

        // Updating style identifier
        updateStyleId ( parameters );

        // Updating renderer view
        updateView ( parameters );

        // Updating decoration states for this render cycle
        DecorationUtils.fireStatesChanged ( this );

        return this;
    }

    /**
     * Returns {@link TreeNodeParameters}.
     *
     * @param tree       {@link JTree}
     * @param node       {@link TreeNode}
     * @param isSelected whether or not {@link TreeNode} is selected
     * @param expanded   whether or not {@link TreeNode} is expanded
     * @param leaf       whether or not {@link TreeNode} is leaf
     * @param row        {@link TreeNode} row number
     * @param hasFocus   whether or not {@link TreeNode} has focus
     * @return {@link TreeNodeParameters}
     */
    @NotNull
    protected P getRenderingParameters ( @NotNull final C tree, @NotNull final N node, final boolean isSelected,
                                         final boolean expanded, final boolean leaf, final int row, final boolean hasFocus )
    {
        return ( P ) new TreeNodeParameters<N, C> ( tree, node, row, leaf, isSelected, expanded, hasFocus );
    }

    @Override
    public final void validate ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public final void invalidate ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public final void revalidate ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public final void repaint ( final long tm, final int x, final int y, final int width, final int height )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public final void repaint ( @NotNull final Rectangle r )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public final void repaint ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    /**
     * Checks whether or not specified property change should actually be fired.
     * All property fire methods are overridden and made final for performance reasons.
     *
     * @param propertyName changed property name
     * @param oldValue     old property value
     * @param newValue     new property value
     */
    protected void checkPropertyChange ( @NotNull final String propertyName, @Nullable final Object oldValue,
                                         @Nullable final Object newValue )
    {
        if ( Objects.equals ( propertyName, StyleId.STYLE_PROPERTY, StyleId.PARENT_STYLE_PROPERTY,
                AbstractDecorationPainter.DECORATION_STATES_PROPERTY, WebStyledLabel.STYLE_RANGES_PROPERTY,
                WebLookAndFeel.TEXT_PROPERTY, WebLookAndFeel.BORDER_PROPERTY, WebLookAndFeel.MODEL_PROPERTY ) )
        {
            allowPropertyChange ( propertyName, oldValue, newValue );
        }
        else if ( Objects.equals ( propertyName, WebLookAndFeel.FONT_PROPERTY, WebLookAndFeel.FOREGROUND_PROPERTY ) &&
                oldValue != newValue && getClientProperty ( BasicHTML.propertyKey ) != null )
        {
            allowPropertyChange ( propertyName, oldValue, newValue );
        }
    }

    /**
     * Allows property change event to be fired.
     *
     * @param propertyName changed property name
     * @param oldValue     old property value
     * @param newValue     new property value
     */
    protected void allowPropertyChange ( @NotNull final String propertyName, @Nullable final Object oldValue,
                                         @Nullable final Object newValue )
    {
        super.firePropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    protected final void firePropertyChange ( @NotNull final String propertyName, @Nullable final Object oldValue,
                                              @Nullable final Object newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( @NotNull final String propertyName, final byte oldValue, final byte newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( @NotNull final String propertyName, final char oldValue, final char newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( @NotNull final String propertyName, final short oldValue, final short newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( @NotNull final String propertyName, final int oldValue, final int newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( @NotNull final String propertyName, final long oldValue, final long newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( @NotNull final String propertyName, final float oldValue, final float newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( @NotNull final String propertyName, final double oldValue, final double newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( @NotNull final String propertyName, final boolean oldValue, final boolean newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    /**
     * A subclass of {@link WebTreeCellRenderer} that implements {@link javax.swing.plaf.UIResource}.
     * It is used to determine renderer provided by the UI class to properly uninstall it on UI uninstall.
     *
     * @param <N> {@link TreeNode} type
     * @param <C> {@link JTree} type
     * @param <P> {@link TreeNodeParameters} type
     */
    public static final class UIResource<N extends TreeNode, C extends JTree, P extends TreeNodeParameters<N, C>>
            extends WebTreeCellRenderer<N, C, P> implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link WebTreeCellRenderer}.
         */
    }
}