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

package com.alee.extended.label;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.painter.decoration.DecorationException;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.background.AbstractBackground;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;

/**
 * Custom background implementation for {@link WebHotkeyLabel}.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> background type
 * @author Mikle Garin
 * @see WebHotkeyLabel
 */
@XStreamAlias ( "HotkeyLabelBackground" )
public class HotkeyLabelBackground<C extends WebHotkeyLabel, D extends IDecoration<C, D>, I extends HotkeyLabelBackground<C, D, I>>
        extends AbstractBackground<C, D, I>
{
    /**
     * todo 1. Replace with proper border/background-based implementations later when different borders are supported
     */

    /**
     * Shape round.
     */
    @Nullable
    @XStreamAsAttribute
    protected Integer round;

    /**
     * Border {@link Color}.
     */
    @Nullable
    @XStreamAsAttribute
    protected Color border;

    /**
     * Spacing {@link Color}.
     */
    @Nullable
    @XStreamAsAttribute
    protected Color spacing;

    /**
     * Background {@link Color}.
     */
    @Nullable
    @XStreamAsAttribute
    protected Color color;

    @NotNull
    @Override
    public String getId ()
    {
        return id != null ? id : "hotkey-background";
    }

    /**
     * Returns shape round.
     *
     * @param c {@link WebHotkeyLabel} that is being painted
     * @param d {@link IDecoration} state
     * @return shape round
     */
    protected int getRound (  @NotNull final C c, @NotNull final D d  )
    {
        if ( round == null )
        {
            throw new DecorationException ( "Shape round must be specified" );
        }
        return round;
    }

    /**
     * Returns border {@link Color}.
     *
     * @param c {@link WebHotkeyLabel} that is being painted
     * @param d {@link IDecoration} state
     * @return border {@link Color}
     */
    @NotNull
    protected Color getBorderColor ( @NotNull final C c, @NotNull final D d )
    {
        if ( border == null )
        {
            throw new DecorationException ( "Border color must be specified" );
        }
        return border;
    }

    /**
     * Returns spacing {@link Color}.
     *
     * @param c {@link WebHotkeyLabel} that is being painted
     * @param d {@link IDecoration} state
     * @return spacing {@link Color}
     */
    @NotNull
    protected Color getSpacingColor ( @NotNull final C c, @NotNull final D d )
    {
        if ( spacing == null )
        {
            throw new DecorationException ( "Spacing color must be specified" );
        }
        return spacing;
    }

    /**
     * Returns background {@link Color}.
     *
     * @param c {@link WebHotkeyLabel} that is being painted
     * @param d {@link IDecoration} state
     * @return background {@link Color}
     */
    @NotNull
    protected Color getBackgroundColor ( @NotNull final C c, @NotNull final D d )
    {
        if ( color == null )
        {
            throw new DecorationException ( "Background color must be specified" );
        }
        return color;
    }

    @Override
    public void paint ( @NotNull final Graphics2D g2d, @NotNull final Rectangle bounds, @NotNull final C c, @NotNull final D d,
                        @NotNull final Shape shape )
    {
        final int round = getRound (c,d);

        // White spacer
        g2d.setPaint ( getSpacingColor ( c, d ) );
        g2d.fillRoundRect ( bounds.x, bounds.y, bounds.width, bounds.height - 1, round, round );

        // Background
        g2d.setPaint ( getBackgroundColor ( c, d ) );
        g2d.fillRect ( bounds.x + 3, bounds.y + 3, bounds.width - 6, bounds.height - 7 );

        // Border
        g2d.setPaint ( getBorderColor ( c, d ) );
        g2d.drawRoundRect ( bounds.x, bounds.y, bounds.width - 1, bounds.height - 2, round, round );
        g2d.drawRoundRect ( bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, round, round );
    }
}