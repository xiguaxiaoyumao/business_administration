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

package com.alee.laf;

import com.alee.api.annotations.Nullable;

/**
 * Interface for elements supporting input prompt.
 * It could be anything from component or UI to any custom object.
 *
 * @author Mikle Garin
 */
public interface IInputPrompt
{
    /**
     * Returns input prompt text.
     *
     * @return input prompt text
     */
    @Nullable
    public String getInputPrompt ();

    /**
     * Sets input prompt text.
     *
     * @param text input prompt text
     */
    public void setInputPrompt ( @Nullable String text );
}