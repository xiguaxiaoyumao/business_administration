package com.alee.laf.slider;

import com.alee.api.annotations.NotNull;
import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link SliderPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WebSliderUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveSliderPainter<C extends JSlider, U extends WebSliderUI> extends AdaptivePainter<C, U>
        implements ISliderPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveSliderPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveSliderPainter ( @NotNull final Painter painter )
    {
        super ( painter );
    }

    @Override
    public void prepareToPaint ( @NotNull final SliderPaintParameters parameters )
    {
        /**
         * Nothing needs to be done for adaptive class.
         */
    }

    @Override
    public void cleanupAfterPaint ()
    {
        /**
         * Nothing needs to be done for adaptive class.
         */
    }
}