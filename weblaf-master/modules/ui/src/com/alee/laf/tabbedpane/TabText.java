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

package com.alee.laf.tabbedpane;

import com.alee.api.annotations.NotNull;
import com.alee.extended.label.StyledLabelText;
import com.alee.painter.decoration.IDecoration;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.awt.*;

/**
 * Styled text content implementation for {@link Tab}.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
@XStreamAlias ( "TabText" )
public class TabText<C extends Tab, D extends IDecoration<C, D>, I extends TabText<C, D, I>> extends StyledLabelText<C, D, I>
{
    @NotNull
    @Override
    protected Color getCustomColor ( @NotNull final C c, @NotNull final D d )
    {
        return c.getTabbedPane ().getForegroundAt ( c.getIndex () );
    }
}