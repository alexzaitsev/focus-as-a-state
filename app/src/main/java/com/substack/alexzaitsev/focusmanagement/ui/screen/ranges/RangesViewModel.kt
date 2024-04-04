package com.substack.alexzaitsev.focusmanagement.ui.screen.ranges

import androidx.compose.ui.input.key.Key
import androidx.lifecycle.viewModelScope
import com.substack.alexzaitsev.focusmanagement.model.MountainRange
import com.substack.alexzaitsev.focusmanagement.ui.NavController
import com.substack.alexzaitsev.focusmanagement.ui.Screen
import com.substack.alexzaitsev.focusmanagement.ui.screen.BaseViewModel
import com.substack.alexzaitsev.focusmanagement.ui.screen.ranges.RangesState.FocusableBlock
import com.substack.alexzaitsev.focusmanagement.ui.screen.util.FOCUS_IS_LEAVING
import com.substack.alexzaitsev.focusmanagement.ui.screen.util.getNextDownFocusedGridIndex
import com.substack.alexzaitsev.focusmanagement.ui.screen.util.getNextDownFocusedListIndex
import com.substack.alexzaitsev.focusmanagement.ui.screen.util.getNextLeftFocusedGridIndex
import com.substack.alexzaitsev.focusmanagement.ui.screen.util.getNextRightFocusedGridIndex
import com.substack.alexzaitsev.focusmanagement.ui.screen.util.getNextUpFocusedGridIndex
import com.substack.alexzaitsev.focusmanagement.ui.screen.util.getNextUpFocusedListIndex
import kotlinx.coroutines.launch

class RangesViewModel(
    private val navController: NavController,
    mountainRanges: List<MountainRange>
) : BaseViewModel<RangesState>(RangesState.initial()) {

    init {
        viewModelScope.launch {
            state = state.copy(
                mountainRanges = mountainRanges
            )
        }
    }

    override fun onKeyDown(key: Key) {
        viewModelScope.launch {
            when (key) {
                Key.Back -> navController.emit(Screen.Exit)

                Key.DirectionUp -> onUpPressed()

                Key.DirectionDown -> onDownPressed()

                Key.DirectionLeft -> onLeftPressed()

                Key.DirectionRight -> onRightPressed()

                Key.DirectionCenter, Key.Enter -> onEnterPressed()
            }
        }
    }

    private fun onUpPressed() = when (state.focusedBlock) {
        FocusableBlock.RANGES -> {
            state = state.copy(
                focusedRangeIndex = getNextUpFocusedListIndex(state.focusedRangeIndex),
                focusedMountainIndex = 0,
            )
        }

        FocusableBlock.MOUNTAINS -> {
            state = state.copy(
                focusedMountainIndex = getNextUpFocusedGridIndex(
                    currentFocusedIndex = state.focusedMountainIndex,
                    itemsPerRow = CONTENT_ITEMS_PER_ROW
                )
            )
        }
    }

    private fun onDownPressed() = when (state.focusedBlock) {
        FocusableBlock.RANGES -> {
            state = state.copy(
                focusedRangeIndex = getNextDownFocusedListIndex(
                    currentFocusedIndex = state.focusedRangeIndex,
                    itemsSize = state.mountainRanges.size
                ),
                focusedMountainIndex = 0,
            )
        }

        FocusableBlock.MOUNTAINS -> {
            val mountains = state.mountainRanges[state.focusedRangeIndex].mountains
            state = state.copy(
                focusedMountainIndex = getNextDownFocusedGridIndex(
                    currentFocusedIndex = state.focusedMountainIndex,
                    itemsPerRow = CONTENT_ITEMS_PER_ROW,
                    itemsSize = mountains.size
                )
            )
        }
    }

    private fun onRightPressed() = when (state.focusedBlock) {
        FocusableBlock.RANGES -> {
            state = state.copy(focusedBlock = FocusableBlock.MOUNTAINS)
        }

        FocusableBlock.MOUNTAINS -> {
            val mountains = state.mountainRanges[state.focusedRangeIndex].mountains
            state = state.copy(
                focusedMountainIndex = getNextRightFocusedGridIndex(
                    currentFocusedIndex = state.focusedMountainIndex,
                    itemsPerRow = CONTENT_ITEMS_PER_ROW,
                    itemsSize = mountains.size
                )
            )
        }
    }

    private fun onLeftPressed() = when (state.focusedBlock) {
        FocusableBlock.RANGES -> {} // do nothing

        FocusableBlock.MOUNTAINS -> {
            val newIndex = getNextLeftFocusedGridIndex(
                currentFocusedIndex = state.focusedMountainIndex,
                itemsPerRow = CONTENT_ITEMS_PER_ROW
            )
            if (newIndex == FOCUS_IS_LEAVING) {
                // move focus to ranges
                state = state.copy(focusedBlock = FocusableBlock.RANGES)
            } else {
                state = state.copy(focusedMountainIndex = newIndex)
            }
        }
    }

    private suspend fun onEnterPressed() = when (state.focusedBlock) {
        FocusableBlock.RANGES -> {}  // ignore

        FocusableBlock.MOUNTAINS -> {
            val focusedRange = state.mountainRanges[state.focusedRangeIndex]
            val focusedMountain = focusedRange.mountains[state.focusedMountainIndex]
            navController.emit(Screen.Mountain(mountain = focusedMountain))
        }
    }

    companion object {
        const val CONTENT_ITEMS_PER_ROW = 3
    }
}
