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
import com.alee.api.annotations.Nullable;
import com.alee.api.matcher.Matcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract {@link DuplicateResolver} implementation that provides basic methods for duplicates detection based on {@link Matcher}.
 *
 * @author Mikle Garin
 */
public abstract class AbstractDuplicateResolver implements DuplicateResolver
{
    /**
     * {@link Matcher} for duplicates detection.
     */
    @NotNull
    private final Matcher matcher;

    /**
     * Constructs new {@link AbstractDuplicateResolver}.
     *
     * @param matcher {@link Matcher} for duplicates detection
     */
    public AbstractDuplicateResolver ( @NotNull final Matcher matcher )
    {
        this.matcher = matcher;
    }

    /**
     * Returns whether or not specified {@link Collection} has one or more duplicates.
     *
     * @param collection {@link Collection} to check for duplicates
     * @return {@code true} if specified {@link Collection} has one or more duplicates, {@code false} otherwise
     */
    protected boolean hasDuplicates ( @NotNull final Collection collection )
    {
        boolean hasDuplicates = false;
        final List checked = new ArrayList ( collection.size () );
        for ( final Object next : collection )
        {
            if ( matcher.supports ( next ) )
            {
                for ( final Object previous : checked )
                {
                    if ( matcher.supports ( previous ) && matcher.match ( previous, next ) )
                    {
                        hasDuplicates = true;
                        break;
                    }
                }
                if ( hasDuplicates )
                {
                    break;
                }
            }
            checked.add ( next );
        }
        return hasDuplicates;
    }

    /**
     * Returns first {@link Collection} duplicates if it has any, {@code null} if it has none.
     *
     * @param collection {@link Collection} to check for duplicates
     * @return first {@link Collection} duplicates if it has any, {@code null} if it has none
     */
    @Nullable
    protected Object firstDuplicate ( @NotNull final Collection collection )
    {
        Object duplicate = null;
        boolean found = false;
        final List checked = new ArrayList ( collection.size () );
        for ( final Object next : collection )
        {
            if ( matcher.supports ( next ) )
            {
                for ( final Object previous : checked )
                {
                    if ( matcher.supports ( previous ) && matcher.match ( previous, next ) )
                    {
                        duplicate = next;
                        found = true;
                        break;
                    }
                }
                if ( found )
                {
                    break;
                }
            }
            checked.add ( next );
        }
        return duplicate;
    }

    /**
     * Removes any duplicates from the specified {@link Collection} and returns it.
     * todo This will always remove duplicates starting from second, while removing all but last can be more convenient at times
     *
     * @param collection {@link Collection} to remove duplicates from
     * @return {@link Collection} with all duplicates removed from it
     */
    @NotNull
    protected Collection removeDuplicates ( @NotNull final Collection collection )
    {
        final List checked = new ArrayList ( collection.size () );
        final Iterator iterator = collection.iterator ();
        while ( iterator.hasNext () )
        {
            final Object next = iterator.next ();
            boolean unique = true;
            if ( matcher.supports ( next ) )
            {
                for ( final Object previous : checked )
                {
                    if ( matcher.supports ( previous ) && matcher.match ( previous, next ) )
                    {
                        unique = false;
                        iterator.remove ();
                        break;
                    }
                }
            }
            if ( unique )
            {
                checked.add ( next );
            }
        }
        return collection;
    }
}