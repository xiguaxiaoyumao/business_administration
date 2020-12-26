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

package com.alee.laf.filechooser;

import com.alee.api.annotations.NotNull;

import javax.swing.plaf.FileChooserUI;

/**
 * Pluggable look and feel interface for {@link WebFileChooser} component.
 *
 * @author Mikle Garin
 */
public abstract class WFileChooserUI extends FileChooserUI
{
    /**
     * Returns {@link WebFileChooserPanel}.
     *
     * @return {@link WebFileChooserPanel}
     */
    @NotNull
    public abstract WebFileChooserPanel getFileChooserPanel ();
}