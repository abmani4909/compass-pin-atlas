package com.meridian.compasspin.presentation.weakspots

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meridian.compasspin.R
import com.meridian.compasspin.core.di.AppViewModelFactory
import com.meridian.compasspin.core.theme.Accent
import com.meridian.compasspin.core.theme.Hairline
import com.meridian.compasspin.core.theme.InkText
import com.meridian.compasspin.core.theme.Land
import com.meridian.compasspin.core.ui.Labels
import com.meridian.compasspin.core.ui.PaperCard
import com.meridian.compasspin.core.ui.PrimaryButton
import com.meridian.compasspin.core.ui.SecondaryButton
import com.meridian.compasspin.core.ui.SectionRule
import com.meridian.compasspin.data.sample.SampleData
import com.meridian.compasspin.domain.model.WeakSpot

@Composable
fun WeakSpotsScreen(
    onPlay: (String) -> Unit,
    onMenu: () -> Unit
) {
    val weakSpotsViewModel: WeakSpotsViewModel = viewModel(factory = AppViewModelFactory())
    val state by weakSpotsViewModel.state.collectAsStateWithLifecycle()
    WeakSpotsContent(state = state, onPlay = { onPlay(SampleData.WORLD) }, onMenu = onMenu)
}

@Composable
private fun WeakSpotsContent(
    state: WeakSpotsUiState,
    onPlay: () -> Unit,
    onMenu: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xF2EAF0E6),
                            Color(0xE6DCE6D6),
                            Color(0xF2F2EAD8)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.weak_spots),
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 22.sp,
                        color = InkText
                    )
                    Text(
                        text = stringResource(R.string.weak_spots_sub),
                        style = MaterialTheme.typography.labelSmall,
                        color = Land
                    )
                }
                Image(
                    painter = painterResource(R.drawable.sprite_4),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(40.dp)
                )
            }
            SectionRule()
            Spacer(modifier = Modifier.height(14.dp))

            if (state.spots.isEmpty()) {
                EmptyState(onPlay = onPlay, modifier = Modifier.weight(1f))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items = state.spots, key = { spot -> spot.placeId }) { spot ->
                        WeakSpotRow(spot = spot, worstKm = state.worstKm)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                leadingImage = R.drawable.icon_home,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}

@Composable
private fun EmptyState(onPlay: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_2),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(96.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.weak_spots_empty),
            style = MaterialTheme.typography.bodyLarge,
            color = Land,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        PrimaryButton(
            text = stringResource(R.string.start_a_trip),
            onClick = onPlay,
            leadingIcon = Icons.Default.PlayArrow,
            height = 56
        )
    }
}

@Composable
private fun WeakSpotRow(spot: WeakSpot, worstKm: Int) {
    val share = if (worstKm <= 0) {
        0f
    } else {
        (spot.averageKm.toFloat() / worstKm.toFloat()).coerceIn(0f, 1f)
    }
    PaperCard(modifier = Modifier.fillMaxWidth(), radius = 8, elevation = 1) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = spot.placeName,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 15.sp,
                    color = InkText,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.distance_km, Labels.groupNumber(spot.averageKm)),
                    style = MaterialTheme.typography.titleSmall,
                    color = Accent
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(color = Hairline, shape = RoundedCornerShape(3.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(share)
                        .height(6.dp)
                        .background(color = Accent, shape = RoundedCornerShape(3.dp))
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.weak_spot_attempts, spot.attempts),
                style = MaterialTheme.typography.labelSmall,
                color = Land.copy(alpha = 0.8f)
            )
        }
    }
}
