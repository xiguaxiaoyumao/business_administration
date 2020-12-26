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

package com.alee.utils.filefilter;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.ui.RenderingParameters;
import com.alee.managers.language.LM;
import com.alee.utils.ImageUtils;

import javax.swing.*;
import java.io.File;

/**
 * Custom file filter that accepts only image files.
 *
 * @author Mikle Garin
 */
public class ImageFilesFilter extends AbstractFileFilter
{
    /**
     * Filter icon.
     */
    public static final ImageIcon ICON = new ImageIcon ( ImageFilesFilter.class.getResource ( "icons/image.png" ) );

    @Nullable
    @Override
    public Icon getIcon ( @NotNull final RenderingParameters parameters )
    {
        return ICON;
    }

    @NotNull
    @Override
    public String getDescription ()
    {
        return LM.get ( "weblaf.file.filter.images" );
    }

    @Override
    public boolean accept ( @NotNull final File file )
    {
        return ImageUtils.isImageSupported ( file.getName () );
    }
}