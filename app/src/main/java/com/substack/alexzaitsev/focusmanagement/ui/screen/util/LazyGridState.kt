package com.substack.alexzaitsev.focusmanagement.ui.screen.util

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

val LazyGridState.fullyVisibleIndexes: List<Int>
    get() {
        val visibleItemsInfo = layoutInfo.visibleItemsInfo
        return if (visibleItemsInfo.isEmpty()) {
            emptyList()
        } else {
            val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset
            val fullyVisibleItemsInfo = visibleItemsInfo.filter { item ->
                item.offset.y >= layoutInfo.viewportStartOffset && item.offset.y + item.size.height <= viewportHeight
            }
            fullyVisibleItemsInfo.map { it.index }
        }
    }

@Composable
fun rememberSyncedLazyVerticalGridState(
    key: Any,
    rowSize: Int,
    focusedIndex: Int,
    listSize: Int
): LazyGridState {
    val lazyGridState = rememberLazyGridState()
    val firstVisibleItemIndex by remember { derivedStateOf { lazyGridState.firstVisibleItemIndex } }
    val fullyVisibleIndexes by remember(key1 = focusedIndex) { derivedStateOf { lazyGridState.fullyVisibleIndexes } }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = key, key2 = focusedIndex) {
        coroutineScope.launch {
            if (focusedIndex in 0 until listSize && fullyVisibleIndexes.isNotEmpty()) {
                if (focusedIndex !in fullyVisibleIndexes) {
                    if (focusedIndex < fullyVisibleIndexes.first()) {
                        lazyGridState.animateScrollToItem(index = focusedIndex)
                    } else if (focusedIndex > fullyVisibleIndexes.last()) {
                        val diff = focusedIndex - fullyVisibleIndexes.last()
                        lazyGridState.animateScrollToItem(index = firstVisibleItemIndex + diff + rowSize)
                    }
                }
            }
        }
    }

    return lazyGridState
}