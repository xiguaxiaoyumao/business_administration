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

package com.alee.demo.api.example.wiki;

import com.alee.demo.skin.DemoIcons;
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.link.UrlLinkAction;
import com.alee.extended.link.WebLink;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.StyleId;

import javax.swing.*;

/**
 * WebLaF wiki page.
 *
 * @author Mikle Garin
 */
public class WebLafWikiPage implements WikiPage
{
    /**
     * Wiki article title.
     */
    private final String title;

    /**
     * Constructs new wiki page.
     *
     * @param title wiki article title
     */
    public WebLafWikiPage ( final String title )
    {
        super ();
        this.title = title;
    }

    @Override
    public String getTitle ()
    {
        return title;
    }

    @Override
    public String getAddress ()
    {
        return "https://github.com/mgarin/weblaf/wiki/" + getTitle ().replaceAll ( " ", "-" );
    }

    @Override
    public JComponent createLink ()
    {
        final WebPanel link = new WebPanel ( StyleId.panelTransparent, new HorizontalFlowLayout ( 4, false ) );
        link.add ( new WebLabel ( DemoStyles.wikiLabel, "demo.content.example.wiki.weblaf", DemoIcons.github16 ) );
        link.add ( new WebLink ( DemoStyles.wikiLink, getTitle (), new UrlLinkAction ( getAddress () ) ) );
        return link;
    }
}