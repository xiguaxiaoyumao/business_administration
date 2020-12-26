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
import com.alee.api.annotations.Nullable;
import com.alee.managers.language.*;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.UISettingsManager;
import com.alee.managers.style.*;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.swing.Customizer;
import com.alee.utils.swing.extensions.FontMethods;
import com.alee.utils.swing.extensions.FontMethodsImpl;
import com.alee.utils.swing.extensions.SizeMethods;
import com.alee.utils.swing.extensions.SizeMethodsImpl;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link JFileChooser} extension class.
 * It contains various useful methods to simplify core component usage.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see JFileChooser
 * @see WebFileChooserUI
 * @see FileChooserPainter
 */
public class WebFileChooser extends JFileChooser implements Styleable, LanguageMethods, LanguageEventMethods, SettingsMethods,
        FontMethods<WebFileChooser>, SizeMethods<WebFileChooser>
{
    /**
     * Custom icons for file chooser dialog.
     */
    @Nullable
    protected List<Image> customIcons = null;

    /**
     * Constructs a WebFileChooser pointing to the user's default directory.
     */
    public WebFileChooser ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs a WebFileChooser using the given path.
     * Passing in a null string causes the file chooser to point to the user's default directory.
     *
     * @param dirPath a String giving the path to a file or directory
     */
    public WebFileChooser ( final String dirPath )
    {
        this ( StyleId.auto, dirPath );
    }

    /**
     * Constructs a WebFileChooser using the given File as the path.
     * Passing in a null file causes the file chooser to point to the user's default directory.
     *
     * @param dir a File object specifying the path to a file or directory
     */
    public WebFileChooser ( final File dir )
    {
        this ( StyleId.auto, dir );
    }

    /**
     * Constructs a WebFileChooser using the given FileSystemView.
     *
     * @param fsv file system view
     */
    public WebFileChooser ( final FileSystemView fsv )
    {
        this ( StyleId.auto, fsv );
    }

    /**
     * Constructs a WebFileChooser using the given current directory and FileSystemView.
     *
     * @param dir a File object specifying the path to a file or directory
     * @param fsv file system view
     */
    public WebFileChooser ( final File dir, final FileSystemView fsv )
    {
        this ( StyleId.auto, dir, fsv );
    }

    /**
     * Constructs a WebFileChooser using the given current directory path and FileSystemView.
     *
     * @param dirPath a String giving the path to a file or directory
     * @param fsv     file system view
     */
    public WebFileChooser ( final String dirPath, final FileSystemView fsv )
    {
        this ( StyleId.auto, dirPath, fsv );
    }

    /**
     * Constructs a WebFileChooser pointing to the user's default directory.
     *
     * @param id {@link StyleId}
     */
    public WebFileChooser ( final StyleId id )
    {
        this ( id, FileUtils.getUserHomePath (), null );
    }

    /**
     * Constructs a WebFileChooser using the given path.
     * Passing in a null string causes the file chooser to point to the user's default directory.
     *
     * @param id      {@link StyleId}
     * @param dirPath a String giving the path to a file or directory
     */
    public WebFileChooser ( final StyleId id, final String dirPath )
    {
        this ( id, dirPath, null );
    }

    /**
     * Constructs a WebFileChooser using the given File as the path.
     * Passing in a null file causes the file chooser to point to the user's default directory.
     *
     * @param id  {@link StyleId}
     * @param dir a File object specifying the path to a file or directory
     */
    public WebFileChooser ( final StyleId id, final File dir )
    {
        this ( id, dir, null );
    }

    /**
     * Constructs a WebFileChooser using the given FileSystemView.
     *
     * @param id  {@link StyleId}
     * @param fsv file system view
     */
    public WebFileChooser ( final StyleId id, final FileSystemView fsv )
    {
        this ( id, ( File ) null, fsv );
    }

    /**
     * Constructs a WebFileChooser using the given current directory and FileSystemView.
     *
     * @param id  {@link StyleId}
     * @param dir a File object specifying the path to a file or directory
     * @param fsv file system view
     */
    public WebFileChooser ( final StyleId id, final File dir, final FileSystemView fsv )
    {
        super ( dir, fsv );
        setStyleId ( id );
    }

    /**
     * Constructs a WebFileChooser using the given current directory path and FileSystemView.
     *
     * @param id      {@link StyleId}
     * @param dirPath a String giving the path to a file or directory
     * @param fsv     file system view
     */
    public WebFileChooser ( final StyleId id, final String dirPath, final FileSystemView fsv )
    {
        super ( dirPath, fsv );
        setStyleId ( id );
    }

    @Override
    protected JDialog createDialog ( final Component parent ) throws HeadlessException
    {
        final JDialog dialog = super.createDialog ( parent );
        if ( customIcons != null )
        {
            dialog.setIconImages ( customIcons );
        }
        return dialog;
    }

    /**
     * Returns custom dialog icon.
     *
     * @return custom dialog icon
     */
    @Nullable
    public Image getDialogIcon ()
    {
        return customIcons != null && customIcons.size () > 0 ? customIcons.get ( 0 ) : null;
    }

    /**
     * Returns custom dialog icons.
     *
     * @return custom dialog icons
     */
    @Nullable
    public List<Image> getDialogIcons ()
    {
        return customIcons;
    }

    /**
     * Sets custom dialog icon.
     *
     * @param icon new custom dialog icon
     */
    public void setDialogIcon ( @Nullable final Icon icon )
    {
        setDialogImage ( ImageUtils.toBufferedImage ( icon ) );
    }

    /**
     * Sets custom dialog icon.
     *
     * @param icon new custom dialog icon
     */
    public void setDialogImage ( @Nullable final Image icon )
    {
        setDialogImages ( CollectionUtils.asList ( icon ) );
    }

    /**
     * Sets custom dialog icons.
     *
     * @param customIcons new custom dialog icons
     */
    public void setDialogIcons ( @NotNull final List<? extends Icon> customIcons )
    {
        this.customIcons = new ArrayList<Image> ( customIcons.size () );
        for ( final Icon icon : customIcons )
        {
            final Image image = ImageUtils.toBufferedImage ( icon );
            this.customIcons.add ( image );
        }
        updateWindowIcons ();
    }

    /**
     * Sets custom dialog icons.
     *
     * @param customIcons new custom dialog icons
     */
    public void setDialogImages ( @NotNull final List<? extends Image> customIcons )
    {
        this.customIcons = new ArrayList<Image> ( customIcons );
        updateWindowIcons ();
    }

    /**
     * Updates icons on {@link JDialog}.
     */
    protected void updateWindowIcons ()
    {
        final Window window = CoreSwingUtils.getWindowAncestor ( this );
        if ( window != null && window instanceof JDialog )
        {
            window.setIconImages ( customIcons );
        }
    }

    /**
     * Sets dialog title language key.
     *
     * @param dialogTitle title language key
     */
    public void setDialogTitleKey ( final String dialogTitle )
    {
        setLanguage ( dialogTitle );
    }

    /**
     * Sets currently displayed directory.
     *
     * @param path directory to display
     */
    public void setCurrentDirectory ( final String path )
    {
        setCurrentDirectory ( path != null ? new File ( path ) : null );
    }

    /**
     * Sets currently selected file.
     *
     * @param path file to select
     */
    public void setSelectedFile ( final String path )
    {
        setSelectedFile ( path != null ? new File ( path ) : null );
    }

    /**
     * Returns file chooser panel.
     *
     * @return file chooser panel
     */
    public WebFileChooserPanel getFileChooserPanel ()
    {
        return getUI ().getFileChooserPanel ();
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.filechooser;
    }

    @NotNull
    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( this );
    }

    @NotNull
    @Override
    public StyleId setStyleId ( @NotNull final StyleId id )
    {
        return StyleManager.setStyleId ( this, id );
    }

    @NotNull
    @Override
    public StyleId resetStyleId ()
    {
        return StyleManager.resetStyleId ( this );
    }

    @NotNull
    @Override
    public Skin getSkin ()
    {
        return StyleManager.getSkin ( this );
    }

    @Nullable
    @Override
    public Skin setSkin ( @NotNull final Skin skin )
    {
        return StyleManager.setSkin ( this, skin );
    }

    @Nullable
    @Override
    public Skin setSkin ( @NotNull final Skin skin, final boolean recursively )
    {
        return StyleManager.setSkin ( this, skin, recursively );
    }

    @Nullable
    @Override
    public Skin resetSkin ()
    {
        return StyleManager.resetSkin ( this );
    }

    @Override
    public void addStyleListener ( @NotNull final StyleListener listener )
    {
        StyleManager.addStyleListener ( this, listener );
    }

    @Override
    public void removeStyleListener ( @NotNull final StyleListener listener )
    {
        StyleManager.removeStyleListener ( this, listener );
    }

    @Nullable
    @Override
    public Painter getCustomPainter ()
    {
        return StyleManager.getCustomPainter ( this );
    }

    @Nullable
    @Override
    public Painter setCustomPainter ( @NotNull final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, painter );
    }

    @Override
    public boolean resetCustomPainter ()
    {
        return StyleManager.resetCustomPainter ( this );
    }

    @NotNull
    @Override
    public Shape getPainterShape ()
    {
        return PainterSupport.getShape ( this );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( this );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( this, enabled );
    }

    @Nullable
    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( this );
    }

    @Override
    public void setMargin ( final int margin )
    {
        PainterSupport.setMargin ( this, margin );
    }

    @Override
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        PainterSupport.setMargin ( this, top, left, bottom, right );
    }

    @Override
    public void setMargin ( @Nullable final Insets margin )
    {
        PainterSupport.setMargin ( this, margin );
    }

    @Nullable
    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( this );
    }

    @Override
    public void setPadding ( final int padding )
    {
        PainterSupport.setPadding ( this, padding );
    }

    @Override
    public void setPadding ( final int top, final int left, final int bottom, final int right )
    {
        PainterSupport.setPadding ( this, top, left, bottom, right );
    }

    @Override
    public void setPadding ( @Nullable final Insets padding )
    {
        PainterSupport.setPadding ( this, padding );
    }

    @Nullable
    @Override
    public String getLanguage ()
    {
        return UILanguageManager.getComponentKey ( this );
    }

    @Override
    public void setLanguage ( @NotNull final String key, @Nullable final Object... data )
    {
        UILanguageManager.registerComponent ( this, key, data );
    }

    @Override
    public void updateLanguage ( @Nullable final Object... data )
    {
        UILanguageManager.updateComponent ( this, data );
    }

    @Override
    public void updateLanguage ( @NotNull final String key, @Nullable final Object... data )
    {
        UILanguageManager.updateComponent ( this, key, data );
    }

    @Override
    public void removeLanguage ()
    {
        UILanguageManager.unregisterComponent ( this );
    }

    @Override
    public boolean isLanguageSet ()
    {
        return UILanguageManager.isRegisteredComponent ( this );
    }

    @Override
    public void setLanguageUpdater ( @NotNull final LanguageUpdater updater )
    {
        UILanguageManager.registerLanguageUpdater ( this, updater );
    }

    @Override
    public void removeLanguageUpdater ()
    {
        UILanguageManager.unregisterLanguageUpdater ( this );
    }

    @Override
    public void addLanguageListener ( @NotNull final LanguageListener listener )
    {
        UILanguageManager.addLanguageListener ( getRootPane (), listener );
    }

    @Override
    public void removeLanguageListener ( @NotNull final LanguageListener listener )
    {
        UILanguageManager.removeLanguageListener ( getRootPane (), listener );
    }

    @Override
    public void removeLanguageListeners ()
    {
        UILanguageManager.removeLanguageListeners ( getRootPane () );
    }

    @Override
    public void addDictionaryListener ( @NotNull final DictionaryListener listener )
    {
        UILanguageManager.addDictionaryListener ( getRootPane (), listener );
    }

    @Override
    public void removeDictionaryListener ( @NotNull final DictionaryListener listener )
    {
        UILanguageManager.removeDictionaryListener ( getRootPane (), listener );
    }

    @Override
    public void removeDictionaryListeners ()
    {
        UILanguageManager.removeDictionaryListeners ( getRootPane () );
    }

    @Override
    public void registerSettings ( final Configuration configuration )
    {
        UISettingsManager.registerComponent ( this, configuration );
    }

    @Override
    public void registerSettings ( final SettingsProcessor processor )
    {
        UISettingsManager.registerComponent ( this, processor );
    }

    @Override
    public void unregisterSettings ()
    {
        UISettingsManager.unregisterComponent ( this );
    }

    @Override
    public void loadSettings ()
    {
        UISettingsManager.loadSettings ( this );
    }

    @Override
    public void saveSettings ()
    {
        UISettingsManager.saveSettings ( this );
    }

    @Override
    public WebFileChooser setPlainFont ()
    {
        return FontMethodsImpl.setPlainFont ( this );
    }

    @Override
    public WebFileChooser setPlainFont ( final boolean apply )
    {
        return FontMethodsImpl.setPlainFont ( this, apply );
    }

    @Override
    public boolean isPlainFont ()
    {
        return FontMethodsImpl.isPlainFont ( this );
    }

    @Override
    public WebFileChooser setBoldFont ()
    {
        return FontMethodsImpl.setBoldFont ( this );
    }

    @Override
    public WebFileChooser setBoldFont ( final boolean apply )
    {
        return FontMethodsImpl.setBoldFont ( this, apply );
    }

    @Override
    public boolean isBoldFont ()
    {
        return FontMethodsImpl.isBoldFont ( this );
    }

    @Override
    public WebFileChooser setItalicFont ()
    {
        return FontMethodsImpl.setItalicFont ( this );
    }

    @Override
    public WebFileChooser setItalicFont ( final boolean apply )
    {
        return FontMethodsImpl.setItalicFont ( this, apply );
    }

    @Override
    public boolean isItalicFont ()
    {
        return FontMethodsImpl.isItalicFont ( this );
    }

    @Override
    public WebFileChooser setFontStyle ( final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebFileChooser setFontStyle ( final int style )
    {
        return FontMethodsImpl.setFontStyle ( this, style );
    }

    @Override
    public WebFileChooser setFontSize ( final int fontSize )
    {
        return FontMethodsImpl.setFontSize ( this, fontSize );
    }

    @Override
    public WebFileChooser changeFontSize ( final int change )
    {
        return FontMethodsImpl.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return FontMethodsImpl.getFontSize ( this );
    }

    @Override
    public WebFileChooser setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebFileChooser setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public WebFileChooser setFontName ( final String fontName )
    {
        return FontMethodsImpl.setFontName ( this, fontName );
    }

    @Override
    public String getFontName ()
    {
        return FontMethodsImpl.getFontName ( this );
    }

    @Override
    public int getPreferredWidth ()
    {
        return SizeMethodsImpl.getPreferredWidth ( this );
    }

    @NotNull
    @Override
    public WebFileChooser setPreferredWidth ( final int preferredWidth )
    {
        return SizeMethodsImpl.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeMethodsImpl.getPreferredHeight ( this );
    }

    @NotNull
    @Override
    public WebFileChooser setPreferredHeight ( final int preferredHeight )
    {
        return SizeMethodsImpl.setPreferredHeight ( this, preferredHeight );
    }

    @NotNull
    @Override
    public Dimension getPreferredSize ()
    {
        return SizeMethodsImpl.getPreferredSize ( this, super.getPreferredSize () );
    }

    @NotNull
    @Override
    public Dimension getOriginalPreferredSize ()
    {
        return SizeMethodsImpl.getOriginalPreferredSize ( this, super.getPreferredSize () );
    }

    @NotNull
    @Override
    public WebFileChooser setPreferredSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setPreferredSize ( this, width, height );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeMethodsImpl.getMaximumWidth ( this );
    }

    @NotNull
    @Override
    public WebFileChooser setMaximumWidth ( final int maximumWidth )
    {
        return SizeMethodsImpl.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeMethodsImpl.getMaximumHeight ( this );
    }

    @NotNull
    @Override
    public WebFileChooser setMaximumHeight ( final int maximumHeight )
    {
        return SizeMethodsImpl.setMaximumHeight ( this, maximumHeight );
    }

    @NotNull
    @Override
    public Dimension getMaximumSize ()
    {
        return SizeMethodsImpl.getMaximumSize ( this, super.getMaximumSize () );
    }

    @NotNull
    @Override
    public Dimension getOriginalMaximumSize ()
    {
        return SizeMethodsImpl.getOriginalMaximumSize ( this, super.getMaximumSize () );
    }

    @NotNull
    @Override
    public WebFileChooser setMaximumSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setMaximumSize ( this, width, height );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeMethodsImpl.getMinimumWidth ( this );
    }

    @NotNull
    @Override
    public WebFileChooser setMinimumWidth ( final int minimumWidth )
    {
        return SizeMethodsImpl.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeMethodsImpl.getMinimumHeight ( this );
    }

    @NotNull
    @Override
    public WebFileChooser setMinimumHeight ( final int minimumHeight )
    {
        return SizeMethodsImpl.setMinimumHeight ( this, minimumHeight );
    }

    @NotNull
    @Override
    public Dimension getMinimumSize ()
    {
        return SizeMethodsImpl.getMinimumSize ( this, super.getMinimumSize () );
    }

    @NotNull
    @Override
    public Dimension getOriginalMinimumSize ()
    {
        return SizeMethodsImpl.getOriginalMinimumSize ( this, super.getMinimumSize () );
    }

    @NotNull
    @Override
    public WebFileChooser setMinimumSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setMinimumSize ( this, width, height );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WFileChooserUI} object that renders this component
     */
    @Override
    public WFileChooserUI getUI ()
    {
        return ( WFileChooserUI ) super.getUI ();
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WFileChooserUI}
     */
    public void setUI ( final WFileChooserUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public void updateUI ()
    {
        StyleManager.getDescriptor ( this ).updateUI ( this );
    }

    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }

    /**
     * Constructs and displays file open dialog and returns selected file as a result.
     *
     * @return selected file
     */
    public static File showOpenDialog ()
    {
        return showOpenDialog ( null, null, null );
    }

    /**
     * Constructs and displays file open dialog and returns selected file as a result.
     *
     * @param customizer file chooser customizer
     * @return selected file
     */
    public static File showOpenDialog ( final Customizer<WebFileChooser> customizer )
    {
        return showOpenDialog ( null, null, customizer );
    }

    /**
     * Constructs and displays file open dialog and returns selected file as a result.
     *
     * @param parent     parent component
     * @param customizer file chooser customizer
     * @return selected file
     */
    public static File showOpenDialog ( final Component parent, final Customizer<WebFileChooser> customizer )
    {
        return showOpenDialog ( parent, null, customizer );
    }

    /**
     * Constructs and displays file open dialog and returns selected file as a result.
     *
     * @param currentDirectory current file chooser directory
     * @param customizer       file chooser customizer
     * @return selected file
     */
    public static File showOpenDialog ( final String currentDirectory, final Customizer<WebFileChooser> customizer )
    {
        return showOpenDialog ( null, currentDirectory, customizer );
    }

    /**
     * Constructs and displays file open dialog and returns selected file as a result.
     *
     * @param parent           parent component
     * @param currentDirectory current file chooser directory
     * @return selected file
     */
    public static File showOpenDialog ( final Component parent, final String currentDirectory )
    {
        return showOpenDialog ( parent, currentDirectory, null );
    }

    /**
     * Constructs and displays file open dialog and returns selected file as a result.
     *
     * @param parent           parent component
     * @param currentDirectory current file chooser directory
     * @param customizer       file chooser customizer
     * @return selected file
     */
    public static File showOpenDialog ( final Component parent, final String currentDirectory, final Customizer<WebFileChooser> customizer )
    {
        final WebFileChooser fileChooser = new WebFileChooser ( currentDirectory );
        fileChooser.setMultiSelectionEnabled ( false );
        if ( customizer != null )
        {
            customizer.customize ( fileChooser );
        }
        return fileChooser.showOpenDialog ( parent ) == APPROVE_OPTION
                ? fileChooser.getSelectedFile ()
                : null;
    }

    /**
     * Constructs and displays multiple files open dialog and returns selected files list as a result.
     *
     * @return selected files list
     */
    public static List<File> showMultiOpenDialog ()
    {
        return showMultiOpenDialog ( null, null, null );
    }

    /**
     * Constructs and displays multiple files open dialog and returns selected files list as a result.
     *
     * @param customizer file chooser customizer
     * @return selected files list
     */
    public static List<File> showMultiOpenDialog ( final Customizer<WebFileChooser> customizer )
    {
        return showMultiOpenDialog ( null, null, customizer );
    }

    /**
     * Constructs and displays multiple files open dialog and returns selected files list as a result.
     *
     * @param parent     parent component
     * @param customizer file chooser customizer
     * @return selected files list
     */
    public static List<File> showMultiOpenDialog ( final Component parent, final Customizer<WebFileChooser> customizer )
    {
        return showMultiOpenDialog ( parent, null, customizer );
    }

    /**
     * Constructs and displays multiple files open dialog and returns selected files list as a result.
     *
     * @param currentDirectory current file chooser directory
     * @param customizer       file chooser customizer
     * @return selected files list
     */
    public static List<File> showMultiOpenDialog ( final String currentDirectory, final Customizer<WebFileChooser> customizer )
    {
        return showMultiOpenDialog ( null, currentDirectory, customizer );
    }

    /**
     * Constructs and displays multiple files open dialog and returns selected files list as a result.
     *
     * @param parent           parent component
     * @param currentDirectory current file chooser directory
     * @return selected files list
     */
    public static List<File> showMultiOpenDialog ( final Component parent, final String currentDirectory )
    {
        return showMultiOpenDialog ( parent, currentDirectory, null );
    }

    /**
     * Constructs and displays multiple files open dialog and returns selected files list as a result.
     *
     * @param parent           parent component
     * @param currentDirectory current file chooser directory
     * @param customizer       file chooser customizer
     * @return selected files list
     */
    public static List<File> showMultiOpenDialog ( final Component parent, final String currentDirectory,
                                                   final Customizer<WebFileChooser> customizer )
    {
        final WebFileChooser fileChooser = new WebFileChooser ( currentDirectory );
        fileChooser.setMultiSelectionEnabled ( true );
        if ( customizer != null )
        {
            customizer.customize ( fileChooser );
        }
        return fileChooser.showOpenDialog ( parent ) == APPROVE_OPTION
                ? CollectionUtils.toList ( fileChooser.getSelectedFiles () )
                : null;
    }

    /**
     * Constructs and displays file save dialog and returns selected file as a result.
     *
     * @return selected file
     */
    public static File showSaveDialog ()
    {
        return showSaveDialog ( null, null, null );
    }

    /**
     * Constructs and displays file save dialog and returns selected file as a result.
     *
     * @param customizer file chooser customizer
     * @return selected file
     */
    public static File showSaveDialog ( final Customizer<WebFileChooser> customizer )
    {
        return showSaveDialog ( null, null, customizer );
    }

    /**
     * Constructs and displays file save dialog and returns selected file as a result.
     *
     * @param parent     parent component
     * @param customizer file chooser customizer
     * @return selected file
     */
    public static File showSaveDialog ( final Component parent, final Customizer<WebFileChooser> customizer )
    {
        return showSaveDialog ( parent, null, customizer );
    }

    /**
     * Constructs and displays file save dialog and returns selected file as a result.
     *
     * @param currentDirectory current file chooser directory
     * @param customizer       file chooser customizer
     * @return selected file
     */
    public static File showSaveDialog ( final String currentDirectory, final Customizer<WebFileChooser> customizer )
    {
        return showSaveDialog ( null, currentDirectory, customizer );
    }

    /**
     * Constructs and displays file save dialog and returns selected file as a result.
     *
     * @param parent           parent component
     * @param currentDirectory current file chooser directory
     * @return selected file
     */
    public static File showSaveDialog ( final Component parent, final String currentDirectory )
    {
        return showSaveDialog ( parent, currentDirectory, null );
    }

    /**
     * Constructs and displays file save dialog and returns selected file as a result.
     *
     * @param parent           parent component
     * @param currentDirectory current file chooser directory
     * @param customizer       file chooser customizer
     * @return selected file
     */
    public static File showSaveDialog ( final Component parent, final String currentDirectory, final Customizer<WebFileChooser> customizer )
    {
        final WebFileChooser fileChooser = new WebFileChooser ( currentDirectory );
        fileChooser.setMultiSelectionEnabled ( true );
        if ( customizer != null )
        {
            customizer.customize ( fileChooser );
        }
        return fileChooser.showSaveDialog ( parent ) == APPROVE_OPTION
                ? fileChooser.getSelectedFile ()
                : null;
    }
}