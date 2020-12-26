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

import java.util.Collection;

/**
 * {@link DuplicateResolver} implementation that ignores duplicates within {@link Collection}.
 * This is basically a no-op {@link DuplicateResolver} implementation that might be useful for some cases.
 *
 * @author Mikle Garin
 */
public final class IgnoreDuplicates implements DuplicateResolver
{
    @Override
    public void resolve ( @NotNull final Collection collection )
    {
        /**
         * Simply ignore this call.
         */
    }
}