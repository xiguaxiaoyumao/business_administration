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

package com.alee.laf.splitpane;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.DictionaryListener;
import com.alee.managers.language.LanguageEventMethods;
import com.alee.managers.language.LanguageListener;
import com.alee.managers.language.UILanguageManager;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.UISettingsManager;
import com.alee.managers.style.*;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.extensions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;

/**
 * {@link JSplitPane} extension class.
 * It contains various useful methods to simplify core component usage.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see JSplitPane
 * @see WSplitPaneUI
 * @see WebSplitPaneUI
 * @see SplitPanePainter
 */
public class WebSplitPane extends JSplitPane implements Styleable, EventMethods, LanguageEventMethods, SettingsMethods,
        FontMethods<WebSplitPane>, SizeMethods<WebSplitPane>
{
    /**
     * Constructs new split pane.
     */
    public WebSplitPane ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new split pane.
     *
     * @param orientation split pane orientation
     */
    public WebSplitPane ( final int orientation )
    {
        this ( StyleId.auto, orientation );
    }

    /**
     * Constructs new split pane.
     *
     * @param orientation      split pane orientation
     * @param continuousLayout whether or not split pane should redraw continuously as the divider changes position
     */
    public WebSplitPane ( final int orientation, final boolean continuousLayout )
    {
        this ( StyleId.auto, orientation, continuousLayout );
    }

    /**
     * Constructs new split pane.
     *
     * @param orientation    split pane orientation
     * @param leftComponent  left split component
     * @param rightComponent right split component
     */
    public WebSplitPane ( final int orientation, final Component leftComponent, final Component rightComponent )
    {
        this ( StyleId.auto, orientation, leftComponent, rightComponent );
    }

    /**
     * Constructs new split pane.
     *
     * @param orientation      split pane orientation
     * @param continuousLayout whether or not split pane should redraw continuously as the divider changes position
     * @param leftComponent    left split component
     * @param rightComponent   right split component
     */
    public WebSplitPane ( final int orientation, final boolean continuousLayout, final Component leftComponent,
                          final Component rightComponent )
    {
        this ( StyleId.auto, orientation, continuousLayout, leftComponent, rightComponent );
    }

    /**
     * Constructs new split pane.
     *
     * @param id {@link StyleId}
     */
    public WebSplitPane ( final StyleId id )
    {
        this ( id, HORIZONTAL_SPLIT, true, null, null );
    }

    /**
     * Constructs new split pane.
     *
     * @param id          {@link StyleId}
     * @param orientation split pane orientation
     */
    public WebSplitPane ( final StyleId id, final int orientation )
    {
        this ( id, orientation, true, null, null );
    }

    /**
     * Constructs new split pane.
     *
     * @param id               {@link StyleId}
     * @param orientation      split pane orientation
     * @param continuousLayout whether or not split pane should redraw continuously as the divider changes position
     */
    public WebSplitPane ( final StyleId id, final int orientation, final boolean continuousLayout )
    {
        this ( id, orientation, continuousLayout, null, null );
    }

    /**
     * Constructs new split pane.
     *
     * @param id             {@link StyleId}
     * @param orientation    split pane orientation
     * @param leftComponent  left split component
     * @param rightComponent right split component
     */
    public WebSplitPane ( final StyleId id, final int orientation, final Component leftComponent, final Component rightComponent )
    {
        this ( id, orientation, true, leftComponent, rightComponent );
    }

    /**
     * Constructs new split pane.
     *
     * @param id               {@link StyleId}
     * @param orientation      split pane orientation
     * @param continuousLayout whether or not split pane should redraw continuously as the divider changes position
     * @param leftComponent    left split component
     * @param rightComponent   right split component
     */
    public WebSplitPane ( final StyleId id, final int orientation, final boolean continuousLayout, final Component leftComponent,
                          final Component rightComponent )
    {
        super ( orientation, continuousLayout, leftComponent, rightComponent );
        setStyleId ( id );
    }

    /**
     * Returns proportional split divider location.
     *
     * @return proportional split divider location
     */
    public double getProportionalDividerLocation ()
    {
        final int l = getOrientation () == WebSplitPane.HORIZONTAL_SPLIT ? getWidth () : getHeight ();
        return Math.max ( 0.0, Math.min ( ( double ) getDividerLocation () / ( l - getDividerSize () ), 1.0 ) );
    }

    /**
     * Adds divider listener.
     *
     * @param listener divider listener to add
     */
    public void addDividerListener ( final ComponentListener listener )
    {
        getUI ().getDivider ().addComponentListener ( listener );
    }

    /**
     * Removes divider listener.
     *
     * @param listener divider listener to remove
     */
    public void removeDividerListener ( final ComponentListener listener )
    {
        getUI ().getDivider ().removeComponentListener ( listener );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.splitpane;
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

    @NotNull
    @Override
    public MouseAdapter onMousePress ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMousePress ( @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, mouseButton, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseEnter ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseEnter ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseExit ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseExit ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseDrag ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseDrag ( @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, mouseButton, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseClick ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseClick ( @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, mouseButton, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onDoubleClick ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDoubleClick ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMenuTrigger ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMenuTrigger ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyType ( @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyType ( @Nullable final HotkeyData hotkey, @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, hotkey, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyPress ( @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyPress ( @Nullable final HotkeyData hotkey, @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, hotkey, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyRelease ( @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyRelease ( @Nullable final HotkeyData hotkey, @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, hotkey, runnable );
    }

    @NotNull
    @Override
    public FocusAdapter onFocusGain ( @NotNull final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusGain ( this, runnable );
    }

    @NotNull
    @Override
    public FocusAdapter onFocusLoss ( @NotNull final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusLoss ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onDragStart ( final int shift, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDragStart ( this, shift, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onDragStart ( final int shift, @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDragStart ( this, shift, mouseButton, runnable );
    }

    @Override
    public void addLanguageListener ( @NotNull final LanguageListener listener )
    {
        UILanguageManager.addLanguageListener ( this, listener );
    }

    @Override
    public void removeLanguageListener ( @NotNull final LanguageListener listener )
    {
        UILanguageManager.removeLanguageListener ( this, listener );
    }

    @Override
    public void removeLanguageListeners ()
    {
        UILanguageManager.removeLanguageListeners ( this );
    }

    @Override
    public void addDictionaryListener ( @NotNull final DictionaryListener listener )
    {
        UILanguageManager.addDictionaryListener ( this, listener );
    }

    @Override
    public void removeDictionaryListener ( @NotNull final DictionaryListener listener )
    {
        UILanguageManager.removeDictionaryListener ( this, listener );
    }

    @Override
    public void removeDictionaryListeners ()
    {
        UILanguageManager.removeDictionaryListeners ( this );
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
    public WebSplitPane setPlainFont ()
    {
        return FontMethodsImpl.setPlainFont ( this );
    }

    @Override
    public WebSplitPane setPlainFont ( final boolean apply )
    {
        return FontMethodsImpl.setPlainFont ( this, apply );
    }

    @Override
    public boolean isPlainFont ()
    {
        return FontMethodsImpl.isPlainFont ( this );
    }

    @Override
    public WebSplitPane setBoldFont ()
    {
        return FontMethodsImpl.setBoldFont ( this );
    }

    @Override
    public WebSplitPane setBoldFont ( final boolean apply )
    {
        return FontMethodsImpl.setBoldFont ( this, apply );
    }

    @Override
    public boolean isBoldFont ()
    {
        return FontMethodsImpl.isBoldFont ( this );
    }

    @Override
    public WebSplitPane setItalicFont ()
    {
        return FontMethodsImpl.setItalicFont ( this );
    }

    @Override
    public WebSplitPane setItalicFont ( final boolean apply )
    {
        return FontMethodsImpl.setItalicFont ( this, apply );
    }

    @Override
    public boolean isItalicFont ()
    {
        return FontMethodsImpl.isItalicFont ( this );
    }

    @Override
    public WebSplitPane setFontStyle ( final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebSplitPane setFontStyle ( final int style )
    {
        return FontMethodsImpl.setFontStyle ( this, style );
    }

    @Override
    public WebSplitPane setFontSize ( final int fontSize )
    {
        return FontMethodsImpl.setFontSize ( this, fontSize );
    }

    @Override
    public WebSplitPane changeFontSize ( final int change )
    {
        return FontMethodsImpl.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return FontMethodsImpl.getFontSize ( this );
    }

    @Override
    public WebSplitPane setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebSplitPane setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public WebSplitPane setFontName ( final String fontName )
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
    public WebSplitPane setPreferredWidth ( final int preferredWidth )
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
    public WebSplitPane setPreferredHeight ( final int preferredHeight )
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
    public WebSplitPane setPreferredSize ( final int width, final int height )
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
    public WebSplitPane setMaximumWidth ( final int maximumWidth )
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
    public WebSplitPane setMaximumHeight ( final int maximumHeight )
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
    public WebSplitPane setMaximumSize ( final int width, final int height )
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
    public WebSplitPane setMinimumWidth ( final int minimumWidth )
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
    public WebSplitPane setMinimumHeight ( final int minimumHeight )
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
    public WebSplitPane setMinimumSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setMinimumSize ( this, width, height );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WSplitPaneUI} object that renders this component
     */
    @Override
    public WSplitPaneUI getUI ()
    {
        return ( WSplitPaneUI ) super.getUI ();
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WSplitPaneUI}
     */
    public void setUI ( final WSplitPaneUI ui )
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