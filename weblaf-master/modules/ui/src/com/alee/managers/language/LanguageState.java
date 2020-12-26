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

package com.alee.managers.language;

/**
 * Various common {@link com.alee.managers.language.data.Text} states.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see LanguageManager
 */
public interface LanguageState
{
    /**
     * Tooltip text state.
     */
    public static final String TOOLTIP_TEXT = "tooltip";

    /**
     * Swing tooltip text state.
     */
    public static final String SWING_TOOLTIP_TEXT = "swing-tooltip";

    /**
     * Custom tooltip text state.
     */
    public static final String CUSTOM_TOOLTIP_TEXT = "custom-tooltip";

    /**
     * Input prompt text state.
     */
    public static final String INPUT_PROMPT = "input-prompt";

    /**
     * Drop text state.
     */
    public static final String DROP_TEXT = "drop-text";
}