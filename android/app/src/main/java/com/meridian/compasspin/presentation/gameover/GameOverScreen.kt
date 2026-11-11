package com.meridian.compasspin.presentation.gameover

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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meridian.compasspin.R
import com.meridian.compasspin.core.config.GameConfig
import com.meridian.compasspin.core.di.AppViewModelFactory
import com.meridian.compasspin.core.theme.Accent
import com.meridian.compasspin.core.theme.Hairline
import com.meridian.compasspin.core.theme.InverseText
import com.meridian.compasspin.core.theme.Land
import com.meridian.compasspin.core.ui.GradeStamp
import com.meridian.compasspin.core.ui.Labels
import com.meridian.compasspin.core.ui.PrimaryButton
import com.meridian.compasspin.core.ui.SecondaryButton
import com.meridian.compasspin.core.ui.SectionRule
import com.meridian.compasspin.core.ui.StatCard
import com.meridian.compasspin.domain.model.ClueResult
import com.meridian.compasspin.domain.model.TripResult
import kotlin.math.roundToInt

@Composable
fun GameOverScreen(
    result: TripResult,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(
        key = "gameover-" + result.tripId,
        factory = AppViewModelFactory(atlasId = result.atlasId, trip = result)
    )
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()
    GameOverContent(state = state, onPlayAgain = onPlayAgain, onMenu = onMenu)
}

@Composable
private fun GameOverContent(
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val trip = state.trip
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
                            Color(0xF21E2B23),
                            Color(0xE63E6B53),
                            Color(0xF21E2B23)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.trip_complete),
                style = MaterialTheme.typography.labelSmall,
                color = Hairline
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(Labels.atlasName(trip.atlasId)),
                style = MaterialTheme.typography.headlineMedium,
                color = InverseText,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            GradeStamp(
                grade = trip.grade.letter,
                caption = stringResource(Labels.atlasName(trip.atlasId)),
                size = 118
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(
                    R.string.total_score,
                    Labels.groupNumber(trip.totalScore),
                    Labels.groupNumber(GameConfig.MAX_TRIP_SCORE)
                ),
                style = MaterialTheme.typography.titleLarge,
                color = InverseText,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
            Spacer(modifier = Modifier.height(10.dp))
            ClueTable(clues = trip.clues, useMiles = state.useMiles)
            Spacer(modifier = Modifier.height(10.dp))
            SummaryStats(trip = trip, useMiles = state.useMiles)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.gameover_hint),
                style = MaterialTheme.typography.labelSmall,
                color = Hairline.copy(alpha = 0.75f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(
                text = stringResource(R.string.play_again),
                onClick = onPlayAgain,
                height = 58
            )
            Spacer(modifier = Modifier.height(8.dp))
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                contentColor = InverseText,
                containerColor = Color(0x1AEAF0E6),
                height = 46
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun ClueTable(clues: List<ClueResult>, useMiles: Boolean) {
    Column(modifier = Modifier.fillMaxWidth()) {
        clues.forEach { clue ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .background(
                            color = if (clue.points >= 700) Accent else Hairline,
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = clue.placeName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = InverseText,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = distanceText(clue.distanceKm, useMiles),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    color = Hairline,
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(72.dp)
                )
                Text(
                    text = Labels.groupNumber(clue.points),
                    style = MaterialTheme.typography.titleSmall,
                    color = Accent,
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(58.dp)
                )
            }
            SectionRule(color = Land.copy(alpha = 0.55f))
        }
    }
}

@Composable
private fun SummaryStats(trip: TripResult, useMiles: Boolean) {
    val best = trip.bestClueKm
    val average = trip.averageOffKm
    if (trip.clues.size < 2) return
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatCard(
            label = stringResource(R.string.stat_best_clue),
            value = distanceText(best, useMiles),
            valueColor = Accent,
            containerColor = Color(0x1AEAF0E6),
            labelColor = Hairline,
            height = 70,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            label = stringResource(R.string.stat_average_off),
            value = distanceText(average, useMiles),
            valueColor = InverseText,
            containerColor = Color(0x1AEAF0E6),
            labelColor = Hairline,
            height = 70,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun distanceText(distanceKm: Int, useMiles: Boolean): String = if (useMiles) {
    stringResource(
        R.string.distance_mi,
        Labels.groupNumber((distanceKm * GameConfig.MILES_PER_KM).roundToInt())
    )
} else {
    stringResource(R.string.distance_km, Labels.groupNumber(distanceKm))
}
