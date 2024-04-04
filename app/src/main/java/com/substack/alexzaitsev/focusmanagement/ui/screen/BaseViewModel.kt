package com.substack.alexzaitsev.focusmanagement.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.key.Key
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<T : Any>(initialState: T) : ViewModel() {

    var state by mutableStateOf(initialState)
        protected set

    abstract fun onKeyDown(key: Key)
}
