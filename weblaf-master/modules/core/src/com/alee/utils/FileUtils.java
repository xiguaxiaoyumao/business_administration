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

package com.alee.utils;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.resource.Resource;
import com.alee.managers.language.LM;
import com.alee.managers.proxy.ProxyManager;
import com.alee.utils.compare.Filter;
import com.alee.utils.file.FileComparator;
import com.alee.utils.file.FileDescription;
import com.alee.utils.file.FileDownloadListener;
import com.alee.utils.file.SystemFileListener;
import com.alee.utils.filefilter.AbstractFileFilter;
import com.alee.utils.filefilter.AllFilesFilter;
import com.alee.utils.filefilter.IOFileFilterAdapter;
import com.alee.utils.filefilter.SwingFileFilterAdapter;
import com.alee.utils.swing.WebTimer;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.security.CodeSource;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class provides a set of utilities to work with files, file names and their extensions.
 * <p>
 * Note that methods which request information about files from the system has their own caches to improve performance.
 * If you will need to clear that cache simply call the corresponding clearCache method, for example:
 * For method "isHidden" you will need to call "clearIsHiddenCache" and all cached values will be reset.
 *
 * @author Mikle Garin
 */
public final class FileUtils
{
    /**
     * todo 1. File.exists doesn't work in JDK7? Probably should add workaround for that?
     */

    /**
     * Image cache keys separator.
     */
    private static final String CACHE_KEYS_SEPARATOR = "|";

    /**
     * Number of bytes in 1 kilobyte.
     */
    public static final long KB = 1024;

    /**
     * Number of bytes in 1 megabyte.
     */
    public static final long MB = 1024 * KB;

    /**
     * Number of bytes in 1 gigabyte.
     */
    public static final long GB = 1024 * MB;

    /**
     * Number of bytes in 1 terabyte.
     */
    public static final long TB = 1024 * GB;

    /**
     * Number of bytes in 1 petabyte.
     */
    public static final long PB = 1024 * TB;

    /**
     * Cached file system view.
     */
    private static final FileSystemView fsv = FileSystemView.getFileSystemView ();

    /**
     * Display date format.
     */
    private static final SimpleDateFormat sdf = new SimpleDateFormat ( "dd MMM yyyy HH:mm" );

    /**
     * Default encoding used to read files.
     */
    private static final String defaultEncoding = "UTF-8";

    /**
     * Buffer size for MD5 calculations.
     */
    private static final int MD5_BUFFER_LENGTH = 102400;

    /**
     * Buffer size for text reader.
     */
    private static final int TEXT_BUFFER_SIZE = 65536;

    /**
     * All illegal file name characters.
     */
    private static final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '\"', '`', '!', '?', '*', '\\', '<', '>', '|',
            ':', ';', '.', ',', '%', '$', '@', '#', '^', '{', '}', '[', ']', ']' };

    /**
     * Cache for "isDrive" method result.
     */
    private static final Map<String, Boolean> isDriveCache = new ConcurrentHashMap<String, Boolean> ();

    /**
     * Cache for "isComputer" method result.
     */
    private static final Map<String, Boolean> isComputerCache = new ConcurrentHashMap<String, Boolean> ();

    /**
     * Cache for "isCdDrive" method result.
     */
    private static final Map<String, Boolean> isCdDriveCache = new ConcurrentHashMap<String, Boolean> ();

    /**
     * Cache for "isFile" method result.
     */
    private static final Map<String, Boolean> isFileCache = new ConcurrentHashMap<String, Boolean> ();

    /**
     * Cache for "isDirectory" method result.
     */
    private static final Map<String, Boolean> isDirectoryCache = new ConcurrentHashMap<String, Boolean> ();

    /**
     * Cache for "isHidden" method result.
     */
    private static final Map<String, Boolean> isHiddenCache = new ConcurrentHashMap<String, Boolean> ();

    /**
     * Cache for "getDisplayFileName" method result.
     */
    private static final Map<String, String> displayFileNameCache = new ConcurrentHashMap<String, String> ();

    /**
     * Cache for "getFileDescription" method result.
     */
    private static final Map<String, FileDescription> fileDescriptionCache = new ConcurrentHashMap<String, FileDescription> ();

    /**
     * Cache for "getFileTypeDescription" method result.
     */
    private static final Map<String, String> fileTypeDescriptionCache = new ConcurrentHashMap<String, String> ();

    /**
     * Cache for "getDisplayFileCreationDate" method result.
     */
    private static final Map<String, String> displayFileCreationDateCache = new ConcurrentHashMap<String, String> ();

    /**
     * Cache for "getDisplayFileModificationDate" method result.
     */
    private static final Map<String, String> displayFileModificationDateCache = new ConcurrentHashMap<String, String> ();

    /**
     * File extension icons cache lock.
     */
    private static final Object extensionIconsCacheLock = new Object ();

    /**
     * File extension icons cache.
     */
    private static final Map<String, Icon> extensionIconsCache = new ConcurrentHashMap<String, Icon> ();

    /**
     * Resource icons cache.
     */
    private static final Map<String, Icon> resourceIconsCache = new ConcurrentHashMap<String, Icon> ();

    /**
     * Default file tracking updates delay.
     */
    private static final int FILE_TRACKING_DELAY = 5000;

    /**
     * File comparator.
     */
    public static final FileComparator FILE_COMPARATOR = new FileComparator ();

    /**
     * Private constructor to avoid instantiation.
     */
    private FileUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns JAR location URL for the specified class.
     *
     * @param jarClass any class from that JAR
     * @return JAR location URL
     */
    @Nullable
    public static URL getJarLocationURL ( @NotNull final Class jarClass )
    {
        final CodeSource src = jarClass.getProtectionDomain ().getCodeSource ();
        return src != null ? src.getLocation () : null;
    }

    /**
     * Returns JAR location File for the specified class.
     *
     * @param jarClass any class from that JAR
     * @return JAR location File
     */
    @Nullable
    public static File getJarLocationFile ( @NotNull final Class jarClass )
    {
        File file = null;
        try
        {
            final CodeSource src = jarClass.getProtectionDomain ().getCodeSource ();
            if ( src != null )
            {
                final URL jarUrl = src.getLocation ();
                final URI uri = jarUrl.toURI ();
                final String scheme = uri.getScheme ();
                if ( scheme != null && scheme.equalsIgnoreCase ( "file" ) )
                {
                    file = new File ( uri );
                }
            }
        }
        catch ( final URISyntaxException e )
        {
            LoggerFactory.getLogger ( FileUtils.class ).error ( e.toString (), e );
        }
        return file;
    }

    /**
     * Clears all caches for specified files.
     *
     * @param files files to process
     */
    public static void clearFilesCaches ( @Nullable final File... files )
    {
        if ( files != null && files.length > 0 )
        {
            for ( final File file : files )
            {
                clearFileCaches ( file );
            }
        }
    }

    /**
     * Clears all caches for specified file.
     *
     * @param file file to process
     */
    public static void clearFileCaches ( @Nullable final File file )
    {
        if ( file != null )
        {
            clearFileCaches ( file.getAbsolutePath () );
        }
    }

    /**
     * Clears all caches for file under the specified path.
     *
     * @param path file path
     */
    public static void clearFileCaches ( @NotNull final String path )
    {
        clearDisplayFileNameCache ( path );
        clearIsHiddenCache ( path );
        clearIsFileCache ( path );
        clearIsDirectoryCache ( path );
        clearIsComputerCache ( path );
        clearIsDriveCache ( path );
        clearIsCdDriveCache ( path );
        clearFileDescriptionCache ( path );
        clearFileTypeDescriptionCache ( path );
        clearDisplayFileCreationDateCache ( path );
        clearDisplayFileModificationDateCache ( path );
    }

    /**
     * Returns list of files contained in path of the specified file.
     * <p>
     * For example if you have some file that points to some local file:
     * "C:\folder\file.txt"
     * You will get this list of files:
     * "C:\", "C:\folder\", "C:\folder\file.txt"
     *
     * @param file file to process
     * @return list of files contained in path of the specified file
     */
    @NotNull
    public static List<File> getFilePath ( @Nullable File file )
    {
        final List<File> path = new ArrayList<File> ();
        while ( file != null )
        {
            path.add ( 0, file );
            file = getParent ( file );
        }
        return path;
    }

    /**
     * Returns file path relative to specified folder or canonical path if file is not inside that folder.
     *
     * @param file   file to get relative path to
     * @param folder one of file's parent folders
     * @return file path relative to specified folder or canonical path if file is not inside that folder
     */
    @NotNull
    public static String getRelativePath ( @NotNull final File file, @NotNull final File folder )
    {
        return folder.toURI ().relativize ( file.toURI () ).getPath ();
    }

    /**
     * Returns whether specified file's name can be edited.
     *
     * @param file file to edit
     * @return true if specified file's name can be edited, false otherwise
     */
    public static boolean isNameEditable ( @NotNull final File file )
    {
        final File parent = getParent ( file );
        return parent != null && parent.canWrite () && file.canWrite ();
    }

    /**
     * Sets file name as text and selects its name part in any text component.
     *
     * @param editor text editor to process
     * @param file   file to process
     */
    public static void displayFileName ( @NotNull final JTextComponent editor, @NotNull final File file )
    {
        final String name = file.getName ();
        editor.setText ( name );
        editor.setSelectionStart ( 0 );
        editor.setSelectionEnd ( file.isDirectory () ? name.length () : FileUtils.getFileNamePart ( name ).length () );
    }

    /**
     * Returns system directory for temporary files.
     *
     * @return system directory for temporary files
     */
    @NotNull
    public static File getSystemTemp ()
    {
        return new File ( getSystemTempPath () );
    }

    /**
     * Returns path to system directory for temporary files.
     *
     * @return path to system directory for temporary files
     */
    @NotNull
    public static String getSystemTempPath ()
    {
        return System.getProperty ( "java.io.tmpdir" );
    }

    /**
     * Grants file execution permission for all users for specified file.
     *
     * @param file file for permissions change
     * @return true if permissions change operation succeed, false otherwise
     */
    public static boolean grantExecutePermissions ( @NotNull final File file )
    {
        boolean granted;
        try
        {
            granted = file.setExecutable ( true, false );
        }
        catch ( final Exception e )
        {
            LoggerFactory.getLogger ( FileUtils.class ).error ( e.toString (), e );
            granted = false;
        }
        return granted;
    }

    /**
     * Returns normalized file without redundant parts in its path.
     *
     * @param file file to normalize
     * @return normalized file
     */
    @Nullable
    public static File normalize ( @Nullable final File file )
    {
        File normalized;
        try
        {
            normalized = file != null ? file.getCanonicalFile () : null;
        }
        catch ( final IOException e )
        {
            normalized = file;
        }
        return normalized;
    }

    /**
     * Returns first available file system root.
     *
     * @return first available file system root
     */
    @Nullable
    public static File getSystemRoot ()
    {
        final File[] roots = getSystemRoots ();
        return roots.length > 0 ? roots[ 0 ] : null;
    }

    /**
     * Returns array of available file system roots.
     *
     * @return array of available file system roots
     */
    @NotNull
    public static File[] getSystemRoots ()
    {
        final File[] systemRoots;
        synchronized ( fsv )
        {
            systemRoots = fsv.getRoots ();
        }
        final File[] roots;
        if ( systemRoots != null && systemRoots.length > 0 )
        {
            roots = systemRoots;
        }
        else
        {
            roots = getDiskRoots ();
        }
        return roots;
    }

    /**
     * Returns array of available system disks.
     *
     * @return array of available system disks
     */
    @NotNull
    public static File[] getDiskRoots ()
    {
        final File[] roots = File.listRoots ();
        int proper = 0;
        for ( final File root : roots )
        {
            if ( isDirectory ( root ) )
            {
                proper++;
            }
        }
        final File[] properRoots = new File[ proper ];
        int currentIndex = 0;
        for ( final File root : roots )
        {
            if ( isDirectory ( root ) )
            {
                properRoots[ currentIndex ] = root;
                currentIndex++;
            }
        }
        return properRoots;
    }

    /**
     * Returns directory files array or empty array (instead of null) if no files present.
     *
     * @param directory  directory to look into
     * @return directory files array or empty array (instead of null) if no files present
     */
    @NotNull
    public static File[] listFiles ( @NotNull final File directory )
    {
        return listFiles ( directory, ( FileFilter ) null );
    }

    /**
     * Returns directory files array or empty array (instead of null) if no files present.
     *
     * @param directory  directory to look into
     * @param fileFilter file filter
     * @return directory files array or empty array (instead of null) if no files present
     */
    @NotNull
    public static File[] listFiles ( @NotNull final File directory, @Nullable final Filter<File> fileFilter )
    {
        return listFiles ( directory, fileFilter != null ? new FileFilter ()
        {
            @Override
            public boolean accept ( final File file )
            {
                return fileFilter.accept ( file );
            }
        } : null );
    }

    /**
     * Returns directory files array or empty array (instead of null) if no files present.
     *
     * @param directory  directory to look into
     * @param fileFilter file filter
     * @return directory files array or empty array (instead of null) if no files present
     */
    @NotNull
    public static File[] listFiles ( @NotNull final File directory, @Nullable final FileFilter fileFilter )
    {
        final File[] files = fileFilter != null ? directory.listFiles ( fileFilter ) : directory.listFiles ();
        return files != null ? files : new File[ 0 ];
    }

    /**
     * Returns MD5 for specified {@link String} data.
     *
     * @param data file to process
     * @return MD5 for specified {@link String} data
     */
    @NotNull
    public static String computeMD5 ( @NotNull final String data )
    {
        return computeMD5 ( data, MD5_BUFFER_LENGTH );
    }

    /**
     * Returns MD5 for specified {@link String} data and uses a buffer of the specified length.
     *
     * @param data         file to process
     * @param bufferLength buffer length
     * @return MD5 for specified {@link String} data
     */
    @NotNull
    public static String computeMD5 ( @NotNull final String data, final int bufferLength )
    {
        return computeMD5 ( new ByteArrayInputStream ( data.getBytes () ), bufferLength );
    }

    /**
     * Returns MD5 for specified {@link File} content.
     *
     * @param resource {@link Resource} to process
     * @return MD5 for specified {@link File} content
     */
    @NotNull
    public static String computeMD5 ( @NotNull final Resource resource )
    {
        return computeMD5 ( resource, MD5_BUFFER_LENGTH );
    }

    /**
     * Returns MD5 for specified {@link File} content and uses a buffer of the specified length.
     *
     * @param resource     {@link Resource} to process
     * @param bufferLength buffer length
     * @return MD5 for specified {@link File} content
     */
    @NotNull
    public static String computeMD5 ( @NotNull final Resource resource, final int bufferLength )
    {
        return computeMD5 ( resource.getInputStream (), bufferLength );
    }

    /**
     * Returns MD5 for the data provided by {@link InputStream}.
     *
     * @param inputStream data stream to process
     * @return MD5 for the data provided by {@link InputStream}
     */
    @NotNull
    public static String computeMD5 ( @NotNull final InputStream inputStream )
    {
        return computeMD5 ( inputStream, MD5_BUFFER_LENGTH );
    }

    /**
     * Returns MD5 for the data provided by {@link InputStream} and uses a buffer of the specified length.
     *
     * @param inputStream  data stream to process
     * @param bufferLength buffer length
     * @return MD5 for the data provided by {@link InputStream}
     */
    @NotNull
    public static String computeMD5 ( @NotNull final InputStream inputStream, final int bufferLength )
    {
        final BufferedInputStream bis = new BufferedInputStream ( inputStream );
        try
        {
            final MessageDigest digest = MessageDigest.getInstance ( "MD5" );
            final byte[] buffer = new byte[ bufferLength ];
            int bytesRead;
            while ( ( bytesRead = bis.read ( buffer, 0, buffer.length ) ) > 0 )
            {
                digest.update ( buffer, 0, bytesRead );
            }
            final byte[] md5sum = digest.digest ();
            final BigInteger bigInt = new BigInteger ( 1, md5sum );
            return bigInt.toString ( 16 );
        }
        catch ( final Exception e )
        {
            throw new UtilityException ( "Unable to compute MD5 for InputStream: " + inputStream, e );
        }
        finally
        {
            try
            {
                bis.close ();
            }
            catch ( final Exception ignored )
            {
                //
            }
        }
    }

    /**
     * Returns application working directory.
     *
     * @return application working directory
     */
    @NotNull
    public static File getWorkingDirectory ()
    {
        return new File ( getWorkingDirectoryPath () );
    }

    /**
     * Returns application working directory.
     *
     * @return application working directory
     */
    @NotNull
    public static String getWorkingDirectoryPath ()
    {
        return System.getProperty ( "user.dir" );
    }

    /**
     * Returns user home directory.
     *
     * @return user home directory
     */
    @NotNull
    public static File getUserHome ()
    {
        return new File ( getUserHomePath () );
    }

    /**
     * Returns path to user home directory.
     *
     * @return path to user home directory
     */
    @NotNull
    public static String getUserHomePath ()
    {
        String home = System.getProperty ( "user.home" );
        if ( !home.endsWith ( File.separator ) )
        {
            home += File.separator;
        }
        return home;
    }

    /**
     * Returns desktop directory if one can be found.
     *
     * @return desktop directory if one can be found
     */
    @Nullable
    public static File getDesktop ()
    {
        File desktop = null;
        final File[] roots = fsv.getRoots ();
        if ( roots.length > 0 )
        {
            // Trying file system roots
            for ( final File root : roots )
            {
                if ( root.getName ().toLowerCase ( Locale.ROOT ).contains ( "desktop" ) )
                {
                    desktop = root;
                    break;
                }
            }
        }
        if ( desktop == null )
        {
            // Trying common known location
            final File common = new File ( getUserHome (), "Desktop" );
            if ( common.exists () )
            {
                desktop = common;
            }
        }
        return desktop;
    }

    /**
     * Returns desktop directory path if one can be found.
     *
     * @return desktop directory path if one can be found
     */
    @Nullable
    public static String getDesktopPath ()
    {
        final File desktop = getDesktop ();
        return desktop != null ? desktop.getAbsolutePath () : null;
    }

    /**
     * Returns whether both files represent the same path in file system or not.
     *
     * @param file1 first file to be compared
     * @param file2 second file to be compared
     * @return true if both files represent the same path in file system, false otherwise
     */
    public static boolean equals ( @Nullable final File file1, @Nullable final File file2 )
    {
        boolean equals;
        if ( file1 == null && file2 == null )
        {
            equals = true;
        }
        else
        {
            try
            {
                final boolean notNull = file1 != null && file2 != null;
                equals = notNull && file1.getCanonicalPath ().equals ( file2.getCanonicalPath () );
            }
            catch ( final IOException e )
            {
                equals = file1.getAbsolutePath ().equals ( file2.getAbsolutePath () );
            }
        }
        return equals;
    }

    /**
     * Returns whether both list of files have equal files in the same positions or not.
     *
     * @param files1 first files list to be compared
     * @param files2 second files list to be compared
     * @return true if both list of files have equal files in the same positions, false otherwise
     */
    public static boolean equals ( @NotNull final List<File> files1, @NotNull final List<File> files2 )
    {
        boolean equals;
        if ( files1.size () == files2.size () )
        {
            equals = true;
            if ( files1.size () != 0 )
            {
                for ( int i = 0; i < files1.size (); i++ )
                {
                    if ( !equals ( files1.get ( i ), files2.get ( i ) ) )
                    {
                        equals = false;
                        break;
                    }
                }
            }
        }
        else
        {
            equals = false;
        }
        return equals;
    }

    /**
     * Returns whether list of files or file paths contains the specified file or not.
     *
     * @param files list of files to search in
     * @param file  file to look for
     * @return true if list of files or file paths contains the specified file, false otherwise
     */
    public static boolean containsFile ( @NotNull final List files, @NotNull final File file )
    {
        boolean contains = false;
        for ( final Object f : files )
        {
            if ( f instanceof File )
            {
                if ( ( ( File ) f ).getAbsolutePath ().equals ( file.getAbsolutePath () ) )
                {
                    contains = true;
                    break;
                }
            }
            else if ( f instanceof String )
            {
                if ( f.equals ( file.getAbsolutePath () ) )
                {
                    contains = true;
                    break;
                }
            }
        }
        return contains;
    }

    /**
     * Returns whether any of the specified file filters accept the file or not.
     *
     * @param file    file to process
     * @param filters file filters list
     * @return true if any of the specified file filters accept the file, false otherwise
     */
    public static boolean isFileAccepted ( @NotNull final File file, @Nullable final List<AbstractFileFilter> filters )
    {
        boolean accepted;
        if ( filters != null && filters.size () != 0 )
        {
            accepted = false;
            for ( final FileFilter fileFilter : filters )
            {
                if ( fileFilter.accept ( file ) )
                {
                    accepted = true;
                    break;
                }
            }
        }
        else
        {
            accepted = true;
        }
        return accepted;
    }

    /**
     * Returns a valid for any file system file name based on specified name.
     *
     * @param name name to process
     * @return file name valid for any file system
     */
    @NotNull
    public static String getProperFileName ( @NotNull final String name )
    {
        final StringBuilder newName = new StringBuilder ();
        for ( int i = 0; i < name.length (); ++i )
        {
            if ( !isIllegalFileNameChar ( name.charAt ( i ) ) )
            {
                newName.append ( name.charAt ( i ) );
            }
        }
        return newName.toString ().replaceAll ( " ", "_" );
    }

    /**
     * Returns whether the specified character is illegal or not.
     *
     * @param c character to process
     * @return true the specified character is illegal, false otherwise
     */
    public static boolean isIllegalFileNameChar ( final char c )
    {
        boolean isIllegal = false;
        for ( final char ILLEGAL_CHARACTER : ILLEGAL_CHARACTERS )
        {
            if ( c == ILLEGAL_CHARACTER )
            {
                isIllegal = true;
            }
        }
        return isIllegal;
    }

    /**
     * Returns file canonical path if its possible or absolute path otherwise.
     *
     * @param file file to process
     * @return file canonical path if its possible or absolute path otherwise
     */
    @NotNull
    public static String canonicalPath ( final File file )
    {
        try
        {
            return file.getCanonicalPath ();
        }
        catch ( final IOException e )
        {
            throw new UtilityException ( "Unable to retrieve file canonical path: " + file, e );
        }
    }

    /**
     * Returns parent {@link File}.
     * Unlike {@link File#getParentFile()} it doesn't fail on cases when native parent is {@code null}.
     * Whenever native parent is {@code null} this method will fallback to {@link File#getParent()} path usage.
     *
     * @param file {@link File} to return parent {@link File} for
     * @return parent {@link File}
     */
    @Nullable
    public static File getParent ( @NotNull final File file )
    {
        final File parent;
        final File nativeParent = file.getParentFile ();
        if ( nativeParent != null )
        {
            parent = nativeParent;
        }
        else
        {
            final String parentPath = file.getParent ();
            parent = parentPath != null ? new File ( parentPath ) : null;
        }
        return parent;
    }

    /**
     * Returns top not-null parent for the specified file.
     * If file has no parents at all simply returns null.
     *
     * @param file file to process
     * @return top not-null parent for the specified file or null if it has no parent files
     */
    @Nullable
    public static File getTopParent ( @NotNull final File file )
    {
        File result = null;
        File parent = getParent ( file.getAbsoluteFile () );
        while ( parent != null )
        {
            result = parent;
            parent = getParent ( parent );
        }
        return result;
    }

    /**
     * Returns whether the specified child file is one of parent file children or not.
     *
     * @param parent parent file
     * @param child  child file
     * @return true if the specified child file is one of parent file children, false otherwise
     */
    public static boolean isParent ( @Nullable final File parent, @Nullable File child )
    {
        boolean isParent;
        if ( child == parent )
        {
            isParent = false;
        }
        else if ( child == null )
        {
            isParent = false;
        }
        else if ( parent == null )
        {
            isParent = true;
        }
        else if ( child.equals ( parent ) )
        {
            isParent = false;
        }
        else
        {
            isParent = false;
            child = child.getAbsoluteFile ();
            File cp = getParent ( child );
            while ( cp != null )
            {
                if ( cp.equals ( parent ) )
                {
                    isParent = true;
                    break;
                }
                cp = getParent ( cp );
            }
        }
        return isParent;
    }

    /**
     * Returns file name without extension.
     *
     * @param file file to process
     * @return file name without extension
     */
    @NotNull
    public static String getFileNamePart ( @Nullable final File file )
    {
        return file != null ? getFileNamePart ( file.getName () ) : "";
    }

    /**
     * Returns file name without extension.
     *
     * @param name file name to trim
     * @return file name without extension
     */
    @NotNull
    public static String getFileNamePart ( @Nullable final String name )
    {
        final String namePart;
        if ( TextUtils.notEmpty ( name ) )
        {
            final int first = name.indexOf ( "." );
            final int last = name.lastIndexOf ( "." );
            namePart = last == -1 || first == 0 && first == last ? name : name.substring ( 0, last );
        }
        else
        {
            namePart = "";
        }
        return namePart;
    }

    /**
     * Returns file extension either with or without dot.
     *
     * @param file    file to process
     * @param withDot whether return the extension with dot, or not
     * @return file extension
     */
    @NotNull
    public static String getFileExtPart ( @Nullable final File file, final boolean withDot )
    {
        return file != null ? getFileExtPart ( file.getName (), withDot ) : "";
    }

    /**
     * Returns file extension either with or without dot.
     *
     * @param name    file name to process
     * @param withDot whether return the extension with dot, or not
     * @return file extension
     */
    @NotNull
    public static String getFileExtPart ( @Nullable final String name, final boolean withDot )
    {
        final String ext;
        if ( TextUtils.notEmpty ( name ) )
        {
            final int i = name.lastIndexOf ( "." );
            ext = i == -1 ? "" : withDot ? name.substring ( i ) : name.substring ( i + 1 );
        }
        else
        {
            ext = "";
        }
        return ext;
    }

    /**
     * Returns shortened file name.
     *
     * @param name file name to shorten
     * @return shortened file name
     */
    @NotNull
    public static String getShortFileName ( @Nullable final String name )
    {
        return getShortFileName ( name, 30 );
    }

    /**
     * Returns shortened to the specified length file name.
     * Note that file extension length is not counted.
     *
     * @param name   file name to shorten
     * @param length maximum allowed file name length
     * @return shortened file name
     */
    @NotNull
    public static String getShortFileName ( @Nullable final String name, final int length )
    {
        String shortName = name != null ? name : "";
        if ( length >= 2 )
        {
            final String newName = getFileNamePart ( name );
            if ( newName.length () > length )
            {
                shortName = newName.substring ( 0, length - 2 ) + "..." + getFileExtPart ( name, false );
            }
        }
        return shortName;
    }

    /**
     * Returns sorted list of files.
     *
     * @param files list of files to sort
     * @return sorted list of files
     */
    @Nullable
    public static List<File> sortFiles ( @Nullable final List<File> files )
    {
        if ( files != null )
        {
            Collections.sort ( files, FILE_COMPARATOR );
        }
        return files;
    }

    /**
     * Returns sorted array of files.
     *
     * @param files array of files to sort
     * @return sorted array of files
     */
    @Nullable
    public static File[] sortFiles ( @Nullable final File[] files )
    {
        if ( files != null )
        {
            Arrays.sort ( files, FILE_COMPARATOR );
        }
        return files;
    }

    /**
     * Returns available file name in the specified directory and which is similar the specified name.
     *
     * @param dir  directory path to check
     * @param name file name to check
     * @return available file name
     */
    @NotNull
    public static String getAvailableName ( @NotNull final String dir, @NotNull final String name )
    {
        return getAvailableName ( new File ( dir ), name );
    }

    /**
     * Returns available file name in the specified directory and which is similar the specified name.
     *
     * @param dir  directory to check
     * @param name file name to check
     * @return available file name
     */
    @NotNull
    public static String getAvailableName ( @NotNull final File dir, @NotNull final String name )
    {
        final List<String> exist = new ArrayList<String> ();
        final File[] files = dir.listFiles ();
        if ( files != null )
        {
            for ( final File file : files )
            {
                exist.add ( file.getName () );
            }
        }
        return getAvailableName ( exist, name );
    }

    /**
     * Returns available file name that is not contained in the existing names list.
     *
     * @param existingNames list of existing file names
     * @param name          file name to check
     * @return available file name that is not contained in the existing names list
     */
    @NotNull
    public static String getAvailableName ( @NotNull final List<String> existingNames, @NotNull final String name )
    {
        String availableName = name;
        if ( existingNames.contains ( availableName ) )
        {
            // Gathering name parts
            final int dot = availableName.lastIndexOf ( "." );
            final String nameStart = dot != -1 ? availableName.substring ( 0, dot ).trim () : availableName;
            final String nameExt = dot != -1 ? availableName.substring ( availableName.lastIndexOf ( "." ) ) : null;
            final int ob = nameStart.lastIndexOf ( "(" );
            final int cb = nameStart.lastIndexOf ( ")" );
            String nameReal = null;
            Integer index = null;
            if ( ob < cb && cb == nameStart.length () - 1 )
            {
                try
                {
                    nameReal = nameStart.substring ( 0, ob );
                    index = Integer.parseInt ( nameStart.substring ( ob + 1, cb ) );
                    index++;
                }
                catch ( final Exception ignored )
                {
                    //
                }
            }

            // Choosing new name
            int i = 1;
            while ( existingNames.contains ( availableName ) )
            {
                if ( nameReal != null && index != null )
                {
                    availableName = nameReal + "(" + index + ")" + nameExt;
                    index++;
                }
                else
                {
                    availableName = nameStart + " (" + i + ")" + ( nameExt != null ? nameExt : "" );
                    i++;
                }
            }
        }
        return availableName;
    }

    /**
     * Converts objects array into list of files.
     * Non-file type objects will be simply ignored.
     *
     * @param object file object
     * @return list of files
     */
    @NotNull
    public static List<File> toFilesList ( @Nullable final Object... object )
    {
        final List<File> files = new ArrayList<File> ( object != null ? object.length : 0 );
        for ( final Object file : files )
        {
            if ( file instanceof File )
            {
                files.add ( ( File ) file );
            }
        }
        return files;
    }

    /**
     * Deletes all specified files.
     *
     * @param files files to delete
     */
    public static void deleteFiles ( @NotNull final Object... files )
    {
        for ( final Object object : files )
        {
            if ( object instanceof File )
            {
                deleteFile ( ( File ) object );
            }
        }
    }

    /**
     * Deletes all specified files.
     *
     * @param files files to delete
     */
    public static void deleteFiles ( @NotNull final List files )
    {
        for ( final Object object : files )
        {
            if ( object instanceof File )
            {
                deleteFile ( ( File ) object );
            }
        }
    }

    /**
     * Deletes file or directory completely.
     * All child files and directories will be removed first in case directory is deleted.
     *
     * @param file file to delete
     */
    public static void deleteFile ( @NotNull final File file )
    {
        if ( file.exists () )
        {
            if ( file.isFile () )
            {
                if ( !file.delete () )
                {
                    file.deleteOnExit ();
                }
            }
            else if ( file.isDirectory () )
            {
                final File[] files = file.listFiles ();
                if ( files != null )
                {
                    for ( final File child : files )
                    {
                        deleteFile ( child );
                    }
                }
                if ( !file.delete () )
                {
                    file.deleteOnExit ();
                }
            }
        }
    }

    /**
     * Deletes all child files and directories for specified directory.
     * Directory itself will not be deleted.
     *
     * @param dir directory to clear
     */
    public static void clearDirectory ( @NotNull final File dir )
    {
        if ( dir.exists () )
        {
            if ( dir.isDirectory () )
            {
                final File[] files = dir.listFiles ();
                if ( files != null )
                {
                    for ( final File child : files )
                    {
                        deleteFile ( child );
                    }
                }
            }
        }
    }

    /**
     * Returns transformed file filter.
     *
     * @param fileFilter IO file filter
     * @return transformed file filter
     */
    @NotNull
    public static AbstractFileFilter transformFileFilter ( @NotNull final FileFilter fileFilter )
    {
        final AbstractFileFilter abstractFileFilter;
        if ( fileFilter instanceof AbstractFileFilter )
        {
            abstractFileFilter = ( AbstractFileFilter ) fileFilter;
        }
        else
        {
            abstractFileFilter = new IOFileFilterAdapter ( fileFilter, AllFilesFilter.ICON, LM.get ( "weblaf.file.filter.custom" ) );
        }
        return abstractFileFilter;
    }

    /**
     * Returns transformed file filter.
     *
     * @param fileFilter Swing file filter
     * @return transformed file filter.
     */
    @NotNull
    public static AbstractFileFilter transformFileFilter ( @NotNull final javax.swing.filechooser.FileFilter fileFilter )
    {
        final AbstractFileFilter abstractFileFilter;
        if ( fileFilter instanceof AbstractFileFilter )
        {
            abstractFileFilter = ( AbstractFileFilter ) fileFilter;
        }
        else
        {
            abstractFileFilter = new SwingFileFilterAdapter ( fileFilter, AllFilesFilter.ICON );
        }
        return abstractFileFilter;
    }

    /**
     * Returns actual Swing file filter from the specified filter.
     *
     * @param fileFilter WebLaF file filter
     * @return actual Swing file filter from the specified filter
     */
    @Nullable
    public static javax.swing.filechooser.FileFilter getSwingFileFilter ( @Nullable final AbstractFileFilter fileFilter )
    {
        return fileFilter != null && fileFilter instanceof SwingFileFilterAdapter ?
                ( ( SwingFileFilterAdapter ) fileFilter ).getFileFilter () : fileFilter;
    }

    /**
     * Returns filtered files list.
     *
     * @param files      files collection to filter
     * @param fileFilter file filter
     * @return filtered files list
     */
    @NotNull
    public static List<File> filterFiles ( @NotNull final Collection<File> files, @NotNull final AbstractFileFilter fileFilter )
    {
        final List<File> filteredFiles = new ArrayList<File> ( files.size () );
        for ( final File file : files )
        {
            if ( fileFilter.accept ( file ) )
            {
                filteredFiles.add ( file );
            }
        }
        return filteredFiles;
    }

    /**
     * Returns filtered files list.
     *
     * @param files      files collection to filter
     * @param fileFilter file filter
     * @return filtered files list
     */
    @NotNull
    public static List<File> filterFiles ( @NotNull final Collection<File> files, @NotNull final Filter<File> fileFilter )
    {
        final List<File> filteredFiles = new ArrayList<File> ( files.size () );
        for ( final File file : files )
        {
            if ( fileFilter.accept ( file ) )
            {
                filteredFiles.add ( file );
            }
        }
        return filteredFiles;
    }

    /**
     * Returns complete file description.
     *
     * @param file     file to process
     * @param fileSize file size
     * @return complete file description
     */
    @NotNull
    public static FileDescription createFileDescription ( @NotNull final File file, @Nullable final String fileSize )
    {
        // File name
        final String name = getDisplayFileName ( file );

        // File or image size
        final String size = file.isFile () ? getDisplayFileSize ( file ) + ( fileSize != null ? " (" + fileSize + ")" : "" ) : null;

        // File type description
        final String description = getFileTypeDescription ( file );

        // Modification date
        //        long modified = file.lastModified ();
        //        String date = modified != 0 ? /*"<br>" +*/ sdf.format ( new Date ( modified ) ) : null;

        return new FileDescription ( name, size, description, null /*date*/ );
    }

    /**
     * Returns file size to display.
     *
     * @param file file to process
     * @return file size to display
     */
    @NotNull
    public static String getDisplayFileSize ( @NotNull final File file )
    {
        // todo Cache this value
        return getFileSizeString ( file.length () );
    }

    /**
     * Returns file size to display.
     *
     * @param file   file to process
     * @param digits number of digits after the dot
     * @return file size to display
     */
    @NotNull
    public static String getDisplayFileSize ( @NotNull final File file, final int digits )
    {
        // todo Cache file length value
        return getFileSizeString ( file.length (), digits );
    }

    /**
     * Returns file size to display.
     *
     * @param size size of the file
     * @return file size to display
     */
    public static String getFileSizeString ( final long size )
    {
        return getFileSizeString ( size, 2 );
    }

    /**
     * Returns file size to display.
     *
     * @param size   size of the file
     * @param digits number of digits after the dot
     * @return file size to display
     */
    @NotNull
    public static String getFileSizeString ( final long size, final int digits )
    {
        final String fileSizeString;
        final DecimalFormat df = new DecimalFormat ( digits > 0 ? "#." + TextUtils.createString ( "#", digits ) : "#" );
        if ( size < KB )
        {
            fileSizeString = df.format ( size ) + " " + LM.get ( "weblaf.file.size.b" );
        }
        else if ( size < MB )
        {
            fileSizeString = df.format ( ( float ) size / KB ) + " " + LM.get ( "weblaf.file.size.kb" );
        }
        else if ( size < GB )
        {
            fileSizeString = df.format ( ( float ) size / MB ) + " " + LM.get ( "weblaf.file.size.mb" );
        }
        else if ( size < TB )
        {
            fileSizeString = df.format ( ( float ) size / GB ) + " " + LM.get ( "weblaf.file.size.gb" );
        }
        else if ( size < PB )
        {
            fileSizeString = df.format ( ( float ) size / TB ) + " " + LM.get ( "weblaf.file.size.tb" );
        }
        else
        {
            fileSizeString = df.format ( ( float ) size / PB ) + " " + LM.get ( "weblaf.file.size.pb" );
        }
        return fileSizeString;
    }

    /**
     * Copies src directory content into dst directory and returns whether operation succeed or not.
     * Also ignores any exceptions that might occur during the copy process.
     *
     * @param src source directory path
     * @param dst destination directory path
     * @return true if copy operation succeed, false otherwise
     */
    public static boolean copyDirectory ( @NotNull final String src, @NotNull final String dst )
    {
        return copyDirectory ( src, dst, false );
    }

    /**
     * Copies src directory content into dst directory and returns whether operation succeed or not.
     * Whether to stop copy operation if any exception occurs or not is set by stopOnFail argument.
     *
     * @param src        source directory path
     * @param dst        destination directory path
     * @param stopOnFail whether to stop copy operation if any exception occurs or not
     * @return true if copy operation succeed, false otherwise
     */
    public static boolean copyDirectory ( @NotNull final String src, @NotNull final String dst, final boolean stopOnFail )
    {
        return copyDirectory ( new File ( src ), new File ( dst ), stopOnFail );
    }

    /**
     * Copies src directory content into dst directory and returns whether operation succeed or not.
     * Also ignores any exceptions that might occur during the copy process.
     *
     * @param srcDir source directory
     * @param dstDir destination directory
     * @return true if copy operation succeed, false otherwise
     */
    public static boolean copyDirectory ( @NotNull final File srcDir, @NotNull final File dstDir )
    {
        return copyDirectory ( srcDir, dstDir, false );
    }

    /**
     * Copies src directory content into dst directory and returns whether operation succeed or not.
     * Whether to stop copy operation if any exception occurs or not is set by stopOnFail argument.
     *
     * @param srcDir     source directory
     * @param dstDir     destination directory
     * @param stopOnFail whether to stop copy operation if any exception occurs or not
     * @return true if copy operation succeed, false otherwise
     */
    public static boolean copyDirectory ( @NotNull final File srcDir, @NotNull final File dstDir, final boolean stopOnFail )
    {
        // todo Actually ignore exceptions as well if stopOnFail = false
        boolean success;
        if ( srcDir.exists () && srcDir.isDirectory () )
        {
            // Ensure destination directory exists and perform copy
            if ( ensureDirectoryExists ( dstDir ) )
            {
                // Copying all subdirectories and sub-files
                success = true;
                final File[] files = srcDir.listFiles ();
                if ( files != null )
                {
                    for ( final File file : files )
                    {
                        final String copied = dstDir.getAbsolutePath () + File.separator + file.getName ();
                        if ( file.isDirectory () )
                        {
                            success = copyDirectory ( file.getAbsolutePath (), copied ) && success;
                            if ( !success && stopOnFail )
                            {
                                break;
                            }
                        }
                        else
                        {
                            success = copyFile ( file.getAbsolutePath (), copied ) && success;
                            if ( !success && stopOnFail )
                            {
                                break;
                            }
                        }
                    }
                }
            }
            else
            {
                success = false;
            }
        }
        else
        {
            success = false;
        }
        return success;
    }

    /**
     * Copies source file content into destination file.
     * If destination directory doesn't exist it will be created in the process.
     * If destination file doesn't exist it will be also created in the process.
     *
     * @param src source file path
     * @param dst destination file path
     * @return true if copy operation succeed, false otherwise
     */
    public static boolean copyFile ( @Nullable final String src, @Nullable final String dst )
    {
        final File srcFile = src != null ? new File ( src ) : null;
        final File dstFile = dst != null ? new File ( dst ) : null;
        return copyFile ( srcFile, dstFile );
    }

    /**
     * Copies source file content into destination file.
     * If destination directory doesn't exist it will be created in the process.
     * If destination file doesn't exist it will be also created in the process.
     *
     * @param srcFile source file
     * @param dstFile destination file
     * @return true if copy operation succeed, false otherwise
     */
    public static boolean copyFile ( @Nullable final File srcFile, @Nullable final File dstFile )
    {
        boolean success = false;
        if ( srcFile != null && srcFile.exists () && srcFile.isFile () && dstFile != null )
        {
            try
            {
                // Creating destination directory if needed
                if ( ensureDirectoryExists ( getParent ( dstFile ) ) )
                {
                    final FileChannel srcFC = new FileInputStream ( srcFile ).getChannel ();
                    final FileChannel dstFC = new FileOutputStream ( dstFile ).getChannel ();
                    success = copyFile ( srcFC, dstFC );
                }
            }
            catch ( final FileNotFoundException ignored )
            {
            }
        }
        return success;
    }

    /**
     * Copies file data from source file channel into destination file channel.
     *
     * @param srcFC source file channel
     * @param dstFC destination file channel
     * @return true if copy operation succeed, false otherwise
     */
    public static boolean copyFile ( @NotNull final FileChannel srcFC, @NotNull final FileChannel dstFC )
    {
        boolean success;
        try
        {
            dstFC.transferFrom ( srcFC, 0, srcFC.size () );
            srcFC.close ();
            dstFC.close ();
            success = true;
        }
        catch ( final IOException e )
        {
            success = false;
        }
        return success;
    }

    /**
     * Returns {@link String} content read from the {@link Resource}.
     *
     * @param resource {@link Resource}
     * @return {@link String} content read from the {@link Resource}
     */
    @NotNull
    public static String readToString ( @NotNull final Resource resource )
    {
        return readToString ( resource, defaultEncoding );
    }

    /**
     * Returns {@link String} content read from the {@link Resource}.
     *
     * @param resource {@link Resource}
     * @param encoding content encoding
     * @return {@link String} content read from the {@link Resource}
     */
    @NotNull
    public static String readToString ( @NotNull final Resource resource, @NotNull final String encoding )
    {
        return readToString ( resource.getInputStream (), encoding );
    }

    /**
     * Returns content read from the input stream.
     *
     * @param inputStream text content input stream
     * @return text content
     */
    @NotNull
    public static String readToString ( @NotNull final InputStream inputStream )
    {
        return readToString ( inputStream, defaultEncoding );
    }

    /**
     * Returns content read from the input stream.
     *
     * @param inputStream text content input stream
     * @param encoding    stream data encoding
     * @return content
     */
    @NotNull
    public static String readToString ( @NotNull final InputStream inputStream, @NotNull final String encoding )
    {
        try
        {
            return readToString ( new InputStreamReader ( inputStream, encoding ) );
        }
        catch ( final Exception e )
        {
            throw new UtilityException ( "Unable to read InputStream to string: " + inputStream, e );
        }
        finally
        {
            try
            {
                inputStream.close ();
            }
            catch ( final IOException ignored )
            {
                // Ignore this exception
            }
        }
    }

    /**
     * Returns content read from the specified reader.
     *
     * @param reader text content reader
     * @return content
     */
    @NotNull
    public static String readToString ( @NotNull final Reader reader )
    {
        try
        {
            int charsRead;
            final char[] buffer = new char[ TEXT_BUFFER_SIZE ];
            final StringBuilder sb = new StringBuilder ();
            while ( ( charsRead = reader.read ( buffer, 0, TEXT_BUFFER_SIZE ) ) != -1 )
            {
                sb.append ( buffer, 0, charsRead );
            }
            return sb.toString ();
        }
        catch ( final Exception e )
        {
            throw new UtilityException ( "Unable to read Reader to string: " + reader, e );
        }
        finally
        {
            try
            {
                reader.close ();
            }
            catch ( final IOException ignored )
            {
                // Ignore this exception
            }
        }
    }

    /**
     * Writes text to the specified file overwriting any content inside the file.
     * If file or even its directory doesn't exist - they will be created.
     *
     * @param text text to write
     * @param file file to write text into
     */
    public static void writeStringToFile ( @NotNull final String text, @NotNull final File file )
    {
        writeStringToFile ( text, file, defaultEncoding );
    }

    /**
     * Writes text to the specified file overwriting any content inside the file.
     * If file or even its directory doesn't exist - they will be created.
     *
     * @param text     text to write
     * @param file     file to write text into
     * @param encoding file encoding
     */
    public static void writeStringToFile ( @NotNull final String text, @NotNull final File file, @NotNull final String encoding )
    {
        if ( !file.exists () || !file.isDirectory () )
        {
            final File parent = getParent ( file );
            if ( parent != null )
            {
                // Creating directories if necessary
                parent.mkdirs ();

                // Writing text to file
                PrintWriter writer = null;
                try
                {
                    writer = new PrintWriter ( file, encoding );
                    writer.write ( text );
                }
                catch ( final Exception e )
                {
                    throw new UtilityException ( "Unable to write string to file: " + file, e );
                }
                finally
                {
                    if ( writer != null )
                    {
                        writer.close ();
                    }
                }
            }
            else
            {
                throw new UtilityException ( "Unable to determine file location: " + file );
            }
        }
        else
        {
            throw new UtilityException ( "Specified file points to existing folder!" );
        }
    }

    /**
     * Returns list of all file paths in this directory and all subdirectories.
     *
     * @param dir path of directory to process
     * @return list of file paths
     */
    @NotNull
    public static List<String> getSubPaths ( @NotNull final String dir )
    {
        return getSubPaths ( new File ( dir ) );
    }

    /**
     * Returns list of all file paths in this directory and all subdirectories.
     *
     * @param dir directory to process
     * @return list of file paths
     */
    @NotNull
    public static List<String> getSubPaths ( @NotNull final File dir )
    {
        return getSubPaths ( dir, "" );
    }

    /**
     * Returns list of all file paths in this directory and all subdirectories.
     *
     * @param dir  directory to process
     * @param path path to current position
     * @return list of file paths
     */
    @NotNull
    public static List<String> getSubPaths ( @NotNull final File dir, @NotNull final String path )
    {
        return getSubPaths ( dir, path, new ArrayList<String> () );
    }

    /**
     * Returns list of all file paths in this directory and all subdirectories.
     *
     * @param dir   directory to process
     * @param path  path to current position
     * @param paths list of collected paths
     * @return list of file paths
     */
    @NotNull
    public static List<String> getSubPaths ( @NotNull final File dir, @NotNull final String path, @NotNull final List<String> paths )
    {
        final File[] files = dir.listFiles ();
        if ( files != null )
        {
            for ( final File file : files )
            {
                if ( file.isFile () )
                {
                    paths.add ( path + file.getName () );
                }
                else if ( file.isDirectory () )
                {
                    getSubPaths ( file, path + file.getName () + File.separator, paths );
                }
            }
        }
        return paths;
    }

    /**
     * Downloads file from the specified url to destination file and returns it if download succeed.
     * todo Instead of multiple methods create DownloadParameters class that would contain all of the settings
     *
     * @param url     file source url
     * @param dstFile destination file
     * @return downloaded file if download succeed
     */
    @NotNull
    public static File downloadFile ( @NotNull final String url, @NotNull final File dstFile )
    {
        return downloadFile ( url, dstFile, false, null, 3000, null );
    }

    /**
     * Downloads file from the specified url to destination file and returns it if download succeed.
     * You can observe and manipulate the download process by providing a file download listener.
     * todo Instead of multiple methods create DownloadParameters class that would contain all of the settings
     *
     * @param url      file source url
     * @param dstFile  destination file
     * @param listener file download process listener
     * @return downloaded file if download succeed
     */
    @NotNull
    public static File downloadFile ( @NotNull final String url, @NotNull final File dstFile,
                                      @Nullable final FileDownloadListener listener )
    {
        return downloadFile ( url, dstFile, false, null, 3000, listener );
    }

    /**
     * Downloads file from the specified url to destination file and returns it if download succeed.
     * todo Instead of multiple methods create DownloadParameters class that would contain all of the settings
     *
     * @param url         file source url
     * @param dst         destination file path
     * @param encodeUrl   whether encode the source url or not
     * @param contentType content type limitation
     * @param timeout     connection and read timeout
     * @return downloaded file if download succeed
     */
    @NotNull
    public static File downloadFile ( @NotNull final String url, @NotNull final String dst, final boolean encodeUrl,
                                      @Nullable final String contentType, final int timeout )
    {
        return downloadFile ( url, new File ( dst ), encodeUrl, contentType, timeout, null );
    }

    /**
     * Downloads file from the specified url to destination file and returns it if download succeed.
     * You can observe and manipulate the download process by providing a file download listener.
     * todo Instead of multiple methods create DownloadParameters class that would contain all of the settings
     *
     * @param url         file source url
     * @param dst         destination file path
     * @param encodeUrl   whether encode the source url or not
     * @param contentType content type limitation
     * @param timeout     connection and read timeout
     * @param listener    file download process listener
     * @return downloaded file if download succeed
     */
    @NotNull
    public static File downloadFile ( @NotNull final String url, @NotNull final String dst, final boolean encodeUrl,
                                      @Nullable final String contentType, final int timeout, @Nullable final FileDownloadListener listener )
    {
        return downloadFile ( url, new File ( dst ), encodeUrl, contentType, timeout, listener );
    }

    /**
     * Downloads file from the specified url to destination file and returns it if download succeed.
     * todo Instead of multiple methods create DownloadParameters class that would contain all of the settings
     *
     * @param url         file source url
     * @param dstFile     destination file
     * @param encodeUrl   whether encode the source url or not
     * @param contentType content type limitation
     * @param timeout     connection and read timeout
     * @return downloaded file if download succeed
     */
    @NotNull
    public static File downloadFile ( @NotNull final String url, @NotNull final File dstFile, final boolean encodeUrl,
                                      @Nullable final String contentType, final int timeout )
    {
        return downloadFile ( url, dstFile, encodeUrl, contentType, timeout, null );
    }

    /**
     * Downloads file from the specified url to destination file and returns it if download succeed.
     * You can observe and manipulate the download process by providing a file download listener.
     * todo Instead of multiple methods create DownloadParameters class that would contain all of the settings
     *
     * @param url         file source url
     * @param dstFile     destination file
     * @param encodeUrl   whether encode the source url or not
     * @param contentType content type limitation
     * @param timeout     connection and read timeout
     * @param listener    file download process listener
     * @return downloaded file if download succeed
     */
    @NotNull
    public static File downloadFile ( @NotNull final String url, @NotNull final File dstFile, final boolean encodeUrl,
                                      @Nullable final String contentType, final int timeout, @Nullable final FileDownloadListener listener )
    {
        try
        {
            // Creating connection
            final URL encodedUrl = new URL ( encodeUrl ? WebUtils.encodeUrl ( url ) : url );
            final URLConnection uc = ProxyManager.getURLConnection ( encodedUrl );

            // Configuring timeouts
            if ( timeout != 0 )
            {
                uc.setConnectTimeout ( timeout );
                uc.setReadTimeout ( timeout );
            }

            // Checking stop flag
            if ( listener != null && listener.shouldStopDownload () )
            {
                deleteFile ( dstFile );
                listener.fileDownloadAborted ();
                throw new UtilityException ( "File download was aborted" );
            }

            // Content type limitation
            if ( contentType != null )
            {
                final String ct = uc.getContentType ();
                if ( !ct.contains ( contentType ) )
                {
                    deleteFile ( dstFile );
                    if ( listener != null )
                    {
                        listener.fileDownloadAborted ();
                    }
                    throw new UtilityException ( "File download was aborted due to content type not matching requested content type" );
                }
            }

            // Notifying about file size
            if ( listener != null )
            {
                listener.sizeDetermined ( uc.getContentLength () );

                // Checking stop flag
                if ( listener.shouldStopDownload () )
                {
                    deleteFile ( dstFile );
                    listener.fileDownloadAborted ();
                    throw new UtilityException ( "File download was aborted" );
                }
            }

            // Opening file stream
            final InputStream in = uc.getInputStream ();
            final FileOutputStream out = new FileOutputStream ( dstFile );

            // Checking stop flag
            if ( listener != null && listener.shouldStopDownload () )
            {
                out.flush ();
                out.close ();
                in.close ();
                deleteFile ( dstFile );
                listener.fileDownloadAborted ();
                throw new UtilityException ( "File download was aborted" );
            }

            // Downloading content part by part
            final byte[] buf = new byte[ 50 * 1024 ];
            int totalBytesRead = 0;
            int bytesRead;
            while ( ( bytesRead = in.read ( buf ) ) != -1 )
            {
                if ( listener != null )
                {
                    totalBytesRead += bytesRead;
                    listener.partDownloaded ( totalBytesRead );

                    // Checking stop flag
                    if ( listener.shouldStopDownload () )
                    {
                        out.flush ();
                        out.close ();
                        in.close ();
                        deleteFile ( dstFile );
                        listener.fileDownloadAborted ();
                        throw new UtilityException ( "File download was aborted" );
                    }
                }
                out.write ( buf, 0, bytesRead );
            }
            out.flush ();
            out.close ();
            in.close ();

            // Informing about completed download
            if ( listener != null )
            {
                listener.fileDownloaded ( dstFile );
            }
            return dstFile;
        }
        catch ( final Exception e )
        {
            // Informing about failed download
            if ( listener != null )
            {
                listener.fileDownloadFailed ( e );
            }
            throw new UtilityException ( "Unable to download file from URL: " + url, e );
        }
    }

    /**
     * Returns file size, located at the specified url.
     *
     * @param url file location url
     * @return file size
     */
    public static int getFileSize ( @NotNull final String url )
    {
        try
        {
            return getFileSize ( new URL ( url ) );
        }
        catch ( final Exception e )
        {
            throw new UtilityException ( "Unable to retrieve file size for URL: " + url, e );
        }
    }

    /**
     * Returns file size, located at the specified url.
     *
     * @param url file location url
     * @return file size
     */
    public static int getFileSize ( @NotNull final URL url )
    {
        try
        {
            // Creating URLConnection
            final URLConnection uc = ProxyManager.getURLConnection ( url );

            // todo This size is limited to maximum of 2GB, should retrieve long instead
            // Retrieving file size
            return uc.getContentLength ();
        }
        catch ( final Exception e )
        {
            throw new UtilityException ( "Unable to retrieve file size for URL: " + url, e );
        }
    }

    /**
     * Returns list of files with specified extensions found in the specified directory and its subdirectories.
     *
     * @param dir       path to directory to process
     * @param extension file extensions list
     * @return list of found files
     */
    @NotNull
    public static List<File> findFilesRecursively ( @NotNull final String dir, @NotNull final List<String> extension )
    {
        return findFilesRecursively ( new File ( dir ), extension );
    }

    /**
     * Returns list of files with specified extensions found in the specified directory and its subdirectories.
     *
     * @param dir       directory to process
     * @param extension file extensions list
     * @return list of found files
     */
    @NotNull
    public static List<File> findFilesRecursively ( @NotNull final File dir, @NotNull final List<String> extension )
    {
        return findFilesRecursively ( dir, extension, true );
    }

    /**
     * Returns list of files with specified extensions found in the specified directory and its subdirectories.
     *
     * @param dir       path to directory to process
     * @param extension file extensions list
     * @param withDot   whether extensions contain dot or not
     * @return list of found files
     */
    @NotNull
    public static List<File> findFilesRecursively ( @NotNull final String dir, @NotNull final List<String> extension,
                                                    final boolean withDot )
    {
        return findFilesRecursively ( new File ( dir ), extension, withDot );
    }

    /**
     * Returns list of files with specified extensions found in the specified directory and its subdirectories.
     *
     * @param dir       directory to process
     * @param extension file extensions list
     * @param withDot   whether extensions contain dot or not
     * @return list of found files
     */
    @NotNull
    public static List<File> findFilesRecursively ( @NotNull final File dir, @NotNull final List<String> extension, final boolean withDot )
    {
        return findFilesRecursively ( dir, extension, withDot, new ArrayList<File> () );
    }

    /**
     * Returns list of files with specified extensions found in the specified directory and its subdirectories.
     *
     * @param dir       path to directory to process
     * @param extension file extensions list
     * @param withDot   whether extensions contain dot or not
     * @param found     list in which found files should be stored
     * @return list of found files
     */
    @NotNull
    public static List<File> findFilesRecursively ( @NotNull final String dir, @NotNull final List<String> extension, final boolean withDot,
                                                    @NotNull final List<File> found )
    {
        return findFilesRecursively ( new File ( dir ), extension, withDot, found );
    }

    /**
     * Returns list of files with specified extensions found in the specified directory and its subdirectories.
     *
     * @param dir       directory to process
     * @param extension file extensions list
     * @param withDot   whether extensions contain dot or not
     * @param found     list in which found files should be stored
     * @return list of found files
     */
    @NotNull
    public static List<File> findFilesRecursively ( @NotNull final File dir, @NotNull final List<String> extension, final boolean withDot,
                                                    @NotNull final List<File> found )
    {
        return findFilesRecursively ( dir, new FileFilter ()
        {
            @Override
            public boolean accept ( final File file )
            {
                return file.isFile () && extension.contains ( getFileExtPart ( file.getName (), withDot ) );
            }
        }, found );
    }

    /**
     * Returns list of files accepted by file filter found in the specified directory and its subdirectories.
     *
     * @param dir    path to directory to process
     * @param filter file filter
     * @return list of found files
     */
    @NotNull
    public static List<File> findFilesRecursively ( @NotNull final String dir, @NotNull final FileFilter filter )
    {
        return findFilesRecursively ( new File ( dir ), filter );
    }

    /**
     * Returns list of files accepted by file filter found in the specified directory and its subdirectories.
     *
     * @param dir    directory to process
     * @param filter file filter
     * @return list of found files
     */
    @NotNull
    public static List<File> findFilesRecursively ( @NotNull final File dir, @NotNull final FileFilter filter )
    {
        return findFilesRecursively ( dir, filter, new ArrayList<File> () );
    }

    /**
     * Returns list of files accepted by file filter found in the specified directory and its subdirectories.
     *
     * @param dir    path to directory to process
     * @param filter file filter
     * @param found  list in which found files should be stored
     * @return list of found files
     */
    @NotNull
    public static List<File> findFilesRecursively ( @NotNull final String dir, @NotNull final FileFilter filter,
                                                    @NotNull final List<File> found )
    {
        return findFilesRecursively ( new File ( dir ), filter, found );
    }

    /**
     * Returns list of files accepted by file filter found in the specified directory and its subdirectories.
     *
     * @param dir    directory to process
     * @param filter file filter
     * @param found  list in which found files should be stored
     * @return list of found files
     */
    @NotNull
    public static List<File> findFilesRecursively ( @NotNull final File dir, @NotNull final FileFilter filter,
                                                    @NotNull final List<File> found )
    {
        if ( dir.exists () && dir.isDirectory () )
        {
            final File[] files = dir.listFiles ();
            if ( files != null )
            {
                for ( final File file : files )
                {
                    if ( filter.accept ( file ) )
                    {
                        found.add ( file );
                    }
                    if ( file.isDirectory () )
                    {
                        findFilesRecursively ( file, filter, found );
                    }
                }
            }
        }
        return found;
    }

    /**
     * Returns true if directory exists or was successfully created during this check, false otherwise.
     *
     * @param dir path to directory to check
     * @return true if directory exists or was successfully created during this check, false otherwise
     */
    public static boolean ensureDirectoryExists ( @Nullable final String dir )
    {
        return dir != null && ensureDirectoryExists ( new File ( dir ) );
    }

    /**
     * Returns true if directory exists or was successfully created during this check, false otherwise.
     *
     * @param dir directory to check
     * @return true if directory exists or was successfully created during this check, false otherwise
     */
    public static boolean ensureDirectoryExists ( @Nullable final File dir )
    {
        return dir != null && ( dir.exists () || dir.mkdirs () );
    }

    /**
     * Clears cache for "isDrive" method.
     */
    public static void clearIsDriveCache ()
    {
        isDriveCache.clear ();
    }

    /**
     * Clears cache for "isDrive" method for specified file path.
     *
     * @param absolutePath path to clear file isDrive mark cache for
     */
    public static void clearIsDriveCache ( @NotNull final String absolutePath )
    {
        isDriveCache.remove ( absolutePath );
    }

    /**
     * Returns whether the specified file points to system hard drive or not.
     *
     * @param file file to process
     * @return true if the specified file points to system hard drive, false otherwise
     */
    public static boolean isDrive ( @NotNull final File file )
    {
        final boolean isDrive;
        final String absolutePath = file.getAbsolutePath ();
        if ( isDriveCache.containsKey ( absolutePath ) )
        {
            isDrive = isDriveCache.get ( absolutePath );
        }
        else if ( file.exists () )
        {
            synchronized ( fsv )
            {
                isDrive = fsv.isDrive ( file );
            }
            isDriveCache.put ( absolutePath, isDrive );
        }
        else
        {
            isDrive = false;
        }
        return isDrive;
    }

    /**
     * Clears cache for "isComputer" method.
     */
    public static void clearIsComputerCache ()
    {
        isComputerCache.clear ();
    }

    /**
     * Clears cache for "isComputer" method for specified file path.
     *
     * @param absolutePath path to clear file isComputer mark cache for
     */
    public static void clearIsComputerCache ( @NotNull final String absolutePath )
    {
        isComputerCache.remove ( absolutePath );
    }

    /**
     * Returns whether the specified file points to system hard drive or not.
     *
     * @param file file to process
     * @return true if the specified file points to system hard drive, false otherwise
     */
    public static boolean isComputer ( @NotNull final File file )
    {
        final boolean isComputer;
        final String absolutePath = file.getAbsolutePath ();
        if ( isComputerCache.containsKey ( absolutePath ) )
        {
            isComputer = isComputerCache.get ( absolutePath );
        }
        else if ( file.exists () )
        {
            synchronized ( fsv )
            {
                isComputer = fsv.isComputerNode ( file );
            }
            isComputerCache.put ( absolutePath, isComputer );
        }
        else
        {
            isComputer = false;
        }
        return isComputer;
    }

    /**
     * Clears cache for "isCdDrive" method.
     */
    public static void clearIsCdDriveCache ()
    {
        isCdDriveCache.clear ();
    }

    /**
     * Clears cache for "isCdDrive" method for specified file path.
     *
     * @param absolutePath path to clear file isCdDrive mark cache for
     */
    public static void clearIsCdDriveCache ( @NotNull final String absolutePath )
    {
        isCdDriveCache.remove ( absolutePath );
    }

    /**
     * Returns whether the specified file points to system CD/DVD/Bluray drive or not.
     * This method bases on file type description and might not work on some systems which does not provide it.
     * This method caches its result to improve its performance when used in various renderers.
     *
     * @param file file to process
     * @return true if the specified file points to system CD, DVD or Bluray drive, false otherwise
     */
    public static boolean isCdDrive ( @NotNull final File file )
    {
        final boolean isCdDrive;
        final String absolutePath = file.getAbsolutePath ();
        if ( isCdDriveCache.containsKey ( absolutePath ) )
        {
            isCdDrive = isCdDriveCache.get ( absolutePath );
        }
        else if ( file.exists () )
        {
            if ( file.getParent () == null )
            {
                final String sysDes = getFileTypeDescription ( file );
                final String description;
                if ( TextUtils.notEmpty ( sysDes ) )
                {
                    description = sysDes.toLowerCase ( Locale.ROOT );
                }
                else
                {
                    description = file.getName ();
                }
                isCdDrive = description.contains ( "cd" ) || description.contains ( "dvd" ) || description.contains ( "blu-ray" ) ||
                        description.contains ( "bluray" );
            }
            else
            {
                isCdDrive = false;
            }
            isCdDriveCache.put ( absolutePath, isCdDrive );
        }
        else
        {
            isCdDrive = false;
        }
        return isCdDrive;
    }

    /**
     * Clears cache for "isFile" method.
     */
    public static void clearIsFileCache ()
    {
        isFileCache.clear ();
    }

    /**
     * Clears cache for "isFile" method for specified file path.
     *
     * @param absolutePath path to clear file isFile mark cache for
     */
    public static void clearIsFileCache ( @NotNull final String absolutePath )
    {
        isFileCache.remove ( absolutePath );
    }

    /**
     * Returns whether the specified file is actually a file (and not a directory, disk or some system folder) or not.
     *
     * @param file file to process
     * @return true if the specified file is actually a file, false otherwise
     */
    public static boolean isFile ( @Nullable final File file )
    {
        boolean isFile = false;
        if ( file != null )
        {
            final String absolutePath = file.getAbsolutePath ();
            if ( isFileCache.containsKey ( absolutePath ) )
            {
                isFile = isFileCache.get ( absolutePath );
            }
            else if ( file.exists () )
            {
                isFile = file.isFile ();
                isFileCache.put ( absolutePath, isFile );
            }
        }
        return isFile;
    }

    /**
     * Clears cache for "isDirectory" method.
     */
    public static void clearIsDirectoryCache ()
    {
        isDirectoryCache.clear ();
    }

    /**
     * Clears cache for "isDirectory" method for specified file path.
     *
     * @param absolutePath path to clear file isDirectory mark cache for
     */
    public static void clearIsDirectoryCache ( @NotNull final String absolutePath )
    {
        isDirectoryCache.remove ( absolutePath );
    }

    /**
     * Returns whether the specified file is directory or not.
     *
     * @param file file to process
     * @return true if the specified file is directory, false otherwise
     */
    public static boolean isDirectory ( @Nullable final File file )
    {
        boolean isDirectory = false;
        if ( file != null )
        {
            final String absolutePath = file.getAbsolutePath ();
            if ( isDirectoryCache.containsKey ( absolutePath ) )
            {
                isDirectory = isDirectoryCache.get ( absolutePath );
            }
            else if ( file.exists () )
            {
                isDirectory = file.isDirectory ();
                isDirectoryCache.put ( absolutePath, isDirectory );
            }
        }
        return isDirectory;
    }

    /**
     * Clears cache for "isHidden" method.
     */
    public static void clearIsHiddenCache ()
    {
        isHiddenCache.clear ();
    }

    /**
     * Clears cache for "isHidden" method for specified file path.
     *
     * @param absolutePath path to clear file hidden mark cache for
     */
    public static void clearIsHiddenCache ( @NotNull final String absolutePath )
    {
        isHiddenCache.remove ( absolutePath );
    }

    /**
     * Returns whether the specified file is hidden or not.
     *
     * @param file file to process
     * @return true if the specified file is hidden, false otherwise
     */
    public static boolean isHidden ( @Nullable final File file )
    {
        boolean isHidden = false;
        if ( file != null )
        {
            final String absolutePath = file.getAbsolutePath ();
            if ( isHiddenCache.containsKey ( absolutePath ) )
            {
                isHidden = isHiddenCache.get ( absolutePath );
            }
            else if ( file.exists () )
            {
                final File absoluteFile = file.getAbsoluteFile ();
                isHidden = getParent ( absoluteFile ) != null && absoluteFile.isHidden ();
                isHiddenCache.put ( absolutePath, isHidden );
            }
        }
        return isHidden;
    }

    /**
     * Clears cache for "getFileDescription" method.
     */
    public static void clearFileDescriptionCache ()
    {
        fileDescriptionCache.clear ();
    }

    /**
     * Clears cache for "getFileDescription" method for specified file path.
     *
     * @param absolutePath path to clear file description cache
     */
    public static void clearFileDescriptionCache ( @NotNull final String absolutePath )
    {
        fileDescriptionCache.remove ( absolutePath );
    }

    /**
     * Returns complete file description.
     *
     * @param file     file to process
     * @param fileSize file size on disk
     * @return complete file description
     */
    public static FileDescription getFileDescription ( @NotNull final File file, @Nullable final String fileSize )
    {
        final FileDescription fileDescription;
        final String absolutePath = file.getAbsolutePath ();
        if ( fileDescriptionCache.containsKey ( absolutePath ) )
        {
            fileDescription = fileDescriptionCache.get ( absolutePath );
        }
        else if ( file.exists () )
        {
            fileDescription = createFileDescription ( file, fileSize );
            fileDescriptionCache.put ( absolutePath, fileDescription );
        }
        else
        {
            fileDescription = new FileDescription ( "Unknown", "0", "", null );
        }
        return fileDescription;
    }

    /**
     * Returns appropriate file name based on provided text.
     *
     * @param text text to trim
     * @return appropriate file name based on provided text
     */
    @NotNull
    public static String appropriateFileName ( @NotNull final String text )
    {
        return appropriateFileName ( text, "" );
    }

    /**
     * Returns appropriate file name based on provided text.
     *
     * @param text        text to trim
     * @param replacement replacement for each invalid symbol
     * @return appropriate file name based on provided text
     */
    @NotNull
    public static String appropriateFileName ( @NotNull final String text, @NotNull final String replacement )
    {
        return text.replaceAll ( "[^ a-zA-Zа-яА-Я0-9.-]", replacement );
    }

    /**
     * Clears cache for "getDisplayFileName" method.
     */
    public static void clearDisplayFileNameCache ()
    {
        displayFileNameCache.clear ();
    }

    /**
     * Clears cache for "getDisplayFileName" method for specified file path.
     *
     * @param absolutePath path path path to clear file display name cache for
     */
    public static void clearDisplayFileNameCache ( @NotNull final String absolutePath )
    {
        displayFileNameCache.remove ( absolutePath );
    }

    /**
     * Returns file name to display.
     *
     * @param file file to process
     * @return file name to display
     */
    @NotNull
    public static String getDisplayFileName ( @NotNull final File file )
    {
        String name;
        final String absolutePath = file.getAbsolutePath ();
        if ( displayFileNameCache.containsKey ( absolutePath ) )
        {
            name = displayFileNameCache.get ( absolutePath );
        }
        else if ( file.exists () )
        {
            synchronized ( fsv )
            {
                name = fsv.getSystemDisplayName ( file );
            }
            if ( TextUtils.isBlank ( name ) )
            {
                name = getFileTypeDescription ( file );
            }
            displayFileNameCache.put ( absolutePath, name );
        }
        else if ( !file.exists () )
        {
            displayFileNameCache.put ( absolutePath, file.getName () );
            name = file.getName ();
        }
        else
        {
            name = file.getName ();
        }
        return name;
    }

    /**
     * Clears cache for "getDisplayFileCreationDate" method.
     */
    public static void clearDisplayFileCreationDateCache ()
    {
        displayFileCreationDateCache.clear ();
    }

    /**
     * Clears cache for "getDisplayFileCreationDate" method for specified file path.
     *
     * @param absolutePath path path to clear file creation date cache for
     */
    public static void clearDisplayFileCreationDateCache ( @NotNull final String absolutePath )
    {
        displayFileCreationDateCache.remove ( absolutePath );
    }

    /**
     * Returns file modification date to display.
     *
     * @param file file to process
     * @return file modification date to display
     */
    @NotNull
    public static String getDisplayFileCreationDate ( @NotNull final File file )
    {
        final String date;
        final String absolutePath = file.getAbsolutePath ();
        if ( displayFileCreationDateCache.containsKey ( absolutePath ) )
        {
            date = displayFileCreationDateCache.get ( absolutePath );
        }
        else if ( file.exists () )
        {
            date = sdf.format ( new Date ( file.lastModified () ) );
            displayFileCreationDateCache.put ( absolutePath, date );
        }
        else
        {
            date = "";
        }
        return date;
    }

    /**
     * Clears cache for "getDisplayFileModificationDate" method.
     */
    public static void clearDisplayFileModificationDateCache ()
    {
        displayFileModificationDateCache.clear ();
    }

    /**
     * Clears cache for "getDisplayFileModificationDate" method for specified file path.
     *
     * @param absolutePath path path to clear file modification date cache for
     */
    public static void clearDisplayFileModificationDateCache ( @NotNull final String absolutePath )
    {
        displayFileModificationDateCache.remove ( absolutePath );
    }

    /**
     * Returns file modification date to display.
     *
     * @param file file to process
     * @return file modification date to display
     */
    @NotNull
    public static String getDisplayFileModificationDate ( @NotNull final File file )
    {
        final String date;
        final String absolutePath = file.getAbsolutePath ();
        if ( displayFileModificationDateCache.containsKey ( absolutePath ) )
        {
            date = displayFileModificationDateCache.get ( absolutePath );
        }
        else if ( file.exists () )
        {
            date = sdf.format ( new Date ( file.lastModified () ) );
            displayFileModificationDateCache.put ( absolutePath, date );
        }
        else
        {
            date = "";
        }
        return date;
    }

    /**
     * Clears cache for "getFileTypeDescription" method.
     */
    public static void clearFileTypeDescriptionCache ()
    {
        fileTypeDescriptionCache.clear ();
    }

    /**
     * Clears cache for "getFileTypeDescription" method for specified file path.
     *
     * @param absolutePath path to clear file type description cache for
     */
    public static void clearFileTypeDescriptionCache ( @NotNull final String absolutePath )
    {
        fileTypeDescriptionCache.remove ( absolutePath );
    }

    /**
     * Returns file type description.
     *
     * @param file file to process
     * @return file type description
     */
    @NotNull
    public static String getFileTypeDescription ( @Nullable final File file )
    {
        String description = "";
        if ( file != null )
        {
            final String absolutePath = file.getAbsolutePath ();
            if ( fileTypeDescriptionCache.containsKey ( absolutePath ) )
            {
                description = fileTypeDescriptionCache.get ( absolutePath );
            }
            else if ( file.exists () )
            {
                synchronized ( fsv )
                {
                    description = fsv.getSystemTypeDescription ( file );
                }
                if ( description == null )
                {
                    description = "";
                }
                fileTypeDescriptionCache.put ( absolutePath, absolutePath );
            }
        }
        return description;
    }

    /**
     * Returns system file icon.
     *
     * @param file file to process
     * @return system file icon
     */
    @Nullable
    public static Icon getFileIcon ( @Nullable final File file )
    {
        return getFileIcon ( file, false );
    }

    /**
     * Returns either large or small system file icon.
     *
     * @param file  file to process
     * @param large whether return large icon or not
     * @return either large or small system file icon
     */
    @Nullable
    public static Icon getFileIcon ( @Nullable final File file, final boolean large )
    {
        // This way of icon retrieval is disabled due to inaccurate icon sizes on Win Vista/7/8
        //        if ( SystemUtils.isWindows () )
        //        {
        //            if ( file == null )
        //            {
        //                return null;
        //            }
        //
        //            ShellFolder sf;
        //            try
        //            {
        //                sf = ShellFolder.getShellFolder ( file );
        //            }
        //            catch ( FileNotFoundException e )
        //            {
        //                return null;
        //            }
        //
        //            Image img = sf.getIcon ( large );
        //            if ( img != null )
        //            {
        //                return new ImageIcon ( img, sf.getFolderType () );
        //            }
        //            else
        //            {
        //                return null;
        //            }
        //        }
        //        else
        //        {
        return getStandardFileIcon ( file, large );
        //        }
    }

    /**
     * Returns either large or small file icon from a standard icons set.
     *
     * @param file  file to process
     * @param large whether return large icon or not
     * @return either large or small file icon
     */
    @Nullable
    public static Icon getStandardFileIcon ( @Nullable final File file, final boolean large )
    {
        return getStandardFileIcon ( file, large, true );
    }

    /**
     * Returns either large or small file icon from a standard icons set.
     *
     * @param file    file to process
     * @param large   whether return large icon or not
     * @param enabled whether enabled icon or not
     * @return either large or small file icon
     */
    @Nullable
    public static Icon getStandardFileIcon ( @Nullable final File file, final boolean large, final boolean enabled )
    {
        Icon icon = null;
        if ( file != null )
        {
            // todo Properly lock operations for all cached file methods

            // Retrieving required icon extension or type
            String extension;
            if ( !isDirectory ( file ) )
            {
                extension = getFileExtPart ( file.getName (), false ).trim ().toLowerCase ( Locale.ROOT );
                if ( extension.trim ().equals ( "" ) )
                {
                    extension = file.getAbsolutePath ();
                }
            }
            else if ( isCdDrive ( file ) )
            {
                extension = "cd_drive";
            }
            else if ( isDrive ( file ) )
            {
                extension = "drive";
            }
            else if ( isComputer ( file ) )
            {
                extension = "computer";
            }
            else
            {
                extension = "folder";
            }

            // Constructing icon cache key
            final float opacity = isHidden ( file ) ? 0.5f : 1f;
            final String key = getStandardFileIconCacheKey ( extension, large, opacity, enabled );

            // Retrieving icon
            final boolean contains;
            synchronized ( extensionIconsCacheLock )
            {
                contains = extensionIconsCache.containsKey ( key );
            }
            if ( contains )
            {
                synchronized ( extensionIconsCacheLock )
                {
                    icon = extensionIconsCache.get ( key );
                }
            }
            else
            {
                // Retrieving file type icon
                icon = getStandardFileIcon ( large, extension, opacity );
                if ( icon == null )
                {
                    // Simply use unknown file icon
                    icon = getStandardFileIcon ( large, "file", opacity );
                }

                // Caching the resulting icon
                if ( enabled )
                {
                    // Cache enabled icon
                    if ( icon != null )
                    {
                        synchronized ( extensionIconsCacheLock )
                        {
                            extensionIconsCache.put ( key, icon );
                        }
                    }
                }
                else
                {
                    // Cache enabled icon
                    final String keyEnabled = getStandardFileIconCacheKey ( extension, large, opacity, true );
                    if ( icon != null )
                    {
                        synchronized ( extensionIconsCacheLock )
                        {
                            extensionIconsCache.put ( keyEnabled, icon );
                        }
                    }

                    // Cache disabled icon
                    if ( icon != null )
                    {
                        icon = ImageUtils.getDisabledCopy ( icon );
                        synchronized ( extensionIconsCacheLock )
                        {
                            extensionIconsCache.put ( key, icon );
                        }
                    }
                }
            }
        }
        return icon;
    }

    /**
     * Returns standard file icon cache key.
     *
     * @param extension file extension or identifier
     * @param large     whether large icon used or not
     * @param opacity   icon opacity
     * @param enabled   whether enabled icon or not
     * @return standard file icon cache key
     */
    @NotNull
    private static String getStandardFileIconCacheKey ( @NotNull final String extension, final boolean large, final float opacity,
                                                        final boolean enabled )
    {
        return extension + CACHE_KEYS_SEPARATOR + large + CACHE_KEYS_SEPARATOR + opacity + CACHE_KEYS_SEPARATOR + enabled;
    }

    /**
     * Returns either large or small icon for the specified extension from a standard icons set.
     *
     * @param large     whether return large icon or not
     * @param extension file extension
     * @param opacity   icon opacity
     * @return either large or small icon for the specified extension
     */
    @Nullable
    public static Icon getStandardFileIcon ( final boolean large, @NotNull final String extension, final float opacity )
    {
        final String path = "icons/extensions/" + ( large ? "32" : "16" ) + "/file_extension_" + extension + ".png";
        return getIconResource ( FileUtils.class, path, opacity );
    }

    /**
     * Returns resource icon.
     * Note that returned icon will be cached using its placement.
     *
     * @param nearClass class near which the icon is located
     * @param resource  icon location
     * @return resource icon
     */
    @Nullable
    public static Icon getIconResource ( @NotNull final Class nearClass, @NotNull final String resource )
    {
        return getIconResource ( nearClass, resource, 1f );
    }

    /**
     * Returns resource icon with the specified opacity.
     * Note that returned icon will be cached using its placement and opacity value.
     *
     * @param nearClass class near which the icon is located
     * @param resource  icon location
     * @param opacity   custom icon opacity
     * @return resource icon
     */
    @Nullable
    public static Icon getIconResource ( @NotNull final Class nearClass, @NotNull final String resource, final float opacity )
    {
        Icon icon;
        final String key = nearClass.getCanonicalName () + CACHE_KEYS_SEPARATOR + resource + CACHE_KEYS_SEPARATOR + opacity;
        if ( resourceIconsCache.containsKey ( key ) )
        {
            icon = resourceIconsCache.get ( key );
        }
        else
        {
            final URL url = nearClass.getResource ( resource );
            if ( url != null )
            {
                icon = new ImageIcon ( url );
                if ( opacity < 1f )
                {
                    icon = ImageUtils.createTransparentCopy ( icon, opacity );
                }
            }
            else
            {
                icon = null;
            }
            if ( icon != null )
            {
                resourceIconsCache.put ( key, icon );
            }
        }
        return icon;
    }

    /**
     * Starts tracking file for possible changes.
     *
     * @param file     file to track
     * @param listener system file listener
     * @return tracking timer
     */
    @NotNull
    public static WebTimer trackFile ( @NotNull final File file, @NotNull final SystemFileListener listener )
    {
        return trackFile ( file, listener, FILE_TRACKING_DELAY );
    }

    /**
     * Starts tracking file for possible changes.
     *
     * @param file     file to track
     * @param listener system file listener
     * @param delay    delay between checks for changes
     * @return tracking timer
     */
    @NotNull
    public static WebTimer trackFile ( @NotNull final File file, @NotNull final SystemFileListener listener, final long delay )
    {
        final WebTimer tracker = new WebTimer ( "File tracker - " + file.getName (), delay, 0 );
        tracker.addActionListener ( new ActionListener ()
        {
            private Long lastModified = null;

            @Override
            public void actionPerformed ( @NotNull final ActionEvent e )
            {
                if ( file.exists () )
                {
                    final long lm = file.lastModified ();
                    if ( lastModified != lm )
                    {
                        listener.modified ( file );
                        lastModified = lm;
                    }
                }
                else
                {
                    listener.unbound ( file );
                    tracker.stop ();
                }
            }
        } );
        tracker.setUseDaemonThread ( true );
        return tracker;
    }
}