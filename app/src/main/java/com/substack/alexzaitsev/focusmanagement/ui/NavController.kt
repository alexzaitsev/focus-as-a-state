package com.substack.alexzaitsev.focusmanagement.ui

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class NavController {
    private val _screen = MutableSharedFlow<Screen>()
    val screen: SharedFlow<Screen> = _screen
    suspend fun emit(screen: Screen) = _screen.emit(screen)
}

sealed class Screen {
    data object Ranges : Screen()
    data class Mountain(val mountain: com.substack.alexzaitsev.focusmanagement.model.Mountain) : Screen()
    data object Exit : Screen()
}
