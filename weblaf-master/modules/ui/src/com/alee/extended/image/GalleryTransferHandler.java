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

package com.alee.extended.image;

import com.alee.api.annotations.NotNull;
import com.alee.managers.drag.transfer.FilesTransferHandler;
import com.alee.utils.ImageUtils;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class GalleryTransferHandler extends FilesTransferHandler
{
    private WebImageGallery gallery = null;

    public GalleryTransferHandler ( final WebImageGallery gallery )
    {
        super ( false, true );
        this.gallery = gallery;
    }

    @Override
    public boolean filesDropped ( @NotNull final List<File> files )
    {
        boolean added = false;
        if ( gallery != null )
        {
            for ( final File file : files )
            {
                if ( ImageUtils.isImageSupported ( file.getName () ) )
                {
                    gallery.addImage ( new ImageIcon ( file.getAbsolutePath () ) );
                    added = true;
                }
            }
        }
        return added;
    }
}