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

package com.alee.utils.ninepatch;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.merge.Overwriting;
import com.alee.api.resource.Resource;
import com.alee.utils.ImageUtils;
import com.alee.utils.NinePatchUtils;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Android 9-patch images {@link Icon} implementation for Swing components.
 * It parses 9-patch image data and uses it to properly stretch upon painting on specific {@link JComponent}.
 *
 * @author Mikle Garin
 */
@XStreamConverter ( NinePatchIconConverter.class )
public class NinePatchIcon implements Icon, Overwriting
{
    /**
     * Raw image without patches.
     */
    @NotNull
    protected BufferedImage rawImage;

    /**
     * Horizontal stretch intervals taken from image patches (top image patches).
     * Note that non-stretched parts are also included here and are marked as {@link NinePatchInterval#isPixel()}.
     */
    @NotNull
    protected List<NinePatchInterval> horizontalStretch;

    /**
     * Vertical stretch intervals taken from image patches (left image patches).
     * Note that non-stretched parts are also included here and are marked as {@link NinePatchInterval#isPixel()}.
     */
    @NotNull
    protected List<NinePatchInterval> verticalStretch;

    /**
     * Content margin taken from image patches (right and bottom patches).
     * This margin is generally valuable for components which uses this icon as a background to set their style margins properly.
     */
    @NotNull
    protected Insets margin;

    /**
     * Cached fixed areas width of the nine-patch image with additional 1px for each stretchable area.
     */
    @Nullable
    protected Integer cachedWidth0;

    /**
     * Cached fixed areas width of the nine-patch image.
     */
    @Nullable
    protected Integer cachedWidth1;

    /**
     * Cached fixed areas height of the nine-patch image with additional 1px for each stretchable area.
     */
    @Nullable
    protected Integer cachedHeight0;

    /**
     * Cached fixed areas height of the nine-patch image.
     */
    @Nullable
    protected Integer cachedHeight1;

    /**
     * {@link JComponent} on which this {@link NinePatchIcon} is painted.
     */
    @Nullable
    protected transient WeakReference<JComponent> component;

    /**
     * Constructs new NinePatchIcon using the nine-patch image from the specified path.
     *
     * @param resource {@link Resource} of image with patch information on it
     */
    public NinePatchIcon ( @NotNull final Resource resource )
    {
        this ( resource, true );
    }

    /**
     * Constructs new NinePatchIcon using the nine-patch image from the specified path.
     *
     * @param resource     {@link Resource} of image
     * @param parsePatches whether or not information about patches should be parsed from the image
     */
    public NinePatchIcon ( @NotNull final Resource resource, final boolean parsePatches )
    {
        this ( ImageUtils.loadBufferedImage ( resource ), parsePatches );
    }

    /**
     * Constructs new NinePatchIcon using the specified nine-patch image.
     *
     * @param icon {@link Icon} with patch information on it
     */
    public NinePatchIcon ( @NotNull final Icon icon )
    {
        this ( icon, true );
    }

    /**
     * Constructs new NinePatchIcon using the specified nine-patch image.
     *
     * @param icon         {@link Icon}
     * @param parsePatches whether or not information about patches should be parsed from the image
     */
    public NinePatchIcon ( @NotNull final Icon icon, final boolean parsePatches )
    {
        this ( ImageUtils.toNonNullBufferedImage ( icon ), parsePatches );
    }

    /**
     * Constructs new NinePatchIcon using the specified nine-patch image.
     *
     * @param image {@link Image} with patch information on it
     */
    public NinePatchIcon ( @NotNull final Image image )
    {
        this ( image, true );
    }

    /**
     * Constructs new NinePatchIcon using the specified nine-patch image.
     *
     * @param image        {@link Image}
     * @param parsePatches whether or not information about patches should be parsed from the image
     */
    public NinePatchIcon ( @NotNull final Image image, final boolean parsePatches )
    {
        this ( ImageUtils.toNonNullBufferedImage ( image ), parsePatches );
    }

    /**
     * Constructs new NinePatchIcon using the specified nine-patch image.
     *
     * @param image {@link BufferedImage} with patch information on it
     */
    public NinePatchIcon ( @NotNull final BufferedImage image )
    {
        this ( image, true );
    }

    /**
     * Constructs new NinePatchIcon using the specified nine-patch image.
     *
     * @param image        {@link BufferedImage}
     * @param parsePatches whether or not information about patches should be parsed from the image
     */
    public NinePatchIcon ( @NotNull final BufferedImage image, final boolean parsePatches )
    {
        // Parsing patches or creating new 9-patch icon
        if ( parsePatches )
        {
            // Incorrect image
            if ( image.getWidth () < 3 || image.getHeight () < 3 )
            {
                throw new IllegalArgumentException ( "Buffered image must be atleast 3x3 pixels size" );
            }

            // Creating actual image in a compatible format
            final int w = image.getWidth () - 2;
            final int h = image.getHeight () - 2;
            rawImage = ImageUtils.createCompatibleImage ( w, h, image.getTransparency () );
            final Graphics2D g2d = rawImage.createGraphics ();
            g2d.drawImage ( image, 0, 0, w, h, 1, 1, image.getWidth () - 1, image.getHeight () - 1, null );
            g2d.dispose ();

            // Parsing stretch variables
            horizontalStretch = NinePatchUtils.parseIntervals ( image, NinePatchIntervalType.horizontalStretch );
            verticalStretch = NinePatchUtils.parseIntervals ( image, NinePatchIntervalType.verticalStretch );

            // Incorrect image
            if ( !( ( horizontalStretch.size () > 1 || horizontalStretch.size () == 1 && !horizontalStretch.get ( 0 ).isPixel () ) &&
                    ( verticalStretch.size () > 1 || verticalStretch.size () == 1 && !verticalStretch.get ( 0 ).isPixel () ) ) )
            {
                throw new IllegalArgumentException ( "There must be stretch constraints specified on image" );
            }

            // Parsing content margins
            final List<NinePatchInterval> vc = NinePatchUtils.parseIntervals ( image, NinePatchIntervalType.verticalContent );
            final List<NinePatchInterval> hc = NinePatchUtils.parseIntervals ( image, NinePatchIntervalType.horizontalContent );
            final int top = vc.size () == 0 ? 0 : vc.get ( 0 ).getStart ();
            final int bottom = vc.size () == 0 ? 0 : rawImage.getHeight () - vc.get ( 0 ).getEnd () - 1;
            final int left = hc.size () == 0 ? 0 : hc.get ( 0 ).getStart ();
            final int right = hc.size () == 0 ? 0 : rawImage.getWidth () - hc.get ( 0 ).getEnd () - 1;
            margin = new Insets ( top, left, bottom, right );

            // Forcing cached data calculation on initialization
            getFixedPixelsWidth ( true );
            getFixedPixelsWidth ( false );
            getFixedPixelsHeight ( true );
            getFixedPixelsHeight ( false );
        }
        else
        {
            // Actual image
            this.rawImage = image;

            // Stretch variables
            horizontalStretch = new ArrayList<NinePatchInterval> ();
            verticalStretch = new ArrayList<NinePatchInterval> ();

            // Empty margin
            margin = new Insets ( 0, 0, 0, 0 );
        }
    }

    /**
     * Always returns {@code true} to avoid any merge operations between {@link NinePatchIcon} instances.
     *
     * @return always {@code true} to avoid any merge operations between {@link NinePatchIcon} instances
     */
    @Override
    public boolean isOverwrite ()
    {
        return true;
    }

    /**
     * Returns raw image without patches.
     *
     * @return raw image without patches
     */
    @NotNull
    public BufferedImage getRawImage ()
    {
        return rawImage;
    }

    /**
     * Returns {@link JComponent} on which this {@link NinePatchIcon} is painted.
     *
     * @return {@link JComponent} on which this {@link NinePatchIcon} is painted
     */
    @Nullable
    public JComponent getComponent ()
    {
        return component != null ? component.get () : null;
    }

    /**
     * Sets {@link JComponent} on which this {@link NinePatchIcon} is painted.
     *
     * @param component {@link JComponent} on which this {@link NinePatchIcon} is painted
     */
    public void setComponent ( @Nullable final JComponent component )
    {
        this.component = component != null ? new WeakReference<JComponent> ( component ) : null;
    }

    /**
     * Returns list of horizontal stretch intervals taken from image patches.
     *
     * @return list of horizontal stretch intervals taken from image patches
     */
    @NotNull
    public List<NinePatchInterval> getHorizontalStretch ()
    {
        return horizontalStretch;
    }

    /**
     * Sets list of horizontal stretch intervals.
     *
     * @param horizontalStretch list of horizontal stretch intervals
     */
    public void setHorizontalStretch ( @NotNull final List<NinePatchInterval> horizontalStretch )
    {
        this.horizontalStretch = horizontalStretch;
        updateCachedWidthData ();
    }

    /**
     * Adds horizontal stretch interval.
     *
     * @param interval horizontal stretch interval to add
     */
    public void addHorizontalStretch ( @NotNull final NinePatchInterval interval )
    {
        this.horizontalStretch.add ( interval );
        updateCachedWidthData ();
    }

    /**
     * Adds horizontal stretch interval.
     *
     * @param start interval start
     * @param end   interval end
     * @param pixel whether fixed interval or not
     */
    public void addHorizontalStretch ( final int start, final int end, final boolean pixel )
    {
        addHorizontalStretch ( new NinePatchInterval ( start, end, pixel ) );
    }

    /**
     * Returns list of vertical stretch intervals taken from image patches.
     *
     * @return list of vertical stretch intervals taken from image patches
     */
    @NotNull
    public List<NinePatchInterval> getVerticalStretch ()
    {
        return verticalStretch;
    }

    /**
     * Sets list of vertical stretch intervals.
     *
     * @param verticalStretch list of vertical stretch intervals
     */
    public void setVerticalStretch ( @NotNull final List<NinePatchInterval> verticalStretch )
    {
        this.verticalStretch = verticalStretch;
        updateCachedHeightData ();
    }

    /**
     * Adds vertical stretch interval.
     *
     * @param interval vertical stretch interval to add
     */
    public void addVerticalStretch ( @NotNull final NinePatchInterval interval )
    {
        this.verticalStretch.add ( interval );
        updateCachedHeightData ();
    }

    /**
     * Adds vertical stretch interval.
     *
     * @param start interval start
     * @param end   interval end
     * @param pixel whether fixed interval or not
     */
    public void addVerticalStretch ( final int start, final int end, final boolean pixel )
    {
        addVerticalStretch ( new NinePatchInterval ( start, end, pixel ) );
    }

    /**
     * Returns margin taken from image content patches.
     *
     * @return margin taken from image content patches
     */
    @NotNull
    public Insets getMargin ()
    {
        return ( Insets ) margin.clone ();
    }

    /**
     * Returns margin taken from image stretch patches.
     *
     * @return margin taken from image stretch patches
     */
    @NotNull
    public Insets getStretchMargin ()
    {
        final NinePatchInterval top = verticalStretch.get ( 0 );
        final NinePatchInterval left = horizontalStretch.get ( 0 );
        final NinePatchInterval bottom = verticalStretch.get ( verticalStretch.size () - 1 );
        final NinePatchInterval right = horizontalStretch.get ( horizontalStretch.size () - 1 );
        return new Insets ( top.getLength (), left.getLength (), bottom.getLength (), right.getLength () );
    }

    /**
     * Sets content margin.
     *
     * @param margin content margin
     */
    public void setMargin ( @NotNull final Insets margin )
    {
        this.margin = margin;
    }

    /**
     * Sets content margin.
     *
     * @param top    top margin
     * @param left   left margin
     * @param bottom bottom margin
     * @param right  right margin
     */
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    /**
     * Sets content margin.
     *
     * @param spacing sides margin
     */
    public void setMargin ( final int spacing )
    {
        setMargin ( spacing, spacing, spacing, spacing );
    }

    /**
     * Paints icon for the specified component.
     *
     * @param c component to process
     * @param g graphics context
     */
    public void paintIcon ( @NotNull final JComponent c, @NotNull final Graphics g )
    {
        paintIcon ( ( Graphics2D ) g, 0, 0, getIconWidth ( c ), getIconHeight ( c ) );
    }

    /**
     * Paints icon for the specified component at the specified location.
     *
     * @param c component to process
     * @param g graphics context
     * @param x location X coordinate
     * @param y location Y coordinate
     */
    @Override
    public void paintIcon ( @NotNull final Component c, @NotNull final Graphics g, final int x, final int y )
    {
        paintIcon ( ( Graphics2D ) g, x, y, getFixedPixelsWidth ( true ), getFixedPixelsHeight ( true ) );
    }

    /**
     * Paints icon at the specified bounds.
     *
     * @param g2d    graphics context
     * @param bounds icon bounds
     */
    public void paintIcon ( @NotNull final Graphics2D g2d, @NotNull final Rectangle bounds )
    {
        paintIcon ( g2d, bounds.x, bounds.y, bounds.width, bounds.height );
    }

    /**
     * Paints icon at the specified bounds.
     *
     * @param g2d    graphics context
     * @param x      location X coordinate
     * @param y      location Y coordinate
     * @param width  icon width
     * @param height icon height
     */
    public void paintIcon ( @NotNull final Graphics2D g2d, final int x, final int y, final int width, final int height )
    {
        final int availableWidth = Math.max ( width, getFixedPixelsWidth ( true ) );
        final int availableHeight = Math.max ( height, getFixedPixelsHeight ( true ) );
        final int fixedPixelsX = getFixedPixelsWidth ( false );
        final int unfixedX = availableWidth - fixedPixelsX;
        final int fixedPixelsY = getFixedPixelsHeight ( false );
        final int unfixedY = availableHeight - fixedPixelsY;

        int currentY = y;
        for ( final NinePatchInterval intervalY : verticalStretch )
        {
            // Percent part height
            final int intervalHeight = intervalY.getEnd () - intervalY.getStart () + 1;
            final int finalHeight;
            if ( intervalY.isPixel () )
            {
                finalHeight = intervalHeight;
            }
            else
            {
                final float percents = ( float ) intervalHeight / ( rawImage.getHeight () - fixedPixelsY );
                finalHeight = Math.round ( percents * unfixedY );
            }

            int currentX = x;
            for ( final NinePatchInterval intervalX : horizontalStretch )
            {
                // Percent part width
                final int intervalWidth = intervalX.getEnd () - intervalX.getStart () + 1;
                final int finalWidth;
                if ( intervalX.isPixel () )
                {
                    finalWidth = intervalWidth;
                }
                else
                {
                    final float percents = ( float ) intervalWidth / ( rawImage.getWidth () - fixedPixelsX );
                    finalWidth = Math.round ( percents * unfixedX );
                }

                // Drawing image part
                g2d.drawImage ( rawImage, currentX, currentY, currentX + finalWidth, currentY + finalHeight, intervalX.getStart (),
                        intervalY.getStart (), intervalX.getStart () + intervalWidth, intervalY.getStart () + intervalHeight, null );

                // Icrementing current X
                currentX += finalWidth;
            }

            // Icrementing current Y
            currentY += finalHeight;
        }
    }

    /**
     * Returns cached fixed minimum width for this icon.
     *
     * @param addUnfixedSpaces whether to add 1px for each stretchable area or not
     * @return cached fixed minimum width for this icon
     */
    public int getFixedPixelsWidth ( final boolean addUnfixedSpaces )
    {
        final int width;
        if ( addUnfixedSpaces )
        {
            if ( cachedWidth0 == null )
            {
                cachedWidth0 = calculateFixedPixelsWidth ( true );
            }
            width = cachedWidth0;
        }
        else
        {
            if ( cachedWidth1 == null )
            {
                cachedWidth1 = calculateFixedPixelsWidth ( false );
            }
            width = cachedWidth1;
        }
        return width;
    }

    /**
     * Returns fixed minimum width for this icon.
     *
     * @param addUnfixedSpaces whether to add 1px for each stretchable area or not
     * @return fixed minimum width for this icon
     */
    protected int calculateFixedPixelsWidth ( final boolean addUnfixedSpaces )
    {
        int fixedPixelsX = rawImage.getWidth ();
        for ( final NinePatchInterval interval : horizontalStretch )
        {
            if ( !interval.isPixel () )
            {
                fixedPixelsX -= interval.getEnd () - interval.getStart () + 1;
                if ( addUnfixedSpaces )
                {
                    fixedPixelsX += 1;
                }
            }
        }
        return fixedPixelsX;
    }

    /**
     * Clears fixed pixels width caches.
     */
    protected void updateCachedWidthData ()
    {
        cachedWidth0 = null;
        cachedWidth1 = null;
        getFixedPixelsWidth ( true );
        getFixedPixelsWidth ( false );
    }

    /**
     * Returns cached fixed minimum height for this icon.
     *
     * @param addUnfixedSpaces whether to add 1px for each stretchable area or not
     * @return cached fixed minimum height for this icon
     */
    public int getFixedPixelsHeight ( final boolean addUnfixedSpaces )
    {
        final int height;
        if ( addUnfixedSpaces )
        {
            if ( cachedHeight0 == null )
            {
                cachedHeight0 = calculateFixedPixelsHeight ( true );
            }
            height = cachedHeight0;
        }
        else
        {
            if ( cachedHeight1 == null )
            {
                cachedHeight1 = calculateFixedPixelsHeight ( false );
            }
            height = cachedHeight1;
        }
        return height;
    }

    /**
     * Returns fixed minimum height for this icon.
     *
     * @param addUnfixedSpaces swhether to add 1px for each stretchable area or not
     * @return fixed minimum height for this icon
     */
    protected int calculateFixedPixelsHeight ( final boolean addUnfixedSpaces )
    {
        int fixedPixelsY = rawImage.getHeight ();
        for ( final NinePatchInterval interval : verticalStretch )
        {
            if ( !interval.isPixel () )
            {
                fixedPixelsY -= interval.getEnd () - interval.getStart () + 1;
                if ( addUnfixedSpaces )
                {
                    fixedPixelsY += 1;
                }
            }
        }
        return fixedPixelsY;
    }

    /**
     * Clears fixed pixels height caches.
     */
    protected void updateCachedHeightData ()
    {
        cachedHeight0 = null;
        cachedHeight1 = null;
        getFixedPixelsHeight ( true );
        getFixedPixelsHeight ( false );
    }

    @Override
    public int getIconWidth ()
    {
        return getIconWidth ( getComponent () );
    }

    /**
     * Returns icon width for the specified component.
     *
     * @param component component atop of which icon will be stretched
     * @return icon width for the specified component
     */
    public int getIconWidth ( @Nullable final JComponent component )
    {
        return Math.max ( component != null ? component.getWidth () : 0, getFixedPixelsWidth ( true ) );
    }

    @Override
    public int getIconHeight ()
    {
        return getIconHeight ( getComponent () );
    }

    /**
     * Returns icon height for the specified component.
     *
     * @param component component atop of which icon will be stretched
     * @return icon height for the specified component
     */
    public int getIconHeight ( @Nullable final JComponent component )
    {
        return Math.max ( component != null ? component.getHeight () : 0, getFixedPixelsHeight ( true ) );
    }

    /**
     * Returns preferred icon size.
     *
     * @return preferred icon size
     */
    @NotNull
    public Dimension getPreferredSize ()
    {
        return new Dimension ( getFixedPixelsWidth ( true ), getFixedPixelsHeight ( true ) );
    }

    /**
     * Returns raw image size.
     *
     * @return raw image size
     */
    @NotNull
    public Dimension getRealImageSize ()
    {
        return new Dimension ( getRawImage ().getWidth (), getRawImage ().getHeight () );
    }
}