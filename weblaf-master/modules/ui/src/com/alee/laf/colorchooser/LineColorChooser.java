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

package com.alee.laf.colorchooser;

import com.alee.extended.layout.TableLayout;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.icon.Icons;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class LineColorChooser extends WebPanel
{
    private final List<ChangeListener> changeListeners = new ArrayList<ChangeListener> ( 1 );

    // Color line painter and component
    private final LineColorChooserPaint lineColorChooserPaint = new LineColorChooserPaint ( 2, 256 );
    private final LinePalette colorLine;

    // Hue (H -> HSB) (0 to 360)
    private int hue = 0;

    // Show only web-safe colors
    private boolean webOnlyColors = false;

    public LineColorChooser ()
    {
        super ();

        setLayout ( new TableLayout ( new double[][]{ { 10, TableLayout.PREFERRED, 10 }, { 3, TableLayout.PREFERRED, 3 } } ) );

        colorLine = new LinePalette ();
        add ( colorLine, "1,1" );

        final ColorLineMouseAdapter adapter = new ColorLineMouseAdapter ();
        addMouseListener ( adapter );
        addMouseMotionListener ( adapter );
    }

    private class LinePalette extends JComponent
    {
        public LinePalette ()
        {
            super ();
            WebLookAndFeel.setOrientation ( this );
        }

        @Override
        public void paint ( final Graphics g )
        {
            super.paint ( g );

            final Graphics2D g2d = ( Graphics2D ) g;

            g2d.setPaint ( Color.GRAY );
            g2d.drawRect ( 0, 0, getWidth () - 1, getHeight () - 1 );
            g2d.setPaint ( Color.WHITE );
            g2d.drawRect ( 1, 1, getWidth () - 3, getHeight () - 3 );

            g2d.setPaint ( lineColorChooserPaint );
            g2d.fillRect ( 2, 2, getWidth () - 4, getHeight () - 4 );
        }

        @Override
        public Dimension getPreferredSize ()
        {
            return new Dimension ( 22, 260 );
        }
    }

    private class ColorLineMouseAdapter extends MouseAdapter
    {
        @Override
        public void mousePressed ( final MouseEvent e )
        {
            updateHue ( e.getY () );
        }

        @Override
        public void mouseDragged ( final MouseEvent e )
        {
            updateHue ( e.getY () );
        }

        private void updateHue ( final int yCoordinate )
        {
            setHue ( Math.round ( ( float ) 360 * ( ( float ) ( yCoordinate - 5 ) / ( float ) ( colorLine.getHeight () - 4 ) ) ), true );
        }
    }

    private void setHue ( int hue, final boolean fireEvent )
    {
        // To filter needless actions
        if ( this.hue == hue )
        {
            return;
        }

        // Limitation
        if ( hue < 0 )
        {
            hue = 0;
        }
        else if ( hue > 360 )
        {
            hue = 360;
        }

        // New value
        this.hue = hue;
        LineColorChooser.this.repaint ();

        // Informing about new value
        if ( fireEvent )
        {
            firePropertyChanged ();
        }
    }

    public int getHue ()
    {
        return 360 - hue;
    }

    public void setColor ( final Color color )
    {
        // Retrieving hue from Color
        setHue ( 360 - Math.round ( ( float ) 360 * Color.RGBtoHSB ( color.getRed (), color.getGreen (), color.getBlue (), null )[ 0 ] ),
                false );
    }

    public Color getColor ()
    {
        return lineColorChooserPaint.getColor ( Math.round ( ( float ) ( colorLine.getHeight () - 4 ) * ( float ) hue / 360 ) );
    }

    @Override
    public void paint ( final Graphics g )
    {
        super.paint ( g );

        // Side grippers painting
        final int y = Math.round ( ( ( float ) hue / 360 ) * ( colorLine.getHeight () - 5 ) ) + Icons.rightSmall.getIconHeight () / 2;
        Icons.rightSmall.paintIcon ( this, g, 1, y );
        Icons.leftSmall.paintIcon ( this, g, getWidth () - Icons.rightSmall.getIconWidth () - 1, y );
    }

    public boolean isWebOnlyColors ()
    {
        return webOnlyColors;
    }

    public void setWebOnlyColors ( final boolean webOnlyColors )
    {
        this.webOnlyColors = webOnlyColors;
        lineColorChooserPaint.setWebSafe ( webOnlyColors );
        LineColorChooser.this.repaint ();
        firePropertyChanged ();
    }

    public void addChangeListener ( final ChangeListener listener )
    {
        changeListeners.add ( listener );
    }

    public void removeChangeListener ( final ChangeListener listener )
    {
        changeListeners.remove ( listener );
    }

    private void firePropertyChanged ()
    {
        final ChangeEvent changeEvent = new ChangeEvent ( LineColorChooser.this );
        for ( final ChangeListener changeListener : CollectionUtils.copy ( changeListeners ) )
        {
            changeListener.stateChanged ( changeEvent );
        }
    }
}