package com.substack.alexzaitsev.focusmanagement.ui.screen.util

const val FOCUS_IS_LEAVING = -1

fun getNextUpFocusedGridIndex(currentFocusedIndex: Int, itemsPerRow: Int): Int {
    val newFocusedIndex = currentFocusedIndex - itemsPerRow
    return if (newFocusedIndex >= 0) newFocusedIndex else currentFocusedIndex
}

fun getNextDownFocusedGridIndex(currentFocusedIndex: Int, itemsPerRow: Int, itemsSize: Int): Int {
    val newFocusedIndex = currentFocusedIndex + itemsPerRow
    return if (newFocusedIndex < itemsSize) newFocusedIndex else currentFocusedIndex
}

fun getNextRightFocusedGridIndex(currentFocusedIndex: Int, itemsPerRow: Int, itemsSize: Int): Int {
    val currentFocusedRowReminder = currentFocusedIndex % itemsPerRow
    return if (currentFocusedRowReminder < itemsPerRow - 1) {
        val newFocusedIndex = currentFocusedIndex + 1
        if (newFocusedIndex < itemsSize) newFocusedIndex else currentFocusedIndex
    } else {
        currentFocusedIndex
    }
}

fun getNextLeftFocusedGridIndex(currentFocusedIndex: Int, itemsPerRow: Int): Int {
    val currentFocusedRowReminder = currentFocusedIndex % itemsPerRow
    return if (currentFocusedRowReminder > 0) currentFocusedIndex - 1 else FOCUS_IS_LEAVING
}
