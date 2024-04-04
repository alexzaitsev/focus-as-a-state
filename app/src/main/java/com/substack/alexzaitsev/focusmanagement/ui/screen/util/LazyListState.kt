package com.substack.alexzaitsev.focusmanagement.ui.screen.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

val LazyListState.fullyVisibleIndexes: List<Int>
    get() {
        val visibleItemsInfo = layoutInfo.visibleItemsInfo
        return if (visibleItemsInfo.isEmpty()) {
            emptyList()
        } else {
            val fullyVisibleItemsInfo = visibleItemsInfo.toMutableList()
            val lastItem = fullyVisibleItemsInfo.last()
            val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset
            if (lastItem.offset + lastItem.size > viewportHeight) {
                fullyVisibleItemsInfo.removeLast()
            }
            val firstItemIfLeft = fullyVisibleItemsInfo.firstOrNull()
            if (firstItemIfLeft != null && firstItemIfLeft.offset < layoutInfo.viewportStartOffset) {
                fullyVisibleItemsInfo.removeFirst()
            }
            fullyVisibleItemsInfo.map { it.index }
        }
    }

@Composable
fun rememberSyncedLazyListState(
    focusedIndex: Int,
    listSize: Int
): LazyListState {
    val lazyListState = rememberLazyListState()
    val firstVisibleItemIndex by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
    val fullyVisibleIndexes by remember(key1 = focusedIndex) { derivedStateOf { lazyListState.fullyVisibleIndexes } }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = focusedIndex) {
        coroutineScope.launch {
            if (focusedIndex in 0 until listSize && fullyVisibleIndexes.isNotEmpty()) {
                val minDesiredVisibleIndex = max(focusedIndex - 1, 0)
                val maxDesiredVisibleIndex = min(focusedIndex + 1, listSize - 1)
                if (minDesiredVisibleIndex !in fullyVisibleIndexes && minDesiredVisibleIndex < fullyVisibleIndexes.first()) {
                    lazyListState.animateScrollToItem(minDesiredVisibleIndex)
                } else if (maxDesiredVisibleIndex !in fullyVisibleIndexes && maxDesiredVisibleIndex > fullyVisibleIndexes.last()) {
                    val diff = maxDesiredVisibleIndex - fullyVisibleIndexes.last()
                    lazyListState.animateScrollToItem(firstVisibleItemIndex + diff)
                }
            }
        }
    }

    return lazyListState
}
