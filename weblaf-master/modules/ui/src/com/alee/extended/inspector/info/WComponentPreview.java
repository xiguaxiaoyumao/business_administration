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

package com.alee.extended.inspector.info;

import com.alee.api.annotations.NotNull;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.painter.PainterSupport;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Default styleable {@link JComponent} information provider.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public class WComponentPreview<C extends JComponent> extends AbstractComponentPreview<C>
{
    @NotNull
    @Override
    public Icon getIconImpl ( @NotNull final C component )
    {
        final Icon icon;
        final JRootPane rootPane = CoreSwingUtils.getRootPane ( component );
        if ( rootPane != null && rootPane.getGlassPane () == component )
        {
            icon = glassPaneType;
        }
        else
        {
            icon = StyleManager.getDescriptor ( component ).getIcon ();
        }
        return icon;
    }

    @NotNull
    @Override
    public String getText ( @NotNull final C component )
    {
        final String title = "{" + ReflectUtils.getCompleteClassName ( component.getClass () ) + ":c(" + getTitleColor ( component ) + ")}";

        final LayoutManager layoutManager = component.getLayout ();
        final String layout = renderLayout ( layoutManager );

        final boolean wlui = LafUtils.hasWebLafUI ( component );
        final String style = wlui ? " {[ " + StyleId.get ( component ).getCompleteId () + " ]:b;c(" + styleIdColor + ")}" : "";

        final Insets margin = PainterSupport.getMargin ( component );
        final String mtext = renderInsets ( margin, marginColor );

        final Insets padding = PainterSupport.getPadding ( component );
        final String ptext = renderInsets ( padding, paddingColor );

        return title + layout + style + mtext + ptext;
    }
}