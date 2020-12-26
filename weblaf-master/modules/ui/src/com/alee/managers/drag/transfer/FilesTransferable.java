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

package com.alee.managers.drag.transfer;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.utils.CollectionUtils;
import org.slf4j.LoggerFactory;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Custom transferable that represents {@link List} of {@link File} in a few different ways.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-DragManager">How to use DragManager</a>
 * @see com.alee.managers.drag.DragManager
 */
public class FilesTransferable implements Transferable
{
    /**
     * URI list mime type.
     */
    public static final String URI_LIST_MIME_TYPE = "text/uri-list;class=java.lang.String";

    /**
     * URI string data separator.
     */
    public static final String uriListSeparator = "\r\n";

    /**
     * URI list data flavor.
     */
    public static DataFlavor uriListFlavor = null;

    /**
     * {@link FilesTransferable} data flavors.
     */
    public static final DataFlavor[] flavors = new DataFlavor[]{ DataFlavor.javaFileListFlavor, getUriListDataFlavor () };

    /**
     * Transferred files.
     */
    @NotNull
    protected final List<File> files;

    /**
     * Constructs new {@link FilesTransferable} for a single file.
     *
     * @param file transferred file
     */
    public FilesTransferable ( @NotNull final File file )
    {
        this ( CollectionUtils.asList ( file ) );
    }

    /**
     * Constructs new {@link FilesTransferable} for a list of files.
     *
     * @param files transferred files list
     */
    public FilesTransferable ( @NotNull final List<File> files )
    {
        this.files = files;
    }

    @NotNull
    @Override
    public DataFlavor[] getTransferDataFlavors ()
    {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported ( @NotNull final DataFlavor flavor )
    {
        boolean dataFlavorSupported = false;
        for ( final DataFlavor dataFlavor : flavors )
        {
            if ( dataFlavor.equals ( flavor ) )
            {
                dataFlavorSupported = true;
                break;
            }
        }
        return dataFlavorSupported;
    }

    @NotNull
    @Override
    public Object getTransferData ( @NotNull final DataFlavor flavor ) throws UnsupportedFlavorException
    {
        final Object transferData;
        if ( flavor.equals ( DataFlavor.javaFileListFlavor ) )
        {
            transferData = files;
        }
        else if ( flavor.equals ( getUriListDataFlavor () ) )
        {
            transferData = fileListToTextURIList ( files );
        }
        else
        {
            throw new UnsupportedFlavorException ( flavor );
        }
        return transferData;
    }

    /**
     * Returns whether or not transferable contains files.
     *
     * @param transferable transferable
     * @return true if transferable contains files, false otherwise
     */
    public static boolean hasFilesList ( @NotNull final Transferable transferable )
    {
        final DataFlavor[] flavors = transferable.getTransferDataFlavors ();
        return hasURIListFlavor ( flavors ) || hasFileListFlavor ( flavors );
    }

    /**
     * Returns list of imported files retrieved from the specified transferable.
     *
     * @param transferable transferable
     * @return list of imported files
     */
    @Nullable
    public static List<File> getFilesList ( @NotNull final Transferable transferable )
    {
        List<File> result = null;
        final DataFlavor[] flavors = transferable.getTransferDataFlavors ();

        // From files list (Linux/MacOS)
        try
        {
            if ( hasURIListFlavor ( flavors ) )
            {
                // Parsing incoming files
                result = textURIListToFileList ( ( String ) transferable.getTransferData ( getUriListDataFlavor () ) );
            }
        }
        catch ( final Exception ignored )
        {
            //
        }

        // From URL
        if ( result == null )
        {
            try
            {
                if ( hasURIListFlavor ( flavors ) )
                {
                    // File link
                    final String url = ( String ) transferable.getTransferData ( getUriListDataFlavor () );
                    final File file = new File ( new URL ( url ).getPath () );

                    // Returning file
                    result = CollectionUtils.asList ( file );
                }
            }
            catch ( final Exception ignored )
            {
                //
            }
        }

        // From files list (Windows)
        if ( result == null )
        {
            try
            {
                if ( hasFileListFlavor ( flavors ) )
                {
                    // Getting files list
                    result = ( List<File> ) transferable.getTransferData ( DataFlavor.javaFileListFlavor );
                }
            }
            catch ( final Exception ignored )
            {
                //
            }
        }

        return result;
    }

    /**
     * Returns list of files from the specified text URI list.
     *
     * @param data text list of URI
     * @return list of files
     */
    @NotNull
    public static List<File> textURIListToFileList ( @NotNull final String data )
    {
        final List<File> list = new ArrayList<File> ( 1 );
        for ( final StringTokenizer st = new StringTokenizer ( data, uriListSeparator ); st.hasMoreTokens (); )
        {
            final String s = st.nextToken ();
            if ( s.startsWith ( "#" ) )
            {
                // the line is a comment (as per the RFC 2483)
                continue;
            }
            try
            {
                list.add ( new File ( new URI ( s ) ) );
            }
            catch ( final Exception ignored )
            {
                //
            }
        }
        return list;
    }

    /**
     * Returns text URI list for the specified list of files.
     *
     * @param files list of files to convert
     * @return text URI list
     */
    @NotNull
    public static String fileListToTextURIList ( @NotNull final List<File> files )
    {
        final StringBuilder sb = new StringBuilder ();
        for ( final File file : files )
        {
            sb.append ( file.toURI ().toASCIIString () );
            sb.append ( uriListSeparator );
        }
        return sb.toString ();
    }

    /**
     * Returns whether flavors array has URI list flavor or not.
     *
     * @param flavors flavors array
     * @return true if flavors array has URI list flavor, false otherwise
     */
    public static boolean hasURIListFlavor ( @NotNull final DataFlavor[] flavors )
    {
        boolean hasURIListFlavor = false;
        for ( final DataFlavor flavor : flavors )
        {
            if ( getUriListDataFlavor ().equals ( flavor ) )
            {
                hasURIListFlavor = true;
                break;
            }
        }
        return hasURIListFlavor;
    }

    /**
     * Returns whether flavors array has file list flavor or not.
     *
     * @param flavors flavors array
     * @return true if flavors array has file list flavor, false otherwise
     */
    public static boolean hasFileListFlavor ( @NotNull final DataFlavor[] flavors )
    {
        boolean hasFileListFlavor = false;
        for ( final DataFlavor flavor : flavors )
        {
            if ( DataFlavor.javaFileListFlavor.equals ( flavor ) )
            {
                hasFileListFlavor = true;
                break;
            }
        }
        return hasFileListFlavor;
    }

    /**
     * Returns URI list data flavor.
     *
     * @return URI list data flavor
     */
    @NotNull
    public static DataFlavor getUriListDataFlavor ()
    {
        if ( uriListFlavor == null )
        {
            try
            {
                uriListFlavor = new DataFlavor ( URI_LIST_MIME_TYPE );
            }
            catch ( final Exception e )
            {
                LoggerFactory.getLogger ( FilesTransferable.class ).error ( e.toString (), e );
            }
        }
        return uriListFlavor;
    }
}