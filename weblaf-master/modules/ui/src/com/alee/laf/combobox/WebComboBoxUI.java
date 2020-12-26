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

package com.alee.laf.combobox;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.list.WebList;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.separator.WebSeparator;
import com.alee.managers.style.*;
import com.alee.painter.PainterSupport;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.MathUtils;
import com.alee.utils.swing.ClientProperty;
import com.alee.utils.swing.EditabilityListener;
import com.alee.utils.swing.VisibilityListener;
import com.alee.utils.swing.extensions.SizeMethods;
import com.alee.utils.swing.extensions.SizeMethodsImpl;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom UI for {@link JComboBox} component.
 *
 * @author Mikle Garin
 */
public class WebComboBoxUI extends WComboBoxUI
{
    /**
     * {@link JComboBox} instance placed as popup {@link JList} client property.
     */
    public static final ClientProperty<JComboBox> COMBOBOX_INSTANCE = new ClientProperty<JComboBox> ( "JComboBox.instance", null );

    /**
     * Default combobox renderer.
     */
    protected static ListCellRenderer DEFAULT_RENDERER;

    /**
     * Listeners.
     */
    protected transient PropertyChangeListener visibilityListener;
    protected transient EventListenerList listenerList;
    protected transient PropertyChangeListener editorChangeListener;

    /**
     * Runtime variables.
     */
    protected transient JSeparator separator;

    /**
     * Returns an instance of the {@link WebComboBoxUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebComboBoxUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebComboBoxUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        // Creating listener list before any operations
        listenerList = new EventListenerList ();

        super.installUI ( c );

        // Listeners
        if ( popup instanceof JComponent )
        {
            visibilityListener = new PropertyChangeListener ()
            {
                @Override
                public void propertyChange ( final PropertyChangeEvent evt )
                {
                    if ( Objects.notEquals ( evt.getOldValue (), evt.getNewValue () ) )
                    {
                        firePopupVisibilityChanged ( ( Boolean ) evt.getNewValue () );
                    }
                }
            };
            ( ( JComponent ) popup ).addPropertyChangeListener ( WebLookAndFeel.VISIBLE_PROPERTY, visibilityListener );
        }
        editorChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                if ( Objects.equals ( evt.getPropertyName (), WebComboBox.EDITOR_PROPERTY ) )
                {
                    updateEditor ( comboBox.getEditor () );
                }
            }
        };
        comboBox.addPropertyChangeListener ( editorChangeListener );

        // Applying skin
        StyleManager.installSkin ( comboBox );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( comboBox );

        // Listeners
        comboBox.removePropertyChangeListener ( editorChangeListener );
        if ( popup instanceof JComponent )
        {
            ( ( JComponent ) popup ).removePropertyChangeListener ( WebLookAndFeel.VISIBLE_PROPERTY, visibilityListener );
        }

        super.uninstallUI ( c );

        // Erasing listener list after all uninstalls
        listenerList = null;
    }

    @Override
    protected void installComponents ()
    {
        // Removing all previous components
        comboBox.removeAll ();

        // Updating layout
        comboBox.setLayout ( createLayoutManager () );

        // Value and button separator
        addSeparator ();

        // Arrow button
        addArrowButton ();

        // Value editor
        if ( comboBox.isEditable () )
        {
            addEditor ();
        }

        // Value renderer pane
        comboBox.add ( currentValuePane );
    }

    @Override
    protected void uninstallComponents ()
    {
        // Removing all components
        removeArrowButton ();
        removeSeparator ();
        removeEditor ();
        comboBox.removeAll ();
    }

    /**
     * Adding separator onto combobox.
     */
    protected void addSeparator ()
    {
        separator = createSeparator ();
        if ( separator instanceof ComboBoxSeparator )
        {
            ( ( ComboBoxSeparator ) separator ).install ();
        }
        comboBox.add ( separator );
    }

    /**
     * Removing separator from combobox.
     */
    protected void removeSeparator ()
    {
        if ( separator != null )
        {
            comboBox.remove ( separator );
            if ( separator instanceof ComboBoxSeparator )
            {
                ( ( ComboBoxSeparator ) separator ).uninstall ();
            }
            separator = null;
        }
    }

    /**
     * Adding arrow button onto combobox.
     */
    protected void addArrowButton ()
    {
        arrowButton = createArrowButton ();
        if ( arrowButton instanceof ComboBoxButton )
        {
            ( ( ComboBoxButton ) arrowButton ).install ();
        }
        configureArrowButton ();
        comboBox.add ( arrowButton );
    }

    /**
     * Removing arrow button from combobox.
     */
    protected void removeArrowButton ()
    {
        if ( arrowButton != null )
        {
            comboBox.remove ( arrowButton );
            unconfigureArrowButton ();
            if ( arrowButton instanceof ComboBoxButton )
            {
                ( ( ComboBoxButton ) arrowButton ).uninstall ();
            }
            arrowButton = null;
        }
    }

    @NotNull
    @Override
    protected ListCellRenderer createRenderer ()
    {
        return new WebComboBoxRenderer.UIResource<Object, JList, ComboBoxCellParameters<Object, JList>> ();
    }

    @NotNull
    @Override
    protected ComboBoxEditor createEditor ()
    {
        final ComboBoxEditor editor = new WebComboBoxEditor.UIResource ();
        updateEditor ( editor );
        return editor;
    }

    /**
     * Forces combobox editor style update.
     * todo Properly remove focus listener?
     *
     * @param editor editor {@link Component}
     */
    private void updateEditor ( @Nullable final ComboBoxEditor editor )
    {
        if ( editor != null )
        {
            final Component editorComponent = editor.getEditorComponent ();
            editorComponent.addFocusListener ( new FocusAdapter ()
            {
                @Override
                public void focusGained ( final FocusEvent e )
                {
                    comboBox.repaint ();
                }

                @Override
                public void focusLost ( final FocusEvent e )
                {
                    comboBox.repaint ();
                }
            } );
            if ( editorComponent instanceof JTextField )
            {
                StyleId.comboboxEditor.at ( comboBox ).set ( ( JTextField ) editorComponent );
            }
        }
    }

    /**
     * Constructs and returns separator to be placed between renderer/editor and button.
     *
     * @return separator to be placed between renderer/editor and button
     */
    @NotNull
    protected JSeparator createSeparator ()
    {
        return new ComboBoxSeparator ();
    }

    @NotNull
    @Override
    protected JButton createArrowButton ()
    {
        return new ComboBoxButton ();
    }

    @NotNull
    @Override
    protected ComboPopup createPopup ()
    {
        return new BasicComboPopup ( comboBox )
        {
            @NotNull
            @Override
            protected JList createList ()
            {
                final WebList list = new WebList ( comboBox.getModel () )
                {
                    @Override
                    public void processMouseEvent ( MouseEvent e )
                    {
                        if ( CoreSwingUtils.isMenuShortcutKeyDown ( e ) )
                        {
                            /**
                             * Fix for 4234053. Filter out the Control Key from the list.
                             * ie., don't allow CTRL key deselection.
                             */
                            final Toolkit toolkit = Toolkit.getDefaultToolkit ();
                            e = new MouseEvent ( ( Component ) e.getSource (), e.getID (), e.getWhen (),
                                    e.getModifiers () ^ toolkit.getMenuShortcutKeyMask (),
                                    e.getX (), e.getY (),
                                    e.getXOnScreen (), e.getYOnScreen (),
                                    e.getClickCount (),
                                    e.isPopupTrigger (),
                                    MouseEvent.NOBUTTON );
                        }
                        super.processMouseEvent ( e );
                    }
                };

                // Adding combobox reference for internal usage
                COMBOBOX_INSTANCE.set ( list, comboBox );

                // todo Handle inside of the popup painter
                // Custom listener to update popup menu dropdown corner
                //                list.addListSelectionListener ( new ListSelectionListener ()
                //                {
                //                    @Override
                //                    public void valueChanged ( final ListSelectionEvent e )
                //                    {
                //                        // Checking that popup is still displaying on screen
                //                        if ( isShowing () && getUI () instanceof WebPopupMenuUI )
                //                        {
                //                            // Only do additional repaints for dropdown-styled menu
                //                            final WebPopupMenuUI ui = ( WebPopupMenuUI ) getUI ();
                //                            if ( ui.getPopupStyle () == PopupStyle.dropdown )
                //                            {
                //                                // Retrieving menu and combobox position on screen and deciding which side to repaint
                //                                final int py = getLocationOnScreen ().y;
                //                                final int cbi = comboBox.getLocationOnScreen ().y;
                //                                final Insets pi = getInsets ();
                //                                if ( py > cbi )
                //                                {
                //                                    // Repainting top corner area
                //                                    repaint ( 0, 0, getWidth (), pi.top );
                //                                }
                //                                else
                //                                {
                //                                    // Repainting bottom corner area
                //                                    repaint ( 0, getHeight () - pi.bottom, getWidth (), pi.bottom );
                //                                }
                //                            }
                //                        }
                //                    }
                //                } );

                return list;
            }

            @Override
            protected void configureList ()
            {
                list.setFocusable ( false );
                list.setFont ( comboBox.getFont () );
                list.setCellRenderer ( comboBox.getRenderer () );
                list.setSelectionMode ( ListSelectionModel.SINGLE_SELECTION );
                setListSelection ( comboBox.getSelectedIndex () );
                installListListeners ();
            }

            @NotNull
            @Override
            protected JScrollPane createScroller ()
            {
                // todo This should be better implemented later on in a custom popup
                StyleId.comboboxPopup.at ( comboBox ).set ( this );

                final WebScrollPane scroll = new WebScrollPane ( StyleId.comboboxPopupScrollPane.at ( this ), list );
                scroll.setHorizontalScrollBar ( null );

                // Custom list styling
                StyleId.comboboxPopupList.at ( scroll ).set ( list );

                return scroll;
            }

            @Override
            protected void configureScroller ()
            {
                scroller.setFocusable ( false );
                scroller.getVerticalScrollBar ().setFocusable ( false );
            }

            @Override
            protected void configurePopup ()
            {
                setOpaque ( false );
                setDoubleBuffered ( true );
                setFocusable ( false );
                setLayout ( new BorderLayout () );
                add ( scroller );
            }

            @Override
            public void show ()
            {
                comboBox.firePopupMenuWillBecomeVisible ();

                setListSelection ( comboBox.getSelectedIndex () );

                final Point location = getPopupLocation ();
                show ( comboBox, location.x, location.y );
            }

            /**
             * Sets list selection to the specified index.
             *
             * @param index selected index
             */
            private void setListSelection ( final int index )
            {
                if ( index == -1 )
                {
                    list.clearSelection ();
                }
                else
                {
                    list.setSelectedIndex ( index );
                    list.ensureIndexIsVisible ( index );
                }
            }

            /**
             * Returns combobox popup location.
             *
             * @return combobox popup location
             */
            @NotNull
            private Point getPopupLocation ()
            {
                // Resetting popup and list sizes
                // This is necessary for proper recalculation of sizes
                setPreferredSize ( null );
                SizeMethodsImpl.setPreferredWidth ( list, SizeMethods.UNDEFINED );

                // Updating list visible row count
                // This is a better way to approach height calculation
                list.setVisibleRowCount ( MathUtils.limit ( 1, list.getModel ().getSize (), comboBox.getMaximumRowCount () ) );

                // Calculating popup size
                final Dimension ps = this.getPreferredSize ();
                final Boolean widePopup = WebComboBox.WIDE_POPUP.get ( comboBox );
                final int width = widePopup ? Math.max ( comboBox.getWidth (), ps.width ) : comboBox.getWidth ();
                final int height = ps.height;

                // Calculating popup bounds
                final Rectangle popupBounds = computePopupBounds ( 0, comboBox.getHeight (), width, height );

                // List size must be zero to allow elements to be horizontally shrinked
                SizeMethodsImpl.setPreferredWidth ( list, 0 );

                // Popup preferred size
                setPreferredSize ( new Dimension ( width, height ) );

                return popupBounds.getLocation ();
            }
        };
    }

    @Override
    public JList getListBox ()
    {
        return listBox;
    }

    @Override
    public boolean contains ( @NotNull final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, x, y );
    }

    @Override
    public void paint ( @NotNull final Graphics g, @NotNull final JComponent c )
    {
        PainterSupport.paint ( g, c, this, new ComboBoxPaintParameters ( currentValuePane ) );
    }

    @NotNull
    @Override
    protected LayoutManager createLayoutManager ()
    {
        return new WebComboBoxLayout ();
    }

    @NotNull
    @Override
    public Dimension getMinimumSize ( @NotNull final JComponent c )
    {
        final Dimension minimumSize;
        if ( isMinimumSizeDirty )
        {
            final Dimension size = getDisplaySize ();
            final Insets insets = getInsets ();

            // Insets sizes
            size.height += insets.top + insets.bottom;
            size.width += insets.left + insets.right;

            // Arrow button width
            if ( arrowButton != null && arrowButton.isVisible () )
            {
                size.width += arrowButton.getPreferredSize ().width;

                // Separator width
                if ( separator != null && separator.isVisible () )
                {
                    size.width += separator.getPreferredSize ().width;
                }
            }

            // Saving resulting size
            cachedMinimumSize.setSize ( size.width, size.height );
            isMinimumSizeDirty = false;

            minimumSize = new Dimension ( size );
        }
        else
        {
            minimumSize = new Dimension ( cachedMinimumSize );
        }

        return minimumSize;
    }

    @NotNull
    @Override
    protected Dimension getDefaultSize ()
    {
        // todo Use current renderer instead?
        // Calculates the height and width using the default text renderer
        return getSizeForComponent ( getDefaultListCellRenderer ().getListCellRendererComponent ( listBox, " ", -1, false, false ) );
    }

    /**
     * Returns renderer component preferred size.
     *
     * @param c renderer component
     * @return renderer component preferred size
     */
    @NotNull
    protected Dimension getSizeForComponent ( @NotNull final Component c )
    {
        currentValuePane.add ( c );
        c.setFont ( comboBox.getFont () );
        final Dimension d = c.getPreferredSize ();
        currentValuePane.remove ( c );
        return d;
    }

    @Nullable
    @Override
    public Dimension getPreferredSize ( @NotNull final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), true );
    }

    @NotNull
    @Override
    protected Rectangle rectangleForCurrentValue ()
    {
        return getValueBounds ();
    }

    @NotNull
    @Override
    public Rectangle getValueBounds ()
    {
        final int width = comboBox.getWidth ();
        final int height = comboBox.getHeight ();
        final Insets i = comboBox.getInsets ();
        final boolean ltr = comboBox.getComponentOrientation ().isLeftToRight ();

        int side = 0;
        if ( arrowButton != null && arrowButton.isVisible () )
        {
            side += arrowButton.getPreferredSize ().width;
            if ( separator != null && separator.isVisible () )
            {
                side += separator.getPreferredSize ().width;
            }
        }

        return new Rectangle ( ltr ? i.left : i.left + side, i.top, width - i.left - i.right - side, height - i.top - i.bottom );
    }

    /**
     * Returns the area that is reserved for drawing separator between currently selected item and arrow button.
     *
     * @return area that is reserved for drawing between currently selected item and arrow button
     */
    @NotNull
    public Rectangle getSeparatorBounds ()
    {
        final int width = comboBox.getWidth ();
        final int height = comboBox.getHeight ();
        final Insets i = comboBox.getInsets ();
        final boolean ltr = comboBox.getComponentOrientation ().isLeftToRight ();
        final int sep = separator.getPreferredSize ().width;

        int button = 0;
        if ( arrowButton != null && arrowButton.isVisible () )
        {
            button += arrowButton.getPreferredSize ().width;
        }

        return new Rectangle ( ltr ? width - i.right - button - sep : i.left + button, i.top, sep, height - i.top - i.bottom );
    }

    /**
     * Returns the area that is reserved for drawing arrow button.
     *
     * @return area that is reserved for drawing arrow button
     */
    @NotNull
    public Rectangle getArrowButtonBounds ()
    {
        final int width = comboBox.getWidth ();
        final int height = comboBox.getHeight ();
        final Insets i = comboBox.getInsets ();
        final boolean ltr = comboBox.getComponentOrientation ().isLeftToRight ();
        final int button = arrowButton.getPreferredSize ().width;
        return new Rectangle ( ltr ? width - i.right - button : i.left, i.top, button, height - i.top - i.bottom );
    }

    @Override
    public void addEditor ()
    {
        super.addEditor ();

        // Inform about editability change
        // This is thrown here instead of property change listener to ensure fires order is preserved
        fireEditabilityChanged ( true );
    }

    @Override
    protected void configureEditor ()
    {
        super.configureEditor ();

        // Applying proper editor orientation on display
        editor.applyComponentOrientation ( comboBox.getComponentOrientation () );
    }

    @Override
    public void removeEditor ()
    {
        super.removeEditor ();

        // Inform about editability change
        // This is thrown here instead of property change listener to ensure fires order is preserved
        fireEditabilityChanged ( false );
    }

    @Override
    public void addEditabilityListener ( @NotNull final EditabilityListener listener )
    {
        listenerList.add ( EditabilityListener.class, listener );
    }

    @Override
    public void removeEditabilityListener ( @NotNull final EditabilityListener listener )
    {
        listenerList.remove ( EditabilityListener.class, listener );
    }

    /**
     * Informs about combobox editability change.
     *
     * @param editable whether or not combobox is editable.
     */
    public void fireEditabilityChanged ( final boolean editable )
    {
        for ( final EditabilityListener listener : listenerList.getListeners ( EditabilityListener.class ) )
        {
            listener.editabilityChanged ( editable );
        }
    }

    @Override
    public void addPopupVisibilityListener ( @NotNull final VisibilityListener listener )
    {
        listenerList.add ( VisibilityListener.class, listener );
    }

    @Override
    public void removePopupVisibilityListener ( @NotNull final VisibilityListener listener )
    {
        listenerList.remove ( VisibilityListener.class, listener );
    }

    /**
     * Informs about combobox popup visibility change.
     *
     * @param visible whether or not combobox popup is visible
     */
    public void firePopupVisibilityChanged ( final boolean visible )
    {
        for ( final VisibilityListener listener : listenerList.getListeners ( VisibilityListener.class ) )
        {
            listener.visibilityChanged ( visible );
        }
    }

    /**
     * Custom layout manager for WebComboBoxUI.
     */
    protected class WebComboBoxLayout extends AbstractLayoutManager
    {
        @Override
        public void layoutContainer ( @NotNull final Container container )
        {
            // Arrow button
            if ( arrowButton != null && arrowButton.isVisible () )
            {
                arrowButton.setBounds ( getArrowButtonBounds () );

                // Separator
                if ( separator != null && separator.isVisible () )
                {
                    separator.setBounds ( getSeparatorBounds () );
                }
            }

            // Value editor
            if ( editor != null )
            {
                editor.setBounds ( getValueBounds () );
            }
        }

        @NotNull
        @Override
        public Dimension minimumLayoutSize ( @NotNull final Container container )
        {
            return container.getMinimumSize ();
        }

        @NotNull
        @Override
        public Dimension preferredLayoutSize ( @NotNull final Container container )
        {
            return container.getPreferredSize ();
        }
    }

    /**
     * Custom combobox separator placed between renderer/editor and button.
     */
    public class ComboBoxSeparator extends WebSeparator implements Stateful, EditabilityListener, VisibilityListener
    {
        /**
         * Constructs new combobox separator.
         */
        public ComboBoxSeparator ()
        {
            super ( StyleId.comboboxSeparator.at ( comboBox ), HORIZONTAL );
        }

        /**
         * Installs custom combobox listeners.
         */
        public void install ()
        {
            addEditabilityListener ( this );
            addPopupVisibilityListener ( this );
        }

        /**
         * Uninstalls custom combobox listeners.
         */
        public void uninstall ()
        {
            removePopupVisibilityListener ( this );
            removeEditabilityListener ( this );
        }

        @Override
        public void visibilityChanged ( final boolean visible )
        {
            DecorationUtils.fireStatesChanged ( this );
        }

        @Override
        public void editabilityChanged ( final boolean editable )
        {
            DecorationUtils.fireStatesChanged ( this );
        }

        @Nullable
        @Override
        public List<String> getStates ()
        {
            final List<String> states = new ArrayList<String> ( 1 );
            if ( comboBox.isEditable () )
            {
                states.add ( DecorationState.editable );
            }
            states.add ( isPopupVisible ( comboBox ) ? DecorationState.expanded : DecorationState.collapsed );
            return states;
        }
    }

    /**
     * Custom combobox button used to display popup menu arrow.
     */
    public class ComboBoxButton extends WebButton implements Stateful, EditabilityListener, VisibilityListener
    {
        /**
         * Constructs new combobox button.
         */
        public ComboBoxButton ()
        {
            super ( StyleId.comboboxArrowButton.at ( comboBox ) );
            setName ( "ComboBox.arrowButton" );
        }

        /**
         * Installs custom combobox listeners.
         */
        public void install ()
        {
            addEditabilityListener ( this );
            addPopupVisibilityListener ( this );
        }

        /**
         * Uninstalls custom combobox listeners.
         */
        public void uninstall ()
        {
            removePopupVisibilityListener ( this );
            removeEditabilityListener ( this );
        }

        @Override
        public void setFocusable ( final boolean focusable )
        {
            // Workaround to completely disable focusability of this button
            super.setFocusable ( false );
        }

        @Override
        public void visibilityChanged ( final boolean visible )
        {
            DecorationUtils.fireStatesChanged ( this );
        }

        @Override
        public void editabilityChanged ( final boolean editable )
        {
            DecorationUtils.fireStatesChanged ( this );
        }

        @NotNull
        @Override
        public List<String> getStates ()
        {
            final List<String> states = new ArrayList<String> ( 1 );
            if ( comboBox.isEditable () )
            {
                states.add ( DecorationState.editable );
            }
            states.add ( isPopupVisible ( comboBox ) ? DecorationState.expanded : DecorationState.collapsed );
            return states;
        }
    }

    /**
     * Returns default list cell renderer instance.
     *
     * @return default list cell renderer instance
     */
    @NotNull
    protected static ListCellRenderer getDefaultListCellRenderer ()
    {
        if ( DEFAULT_RENDERER == null )
        {
            DEFAULT_RENDERER = new WebComboBoxRenderer.UIResource<Object, JList, ComboBoxCellParameters<Object, JList>> ();
        }
        return DEFAULT_RENDERER;
    }
}