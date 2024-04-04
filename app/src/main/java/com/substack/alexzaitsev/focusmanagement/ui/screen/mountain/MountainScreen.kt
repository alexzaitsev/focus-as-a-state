package com.substack.alexzaitsev.focusmanagement.ui.screen.mountain

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.substack.alexzaitsev.focusmanagement.model.Mountain
import com.substack.alexzaitsev.focusmanagement.ui.screen.withManagedFocus
import com.substack.alexzaitsev.focusmanagement.ui.theme.Overlay
import org.koin.androidx.compose.koinViewModel

private const val TAG = "Mountain"

@Composable
fun MountainScreen(mountain: Mountain) {
    val viewModel = koinViewModel<MountainViewModel>().apply { setData(mountain) }
    val screenFocus = withManagedFocus(
        tag = TAG,
        viewModel = viewModel,
    ) {
        ScreenContent(state = viewModel.state)
    }
    LaunchedEffect(key1 = TAG) {
        screenFocus.capture()
    }
}

@Composable
private fun ScreenContent(state: MountainState) {
    val mountain = state.mountain ?: return
    Box(
        modifier = Modifier.padding(70.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))
                .drawWithContent {
                    drawContent()
                    drawRect(color = Overlay)
                },
            painter = painterResource(id = mountain.image),
            contentScale = ContentScale.FillBounds,
            contentDescription = mountain.name
        )
        Text(
            text = mountain.name,
            fontSize = 30.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
