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

package com.alee.managers.language.data;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Supplier;
import com.alee.utils.ArrayUtils;
import com.alee.utils.HtmlUtils;
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;

/**
 * {@link Text} represents translation for a single translation key, language and state.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see com.alee.managers.language.LanguageManager
 * @see com.alee.managers.language.Language
 * @see Value
 */
@XStreamAlias ( "text" )
@XStreamConverter ( TextConverter.class )
public final class Text implements Cloneable, Serializable
{
    /**
     * State this translation is used for.
     */
    @Nullable
    @XStreamAsAttribute
    private String state;

    /**
     * Mnemonic character.
     */
    @XStreamAsAttribute
    private int mnemonic;

    /**
     * Translation text.
     */
    @NotNull
    private String text;

    /**
     * Constructs new {@link Text} with default state and empty text.
     */
    public Text ()
    {
        this ( "", null, -1 );
    }

    /**
     * Constructs new {@link Text} with default state and specified translation text.
     *
     * @param text translation text
     */
    public Text ( @NotNull final String text )
    {
        this ( text, null, -1 );
    }

    /**
     * Constructs new {@link Text} with the specified state and translation text.
     *
     * @param text  translation text
     * @param state state this translation is used for
     */
    public Text ( @NotNull final String text, @Nullable final String state )
    {
        this ( text, state, -1 );
    }

    /**
     * Constructs new {@link Text} with the specified state and translation text.
     *
     * @param text     translation text
     * @param state    state this translation is used for
     * @param mnemonic mnemonic character
     */
    public Text ( @NotNull final String text, @Nullable final String state, final int mnemonic )
    {
        this.text = text;
        this.state = state;
        this.mnemonic = mnemonic;
    }

    /**
     * Returns parsed {@link #text} or raw contents depending on provided data.
     *
     * @param data language data to process
     * @return parsed {@link #text} or raw contents depending on provided data
     */
    @NotNull
    public String getText ( @NotNull final Object... data )
    {
        final String result;
        if ( ArrayUtils.notEmpty ( data ) )
        {
            final Object[] objects = parseData ( data );
            result = String.format ( text, objects );
        }
        else
        {
            result = text;
        }
        return result;
    }

    /**
     * Returns language data transformed into its final form.
     *
     * @param data language data to process
     * @return language data transformed into its final form
     */
    @NotNull
    private Object[] parseData ( @NotNull final Object... data )
    {
        final Object[] parsedData = new Object[ data.length ];
        for ( int i = 0; i < data.length; i++ )
        {
            final Object object = data[ i ];
            if ( object != null )
            {
                if ( object instanceof Supplier )
                {
                    parsedData[ i ] = ( ( Supplier ) object ).get ();
                }
                else
                {
                    parsedData[ i ] = object;
                }
            }
            else
            {
                parsedData[ i ] = object;
            }
        }
        return parsedData;
    }

    /**
     * Sets translation text.
     *
     * @param text translation text
     */
    public void setText ( @NotNull final String text )
    {
        this.text = text;
    }

    /**
     * Returns state this translation is used for.
     *
     * @return state this translation is used for
     */
    @Nullable
    public String getState ()
    {
        return state;
    }

    /**
     * Sets state this translation is used for.
     *
     * @param state state this translation is used for
     */
    public void setState ( @Nullable final String state )
    {
        this.state = state;
    }

    /**
     * Returns mnemonic character.
     *
     * @return mnemonic character
     */
    public int getMnemonic ()
    {
        return mnemonic;
    }

    /**
     * Sets mnemonic character.
     *
     * @param mnemonic new mnemonic character
     */
    public void setMnemonic ( final int mnemonic )
    {
        this.mnemonic = mnemonic;
    }

    @NotNull
    @Override
    public String toString ()
    {
        return TextUtils.shortenText ( HtmlUtils.getPlainText ( text ), 50, true );
    }
}