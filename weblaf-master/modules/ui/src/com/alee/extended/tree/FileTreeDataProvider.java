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

package com.alee.extended.tree;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.task.TaskManager;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.compare.Filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Asynchronous data provider for WebFileTree.
 *
 * @author Mikle Garin
 */
public class FileTreeDataProvider extends AbstractAsyncTreeDataProvider<FileTreeNode>
{
    /**
     * Tree root files.
     */
    protected List<File> rootFiles;

    /**
     * Constructs new {@link FileTreeDataProvider} with the specified root {@link File}s.
     *
     * @param rootFiles root {@link File}s
     */
    public FileTreeDataProvider ( @NotNull final File... rootFiles )
    {
        this ( CollectionUtils.asList ( rootFiles ) );
    }

    /**
     * Constructs new {@link FileTreeDataProvider} with the specified root {@link File}s.
     *
     * @param rootFiles {@link List} of root {@link File}s
     */
    public FileTreeDataProvider ( @NotNull final List<File> rootFiles )
    {
        this.rootFiles = rootFiles;
    }

    @NotNull
    @Override
    public String getThreadGroupId ()
    {
        return TaskManager.FILE_SYSTEM;
    }

    @NotNull
    @Override
    public FileTreeNode getRoot ()
    {
        return rootFiles.size () == 1 ? new FileTreeNode ( rootFiles.get ( 0 ) ) : new FileTreeNode ( null );
    }

    @Override
    public void loadChildren ( @NotNull final FileTreeNode parent, @NotNull final NodesLoadCallback<FileTreeNode> listener )
    {
        try
        {
            listener.completed ( parent.getFile () == null ? getRootChildren () : getFileChildren ( parent ) );
        }
        catch ( final Exception cause )
        {
            listener.failed ( cause );
        }
    }

    /**
     * Returns root child nodes.
     *
     * @return root child nodes
     */
    @NotNull
    protected List<FileTreeNode> getRootChildren ()
    {
        final List<FileTreeNode> children = new ArrayList<FileTreeNode> ( rootFiles.size () );
        for ( final File rootFile : rootFiles )
        {
            children.add ( new FileTreeNode ( rootFile ) );
        }
        return children;
    }

    /**
     * Returns child nodes for specified node.
     *
     * @param node parent node
     * @return child nodes
     */
    @NotNull
    public List<FileTreeNode> getFileChildren ( @NotNull final FileTreeNode node )
    {
        final List<FileTreeNode> children;
        final File file = node.getFile ();
        final File[] childrenArray = file != null ? file.listFiles () : null;
        if ( childrenArray == null || childrenArray.length == 0 )
        {
            children = new ArrayList<FileTreeNode> ( 0 );
        }
        else
        {
            children = new ArrayList<FileTreeNode> ( childrenArray.length );
            for ( final File f : childrenArray )
            {
                children.add ( new FileTreeNode ( f ) );
            }
        }
        return children;
    }

    @Nullable
    @Override
    public Filter<FileTreeNode> getChildrenFilter ( @NotNull final FileTreeNode parent, @NotNull final List<FileTreeNode> children )
    {
        // We must not filter out given roots
        return parent.getFile () != null ? super.getChildrenFilter ( parent, children ) : null;
    }

    @Override
    public boolean isLeaf ( @NotNull final FileTreeNode node )
    {
        return node.getFile () != null && !FileUtils.isDirectory ( node.getFile () );
    }
}