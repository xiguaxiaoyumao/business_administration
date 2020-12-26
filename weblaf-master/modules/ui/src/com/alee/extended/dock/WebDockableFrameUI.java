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

package com.alee.extended.dock;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.extended.behavior.ComponentMoveBehavior;
import com.alee.extended.dock.drag.DockableFrameTransferHandler;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.separator.WebSeparator;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.painter.PainterSupport;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.extensions.MouseEventRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for {@link WebDockableFrame} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see WebDockablePane
 */
public class WebDockableFrameUI<C extends WebDockableFrame> extends WDockableFrameUI<C> implements PropertyChangeListener
{
    /**
     * Listeners.
     */
    protected transient DefaultFocusTracker focusTracker;
    protected transient ComponentMoveBehavior dialogMoveBehavior;

    /**
     * Additional components used be the UI.
     */
    protected transient SidebarButton sidebarButton;
    protected transient WebPanel titlePanel;
    protected transient WebStyledLabel titleLabel;
    protected transient WebPanel buttonsPanel;
    protected transient WebButton dockButton;
    protected transient WebButton minimizeButton;
    protected transient WebButton floatButton;
    protected transient WebButton maximizeButton;
    protected transient WebButton closeButton;

    /**
     * Returns an instance of the {@link WebDockableFrameUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebDockableFrameUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebDockableFrameUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( frame );

        // Installing settings
        installComponents ();
        installActions ();
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling settings
        uninstallActions ();
        uninstallComponents ();

        // Uninstalling applied skin
        StyleManager.uninstallSkin ( frame );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    /**
     * Installs frame decoration elements.
     */
    protected void installComponents ()
    {
        // Frame sidebar button
        sidebarButton = new SidebarButton ( frame );

        // Default frame layout
        frame.setLayout ( new BorderLayout () );

        // Default frame title
        titlePanel = new WebPanel ( StyleId.dockableframeTitlePanel.at ( frame ) );
        frame.add ( titlePanel, BorderLayout.NORTH );

        titleLabel = new WebStyledLabel ( StyleId.dockableframeTitleLabel.at ( titlePanel ), frame.getTitle (), frame.getIcon () );
        titlePanel.add ( titleLabel, BorderLayout.CENTER );

        buttonsPanel = new WebPanel ( StyleId.dockableframeTitleButtonsPanel.at ( titlePanel ), new HorizontalFlowLayout ( 0, false ) );
        titlePanel.add ( buttonsPanel, BorderLayout.LINE_END );

        buttonsPanel.add ( new WebSeparator ( StyleId.dockableframeTitleSeparator.at ( buttonsPanel ) ) );

        minimizeButton = new WebButton ( StyleId.dockableframeTitleIconButton.at ( buttonsPanel ) );
        updateMinimizeButton ();
        buttonsPanel.add ( minimizeButton );

        dockButton = new WebButton ( StyleId.dockableframeTitleIconButton.at ( buttonsPanel ) );
        updateDockButton ();
        buttonsPanel.add ( dockButton );

        floatButton = new WebButton ( StyleId.dockableframeTitleIconButton.at ( buttonsPanel ) );
        updateFloatButton ();
        buttonsPanel.add ( floatButton );

        maximizeButton = new WebButton ( StyleId.dockableframeTitleIconButton.at ( buttonsPanel ) );
        updateMaximizeButton ();
        buttonsPanel.add ( maximizeButton );

        closeButton = new WebButton ( StyleId.dockableframeTitleIconButton.at ( buttonsPanel ) );
        updateCloseButton ();
        buttonsPanel.add ( closeButton );
    }

    /**
     * Uninstalls frame decoration elements.
     */
    protected void uninstallComponents ()
    {
        // Destroying frame decoration
        frame.remove ( titlePanel );
        titlePanel.resetStyleId ();
        titlePanel = null;
        titleLabel = null;
        buttonsPanel = null;
        minimizeButton = null;
        dockButton = null;
        floatButton = null;
        closeButton = null;
        frame.setLayout ( null );

        // Destroying sidebar button
        if ( frame.getDockablePane () != null )
        {
            frame.getDockablePane ().remove ( sidebarButton );
        }
        StyleManager.resetStyleId ( sidebarButton );
        sidebarButton = null;
    }

    /**
     * Installs actions for UI elements.
     */
    protected void installActions ()
    {
        dialogMoveBehavior = new ComponentMoveBehavior ( titlePanel )
        {
            @Override
            public boolean isEnabled ()
            {
                return frame.isFloating ();
            }
        };
        dialogMoveBehavior.install ();

        titlePanel.onMousePress ( MouseButton.left, new MouseEventRunnable ()
        {
            @Override
            public void run ( @NotNull final MouseEvent e )
            {
                // Requesting focus into the frame
                requestFocusInFrame ();
            }
        } );
        titlePanel.onMousePress ( MouseButton.middle, new MouseEventRunnable ()
        {
            @Override
            public void run ( @NotNull final MouseEvent e )
            {
                // Hiding frame on middle mouse button press
                frame.minimize ();
            }
        } );
        titlePanel.onDragStart ( 5, new MouseEventRunnable ()
        {
            @Override
            public void run ( @NotNull final MouseEvent e )
            {
                // Starting frame drag if transfer handler is available
                if ( frame.isDraggable () && !frame.isFloating () && !frame.isMaximized () )
                {
                    final TransferHandler handler = frame.getTransferHandler ();
                    if ( handler != null )
                    {
                        handler.exportAsDrag ( frame, e, TransferHandler.MOVE );
                    }
                }
            }
        } );

        minimizeButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( @NotNull final ActionEvent e )
            {
                frame.minimize ();
            }
        } );

        dockButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( @NotNull final ActionEvent e )
            {
                if ( frame.isPreview () || frame.isFloating () )
                {
                    // Requesting focus into the frame
                    requestFocusInFrame ();

                    // Docking frame
                    frame.dock ();
                }
                else if ( frame.isDocked () )
                {
                    // Minimizing frame
                    frame.minimize ();
                }
            }
        } );

        floatButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( @NotNull final ActionEvent e )
            {
                // Requesting focus into the frame
                requestFocusInFrame ();

                // Detaching frame
                frame.detach ();
            }
        } );

        maximizeButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( @NotNull final ActionEvent e )
            {
                // Requesting focus into the frame
                requestFocusInFrame ();

                // Maximizing frame
                frame.maximize ();
            }
        } );

        closeButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( @NotNull final ActionEvent e )
            {
                // Closing frame
                frame.close ();
            }
        } );

        focusTracker = new DefaultFocusTracker ( frame, true )
        {
            @Override
            public void focusChanged ( final boolean focused )
            {
                // Minimize frame on
                if ( !focused && frame.isPreview () )
                {
                    frame.minimize ();
                }
            }
        };
        FocusManager.addFocusTracker ( frame, focusTracker );

        frame.addPropertyChangeListener ( this );

        frame.setTransferHandler ( new DockableFrameTransferHandler () );
    }

    /**
     * Uninstalls actions from UI elements.
     */
    protected void uninstallActions ()
    {
        frame.setTransferHandler ( null );

        frame.removePropertyChangeListener ( this );

        FocusManager.removeFocusTracker ( frame, focusTracker );
        focusTracker = null;

        dialogMoveBehavior.uninstall ();
        dialogMoveBehavior = null;
    }

    @Override
    public void propertyChange ( @NotNull final PropertyChangeEvent evt )
    {
        final String property = evt.getPropertyName ();
        if ( Objects.equals ( property, WebDockableFrame.STATE_PROPERTY ) )
        {
            final DockableFrameState oldState = ( DockableFrameState ) evt.getOldValue ();
            final DockableFrameState newState = ( DockableFrameState ) evt.getNewValue ();

            // Updating restore state
            if ( oldState == DockableFrameState.docked || oldState == DockableFrameState.floating )
            {
                frame.setRestoreState ( oldState );
            }

            // Updating sidebar button states
            sidebarButton.updateStates ();

            // Updating buttons
            updateMinimizeButton ();
            updateDockButton ();
            updateFloatButton ();
            updateMaximizeButton ();

            // Resetting maximized mark on any state change
            if ( frame.isMaximized () )
            {
                frame.setMaximized ( false );
            }

            // Informing frame listeners
            frame.fireFrameStateChanged ( oldState, newState );

            // Requesting frame focus on preview or dock
            if ( newState == DockableFrameState.preview || newState == DockableFrameState.docked )
            {
                requestFocusInFrame ();
            }
        }
        else if ( Objects.equals ( property, WebDockableFrame.MAXIMIZABLE_PROPERTY ) )
        {
            // Updating maximize button
            updateMaximizeButton ();

            // Cancelling maximized state since it is not allowed anymore
            if ( !frame.isMaximizable () && frame.isMaximized () )
            {
                frame.setMaximized ( false );
            }
        }
        else if ( Objects.equals ( property, WebDockableFrame.MAXIMIZED_PROPERTY ) )
        {
            // Updating maximize button
            updateMaximizeButton ();
        }
        else if ( Objects.equals ( property, WebDockableFrame.CLOSABLE_PROPERTY ) )
        {
            // Updating close button
            updateCloseButton ();
        }
        else if ( Objects.equals ( property, WebDockableFrame.FLOATABLE_PROPERTY ) )
        {
            // Updating float button
            updateFloatButton ();
        }
        else if ( Objects.equals ( property, WebDockableFrame.ICON_PROPERTY ) )
        {
            // Updating title and sidebar button icons
            titleLabel.setIcon ( frame.getIcon () );
            sidebarButton.setIcon ( frame.getIcon () );
        }
        else if ( Objects.equals ( property, WebDockableFrame.TITLE_PROPERTY ) )
        {
            // Updating title and sidebar button texts
            titleLabel.setText ( frame.getTitle () );
            sidebarButton.setText ( frame.getTitle () );
        }
        else if ( Objects.equals ( property, WebDockableFrame.POSITION_PROPERTY ) )
        {
            // Updating sidebar button states
            sidebarButton.updateStates ();
        }
    }

    /**
     * Updates dock button state.
     */
    private void updateMinimizeButton ()
    {
        minimizeButton.setVisible ( frame.isDocked () || frame.isFloating () );
        minimizeButton.setIcon ( Icons.underline );
        minimizeButton.setRolloverIcon ( Icons.underlineHover );
        minimizeButton.setLanguage ( "weblaf.ex.dockable.frame.minimize" );
    }

    /**
     * Updates dock button state.
     */
    private void updateDockButton ()
    {
        dockButton.setVisible ( !frame.isDocked () );
        dockButton.setIcon ( Icons.pin );
        dockButton.setRolloverIcon ( Icons.pinHover );
        dockButton.setLanguage ( "weblaf.ex.dockable.frame.dock" );
    }

    /**
     * Updates float button state.
     */
    protected void updateFloatButton ()
    {
        floatButton.setVisible ( frame.isFloatable () && !frame.isFloating () );
        floatButton.setIcon ( Icons.external );
        floatButton.setRolloverIcon ( Icons.externalHover );
        floatButton.setLanguage ( "weblaf.ex.dockable.frame.float" );
    }

    /**
     * Updates maximize button state.
     */
    protected void updateMaximizeButton ()
    {
        maximizeButton.setVisible ( frame.isMaximizable () && frame.isDocked () );
        maximizeButton.setIcon ( frame.isMaximized () ? Icons.shrink : Icons.maximize );
        maximizeButton.setRolloverIcon ( frame.isMaximized () ? Icons.shrinkHover : Icons.maximizeHover );
        maximizeButton.setLanguage ( "weblaf.ex.dockable.frame." + ( frame.isMaximized () ? "restore" : "maximize" ) );
    }

    /**
     * Updates close button state.
     */
    protected void updateCloseButton ()
    {
        closeButton.setVisible ( frame.isClosable () );
        closeButton.setIcon ( Icons.cross );
        closeButton.setRolloverIcon ( Icons.crossHover );
        closeButton.setLanguage ( "weblaf.ex.dockable.frame.close" );
    }

    @NotNull
    @Override
    public Dimension getMinimumDialogSize ()
    {
        final Dimension ps;
        if ( titlePanel.isVisible () )
        {
            ps = titlePanel.getPreferredSize ();
        }
        else
        {
            if ( frame.getDockablePane () != null )
            {
                ps = frame.getDockablePane ().getMinimumElementSize ();
            }
            else
            {
                ps = new Dimension ( 0, 0 );
            }
        }
        final Insets bi = frame.getInsets ();
        return SwingUtils.stretch ( ps, bi );
    }

    /**
     * Requests focus for frame content if possible.
     */
    protected void requestFocusInFrame ()
    {
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                if ( !SwingUtils.hasFocusOwner ( frame ) )
                {
                    final Component component = SwingUtils.findFocusableComponent ( frame );
                    if ( component != null )
                    {
                        // Pass focus to the first focusable component within container
                        component.requestFocusInWindow ();
                    }
                    else
                    {
                        // Pass focus onto the frame itself
                        // Normally focus will never get onto the frame, but we can still use it when we have no other options
                        frame.requestFocusInWindow ();
                    }
                }
            }
        } );
    }

    @NotNull
    @Override
    public SidebarButton getSidebarButton ()
    {
        return sidebarButton;
    }

    @Override
    public boolean contains ( @NotNull final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, x, y );
    }

    @Override
    public int getBaseline ( @NotNull final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, width, height );
    }

    @NotNull
    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( @NotNull final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this );
    }

    @Override
    public void paint ( @NotNull final Graphics g, @NotNull final JComponent c )
    {
        PainterSupport.paint ( g, c, this );
    }

    @Nullable
    @Override
    public Dimension getPreferredSize ( @NotNull final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c );
    }
}