package com.substack.alexzaitsev.focusmanagement.ui.screen.ranges

import com.substack.alexzaitsev.focusmanagement.model.MountainRange

data class RangesState(
    val mountainRanges: List<MountainRange>,
    val focusedBlock: FocusableBlock,
    val focusedRangeIndex: Int,
    val focusedMountainIndex: Int,
) {
    enum class FocusableBlock {
        RANGES, MOUNTAINS
    }

    companion object {
        fun initial() = RangesState(
            mountainRanges = emptyList(),
            focusedBlock = FocusableBlock.RANGES,
            focusedRangeIndex = 0,
            focusedMountainIndex = 0
        )
    }
}
