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

package com.alee.laf.text;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.extended.behavior.DocumentChangeBehavior;
import com.alee.laf.WebLookAndFeel;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.*;
import com.alee.utils.xml.FontConverter;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.plaf.TextUI;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Abstract painter for {@link JTextComponent} implementations.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */
public abstract class AbstractTextEditorPainter<C extends JTextComponent, U extends TextUI, D extends IDecoration<C, D>>
        extends AbstractDecorationPainter<C, U, D> implements IAbstractTextEditorPainter<C, U>, SwingConstants
{
    /**
     * Input prompt text horizontal position.
     */
    protected int inputPromptHorizontalPosition = SwingConstants.CENTER;

    /**
     * Input prompt text vertical position.
     * Important mostly for text area components.
     */
    protected int inputPromptVerticalPosition = SwingConstants.CENTER;

    /**
     * Input prompt text font.
     */
    @XStreamConverter ( FontConverter.class )
    protected Font inputPromptFont = null;

    /**
     * Input prompt tex foreground.
     */
    protected Color inputPromptForeground = new Color ( 160, 160, 160 );

    /**
     * Whether or not should display input prompt only when component is editable.
     */
    protected boolean inputPromptOnlyWhenEditable = true;

    /**
     * Whether or not should hide input prompt on focus gain.
     */
    protected boolean hideInputPromptOnFocus = true;

    /**
     * Listeners.
     */
    protected transient DocumentChangeBehavior<C> documentChangeBehavior;

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();
        installDocumentChangeListener ();
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallDocumentChangeListener ();
        super.uninstallPropertiesAndListeners ();
    }

    @Override
    protected void propertyChanged ( @NotNull final String property, @Nullable final Object oldValue, @Nullable final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Updating decoration states on editable property change
        if ( Objects.equals ( property, WebLookAndFeel.EDITABLE_PROPERTY ) )
        {
            updateDecorationState ();
        }
    }

    @NotNull
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        if ( component.isEditable () )
        {
            states.add ( DecorationState.editable );
        }
        if ( SwingUtils.isEmpty ( component ) )
        {
            states.add ( DecorationState.empty );
        }
        return states;
    }

    /**
     * Installs {@link DocumentChangeBehavior} required to update emptiness state.
     */
    protected void installDocumentChangeListener ()
    {
        documentChangeBehavior = new DocumentChangeBehavior<C> ( component )
        {
            @Override
            public void documentChanged ( @NotNull final C component, @Nullable final DocumentEvent event )
            {
                updateDecorationState ();
            }
        }.install ();
    }

    /**
     * Uninstalls {@link DocumentChangeBehavior}.
     */
    protected void uninstallDocumentChangeListener ()
    {
        documentChangeBehavior.uninstall ();
        documentChangeBehavior = null;
    }

    @Override
    protected void paintContent ( @NotNull final Graphics2D g2d, @NotNull final C c, @NotNull final U ui, @NotNull final Rectangle bounds )
    {
        // Paints text highligher
        final Highlighter highlighter = c.getHighlighter ();
        if ( highlighter != null )
        {
            highlighter.paint ( g2d );
        }

        final Map hints = SwingUtils.setupTextAntialias ( g2d );

        // Paints input prompt
        paintInputPrompt ( g2d, c, ui );

        // Paints editor view
        final Rectangle alloc = getEditorRect ( c, ui );
        if ( alloc != null )
        {
            ui.getRootView ( c ).paint ( g2d, alloc );
        }

        SwingUtils.restoreTextAntialias ( g2d, hints );

        // Paints caret
        final Caret caret = c.getCaret ();
        if ( caret != null )
        {
            caret.paint ( g2d );
        }

        // Paints drop caret
        final DefaultCaret dropCaret = ReflectUtils.getFieldValueSafely ( ui, "dropCaret" );
        if ( dropCaret != null )
        {
            dropCaret.paint ( g2d );
        }
    }

    /**
     * Draws input prompt text if it is available and should be visible at the moment.
     *
     * @param g2d graphics context
     * @param c   painted component
     * @param ui  painted component UI
     */
    protected void paintInputPrompt ( @NotNull final Graphics2D g2d, final C c, final U ui )
    {
        if ( isInputPromptVisible () )
        {
            final Rectangle b = getEditorRect ( c, ui );
            if ( b != null )
            {
                final Shape oc = GraphicsUtils.intersectClip ( g2d, b );

                g2d.setFont ( inputPromptFont != null ? inputPromptFont : component.getFont () );
                g2d.setPaint ( inputPromptForeground != null ? inputPromptForeground : component.getForeground () );

                final String text = getInputPrompt ();
                final FontMetrics fm = g2d.getFontMetrics ();
                final int x;
                if ( inputPromptHorizontalPosition == CENTER )
                {
                    x = b.x + b.width / 2 - fm.stringWidth ( text ) / 2;
                }
                else if ( ltr && inputPromptHorizontalPosition == LEADING || !ltr && inputPromptHorizontalPosition == TRAILING ||
                        inputPromptHorizontalPosition == LEFT )
                {
                    x = b.x;
                }
                else
                {
                    x = b.x + b.width - fm.stringWidth ( text );
                }
                final int y;
                if ( inputPromptVerticalPosition == CENTER )
                {
                    y = b.y + b.height / 2 + LafUtils.getTextCenterShiftY ( fm );
                }
                else
                {
                    y = this.ui.getBaseline ( component, component.getWidth (), component.getHeight () );
                }
                g2d.drawString ( text, x, y );

                GraphicsUtils.restoreClip ( g2d, oc );
            }
        }
    }

    /**
     * Returns the bounding box for the root view.
     * The component must have a non-zero positive size for this translation to be computed.
     *
     * @param c  painted component
     * @param ui painted component UI
     * @return the bounding box for the root view
     */
    @Nullable
    protected Rectangle getEditorRect ( final C c, final U ui )
    {
        final Rectangle editorBounds;
        final Dimension size = c.getSize ();
        if ( size.width > 0 && size.height > 0 )
        {
            final Insets insets = component.getInsets ();
            final Rectangle innerBounds = new Rectangle ( 0, 0, size.width, size.height );
            editorBounds = SwingUtils.shrink ( innerBounds, insets );
            if ( SystemUtils.isJava9orAbove () )
            {
                Integer caretMargin = ReflectUtils.getFieldValueSafely ( ui, "caretMargin" );
                if ( caretMargin == null )
                {
                    // See BasicTextUI#DEFAULT_CARET_MARGIN
                    caretMargin = 1;
                }
                editorBounds.width -= caretMargin;
            }
        }
        else
        {
            editorBounds = null;
        }
        return editorBounds;
    }

    @Override
    public boolean isInputPromptVisible ()
    {
        final String inputPrompt = getInputPrompt ();
        return TextUtils.notEmpty ( inputPrompt ) && SwingUtils.isEmpty ( component ) &&
                ( !inputPromptOnlyWhenEditable || component.isEditable () && component.isEnabled () ) &&
                ( !hideInputPromptOnFocus || !isFocused () );
    }
}