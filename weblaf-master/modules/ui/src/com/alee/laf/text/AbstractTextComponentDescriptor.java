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

package com.alee.laf.text;

import com.alee.api.annotations.NotNull;
import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;

import javax.swing.plaf.TextUI;
import javax.swing.text.JTextComponent;

/**
 * Abstract descriptor for {@link JTextComponent} implementations.
 * Extend this class for creating custom {@link JTextComponent} descriptors.
 *
 * @param <C> {@link JTextComponent} type
 * @param <U> base {@link TextUI} type
 * @param <P> {@link IAbstractTextEditorPainter} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 */
public class AbstractTextComponentDescriptor<C extends JTextComponent, U extends TextUI, P extends IAbstractTextEditorPainter>
        extends AbstractComponentDescriptor<C, U, P>
{
    /**
     * Constructs new {@link AbstractTextComponentDescriptor}.
     *
     * @param id                  {@link JTextComponent} identifier
     * @param componentClass      {@link JTextComponent} {@link Class}
     * @param uiClassId           {@link TextUI} {@link Class} identifier
     * @param baseUIClass         base {@link TextUI} {@link Class} applicable to {@link JTextComponent}
     * @param uiClass             {@link TextUI} {@link Class} used for {@link JTextComponent} by default
     * @param painterInterface    {@link IAbstractTextEditorPainter} interface {@link Class}
     * @param painterClass        {@link IAbstractTextEditorPainter} implementation {@link Class}
     * @param painterAdapterClass adapter for {@link IAbstractTextEditorPainter}
     * @param defaultStyleId      {@link JTextComponent} default {@link StyleId}
     */
    public AbstractTextComponentDescriptor ( @NotNull final String id, @NotNull final Class<C> componentClass,
                                             @NotNull final String uiClassId, @NotNull final Class<U> baseUIClass,
                                             @NotNull final Class<? extends U> uiClass, @NotNull final Class<P> painterInterface,
                                             @NotNull final Class<? extends P> painterClass,
                                             @NotNull final Class<? extends P> painterAdapterClass, @NotNull final StyleId defaultStyleId )
    {
        super ( id, componentClass, uiClassId, baseUIClass, uiClass, painterInterface, painterClass, painterAdapterClass, defaultStyleId );
    }

    @Override
    public void updateUI ( @NotNull final C component )
    {
        // Updating component UI
        super.updateUI ( component );

        // Invalidating text component contents
        component.invalidate ();
    }
}