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

package com.alee.demo.frames.style;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.data.CompassDirection;
import com.alee.demo.DemoApplication;
import com.alee.demo.api.example.ExampleData;
import com.alee.demo.skin.DemoIcons;
import com.alee.extended.dock.DockableFrameState;
import com.alee.extended.dock.WebDockableFrame;
import com.alee.extended.syntax.SyntaxPreset;
import com.alee.extended.syntax.WebSyntaxArea;
import com.alee.extended.tab.DocumentAdapter;
import com.alee.extended.tab.PaneData;
import com.alee.managers.style.Skin;
import com.alee.managers.style.SkinListener;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;

import java.awt.*;

/**
 * Focused example style frame.
 *
 * @author Mikle Garin
 */
public final class StyleFrame extends WebDockableFrame
{
    /**
     * Frame ID.
     */
    public static final String ID = "demo.style";

    /**
     * Constructs examples frame.
     *
     * @param application demo application
     */
    public StyleFrame ( final DemoApplication application )
    {
        super ( StyleId.dockableframeCompact, ID, DemoIcons.style16, "demo.style.title" );
        setState ( DockableFrameState.minimized );
        setPosition ( CompassDirection.east );
        setPreferredSize ( 300, 200 );

        // Source code area
        final String emptyText = "Select an example to see its style...";
        final WebSyntaxArea styleViewer = new WebSyntaxArea ( emptyText, SyntaxPreset.xml );
        styleViewer.applyPresets ( SyntaxPreset.viewable, SyntaxPreset.base, SyntaxPreset.historyLimit );
        styleViewer.applyPresets ( SyntaxPreset.margin, SyntaxPreset.size, SyntaxPreset.nonOpaque );
        add ( styleViewer.createScroll ( StyleId.syntaxareaScrollTransparentHovering ), BorderLayout.CENTER );


        // Selected example change listener
        application.getExamplesPane ().addDocumentListener ( new DocumentAdapter<ExampleData> ()
        {
            @Override
            public void selected ( final ExampleData document, final PaneData<ExampleData> pane, final int index )
            {
                // Retrieving source code from the example
                styleViewer.setText ( document.getExample ().getStyleCode ( StyleManager.getSkin () ) );
                styleViewer.setCaretPosition ( 0 );
            }

            @Override
            public void closed ( final ExampleData document, final PaneData<ExampleData> pane, final int index )
            {
                // Resetting source code
                if ( application.getExamplesPane ().getDocumentsCount () == 0 )
                {
                    styleViewer.setText ( emptyText );
                    styleViewer.setCaretPosition ( 0 );
                }
            }
        } );

        // Global skin change listener
        StyleManager.addSkinListener ( new SkinListener ()
        {
            @Override
            public void skinChanged ( @Nullable final Skin previous, @NotNull final Skin current )
            {
                // Obtaining active document
                final ExampleData document = application.getExamplesPane ().getSelectedDocument ();
                if ( document != null )
                {
                    // Retrieving source code from the example
                    styleViewer.setText ( document.getExample ().getStyleCode ( current ) );
                    styleViewer.setCaretPosition ( 0 );
                }
            }
        } );
    }
}