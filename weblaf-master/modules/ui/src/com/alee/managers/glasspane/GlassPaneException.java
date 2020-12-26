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

package com.alee.managers.glasspane;

/**
 * Special exception class used to display various glass pane problems.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-GlassPaneManager">How to use GlassPaneManager</a>
 * @see GlassPaneManager
 */
public final class GlassPaneException extends RuntimeException
{
    /**
     * Constructs a new {@link GlassPaneException} with {@code null} as its detail message.
     */
    public GlassPaneException ()
    {
        super ();
    }

    /**
     * Constructs a new {@link GlassPaneException} with the specified detail message.
     *
     * @param message exception message
     */
    public GlassPaneException ( final String message )
    {
        super ( message );
    }

    /**
     * Constructs a new {@link GlassPaneException} with the specified detail message and cause.
     *
     * @param message exception message
     * @param cause   exception cause
     */
    public GlassPaneException ( final String message, final Throwable cause )
    {
        super ( message, cause );
    }

    /**
     * Constructs a new {@link GlassPaneException} with the specified cause.
     *
     * @param cause exception cause
     */
    public GlassPaneException ( final Throwable cause )
    {
        super ( cause );
    }
}