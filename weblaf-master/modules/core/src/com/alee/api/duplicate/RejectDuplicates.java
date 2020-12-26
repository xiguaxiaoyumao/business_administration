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

package com.alee.api.duplicate;

import com.alee.api.annotations.NotNull;
import com.alee.api.matcher.Matcher;

import java.util.Collection;

/**
 * {@link DuplicateResolver} implementation that throws {@link DuplicateException} upon detecting first duplicate within {@link Collection}.
 *
 * @author Mikle Garin
 */
public final class RejectDuplicates extends AbstractDuplicateResolver
{
    /**
     * Constructs new {@link RejectDuplicates}.
     *
     * @param matcher {@link Matcher} for duplicates detection
     */
    public RejectDuplicates ( @NotNull final Matcher matcher )
    {
        super ( matcher );
    }

    @Override
    public void resolve ( @NotNull final Collection collection )
    {
        if ( hasDuplicates ( collection ) )
        {
            throw new DuplicateException ( "Collection has duplicate element: " + firstDuplicate ( collection ) );
        }
    }
}