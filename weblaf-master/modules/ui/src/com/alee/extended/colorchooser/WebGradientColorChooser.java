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

package com.alee.extended.colorchooser;

import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Objects;
import com.alee.laf.colorchooser.WebColorChooser;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.UISettingsManager;
import com.alee.utils.ColorUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.extensions.SizeMethods;
import com.alee.utils.swing.extensions.SizeMethodsImpl;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebGradientColorChooser extends JComponent implements MouseListener, MouseMotionListener, FocusListener, SettingsMethods,
        SizeMethods<WebGradientColorChooser>
{
    /**
     * todo 1. Revamp to make use of UI and styling system
     * todo 2. Allow having empty data
     */

    // Style constants
    private static final Color borderColor = Color.DARK_GRAY; // new Color ( 51, 51, 51 );
    private static final Color disabledBorderColor = Color.LIGHT_GRAY;
    private static final Color foreground = Color.BLACK;
    private static final Color disabledForeground = new Color ( 178, 178, 178 );
    private static final Color innerBorderColor = whiteColor ( 180 );
    private static final float[] overlayFractions = new float[]{ 0f, 0.4f, 0.41f, 1f };
    private static final Color[] lineOverlayColors =
            new Color[]{ whiteColor ( 160 ), whiteColor ( 120 ), whiteColor ( 100 ), whiteColor ( 40 ) };
    private static final Color[] controlOverlayColors =
            new Color[]{ whiteColor ( 80 ), whiteColor ( 50 ), whiteColor ( 20 ), ColorUtils.transparent () };
    private static final float closestPoint = 0.001f;

    // Runtime data
    private final List<ChangeListener> changeListeners = new ArrayList<ChangeListener> ( 1 );
    private GradientData gradientData;

    // Settings
    private int shadeWidth = WebGradientColorChooserStyle.shadeWidth;
    private int lineWidth = WebGradientColorChooserStyle.lineWidth;
    private Dimension gripperSize = WebGradientColorChooserStyle.gripperSize;
    private Insets margin = WebGradientColorChooserStyle.margin;
    private boolean paintLabels = WebGradientColorChooserStyle.paintLabels;

    // Runtime values
    private GradientColorData draggedGripper = null;
    private boolean draggedOut = false;

    public WebGradientColorChooser ()
    {
        this ( new GradientData () );
    }

    public WebGradientColorChooser ( final GradientData gradientData )
    {
        super ();

        setGradientData ( gradientData );

        setFocusable ( true );
        setFont ( WebGradientColorChooserStyle.labelsFont );
        setForeground ( WebGradientColorChooserStyle.foreground );

        addFocusListener ( this );
        addMouseListener ( this );
        addMouseMotionListener ( this );
    }

    @Override
    public void focusGained ( final FocusEvent e )
    {
        repaint ();
    }

    @Override
    public void focusLost ( final FocusEvent e )
    {
        repaint ();
    }

    @Override
    public void mouseClicked ( final MouseEvent e )
    {
        if ( !isEnabled () )
        {
            return;
        }

        // Displaying color chooser dialog on double-click
        if ( SwingUtils.isDoubleClick ( e ) )
        {
            final GradientColorData colorData = getColorDataUnderPoint ( e.getPoint () );
            if ( colorData != null )
            {
                final Color newColor = WebColorChooser.showDialog ( WebGradientColorChooser.this, colorData.getColor () );
                if ( newColor != null )
                {
                    // Updating color
                    colorData.setColor ( newColor );

                    fireStateChanged ();
                    repaint ();
                }
            }
        }
    }

    @Override
    public void mousePressed ( final MouseEvent e )
    {
        if ( !isEnabled () )
        {
            return;
        }

        if ( !isFocusOwner () )
        {
            requestFocusInWindow ();
        }

        if ( SwingUtilities.isLeftMouseButton ( e ) )
        {
            final GradientColorData colorData = getColorDataUnderPoint ( e.getPoint () );
            if ( colorData != null )
            {
                if ( SwingUtils.isAlt ( e ) )
                {
                    // Copying and dragging copied color
                    draggedGripper = colorData.clone ();
                    draggedGripper.setLocation ( findFreeLocation ( draggedGripper.getLocation () ) );
                    gradientData.addGradientColorData ( draggedGripper );
                    draggedOut = false;

                    fireStateChanged ();
                    repaint ();
                }
                else
                {
                    // Dragging color
                    draggedGripper = colorData;
                    draggedOut = false;
                }
            }
            if ( draggedGripper == null && e.getY () > 0 )
            {
                // Checking that click location doesn't have a color
                final float newLocation = getLocationForPoint ( e );
                if ( isFree ( newLocation ) )
                {
                    // Creating new color when clicked not on gripper
                    draggedGripper = new GradientColorData ( newLocation, getColorForLocation ( newLocation ) );
                    gradientData.addGradientColorData ( draggedGripper );
                    draggedOut = false;

                    fireStateChanged ();
                    repaint ();
                }
            }
        }
        else if ( ( SwingUtils.isMiddleMouseButton ( e ) || SwingUtils.isRightMouseButton ( e ) ) && gradientData.size () > 2 )
        {
            final GradientColorData colorData = getColorDataUnderPoint ( e.getPoint () );
            if ( colorData != null )
            {
                // Removing color
                gradientData.removeGradientColorData ( colorData );

                fireStateChanged ();
                repaint ();
            }
        }
    }

    public GradientColorData getColorDataUnderPoint ( final Point point )
    {
        GradientColorData colorData = null;
        for ( int i = gradientData.size () - 1; i >= 0; i-- )
        {
            final GradientColorData gcd = gradientData.get ( i );
            if ( getGripperBounds ( gcd ).contains ( point ) )
            {
                colorData = gcd;
                break;
            }
        }
        return colorData;
    }

    @Override
    public void mouseDragged ( final MouseEvent e )
    {
        if ( !isEnabled () )
        {
            return;
        }

        if ( draggedGripper != null )
        {
            if ( e.getY () < -10 && gradientData.size () + ( draggedOut ? 1 : 0 ) > 2 )
            {
                if ( !draggedOut )
                {
                    // Removing color when dragged out at top
                    draggedOut = true;
                    gradientData.removeGradientColorData ( draggedGripper );

                    fireStateChanged ();
                    repaint ();
                }
            }
            else
            {
                // Restoring color when dragged back
                if ( draggedOut )
                {
                    draggedOut = false;
                    gradientData.addGradientColorData ( draggedGripper );
                }

                // Updating location
                float newLocation = getLocationForPoint ( e );
                newLocation = findFreeLocation ( newLocation );
                draggedGripper.setLocation ( newLocation );

                fireStateChanged ();
                repaint ();
            }
        }
    }

    @Override
    public void mouseReleased ( final MouseEvent e )
    {
        if ( !isEnabled () )
        {
            return;
        }

        if ( SwingUtilities.isLeftMouseButton ( e ) )
        {
            draggedGripper = null;
        }
    }

    @Override
    public void mouseEntered ( final MouseEvent e )
    {
        //
    }

    @Override
    public void mouseExited ( final MouseEvent e )
    {
        //
    }

    @Override
    public void mouseMoved ( final MouseEvent e )
    {
        //
    }

    public boolean isFree ( final float location )
    {
        return !gradientData.containtsLocation ( location );
    }

    private float findFreeLocation ( float newLocation )
    {
        if ( gradientData.containtsLocation ( newLocation ) )
        {
            if ( newLocation == 1f )
            {
                while ( gradientData.containtsLocation ( newLocation ) )
                {
                    newLocation -= closestPoint;
                }
            }
            else
            {
                while ( gradientData.containtsLocation ( newLocation ) )
                {
                    newLocation += closestPoint;
                }
            }
        }
        return newLocation;
    }

    public Rectangle getGripperBounds ( final int index )
    {
        return getGripperBounds ( gradientData.getGradientColorsData ().get ( index ) );
    }

    public Rectangle getGripperBounds ( final GradientColorData colorData )
    {
        final float location = colorData.getLocation ();
        final Rectangle lineBounds = getLineBounds ();
        final int x = lineBounds.x + 2 + Math.round ( ( lineBounds.width - 4 ) * location ) - gripperSize.width / 2;
        final int y = lineBounds.y + lineBounds.height - gripperSize.height / 2;
        return new Rectangle ( x, y, gripperSize.width, gripperSize.height );
    }

    private float getLocationForPoint ( final MouseEvent e )
    {
        return getLocationForPoint ( e.getPoint () );
    }

    public float getLocationForPoint ( final Point point )
    {
        final Rectangle lineBounds = getLineBounds ();
        final int x = lineBounds.x + 2;
        final int w = lineBounds.width - 4;
        return ( float ) Math.max ( 0, Math.min ( point.x - x, w ) ) / w;
    }

    public Color getColorForLocation ( final float location )
    {
        return gradientData.getColorForLocation ( location );
    }

    public GradientData getGradientData ()
    {
        return gradientData;
    }

    public void setGradientData ( final GradientData gradientData )
    {
        if ( Objects.notEquals ( this.gradientData, gradientData ) )
        {
            this.gradientData = gradientData;
            this.draggedGripper = null;
            fireStateChanged ();
            repaint ();
        }
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    public void setShadeWidth ( final int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
        updateBorder ();
    }

    public int getLineWidth ()
    {
        return lineWidth;
    }

    public void setLineWidth ( final int lineWidth )
    {
        this.lineWidth = lineWidth;
    }

    public Dimension getGripperSize ()
    {
        return gripperSize;
    }

    public void setGripperSize ( final Dimension gripperSize )
    {
        this.gripperSize = gripperSize;
    }

    public Insets getMargin ()
    {
        return margin;
    }

    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        updateBorder ();
    }

    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    public void setMargin ( final int spacing )
    {
        setMargin ( spacing, spacing, spacing, spacing );
    }

    private void updateBorder ()
    {
        setBorder ( BorderFactory.createEmptyBorder ( margin.top, margin.left, margin.bottom, margin.right ) );
    }

    public boolean isPaintLabels ()
    {
        return paintLabels;
    }

    public void setPaintLabels ( final boolean paintLabels )
    {
        this.paintLabels = paintLabels;
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );

        final Graphics2D g2d = ( Graphics2D ) g;
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        // Line bounds
        final Rectangle lineBounds = getLineBounds ();

        // Painting main gradient line
        paintBorderedBackground ( g2d, lineBounds, getLineBackgroundPainter ( lineBounds ), getLineOverlayPainter ( lineBounds ), false );

        // Painting controls
        paintControls ( g2d, lineBounds );

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    private void paintControls ( final Graphics2D g2d, final Rectangle lineBounds )
    {
        final int x = lineBounds.x + 2;
        final int w = lineBounds.width - 4;
        final int y = lineBounds.y + lineBounds.height - gripperSize.height / 2;
        final int textY = y + gripperSize.height;
        final FontMetrics fm = g2d.getFontMetrics ();
        final int lineY = textY - LafUtils.getTextCenterShiftY ( fm ) - 1;
        for ( int i = 0; i < gradientData.size (); i++ )
        {
            final GradientColorData gradientColorData = gradientData.get ( i );
            final float currentLocation = gradientColorData.getLocation ();
            final int currentX = x + Math.round ( w * currentLocation ) - gripperSize.width / 2;
            final Rectangle controlBounds = new Rectangle ( currentX, y, gripperSize.width, gripperSize.height );

            // Painting control
            paintBorderedBackground ( g2d, controlBounds, gradientColorData.getColor (), getControlOverlayPainter ( controlBounds ), true );

            // Painting text and separators between controls if there is enough space
            if ( paintLabels && i < gradientData.size () - 1 )
            {
                final GradientColorData nextGradientColorData = gradientData.get ( i + 1 );
                final int gapX1 = currentX + gripperSize.width;
                final int gapX2 = x + Math.round ( w * nextGradientColorData.getLocation () ) - gripperSize.width / 2;
                final int gapInPercents = Math.round ( ( nextGradientColorData.getLocation () - currentLocation ) * 100 );
                final int textWidth = fm.stringWidth ( gapInPercents + "" );
                if ( gapX2 - gapX1 > textWidth + 4 )
                {
                    final int textX = ( gapX1 + gapX2 ) / 2 - textWidth / 2;

                    // Painting text
                    g2d.setPaint ( isEnabled () ? foreground : disabledForeground );
                    g2d.drawString ( gapInPercents + "", textX, textY );

                    // Painting separators
                    if ( gapX2 - gapX1 > textWidth + 14 )
                    {
                        g2d.setPaint ( new GradientPaint ( gapX1 + 2, 0, ColorUtils.transparent (), textX - 2, 0, Color.LIGHT_GRAY ) );
                        g2d.drawLine ( gapX1 + 2, lineY, textX - 2, lineY );
                        g2d.setPaint ( new GradientPaint ( textX + textWidth + 2, 0, Color.LIGHT_GRAY, gapX2 - 2, 0,
                                ColorUtils.transparent () ) );
                        g2d.drawLine ( textX + textWidth + 2, lineY, gapX2 - 2, lineY );
                    }
                }
            }
        }
    }

    private void paintBorderedBackground ( final Graphics2D g2d, final Rectangle bounds, final Paint background, final Paint overlay,
                                           final boolean control )
    {
        // Shade & focus
        if ( isEnabled () && !control )
        {
            GraphicsUtils.drawShade ( g2d, new RoundRectangle2D.Double ( bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, 4, 4 ),
                    isFocusOwner () ? new Color ( 85, 142, 239 ) : new Color ( 170, 170, 170 ), shadeWidth );
        }

        // Background
        g2d.setPaint ( background );
        g2d.fillRoundRect ( bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2, 2, 2 );

        // Inner border
        g2d.setPaint ( innerBorderColor );
        g2d.drawRoundRect ( bounds.x + 1, bounds.y + 1, bounds.width - 3, bounds.height - 3, 2, 2 );

        // Additional control overlay
        if ( control )
        {
            final int alpha = 180;
            g2d.setPaint ( new GradientPaint ( bounds.x + 2, bounds.y + 2, whiteColor ( alpha ), bounds.x + bounds.width * 2 / 3,
                    bounds.y + bounds.height - 2, ColorUtils.transparent () ) );
            g2d.fillRect ( bounds.x + 2, bounds.y + 2, bounds.width - 4, bounds.height - 4 );
        }

        // Overlay
        g2d.setPaint ( overlay );
        g2d.fillRect ( bounds.x + 2, bounds.y + 2, bounds.width - 4, bounds.height - 4 );

        // Border
        g2d.setPaint ( isEnabled () ? borderColor : disabledBorderColor );
        g2d.drawRoundRect ( bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, 4, 4 );
    }

    private LinearGradientPaint getLineBackgroundPainter ( final Rectangle lineBounds )
    {
        return new LinearGradientPaint ( lineBounds.x + 1, 0, lineBounds.x + lineBounds.width - 1, 0, gradientData.getFractions (),
                gradientData.getColors () );
    }

    private LinearGradientPaint getLineOverlayPainter ( final Rectangle bounds )
    {
        return getOverlayPainter ( bounds, overlayFractions, lineOverlayColors );
    }

    private LinearGradientPaint getControlOverlayPainter ( final Rectangle bounds )
    {
        return getOverlayPainter ( bounds, overlayFractions, controlOverlayColors );
    }

    private LinearGradientPaint getOverlayPainter ( final Rectangle bounds, final float[] overlayFractions, final Color[] overlayColors )
    {
        return new LinearGradientPaint ( 0, bounds.y + 2, 0, bounds.y + bounds.height - 2, overlayFractions, overlayColors );
    }

    private Rectangle getLineBounds ()
    {
        final Insets insets = getInsets ();
        return new Rectangle ( insets.left + Math.max ( gripperSize.width / 2, shadeWidth ) - 2, insets.top + shadeWidth,
                getWidth () - insets.left - insets.right - Math.max ( gripperSize.width, shadeWidth * 2 ) + 4, lineWidth );
    }

    public List<ChangeListener> getChangeListeners ()
    {
        return changeListeners;
    }

    public void addChangeListener ( final ChangeListener dataChangeListener )
    {
        changeListeners.add ( dataChangeListener );
    }

    public void removeChangeListener ( final ChangeListener dataChangeListener )
    {
        changeListeners.remove ( dataChangeListener );
    }

    private void fireStateChanged ()
    {
        final ChangeEvent changeEvent = new ChangeEvent ( WebGradientColorChooser.this );
        for ( final ChangeListener dataChangeListener : changeListeners )
        {
            dataChangeListener.stateChanged ( changeEvent );
        }
    }

    @Override
    public void registerSettings ( final Configuration configuration )
    {
        UISettingsManager.registerComponent ( this, configuration );
    }

    @Override
    public void registerSettings ( final SettingsProcessor processor )
    {
        UISettingsManager.registerComponent ( this, processor );
    }

    @Override
    public void unregisterSettings ()
    {
        UISettingsManager.unregisterComponent ( this );
    }

    @Override
    public void loadSettings ()
    {
        UISettingsManager.loadSettings ( this );
    }

    @Override
    public void saveSettings ()
    {
        UISettingsManager.saveSettings ( this );
    }

    @Override
    public int getPreferredWidth ()
    {
        return SizeMethodsImpl.getPreferredWidth ( this );
    }

    @NotNull
    @Override
    public WebGradientColorChooser setPreferredWidth ( final int preferredWidth )
    {
        return SizeMethodsImpl.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeMethodsImpl.getPreferredHeight ( this );
    }

    @NotNull
    @Override
    public WebGradientColorChooser setPreferredHeight ( final int preferredHeight )
    {
        return SizeMethodsImpl.setPreferredHeight ( this, preferredHeight );
    }

    @NotNull
    @Override
    public Dimension getPreferredSize ()
    {
        return SizeMethodsImpl.getPreferredSize ( this, getActualPreferredSize () );
    }

    @NotNull
    @Override
    public Dimension getOriginalPreferredSize ()
    {
        return SizeMethodsImpl.getOriginalPreferredSize ( this, getActualPreferredSize () );
    }

    @NotNull
    @Override
    public WebGradientColorChooser setPreferredSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setPreferredSize ( this, width, height );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeMethodsImpl.getMaximumWidth ( this );
    }

    @NotNull
    @Override
    public WebGradientColorChooser setMaximumWidth ( final int maximumWidth )
    {
        return SizeMethodsImpl.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeMethodsImpl.getMaximumHeight ( this );
    }

    @NotNull
    @Override
    public WebGradientColorChooser setMaximumHeight ( final int maximumHeight )
    {
        return SizeMethodsImpl.setMaximumHeight ( this, maximumHeight );
    }

    @NotNull
    @Override
    public Dimension getMaximumSize ()
    {
        return SizeMethodsImpl.getMaximumSize ( this, super.getMaximumSize () );
    }

    @NotNull
    @Override
    public Dimension getOriginalMaximumSize ()
    {
        return SizeMethodsImpl.getOriginalMaximumSize ( this, super.getMaximumSize () );
    }

    @NotNull
    @Override
    public WebGradientColorChooser setMaximumSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setMaximumSize ( this, width, height );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeMethodsImpl.getMinimumWidth ( this );
    }

    @NotNull
    @Override
    public WebGradientColorChooser setMinimumWidth ( final int minimumWidth )
    {
        return SizeMethodsImpl.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeMethodsImpl.getMinimumHeight ( this );
    }

    @NotNull
    @Override
    public WebGradientColorChooser setMinimumHeight ( final int minimumHeight )
    {
        return SizeMethodsImpl.setMinimumHeight ( this, minimumHeight );
    }

    @NotNull
    @Override
    public Dimension getMinimumSize ()
    {
        return SizeMethodsImpl.getMinimumSize ( this, super.getMinimumSize () );
    }

    @NotNull
    @Override
    public Dimension getOriginalMinimumSize ()
    {
        return SizeMethodsImpl.getOriginalMinimumSize ( this, super.getMinimumSize () );
    }

    @NotNull
    @Override
    public WebGradientColorChooser setMinimumSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setMinimumSize ( this, width, height );
    }

    protected Dimension getActualPreferredSize ()
    {
        // todo Move to custom UI
        final Insets i = getInsets ();
        final int width = i.left + gripperSize.width * 5 + Math.max ( gripperSize.width, shadeWidth * 2 ) - 4 + i.right;
        final int height = i.top + shadeWidth * 2 + lineWidth + gripperSize.height / 2 + ( gripperSize.height % 2 == 0 ? 0 : 1 ) + i.bottom;
        return new Dimension ( width, height );
    }

    @NotNull
    private static Color whiteColor ( final int alpha )
    {
        return new Color ( 255, 255, 255, alpha );
    }
}