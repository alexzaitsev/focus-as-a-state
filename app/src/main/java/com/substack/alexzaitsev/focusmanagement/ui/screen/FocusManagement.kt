package com.substack.alexzaitsev.focusmanagement.ui.screen

import android.util.Log
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ScreenFocus(
    private val focusCaptureRequest: MutableState<Boolean>
) {
    fun capture() {
        focusCaptureRequest.value = true
    }
}

/**
 * Top wrapper for the screen, provides outer focus control.
 */
@Composable
fun withManagedFocus(
    tag: String,
    viewModel: BaseViewModel<*>,
    content: @Composable () -> Unit
): ScreenFocus {
    val coroutineScope = rememberCoroutineScope()
    var focusCaptureRequest: MutableState<Boolean> = remember { mutableStateOf(false) }

    LocalScreenFocusRequestedProvider {
        val parentFocusRequester = remember { FocusRequester() }
        focusCaptureRequest = isScreenFocusRequested()

        LaunchedEffect(tag) {
            while (isActive) {
                if (focusCaptureRequest.value) {
                    Log.d(tag, "is capturing the focus")
                    if (parentFocusRequester.safeCapture()) {
                        focusCaptureRequest.value = false
                    }
                }
                delay(100L)
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .size(1.dp)
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.nativeKeyEvent.action == android.view.KeyEvent.ACTION_DOWN) {
                            coroutineScope.launch {
                                viewModel.onKeyDown(keyEvent.key)
                            }
                        }
                        false
                    }
                    .onFocusChanged {
                        val focused = if (it.isCaptured) "CAPTURED" else "UNCAPTURED"
                        Log.d(tag, "focus changed to $focused")
                    }
                    .focusRequester(parentFocusRequester)
                    .focusable()
            ) // captures the focus
            content()
        }
    }

    return ScreenFocus(
        focusCaptureRequest = focusCaptureRequest
    )
}

// ============== COMPOSITION LOCAL ===================

/**
 * https://developer.android.com/jetpack/compose/compositionlocal
 */
private val LocalScreenFocusRequested = compositionLocalOf<MutableState<Boolean>?> { null }

@Composable
private fun LocalScreenFocusRequestedProvider(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalScreenFocusRequested provides remember { mutableStateOf(false) }, content = content)
}

/**
 * Intentionally private. Stimulates using wrappers provided in ScreenFocus.kt.
 */
@Composable
private fun isScreenFocusRequested(): MutableState<Boolean> {
    return LocalScreenFocusRequested.current
        ?: throw RuntimeException("Please wrap your screen with LocalScreenFocusRequested")
}

// ================== UTILS ======================

fun FocusRequester.safeCapture(): Boolean = try {
    requestFocus()
    captureFocus()
} catch (_: Exception) {
    // ignore
    false
}
