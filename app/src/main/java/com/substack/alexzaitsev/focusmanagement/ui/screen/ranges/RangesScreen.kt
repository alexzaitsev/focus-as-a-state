package com.substack.alexzaitsev.focusmanagement.ui.screen.ranges

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.substack.alexzaitsev.focusmanagement.model.Mountain
import com.substack.alexzaitsev.focusmanagement.model.MountainRange
import com.substack.alexzaitsev.focusmanagement.ui.screen.util.rememberSyncedLazyListState
import com.substack.alexzaitsev.focusmanagement.ui.screen.util.rememberSyncedLazyVerticalGridState
import com.substack.alexzaitsev.focusmanagement.ui.screen.withManagedFocus
import com.substack.alexzaitsev.focusmanagement.ui.theme.Overlay
import com.substack.alexzaitsev.focusmanagement.ui.theme.Purple40
import com.substack.alexzaitsev.focusmanagement.ui.theme.Purple80
import org.koin.androidx.compose.koinViewModel

private const val TAG = "Ranges"

@Composable
fun RangesScreen() {
    val viewModel = koinViewModel<RangesViewModel>()
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
private fun ScreenContent(state: RangesState) {
    Column(modifier = Modifier.padding(20.dp)) {
        Text(
            text = "Mountain ranges",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(20.dp))
        Row {
            RangesList(
                modifier = Modifier.width(250.dp),
                ranges = state.mountainRanges,
                focusedIndex = state.focusedRangeIndex,
                focusedBlock = state.focusedBlock
            )
            Spacer(modifier = Modifier.size(20.dp))
            MountainsGrid(
                modifier = Modifier.weight(1f),
                range = state.mountainRanges[state.focusedRangeIndex],
                mountains = state.mountainRanges[state.focusedRangeIndex].mountains,
                itemsPerRow = RangesViewModel.CONTENT_ITEMS_PER_ROW,
                focusedIndex = state.focusedMountainIndex,
                focusedBlock = state.focusedBlock
            )
        }
    }
}

@Composable
private fun RangesList(
    modifier: Modifier = Modifier,
    ranges: List<MountainRange>,
    focusedIndex: Int,
    focusedBlock: RangesState.FocusableBlock
) {
    val lazyListState = rememberSyncedLazyListState(
        focusedIndex = focusedIndex,
        listSize = ranges.size
    )

    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        itemsIndexed(ranges) { index, range ->
            RangeItem(
                range = range,
                itemFocused = index == focusedIndex,
                listFocused = focusedBlock == RangesState.FocusableBlock.RANGES
            )
        }
    }
}

@Composable
private fun RangeItem(range: MountainRange, itemFocused: Boolean, listFocused: Boolean) {
    Box(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .background(
                color = getBorderColor(listFocused = listFocused, itemFocused = itemFocused),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = range.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = if (itemFocused && !listFocused) Color.Black else Color.White
        )
    }
}

@Composable
private fun MountainsGrid(
    modifier: Modifier = Modifier,
    range: MountainRange,
    mountains: List<Mountain>,
    itemsPerRow: Int,
    focusedIndex: Int,
    focusedBlock: RangesState.FocusableBlock
) {
    val lazyGridState = rememberSyncedLazyVerticalGridState(
        key = range.id,
        rowSize = itemsPerRow,
        focusedIndex = focusedIndex,
        listSize = mountains.size
    )

    LazyVerticalGrid(
        modifier = modifier,
        state = lazyGridState,
        columns = GridCells.Fixed(itemsPerRow),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        itemsIndexed(
            items = mountains,
            key = { _, mountain -> mountain.hashCode() }
        ) { index, mountain ->
            MountainItem(
                mountain = mountain,
                itemFocused = index == focusedIndex,
                listFocused = focusedBlock == RangesState.FocusableBlock.MOUNTAINS
            )
        }
    }
}

@Composable
private fun MountainItem(mountain: Mountain, itemFocused: Boolean, listFocused: Boolean) {
    Box(
        modifier = Modifier
            .aspectRatio(1.778f)
            .border(
                width = 3.dp,
                color = getBorderColor(listFocused = listFocused, itemFocused = itemFocused),
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
    ) {
        Image(
            modifier = Modifier.drawWithContent {
                drawContent()
                drawRect(color = Overlay)
            },
            painter = painterResource(id = mountain.image),
            contentScale = ContentScale.FillBounds,
            contentDescription = mountain.name
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(5.dp),
            text = mountain.name,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun getBorderColor(listFocused: Boolean, itemFocused: Boolean) =
    if (itemFocused) {
        if (listFocused) Purple40 else Purple80
    } else {
        Color.Transparent
    }
