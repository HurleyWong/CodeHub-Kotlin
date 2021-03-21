package ac.hurley.codehub_kotlin.compose.view

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

private val RefreshDistance = 80.dp
private val LoadDistance = 100.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToRefreshAndLoadLayout(
    stateOfRefresh: Boolean,
    stateOfLoad: Boolean,
    onRefresh: () -> Unit,
    onLoad: () -> Unit,
    content: @Composable () -> Unit
) {
    val refreshDistance = with(LocalDensity.current) {
        RefreshDistance.toPx()
    }
    val loadDistance = with(LocalDensity.current) {
        LoadDistance.toPx()
    }

    val refreshState = rememberSwipeableState(initialValue = stateOfRefresh) { newValue ->
        if (newValue && !stateOfRefresh) onRefresh()
        true
    }

    val loadState = rememberSwipeableState(initialValue = stateOfLoad) { newValue ->
        if (newValue && !stateOfLoad) onLoad()
        true
    }

    Box(
        modifier = Modifier
            .nestedScroll(refreshState.RefreshPreUpPostDownNestedScrollConnection)
            .swipeable(
                state = refreshState,
                anchors = mapOf(
                    -refreshDistance to false,
                    refreshDistance to true,
                ),
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Vertical
            )
            .nestedScroll(loadState.LoadPreUpPostDownNestedScrollConnection)
            .swipeable(
                state = loadState,
                anchors = mapOf(
                    loadDistance to false,
                    -loadDistance to true,
                ),
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Vertical
            )
            .fillMaxSize()
    ) {
        content()
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .offset { IntOffset(0, refreshState.offset.value.roundToInt()) }
        ) {
            if (refreshState.offset.value != -refreshDistance) {
                Surface(elevation = 10.dp, shape = CircleShape) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(36.dp)
                            .padding(4.dp)
                    )
                }
            }
        }

        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .offset { IntOffset(0, loadState.offset.value.roundToInt()) }
        ) {
            if (loadState.offset.value != loadDistance) {
                Surface(elevation = 10.dp, shape = CircleShape) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(36.dp)
                            .padding(4.dp)
                    )
                }
            }
        }

        LaunchedEffect(stateOfRefresh) { refreshState.animateTo(stateOfRefresh) }
        LaunchedEffect(stateOfLoad) { loadState.animateTo(stateOfLoad) }

    }
}

/**
 * 下拉刷新
 */
@ExperimentalMaterialApi
private val <T> SwipeableState<T>.RefreshPreUpPostDownNestedScrollConnection: NestedScrollConnection
    get() = object : NestedScrollConnection {
        // 滚动前
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val delta = available.toFloat()
            return if (delta < 0 && source == NestedScrollSource.Drag) {
                performDrag(delta = delta).toOffset()
            } else {
                Offset.Zero
            }
        }

        // 滚动后
        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            return if (source == NestedScrollSource.Drag) {
                performDrag(available.toFloat()).toOffset()
            } else {
                Offset.Zero
            }
        }

        override suspend fun onPreFling(available: Velocity): Velocity {
            val toFling = Offset(available.x, available.y).toFloat()
            return if (toFling < 0) {
                performFling(toFling)
                Velocity.Zero
            } else {
                Velocity.Zero
            }
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            performFling(velocity = Offset(available.x, available.y).toFloat())
            return Velocity.Zero
        }

        private fun Float.toOffset(): Offset = Offset(0f, this)

        private fun Offset.toFloat(): Float = this.y
    }

/**
 * 加载更多
 */
@ExperimentalMaterialApi
private val <T> SwipeableState<T>.LoadPreUpPostDownNestedScrollConnection: NestedScrollConnection
    get() = object : NestedScrollConnection {
        // 滚动前
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val delta = available.toFloat()
            return if (delta > 0 && source == NestedScrollSource.Drag) {
                performDrag(delta = delta).toOffset()
            } else {
                Offset.Zero
            }
        }

        // 滚动后
        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            return if (source == NestedScrollSource.Drag) {
                performDrag(available.toFloat()).toOffset()
            } else {
                Offset.Zero
            }
        }

        override suspend fun onPreFling(available: Velocity): Velocity {
            val toFling = Offset(available.x, available.y).toFloat()
            return if (toFling > 0) {
                performFling(toFling)
                Velocity.Zero
            } else {
                Velocity.Zero
            }
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            performFling(velocity = Offset(available.x, available.y).toFloat())
            return Velocity.Zero
        }

        private fun Float.toOffset(): Offset = Offset(0f, this)

        private fun Offset.toFloat(): Float = this.y
    }




















