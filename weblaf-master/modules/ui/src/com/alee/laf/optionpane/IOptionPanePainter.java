package com.alee.laf.optionpane;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JOptionPane} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface IOptionPanePainter<C extends JOptionPane, U extends WebOptionPaneUI> extends SpecificPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}