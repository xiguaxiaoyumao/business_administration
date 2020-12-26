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

package com.alee.laf.button;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.AbstractTextContent;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;

/**
 * Button text content implementation.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
@XStreamAlias ( "ButtonText" )
public class ButtonText<C extends AbstractButton, D extends IDecoration<C, D>, I extends ButtonText<C, D, I>>
        extends AbstractTextContent<C, D, I>
{
    @Nullable
    @Override
    protected String getText ( @NotNull final C c, @NotNull final D d )
    {
        return c.getText ();
    }

    @Override
    protected int getMnemonicIndex ( @NotNull final C c, @NotNull final D d )
    {
        return c.getDisplayedMnemonicIndex ();
    }
}