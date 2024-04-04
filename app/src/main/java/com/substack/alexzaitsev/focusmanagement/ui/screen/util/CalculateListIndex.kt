package com.substack.alexzaitsev.focusmanagement.ui.screen.util

import kotlin.math.max
import kotlin.math.min

fun getNextUpFocusedListIndex(currentFocusedIndex: Int): Int =
    max(currentFocusedIndex - 1, 0)

fun getNextDownFocusedListIndex(currentFocusedIndex: Int, itemsSize: Int) =
    min(currentFocusedIndex + 1, itemsSize - 1)
