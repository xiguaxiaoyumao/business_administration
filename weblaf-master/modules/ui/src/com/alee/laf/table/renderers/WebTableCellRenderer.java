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

package com.alee.laf.table.renderers;

import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.api.ui.*;
import com.alee.extended.label.WebStyledLabel;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.table.TableCellParameters;
import com.alee.managers.style.ChildStyleId;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;

import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Default {@link TableCellRenderer} implementation based on {@link WebStyledLabel}.
 * Unlike {@link javax.swing.table.DefaultTableCellRenderer} it has generic for table type.
 * It also contains multiple methods for convenient renderer customization that can be overridden.
 * And since it is based on {@link WebStyledLabel} it retains all of its extra features.
 *
 * @param <V> cell value type
 * @param <C> {@link JTable} type
 * @param <P> {@link TableCellParameters} type
 * @author Mikle Garin
 * @see TableCellParameters
 */
public class WebTableCellRenderer<V, C extends JTable, P extends TableCellParameters<V, C>>
        extends WebStyledLabel implements TableCellRenderer, Stateful
{
    /**
     * Additional renderer decoration states.
     */
    protected final List<String> states;

    /**
     * Constructs new {@link WebTableCellRenderer}.
     */
    public WebTableCellRenderer ()
    {
        super ();
        setName ( "Table.cellRenderer" );
        states = new ArrayList<String> ( 3 );
    }

    @Nullable
    @Override
    public List<String> getStates ()
    {
        return states;
    }

    /**
     * Updates custom renderer states based on render cycle settings.
     *
     * @param parameters {@link TableCellParameters}
     */
    protected void updateStates ( final P parameters )
    {
        // Resetting states
        states.clear ();

        // Selection state
        states.add ( parameters.isSelected () ? DecorationState.selected : DecorationState.unselected );

        // Focus state
        if ( parameters.isFocused () )
        {
            states.add ( DecorationState.focused );
        }

        // todo Add hover state when WTableUI is available
        // states.add ( DecorationState.hover );

        // Extra states provided by value
        states.addAll ( DecorationUtils.getExtraStates ( parameters.value () ) );
    }

    /**
     * Updates renderer component style identifier.
     *
     * @param parameters {@link TableCellParameters}
     */
    protected void updateStyleId ( final P parameters )
    {
        StyleId id = null;
        if ( parameters.value () instanceof ChildStyleIdBridge )
        {
            final ChildStyleIdBridge childStyleIdBridge = ( ChildStyleIdBridge ) parameters.value ();
            final ChildStyleId childStyleId = childStyleIdBridge.getChildStyleId ( parameters );
            if ( childStyleId != null )
            {
                id = childStyleId.at ( parameters.table () );
            }
        }
        else if ( parameters.value () instanceof StyleIdBridge )
        {
            final StyleIdBridge styleIdBridge = ( StyleIdBridge ) parameters.value ();
            final StyleId styleId = styleIdBridge.getStyleId ( parameters );
            if ( styleId != null )
            {
                id = styleId;
            }
        }
        if ( id == null )
        {
            id = StyleId.tableCellRenderer.at ( parameters.table () );
        }
        setStyleId ( id );
    }

    /**
     * Updating renderer based on the provided settings.
     *
     * @param parameters {@link TableCellParameters}
     */
    protected void updateView ( final P parameters )
    {
        setEnabled ( enabledForValue ( parameters ) );
        setComponentOrientation ( orientationForValue ( parameters ) );
        setFont ( fontForValue ( parameters ) );
        setForeground ( foregroundForValue ( parameters ) );
        setHorizontalAlignment ( horizontalAlignmentForValue ( parameters ) );
        setIcon ( iconForValue ( parameters ) );
        setText ( textForValue ( parameters ) );
    }

    /**
     * Returns whether or not renderer for the specified cell value should be enabled.
     *
     * @param parameters {@link TableCellParameters}
     * @return {@code true} if renderer for the specified cell value should be enabled, {@code false} otherwise
     */
    protected boolean enabledForValue ( final P parameters )
    {
        return parameters.table ().isEnabled ();
    }

    /**
     * Returns renderer {@link ComponentOrientation} for the specified cell value.
     *
     * @param parameters {@link TableCellParameters}
     * @return renderer {@link ComponentOrientation} for the specified cell value
     */
    protected ComponentOrientation orientationForValue ( final P parameters )
    {
        return parameters.table ().getComponentOrientation ();
    }

    /**
     * Returns renderer {@link Font} for the specified cell value.
     *
     * @param parameters {@link TableCellParameters}
     * @return renderer {@link Font} for the specified cell value
     */
    protected Font fontForValue ( final P parameters )
    {
        return parameters.table ().getFont ();
    }

    /**
     * Returns renderer foreground color for the specified cell value.
     *
     * @param parameters {@link TableCellParameters}
     * @return renderer foreground color for the specified cell value
     */
    protected Color foregroundForValue ( final P parameters )
    {
        final Color foreground;
        if ( parameters.value () instanceof ForegroundBridge )
        {
            final ForegroundBridge foregroundBridge = ( ForegroundBridge ) parameters.value ();
            final Color fg = foregroundBridge.getForeground ( parameters );
            if ( fg != null )
            {
                foreground = fg;
            }
            else if ( parameters.isSelected () )
            {
                foreground = parameters.table ().getSelectionForeground ();
            }
            else
            {
                foreground = parameters.table ().getForeground ();
            }
        }
        else if ( parameters.isSelected () )
        {
            foreground = parameters.table ().getSelectionForeground ();
        }
        else
        {
            foreground = parameters.table ().getForeground ();
        }
        return foreground;
    }

    /**
     * Returns renderer horizontal alignment for the specified cell value.
     *
     * @param parameters {@link TableCellParameters}
     * @return renderer horizontal alignment for the specified cell value
     */
    protected int horizontalAlignmentForValue ( final P parameters )
    {
        return SwingConstants.LEADING;
    }

    /**
     * Returns renderer icon for the specified cell value.
     *
     * @param parameters {@link TableCellParameters}
     * @return renderer icon for the specified cell value
     */
    protected Icon iconForValue ( final P parameters )
    {
        final Icon icon;
        if ( parameters.value () instanceof IconBridge )
        {
            final IconBridge iconBridge = ( IconBridge ) parameters.value ();
            icon = iconBridge.getIcon ( parameters );
        }
        else
        {
            icon = parameters.value () instanceof Icon ? ( Icon ) parameters.value () : null;
        }
        return icon;
    }

    /**
     * Returns renderer text for the specified cell value.
     *
     * @param parameters {@link TableCellParameters}
     * @return renderer text for the specified cell value
     */
    protected String textForValue ( final P parameters )
    {
        final String text;
        if ( parameters.value () instanceof TextBridge )
        {
            final TextBridge textBridge = ( TextBridge ) parameters.value ();
            text = textBridge.getText ( parameters );
        }
        else
        {
            text = parameters.value () != null && !( parameters.value () instanceof Icon ) ? parameters.value ().toString () : "";
        }
        return text;
    }

    /**
     * Returns table cell renderer component.
     *
     * @param table      {@link JTable}
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     * @return table cell renderer component
     */
    @Override
    public Component getTableCellRendererComponent ( final JTable table, final Object value, final boolean isSelected,
                                                     final boolean hasFocus, final int row, final int column )
    {
        // Forming cell parameters
        final P parameters = getRenderingParameters ( ( C ) table, ( V ) value, isSelected, hasFocus, row, column );

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
     * Returns {@link TableCellParameters}.
     *
     * @param table      {@link JTable}
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     * @return {@link TableCellParameters}
     */
    protected P getRenderingParameters ( final C table, final V value, final boolean isSelected,
                                         final boolean hasFocus, final int row, final int column )
    {
        return ( P ) new TableCellParameters<V, C> ( table, value, row, column, isSelected, hasFocus );
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
    public final void repaint ( final Rectangle r )
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
    protected void checkPropertyChange ( final String propertyName, final Object oldValue, final Object newValue )
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
    protected void allowPropertyChange ( final String propertyName, final Object oldValue, final Object newValue )
    {
        super.firePropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    protected final void firePropertyChange ( final String propertyName, final Object oldValue, final Object newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final byte oldValue, final byte newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final char oldValue, final char newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final short oldValue, final short newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final int oldValue, final int newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final long oldValue, final long newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final float oldValue, final float newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final double oldValue, final double newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final boolean oldValue, final boolean newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    /**
     * A subclass of {@link WebTableCellRenderer} that implements {@link javax.swing.plaf.UIResource}.
     * It is used to determine cell renderer provided by the UI class to properly uninstall it on UI uninstall.
     *
     * @param <V> cell value type
     * @param <C> {@link JTable} type
     * @param <P> {@link TableCellParameters} type
     */
    public static final class UIResource<V, C extends JTable, P extends TableCellParameters<V, C>>
            extends WebTableCellRenderer<V, C, P> implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link WebTableCellRenderer}.
         */
    }
}