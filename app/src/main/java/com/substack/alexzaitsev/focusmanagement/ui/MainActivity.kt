package com.substack.alexzaitsev.focusmanagement.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import com.substack.alexzaitsev.focusmanagement.ui.screen.mountain.MountainScreen
import com.substack.alexzaitsev.focusmanagement.ui.screen.ranges.RangesScreen
import com.substack.alexzaitsev.focusmanagement.ui.theme.FocusManagementTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val navController: NavController by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FocusManagementTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    NavigationHost()
                }
            }
        }
    }

    @Composable
    private fun NavigationHost() {
        when (val screen = navController.screen.collectAsState(initial = DEFAULT_SCREEN).value) {
            Screen.Ranges -> RangesScreen()
            is Screen.Mountain -> MountainScreen(screen.mountain)
            Screen.Exit -> super.onBackPressed()
        }
    }

    override fun onBackPressed() {
        // ignore, rely on NavigationHost
    }

    companion object {
        private val DEFAULT_SCREEN = Screen.Ranges
    }
}
