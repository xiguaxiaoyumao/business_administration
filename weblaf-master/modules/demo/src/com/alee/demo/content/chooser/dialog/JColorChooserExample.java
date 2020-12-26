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

package com.alee.demo.content.chooser.dialog;

import com.alee.api.annotations.NotNull;
import com.alee.demo.DemoApplication;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.laf.button.WebButton;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JColorChooserExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "jcolorchooser";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "colorchooser";
    }

    @NotNull
    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @NotNull
    @Override
    public WikiPage getWikiPage ()
    {
        return new OracleWikiPage ( "How to Use Color Choosers", "colorchooser" );
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new ColorChooserDialog ( StyleId.textfield )
        );
    }

    /**
     * Color chooser dialog preview.
     */
    protected class ColorChooserDialog extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview {@link StyleId}
         */
        public ColorChooserDialog ( final StyleId styleId )
        {
            super ( JColorChooserExample.this, "basic", FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            return CollectionUtils.asList ( new WebButton ( "Show color chooser", new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    JColorChooser.showDialog ( DemoApplication.getInstance (), null, Color.WHITE );
                }
            } ) );
        }
    }
}