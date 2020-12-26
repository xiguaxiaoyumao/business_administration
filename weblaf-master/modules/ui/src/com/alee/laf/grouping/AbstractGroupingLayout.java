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

package com.alee.laf.grouping;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.general.Pair;
import com.alee.utils.swing.UnselectableButtonGroup;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract {@link com.alee.laf.grouping.GroupingLayout} that contains most basic features for any final implementation.
 *
 * @author Mikle Garin
 * @see com.alee.laf.grouping.GroupPaneLayout
 * @see com.alee.extended.accordion.AccordionLayout
 * @see com.alee.extended.dock.WebDockablePaneModel
 */
public abstract class AbstractGroupingLayout extends AbstractLayoutManager implements GroupingLayout
{
    /**
     * Whether or not should visually group provided children components.
     * It is always enabled by default but can be disabled if required.
     * Disabling this option will automatically ungroup all components.
     */
    @Nullable
    @XStreamAsAttribute
    protected Boolean group;

    /**
     * Whether or not should group toggle state elements like togglebuttons, radiobuttons or checkboxes.
     * Only elements placed straight within this {@link com.alee.laf.grouping.GroupPane} are grouped.
     */
    @Nullable
    @XStreamAsAttribute
    protected Boolean groupButtons;

    /**
     * Whether or not this button group should allow empty selection state.
     */
    @Nullable
    @XStreamAsAttribute
    protected Boolean unselectableGrouping;

    /**
     * Displayed children decoration sides.
     * It might be useful to disable sides decoration while keeping it between the child components.
     */
    @Nullable
    @XStreamAsAttribute
    protected String sides;

    /**
     * Container children components.
     */
    @Nullable
    protected transient Map<Component, Pair<String, String>> children;

    /**
     * Button group used to group toggle state elements placed within container.
     */
    @Nullable
    protected transient UnselectableButtonGroup buttonGroup;

    @Override
    public boolean isGrouping ()
    {
        return group == null || group;
    }

    /**
     * Sets whether or not components should be visually grouped.
     *
     * @param group whether or not components should be visually grouped
     */
    public void setGroup ( final boolean group )
    {
        if ( isGrouping () != group )
        {
            this.group = group;
            resetDescriptors ();
        }
    }

    /**
     * Returns whether or not should group toggle state elements.
     *
     * @return true if should group toggle state elements, false otherwise
     */
    public boolean isGroupButtons ()
    {
        return groupButtons == null || groupButtons;
    }

    /**
     * Sets whether or not should group toggle state elements.
     * Changing this flag will automatically group or ungroup toggle state elements in this pane.
     *
     * @param group whether or not should group toggle state elements
     */
    public void setGroupButtons ( final boolean group )
    {
        if ( isGroupButtons () != group )
        {
            this.groupButtons = group;
            updateButtonGrouping ();
        }
    }

    /**
     * Returns whether or not this button group should allow empty selection state.
     *
     * @return true if this button group should allow empty selection state, false otherwise
     */
    public boolean isUnselectableGrouping ()
    {
        return unselectableGrouping != null && unselectableGrouping;
    }

    /**
     * Sets whether or not this button group should allow empty selection state.
     *
     * @param unselectable whether or not this button group should allow empty selection state
     */
    public void setUnselectableGrouping ( final boolean unselectable )
    {
        if ( isUnselectableGrouping () != unselectable )
        {
            this.unselectableGrouping = unselectable;
            updateButtonGrouping ();
        }
    }

    /**
     * Returns newly created button group.
     *
     * @return newly created button group
     */
    @NotNull
    protected UnselectableButtonGroup createButtonGroup ()
    {
        return new UnselectableButtonGroup ( isUnselectableGrouping () );
    }

    /**
     * Ungroups all buttons.
     */
    protected void clearGrouping ()
    {
        if ( buttonGroup != null )
        {
            buttonGroup.removeAll ();
            buttonGroup = null;
        }
    }

    /**
     * Updates button grouping.
     */
    protected void updateButtonGrouping ()
    {
        // Clearing grouping first
        clearGrouping ();

        // Grouping buttons
        if ( isGroupButtons () )
        {
            buttonGroup = createButtonGroup ();
            if ( children != null )
            {
                for ( final Map.Entry<Component, Pair<String, String>> entry : children.entrySet () )
                {
                    final Component component = entry.getKey ();
                    if ( isGroupable ( component ) )
                    {
                        buttonGroup.add ( ( AbstractButton ) component );
                    }
                }
            }
        }
    }

    /**
     * Returns whether or not specified component is a groupable button.
     *
     * @param component component to process
     * @return true if specified component is a groupable button, false otherwise
     */
    protected boolean isGroupable ( @NotNull final Component component )
    {
        return component instanceof JToggleButton ||
                component instanceof JCheckBoxMenuItem ||
                component instanceof JRadioButtonMenuItem;
    }

    /**
     * Returns button group used to group toggle state elements placed within container using this layout.
     *
     * @return button group used to group toggle state elements placed within container using this layout
     */
    @Nullable
    public UnselectableButtonGroup getButtonGroup ()
    {
        return buttonGroup;
    }

    /**
     * Returns sides decriptor.
     *
     * @return sides decriptor
     */
    @NotNull
    protected String sides ()
    {
        return sides != null ? sides : ( sides = "1,1,1,1" );
    }

    /**
     * Returns whether or not top side should be decorated.
     *
     * @return true if top side should be decorated, false otherwise
     */
    public boolean isPaintTop ()
    {
        return sides ().charAt ( 0 ) != '0';
    }

    /**
     * Sets whether or not top side should be decorated.
     *
     * @param paint whether or not top side should be decorated
     */
    public void setPaintTop ( final boolean paint )
    {
        TextUtils.replace ( sides (), 6, paint ? '1' : '0' );
        resetDescriptors ();
    }

    /**
     * Returns whether or not left side should be decorated.
     *
     * @return true if left side should be decorated, false otherwise
     */
    public boolean isPaintLeft ()
    {
        return sides ().charAt ( 2 ) != '0';
    }

    /**
     * Sets whether or not left side should be decorated.
     *
     * @param paint whether or not left side should be decorated
     */
    public void setPaintLeft ( final boolean paint )
    {
        TextUtils.replace ( sides (), 6, paint ? '1' : '0' );
        resetDescriptors ();
    }

    /**
     * Returns whether or not bottom side should be decorated.
     *
     * @return true if bottom side should be decorated, false otherwise
     */
    public boolean isPaintBottom ()
    {
        return sides ().charAt ( 4 ) != '0';
    }

    /**
     * Sets whether or not bottom side should be decorated.
     *
     * @param paint whether or not bottom side should be decorated
     */
    public void setPaintBottom ( final boolean paint )
    {
        TextUtils.replace ( sides (), 6, paint ? '1' : '0' );
        resetDescriptors ();
    }

    /**
     * Returns whether or not right side should be decorated.
     *
     * @return true if right side should be decorated, false otherwise
     */
    public boolean isPaintRight ()
    {
        return sides ().charAt ( 6 ) != '0';
    }

    /**
     * Sets whether or not right side should be decorated.
     *
     * @param paint whether or not right side should be decorated
     */
    public void setPaintRight ( final boolean paint )
    {
        TextUtils.replace ( sides (), 6, paint ? '1' : '0' );
        resetDescriptors ();
    }

    /**
     * Sets whether or not right side should be decorated.
     *
     * @param top    whether or not top side should be decorated
     * @param left   whether or not bottom side should be decorated
     * @param bottom whether or not bottom side should be decorated
     * @param right  whether or not right side should be decorated
     */
    public void setPaintSides ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        sides = DecorationUtils.toString ( top, left, bottom, right );
        resetDescriptors ();
    }

    @Override
    public void addComponent ( @NotNull final Component component, @Nullable final Object constraints )
    {
        // Saving child reference
        if ( children == null )
        {
            children = new HashMap<Component, Pair<String, String>> ( 3 );
        }
        children.put ( component, new Pair<String, String> () );

        // Updating grouping
        if ( isGroupable ( component ) )
        {
            updateButtonGrouping ();
        }

        // Resetting descriptors
        resetDescriptors ();
    }

    @Override
    public void removeComponent ( @NotNull final Component component )
    {
        // Removing child reference
        if ( children != null )
        {
            children.remove ( component );
        }

        // Updating grouping
        if ( isGroupable ( component ) )
        {
            updateButtonGrouping ();
        }

        // Resetting descriptors
        resetDescriptors ();
    }

    @Nullable
    @Override
    public final String getSides ( @NotNull final Component component )
    {
        return children != null ? getDescriptors ( component ).getKey () : null;
    }

    @Nullable
    @Override
    public final String getLines ( @NotNull final Component component )
    {
        return children != null ? getDescriptors ( component ).getValue () : null;
    }

    /**
     * Returns descriptors for painted {@link Component} sides and lines.
     *
     * @param component painted {@link Component}
     * @return descriptors for painted {@link Component} sides and lines
     */
    @NotNull
    protected Pair<String, String> getDescriptors ( @NotNull final Component component )
    {
        Pair<String, String> pair;
        if ( children != null )
        {
            pair = children.get ( component );
            if ( pair == null || pair.getKey () == null )
            {
                final Container parent = component.getParent ();
                if ( parent != null && isGrouping () )
                {
                    pair = getDescriptors ( parent, component, SwingUtils.indexOf ( parent, component ) );
                }
                else
                {
                    pair = new Pair<String, String> ();
                }
                children.put ( component, pair );
            }
        }
        else
        {
            pair = new Pair<String, String> ();
        }
        return pair;
    }

    /**
     * Returns descriptors for painted {@link Component} sides and lines.
     * It is requested only if grouping is actually enabled.
     *
     * @param container {@link Container} of the {@link Component}
     * @param component painter {@link Component}
     * @param index     {@link Component} z-index in {@link Container}
     * @return descriptors for painted {@link Component} sides and lines
     */
    @NotNull
    public abstract Pair<String, String> getDescriptors ( @NotNull Container container, @NotNull Component component, int index );

    /**
     * Resets cached sides and lines descriptors.
     */
    protected void resetDescriptors ()
    {
        if ( children != null )
        {
            for ( final Map.Entry<Component, Pair<String, String>> entry : children.entrySet () )
            {
                entry.setValue ( new Pair<String, String> () );
                DecorationUtils.fireBorderChanged ( entry.getKey () );
            }
        }
    }
}