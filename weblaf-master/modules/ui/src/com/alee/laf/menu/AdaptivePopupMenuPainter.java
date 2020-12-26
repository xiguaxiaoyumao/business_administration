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

package com.alee.laf.menu;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;
import java.awt.*;

/**
 * Simple {@link PopupMenuPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WPopupMenuUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */
public final class AdaptivePopupMenuPainter<C extends JPopupMenu, U extends WPopupMenuUI> extends AdaptivePainter<C, U>
        implements IPopupMenuPainter<C, U>
{
    /**
     * Constructs new {@link AdaptivePopupMenuPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptivePopupMenuPainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public Point preparePopupMenu ( final C popupMenu, final Component invoker, final int x, final int y )
    {
        return new Point ( x, y );
    }

    @Override
    public void configurePopup ( @NotNull final C popupMenu, @Nullable final Component invoker, final int x, final int y,
                                 @NotNull final Popup popup )
    {
        /**
         * Do nothing by default.
         */
    }
}