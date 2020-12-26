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

package com.alee.laf.menu;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.*;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.UISettingsManager;
import com.alee.managers.style.*;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.extensions.FontMethods;
import com.alee.utils.swing.extensions.FontMethodsImpl;
import com.alee.utils.swing.extensions.SizeMethods;
import com.alee.utils.swing.extensions.SizeMethodsImpl;

import javax.swing.*;
import java.awt.*;

/**
 * {@link JRadioButtonMenuItem} extension class.
 * It contains various useful methods to simplify core component usage.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see JRadioButtonMenuItem
 * @see WebRadioButtonMenuItemUI
 * @see RadioButtonMenuItemPainter
 */
public class WebRadioButtonMenuItem extends JRadioButtonMenuItem implements Styleable, LanguageMethods, LanguageEventMethods,
        SettingsMethods, FontMethods<WebRadioButtonMenuItem>, SizeMethods<WebRadioButtonMenuItem>
{
    /**
     * Constructs new radiobutton menu item.
     */
    public WebRadioButtonMenuItem ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param action menu item action
     */
    public WebRadioButtonMenuItem ( final Action action )
    {
        this ( StyleId.auto, action );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param icon menu item icon
     */
    public WebRadioButtonMenuItem ( final Icon icon )
    {
        this ( StyleId.auto, icon );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param icon     menu item icon
     * @param selected whether this radiobutton item is selected or not
     */
    public WebRadioButtonMenuItem ( final Icon icon, final boolean selected )
    {
        this ( StyleId.auto, icon, selected );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text menu item text
     */
    public WebRadioButtonMenuItem ( final String text )
    {
        this ( StyleId.auto, text );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text        menu item text
     * @param accelerator menu item accelerator
     */
    public WebRadioButtonMenuItem ( final String text, final KeyStroke accelerator )
    {
        this ( StyleId.auto, text, accelerator );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text        menu item text
     * @param accelerator menu item accelerator
     */
    public WebRadioButtonMenuItem ( final String text, final HotkeyData accelerator )
    {
        this ( StyleId.auto, text, accelerator );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text menu item text
     * @param icon menu item icon
     */
    public WebRadioButtonMenuItem ( final String text, final Icon icon )
    {
        this ( StyleId.auto, text, icon );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text     menu item text
     * @param icon     menu item icon
     * @param selected whether this radiobutton item is selected or not
     */
    public WebRadioButtonMenuItem ( final String text, final Icon icon, final boolean selected )
    {
        this ( StyleId.auto, text, icon, selected );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text     menu item text
     * @param selected whether this radiobutton item is selected or not
     */
    public WebRadioButtonMenuItem ( final String text, final boolean selected )
    {
        this ( StyleId.auto, text, selected );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text        menu item text
     * @param icon        menu item icon
     * @param accelerator menu item accelerator
     */
    public WebRadioButtonMenuItem ( final String text, final Icon icon, final KeyStroke accelerator )
    {
        this ( StyleId.auto, text, icon, accelerator );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text        menu item text
     * @param icon        menu item icon
     * @param selected    whether this checkbox item is selected or not
     * @param accelerator menu item accelerator
     */
    public WebRadioButtonMenuItem ( final String text, final Icon icon, final boolean selected, final KeyStroke accelerator )
    {
        this ( StyleId.auto, text, icon, selected, accelerator );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text        menu item text
     * @param icon        menu item icon
     * @param accelerator menu item accelerator
     */
    public WebRadioButtonMenuItem ( final String text, final Icon icon, final HotkeyData accelerator )
    {
        this ( StyleId.auto, text, icon, accelerator );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param text        menu item text
     * @param icon        menu item icon
     * @param selected    whether this checkbox item is selected or not
     * @param accelerator menu item accelerator
     */
    public WebRadioButtonMenuItem ( final String text, final Icon icon, final boolean selected, final HotkeyData accelerator )
    {
        this ( StyleId.auto, text, icon, selected, accelerator );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param id     {@link StyleId}
     * @param action menu item action
     */
    public WebRadioButtonMenuItem ( final StyleId id, final Action action )
    {
        this ( id, null, null, false, ( KeyStroke ) null );
        setAction ( action );
    }

    /**
     * Constructs new radiobutton menu item.
     *
     * @param id {@link StyleId}
     */
    public WebRadioButtonMenuItem ( final StyleId id )
    {
        this ( id, null, null, false, ( KeyStroke ) null );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param id   {@link StyleId}
     * @param icon menu item icon
     */
    public WebRadioButtonMenuItem ( final StyleId id, final Icon icon )
    {
        this ( id, null, icon, false, ( KeyStroke ) null );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param id       {@link StyleId}
     * @param icon     menu item icon
     * @param selected whether this radiobutton item is selected or not
     */
    public WebRadioButtonMenuItem ( final StyleId id, final Icon icon, final boolean selected )
    {
        this ( id, null, icon, selected, ( KeyStroke ) null );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param id   {@link StyleId}
     * @param text menu item text
     */
    public WebRadioButtonMenuItem ( final StyleId id, final String text )
    {
        this ( id, text, null, false, ( KeyStroke ) null );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param id          {@link StyleId}
     * @param text        menu item text
     * @param accelerator menu item accelerator
     */
    public WebRadioButtonMenuItem ( final StyleId id, final String text, final KeyStroke accelerator )
    {
        this ( id, text, null, false, accelerator );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param id          {@link StyleId}
     * @param text        menu item text
     * @param accelerator menu item accelerator
     */
    public WebRadioButtonMenuItem ( final StyleId id, final String text, final HotkeyData accelerator )
    {
        this ( id, text, null, false, accelerator );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param id   {@link StyleId}
     * @param text menu item text
     * @param icon menu item icon
     */
    public WebRadioButtonMenuItem ( final StyleId id, final String text, final Icon icon )
    {
        this ( id, text, icon, false, ( KeyStroke ) null );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param id       {@link StyleId}
     * @param text     menu item text
     * @param icon     menu item icon
     * @param selected whether this radiobutton item is selected or not
     */
    public WebRadioButtonMenuItem ( final StyleId id, final String text, final Icon icon, final boolean selected )
    {
        this ( id, text, icon, selected, ( KeyStroke ) null );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param id       {@link StyleId}
     * @param text     menu item text
     * @param selected whether this radiobutton item is selected or not
     */
    public WebRadioButtonMenuItem ( final StyleId id, final String text, final boolean selected )
    {
        this ( id, text, null, selected, ( KeyStroke ) null );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param id          {@link StyleId}
     * @param text        menu item text
     * @param icon        menu item icon
     * @param accelerator menu item accelerator
     */
    public WebRadioButtonMenuItem ( final StyleId id, final String text, final Icon icon, final KeyStroke accelerator )
    {
        this ( id, text, icon, false, accelerator );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param id          {@link StyleId}
     * @param text        menu item text
     * @param icon        menu item icon
     * @param accelerator menu item accelerator
     */
    public WebRadioButtonMenuItem ( final StyleId id, final String text, final Icon icon, final HotkeyData accelerator )
    {
        this ( id, text, icon, false, accelerator );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param id          {@link StyleId}
     * @param text        menu item text
     * @param icon        menu item icon
     * @param selected    whether this checkbox item is selected or not
     * @param accelerator menu item accelerator
     */
    public WebRadioButtonMenuItem ( final StyleId id, final String text, final Icon icon, final boolean selected,
                                    final HotkeyData accelerator )
    {
        this ( id, text, icon, selected, SwingUtils.getAccelerator ( accelerator ) );
    }

    /**
     * Constructs new radiobutton menu item using the specified settings.
     *
     * @param id          {@link StyleId}
     * @param text        menu item text
     * @param icon        menu item icon
     * @param selected    whether this checkbox item is selected or not
     * @param accelerator menu item accelerator
     */
    public WebRadioButtonMenuItem ( final StyleId id, final String text, final Icon icon, final boolean selected,
                                    final KeyStroke accelerator )
    {
        super ( text, icon, selected );
        setAccelerator ( accelerator );
        setStyleId ( id );
    }

    @Override
    protected void init ( final String text, final Icon icon )
    {
        super.init ( UILanguageManager.getInitialText ( text ), icon );
        UILanguageManager.registerInitialLanguage ( this, text );
    }

    /**
     * Sets the key combination which invokes the menu item's action listeners without navigating the menu hierarchy.
     *
     * @param accelerator hotkey data
     */
    public void setAccelerator ( final HotkeyData accelerator )
    {
        setAccelerator ( SwingUtils.getAccelerator ( accelerator ) );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.radiobuttonmenuitem;
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
    public WebRadioButtonMenuItem setPlainFont ()
    {
        return FontMethodsImpl.setPlainFont ( this );
    }

    @Override
    public WebRadioButtonMenuItem setPlainFont ( final boolean apply )
    {
        return FontMethodsImpl.setPlainFont ( this, apply );
    }

    @Override
    public boolean isPlainFont ()
    {
        return FontMethodsImpl.isPlainFont ( this );
    }

    @Override
    public WebRadioButtonMenuItem setBoldFont ()
    {
        return FontMethodsImpl.setBoldFont ( this );
    }

    @Override
    public WebRadioButtonMenuItem setBoldFont ( final boolean apply )
    {
        return FontMethodsImpl.setBoldFont ( this, apply );
    }

    @Override
    public boolean isBoldFont ()
    {
        return FontMethodsImpl.isBoldFont ( this );
    }

    @Override
    public WebRadioButtonMenuItem setItalicFont ()
    {
        return FontMethodsImpl.setItalicFont ( this );
    }

    @Override
    public WebRadioButtonMenuItem setItalicFont ( final boolean apply )
    {
        return FontMethodsImpl.setItalicFont ( this, apply );
    }

    @Override
    public boolean isItalicFont ()
    {
        return FontMethodsImpl.isItalicFont ( this );
    }

    @Override
    public WebRadioButtonMenuItem setFontStyle ( final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebRadioButtonMenuItem setFontStyle ( final int style )
    {
        return FontMethodsImpl.setFontStyle ( this, style );
    }

    @Override
    public WebRadioButtonMenuItem setFontSize ( final int fontSize )
    {
        return FontMethodsImpl.setFontSize ( this, fontSize );
    }

    @Override
    public WebRadioButtonMenuItem changeFontSize ( final int change )
    {
        return FontMethodsImpl.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return FontMethodsImpl.getFontSize ( this );
    }

    @Override
    public WebRadioButtonMenuItem setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebRadioButtonMenuItem setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public WebRadioButtonMenuItem setFontName ( final String fontName )
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
    public WebRadioButtonMenuItem setPreferredWidth ( final int preferredWidth )
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
    public WebRadioButtonMenuItem setPreferredHeight ( final int preferredHeight )
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
    public WebRadioButtonMenuItem setPreferredSize ( final int width, final int height )
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
    public WebRadioButtonMenuItem setMaximumWidth ( final int maximumWidth )
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
    public WebRadioButtonMenuItem setMaximumHeight ( final int maximumHeight )
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
    public WebRadioButtonMenuItem setMaximumSize ( final int width, final int height )
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
    public WebRadioButtonMenuItem setMinimumWidth ( final int minimumWidth )
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
    public WebRadioButtonMenuItem setMinimumHeight ( final int minimumHeight )
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
    public WebRadioButtonMenuItem setMinimumSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setMinimumSize ( this, width, height );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WebRadioButtonMenuItemUI} object that renders this component
     */
    @Override
    public WebRadioButtonMenuItemUI getUI ()
    {
        return ( WebRadioButtonMenuItemUI ) super.getUI ();
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WebRadioButtonMenuItemUI}
     */
    public void setUI ( final WebRadioButtonMenuItemUI ui )
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
}