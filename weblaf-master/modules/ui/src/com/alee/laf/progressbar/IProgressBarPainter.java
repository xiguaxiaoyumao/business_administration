package com.alee.laf.progressbar;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JProgressBar} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface IProgressBarPainter<C extends JProgressBar, U extends WProgressBarUI> extends SpecificPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}