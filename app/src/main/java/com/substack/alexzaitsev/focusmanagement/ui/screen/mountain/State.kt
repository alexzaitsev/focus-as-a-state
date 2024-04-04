package com.substack.alexzaitsev.focusmanagement.ui.screen.mountain

import com.substack.alexzaitsev.focusmanagement.model.Mountain

data class MountainState(
    val mountain: Mountain? = null
) {
    companion object {
        fun initial() = MountainState(mountain = null)
    }
}
