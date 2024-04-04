package com.substack.alexzaitsev.focusmanagement.ui.screen.mountain

import androidx.compose.ui.input.key.Key
import androidx.lifecycle.viewModelScope
import com.substack.alexzaitsev.focusmanagement.model.Mountain
import com.substack.alexzaitsev.focusmanagement.ui.NavController
import com.substack.alexzaitsev.focusmanagement.ui.Screen
import com.substack.alexzaitsev.focusmanagement.ui.screen.BaseViewModel
import kotlinx.coroutines.launch

class MountainViewModel(
    private val navController: NavController
) : BaseViewModel<MountainState>(MountainState.initial()) {

    fun setData(mountain: Mountain) {
        state = state.copy(
            mountain = mountain
        )
    }

    override fun onKeyDown(key: Key) {
        viewModelScope.launch {
            when (key) {
                Key.Back -> {
                    navController.emit(Screen.Ranges)
                    resetState()
                }
            }
        }
    }

    private fun resetState() {
        state = state.copy(
            mountain = null
        )
    }
}
