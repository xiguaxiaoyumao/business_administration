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
import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

/**
 * Simple {@link IDockablePanePainter} adapter class.
 * It is used to install simple non-specific painters into {@link WDockablePaneUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 */
public final class AdaptiveDockablePanePainter<C extends WebDockablePane, U extends WDockablePaneUI> extends AdaptivePainter<C, U>
        implements IDockablePanePainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveDockablePanePainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveDockablePanePainter ( @NotNull final Painter painter )
    {
        super ( painter );
    }
}