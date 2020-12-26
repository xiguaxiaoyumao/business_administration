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
import com.alee.utils.compare.Filter;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * This interface provides methods for asynchronous tree nodes retrieval within {@link AsyncTreeModel}.
 * It also extends {@link Serializable} as it is used within {@link AsyncTreeModel} which must also be {@link Serializable}.
 *
 * @param <N> node type
 * @author Mikle Garin
 * @see WebAsyncTree
 * @see AsyncTreeModel
 * @see AsyncUniqueNode
 */
public interface AsyncTreeDataProvider<N extends AsyncUniqueNode> extends Serializable
{
    /**
     * Returns identifier of a {@link ThreadGroup} registered within {@link TaskManager}.
     * It will be used by {@link AsyncTreeModel} to perform asynchronous nodes loading.
     *
     * @return identifier of a {@link ThreadGroup} registered within {@link TaskManager}
     */
    @NotNull
    public String getThreadGroupId ();

    /**
     * Returns root {@link AsyncUniqueNode}.
     * This operation is always performed on EDT and should not take excessive amounts of time.
     *
     * @return root {@link AsyncUniqueNode}
     * @see <a href="https://github.com/mgarin/weblaf/wiki/Event-Dispatch-Thread">Event Dispatch Thread</a>
     */
    @NotNull
    public N getRoot ();

    /**
     * Starts loading child {@link AsyncUniqueNode}s for the specified parent {@link AsyncUniqueNode}.
     * When children loading is finished or failed you must inform the {@link NodesLoadCallback} about result.
     * This operation uses a separate {@link Thread} and it is allowed to take as much time as it needs to complete.
     *
     * @param parent   {@link AsyncUniqueNode} to load children for
     * @param listener {@link NodesLoadCallback} for informing tree about operation result
     */
    public void loadChildren ( @NotNull N parent, @NotNull NodesLoadCallback<N> listener );

    /**
     * Returns whether or not specified {@link AsyncUniqueNode} doesn't have any children.
     * If you are not sure if the node is leaf or not - simply return false, that will allow the tree to expand this node.
     * This method is created to avoid meaningless children requests for nodes which you are sure will never have children.
     * This operation is always performed on EDT and should not take excessive amounts of time.
     *
     * @param node {@link AsyncUniqueNode} to check
     * @return {@code true} if the specified {@link AsyncUniqueNode} doesn't have any children, {@code false} otherwise
     * @see <a href="https://github.com/mgarin/weblaf/wiki/Event-Dispatch-Thread">Event Dispatch Thread</a>
     */
    public boolean isLeaf ( @NotNull N node );

    /**
     * Returns {@link Filter} that will be used for the specified {@link AsyncUniqueNode} children.
     * Specific {@link List} of child {@link AsyncUniqueNode}s is given for every separate filter operation.
     * No filtering applied to children in case {@code null} is returned.
     * This operation is always performed on EDT and should not take excessive amounts of time.
     *
     * @param parent   {@link AsyncUniqueNode} which children will be filtered using returned {@link Filter}
     * @param children {@link AsyncUniqueNode}s to be filtered
     * @return {@link Filter} that will be used for the specified {@link AsyncUniqueNode} children
     * @see <a href="https://github.com/mgarin/weblaf/wiki/Event-Dispatch-Thread">Event Dispatch Thread</a>
     */
    @Nullable
    public Filter<N> getChildrenFilter ( @NotNull N parent, @NotNull List<N> children );

    /**
     * Returns {@link Comparator} that will be used for the specified {@link AsyncUniqueNode} children.
     * Specific {@link List} of child {@link AsyncUniqueNode}s is given for every separate comparison operation.
     * No sorting applied to children in case {@code null} is returned.
     * This operation is always performed on EDT and should not take excessive amounts of time.
     *
     * @param parent   {@link AsyncUniqueNode} which children will be sorted using returned {@link Comparator}
     * @param children {@link AsyncUniqueNode}s to be sorted
     * @return {@link Comparator} that will be used for the specified {@link AsyncUniqueNode} children
     * @see <a href="https://github.com/mgarin/weblaf/wiki/Event-Dispatch-Thread">Event Dispatch Thread</a>
     */
    @Nullable
    public Comparator<N> getChildrenComparator ( @NotNull N parent, @NotNull List<N> children );
}