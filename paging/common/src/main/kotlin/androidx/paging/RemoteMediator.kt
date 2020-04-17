/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.paging

import androidx.paging.RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH

/**
 * Defines a set of callbacks, which may be optionally registered when constructing a stream of
 * [PagingData], that allow for control of the following events:
 *  * Stream initialization
 *  * [LoadType.REFRESH] signal driven from UI
 *  * [PagingSource] returns a [PagingSource.LoadResult] which signals start / end boundary.
 *
 * These events together can be used to implement layered pagination - network + local storage.
 *
 * @sample androidx.paging.samples.remoteMediatorSample
 */
abstract class RemoteMediator<Key : Any, Value : Any> {
    /**
     * Implement this method to load additional remote data, which will then be stored for the
     * [PagingSource] to access. These loads take one of two forms:
     *  * type == [LoadType.START] / [LoadType.END]
     *  The [PagingSource] has loaded a 'boundary' page, with a `null` adjacent key. This means
     *  this method should load additional remote data to append / prepend as appropriate, and store
     *  it locally.
     *  * type == [LoadType.REFRESH]
     *  The app (or [initialize]) has requested a remote refresh of data. This means the method
     *  should generally load remote data, and **replace** all local data.
     *
     * The runtime of this method defines loading state behavior in boundary conditions, which
     * affects e.g., [LoadState] callbacks registered to [androidx.paging.PagingDataAdapter].
     *
     * NOTE: A [PagingSource.load] request which is fulfilled by a page that hits a boundary
     * condition in either direction will trigger this callback with [LoadType.START] or
     * [LoadType.END] or both. [LoadType.REFRESH] occurs as a result of [initialize].
     *
     * @param loadType [LoadType] of the boundary condition which triggered this callback.
     *  * [LoadType.START] indicates a boundary condition at the front of the list.
     *  * [LoadType.END] indicates a boundary condition at the end of the list.
     *  * [LoadType.REFRESH] indicates this callback was triggered as the result of a requested
     *  refresh - either driven by the UI, or by [initialize].
     * @param state A copy of the state including the list of pages currently held in
     * memory of the currently presented [PagingData] at the time of starting the load. E.g. for
     * load(loadType = END), you can use the page or item at the end as input for what to load from
     * the network.
     *
     * @return [MediatorResult] signifying what [LoadState] to be passed to the UI, and whether
     * there's more data available.
     */
    abstract suspend fun load(loadType: LoadType, state: PagingState<Key, Value>): MediatorResult

    /**
     * Callback fired during initialization of a [PagingData] stream, before initial load.
     *
     * This function runs to completion before any loading is performed.
     *
     * @return [InitializeAction] indicating the action to take after initialization:
     *  * [LAUNCH_INITIAL_REFRESH] to immediately dispatch a [load] asynchronously with load type
     *  [LoadType.REFRESH], to update paginated content when the stream is initialized.
     *  * [SKIP_INITIAL_REFRESH][InitializeAction.SKIP_INITIAL_REFRESH] to wait for a
     *  refresh request from the UI before dispatching a [load] with load type [LoadType.REFRESH].
     */
    open suspend fun initialize(): InitializeAction = LAUNCH_INITIAL_REFRESH

    /**
     * Return type of [load], which determines [LoadState].
     */
    sealed class MediatorResult {
        /**
         * Recoverable error that can be retried, sets the [LoadState] to [LoadState.Error].
         */
        class Error(val throwable: Throwable) : MediatorResult()

        /**
         * Success signaling that [LoadState] should be set to [LoadState.NotLoading] if
         * [endOfPaginationReached] is `true`, otherwise [LoadState] is kept at [LoadState.Loading]
         * to await invalidation.
         *
         * NOTE: It is the responsibility of [load] to update the backing dataset and trigger
         * [PagingSource.invalidate] to allow [androidx.paging.PagingDataAdapter] to pick up new
         * items found by [load].
         */
        class Success(
            @get:JvmName("endOfPaginationReached") val endOfPaginationReached: Boolean
        ) : MediatorResult()
    }

    /**
     * Return type of [initialize], which signals the action to take after [initialize] completes.
     */
    enum class InitializeAction {
        /**
         * Immediately dispatch a [load] asynchronously with load type [LoadType.REFRESH], to update
         * paginated content when the stream is initialized.
         */
        LAUNCH_INITIAL_REFRESH,

        /**
         * Wait for a refresh request from the UI before dispatching [load] with load type
         * [LoadType.REFRESH].
         */
        SKIP_INITIAL_REFRESH
    }
}