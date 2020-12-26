package com.alee.laf.text;

import com.alee.api.annotations.Nullable;

import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * Base interface for text field component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface IAbstractTextFieldPainter<C extends JTextComponent, U extends BasicTextUI> extends IAbstractTextEditorPainter<C, U>
{
    /**
     * Returns field leading component.
     *
     * @return field leading component
     */
    @Nullable
    public Component getLeadingComponent ();

    /**
     * Returns field trailing component.
     *
     * @return field trailing component
     */
    @Nullable
    public Component getTrailingComponent ();
}