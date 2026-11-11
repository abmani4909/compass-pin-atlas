package com.meridian.compasspin.presentation.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meridian.compasspin.core.ui.AppIcons
import com.meridian.compasspin.R
import com.meridian.compasspin.core.config.GameConfig
import com.meridian.compasspin.core.di.AppViewModelFactory
import com.meridian.compasspin.core.theme.Accent
import com.meridian.compasspin.core.theme.Hairline
import com.meridian.compasspin.core.theme.InkText
import com.meridian.compasspin.core.theme.Land
import com.meridian.compasspin.core.theme.Paper
import com.meridian.compasspin.core.theme.PaperRaised
import com.meridian.compasspin.core.theme.PaperSurface
import com.meridian.compasspin.core.ui.Labels
import com.meridian.compasspin.core.ui.PaperCard
import com.meridian.compasspin.core.ui.PrimaryButton
import com.meridian.compasspin.core.ui.SectionRule
import com.meridian.compasspin.core.ui.WorldMapCanvas
import com.meridian.compasspin.domain.model.TripResult
import com.meridian.compasspin.presentation.dialog.ConfirmDialog
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun GameScreen(
    atlasId: String,
    session: Long,
    onFinished: (TripResult) -> Unit,
    onExit: () -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(
        key = "game-" + session,
        factory = AppViewModelFactory(atlasId = atlasId)
    )
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(session) {
        gameViewModel.events.collect { result -> onFinished(result) }
    }

    DisposableEffect(session) {
        onDispose { gameViewModel.pause() }
    }

    GameContent(
        state = state,
        onBack = onExit,
        onPause = { gameViewModel.onPauseRequested() },
        onRecentre = { gameViewModel.onRecentre() },
        onTransform = { px, py, zoom, w, h -> gameViewModel.onTransform(px, py, zoom, w, h) },
        onTap = { x, y, w, h -> gameViewModel.onMapTap(x, y, w, h) },
        onConfirm = { gameViewModel.onConfirmPin() },
        onAdvance = { gameViewModel.onAdvance() },
        onAbandonDismissed = { gameViewModel.onAbandonDismissed() },
        onAbandonConfirmed = { gameViewModel.onAbandonConfirmed() }
    )
}

@Composable
private fun GameContent(
    state: GameUiState,
    onBack: () -> Unit,
    onPause: () -> Unit,
    onRecentre: () -> Unit,
    onTransform: (Float, Float, Float, Float, Float) -> Unit,
    onTap: (Float, Float, Float, Float) -> Unit,
    onConfirm: () -> Unit,
    onAdvance: () -> Unit,
    onAbandonDismissed: () -> Unit,
    onAbandonConfirmed: () -> Unit
) {
    val animatedScore by animateIntAsState(
        targetValue = state.score,
        animationSpec = tween(durationMillis = 380, easing = FastOutSlowInEasing)
    )
    val animatedDistance by animateIntAsState(
        targetValue = if (state.arcFraction > 0f) state.distanceKm else 0,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper)
            .safeDrawingPadding()
    ) {
        GameHeader(
            state = state,
            score = animatedScore,
            onBack = onBack,
            onPause = onPause
        )
        ClueCard(state = state)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            WorldMapCanvas(
                camera = state.camera,
                pin = state.pin,
                truth = state.truth,
                arcFraction = state.arcFraction,
                showBorders = state.showBorders,
                interactive = state.interactive,
                mapLabel = stringResource(R.string.map_cd),
                onTransform = onTransform,
                onTap = onTap,
                modifier = Modifier.fillMaxSize()
            )
            CoordinateChip(state = state)
            DistanceBadge(state = state, distance = animatedDistance)
            RevealCard(
                state = state,
                distance = animatedDistance,
                onAdvance = onAdvance
            )
            ControlBar(
                state = state,
                onRecentre = onRecentre,
                onConfirm = onConfirm
            )
        }
    }

    if (state.abandonVisible) {
        ConfirmDialog(
            title = stringResource(R.string.abandon_title),
            message = stringResource(R.string.abandon_message),
            confirmText = stringResource(R.string.abandon_confirm),
            dismissText = stringResource(R.string.cancel),
            onConfirm = onAbandonConfirmed,
            onDismiss = onAbandonDismissed
        )
    }
}

@Composable
private fun GameHeader(
    state: GameUiState,
    score: Int,
    onBack: () -> Unit,
    onPause: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PaperSurface.copy(alpha = 0.94f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                Icon(
                    imageVector = AppIcons.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = Land,
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = onPause, modifier = Modifier.size(48.dp)) {
                Image(
                    painter = painterResource(R.drawable.icon_pause),
                    contentDescription = stringResource(R.string.pause),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(
                        R.string.clue_counter,
                        (state.clueIndex + 1).coerceAtMost(state.totalClues),
                        state.totalClues
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = Land
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(state.totalClues) { index ->
                        Box(
                            modifier = Modifier
                                .width(22.dp)
                                .height(3.dp)
                                .background(
                                    color = if (index < state.results.size) Accent else Hairline,
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }
            }
            Text(
                text = Labels.groupNumber(score),
                style = MaterialTheme.typography.titleMedium,
                color = InkText,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .semantics { liveRegion = LiveRegionMode.Polite }
            )
        }
        SectionRule()
    }
}

@Composable
private fun ClueCard(state: GameUiState) {
    val place = state.place ?: return
    PaperCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        containerColor = PaperRaised.copy(alpha = 0.92f),
        radius = 10,
        elevation = 2
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(Labels.category(place.category)),
                    style = MaterialTheme.typography.labelSmall,
                    color = Accent
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = InkText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(
                        R.string.clue_hint,
                        stringResource(Labels.atlasName(place.region))
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = Land.copy(alpha = 0.8f)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Image(
                painter = painterResource(Labels.categorySprite(place.category)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(34.dp)
            )
        }
    }
}

@Composable
private fun BoxScope.CoordinateChip(state: GameUiState) {
    val pin = state.pin ?: return
    Box(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 12.dp)
            .background(
                color = PaperSurface.copy(alpha = 0.92f),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        val latLabel = stringResource(if (pin.lat >= 0) R.string.dir_north else R.string.dir_south)
        val lonLabel = stringResource(if (pin.lon >= 0) R.string.dir_east else R.string.dir_west)
        val text = stringResource(
            R.string.coordinates,
            abs(pin.lat),
            latLabel,
            abs(pin.lon),
            lonLabel
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = InkText.copy(alpha = 0.75f),
            modifier = Modifier.semantics { stateDescription = text }
        )
    }
}

@Composable
private fun BoxScope.DistanceBadge(state: GameUiState, distance: Int) {
    AnimatedVisibility(
        visible = state.phase == GamePhase.REVEALING,
        enter = fadeIn(tween(220)),
        exit = fadeOut(tween(180)),
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(top = 12.dp, end = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = PaperSurface.copy(alpha = 0.94f),
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = distanceLabel(distance, state.useMiles),
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 24.sp,
                color = Accent,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
        }
    }
}

@Composable
private fun BoxScope.RevealCard(
    state: GameUiState,
    distance: Int,
    onAdvance: () -> Unit
) {
    val place = state.place
    AnimatedVisibility(
        visible = state.phase == GamePhase.REVEALED && place != null,
        enter = fadeIn(tween(360)) + slideInVertically(tween(360)) { it / 4 },
        exit = fadeOut(tween(200)),
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(start = 20.dp, end = 20.dp, bottom = 108.dp)
    ) {
        if (place != null) {
            PaperCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = PaperRaised.copy(alpha = 0.96f),
                radius = 10,
                elevation = 4
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = place.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = InkText
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = place.fact,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Land
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(
                                R.string.points_earned,
                                Labels.groupNumber(state.pointsEarned)
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            color = Accent
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        if (state.bullseye) {
                            Text(
                                text = stringResource(R.string.bullseye),
                                style = MaterialTheme.typography.labelSmall,
                                color = Land
                            )
                        } else {
                            Text(
                                text = distanceLabel(distance, state.useMiles),
                                style = MaterialTheme.typography.labelSmall,
                                color = Land
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    PrimaryButton(
                        text = if (state.lastClue) {
                            stringResource(R.string.see_results)
                        } else {
                            stringResource(R.string.next_clue)
                        },
                        onClick = onAdvance,
                        height = 56
                    )
                }
            }
        }
    }
}

@Composable
private fun BoxScope.ControlBar(
    state: GameUiState,
    onRecentre: () -> Unit,
    onConfirm: () -> Unit
) {
    val enabled = state.phase == GamePhase.PINNED
    val stateLabel = stringResource(
        if (enabled) R.string.cta_ready else R.string.cta_waiting
    )
    PaperCard(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 24.dp),
        containerColor = PaperSurface.copy(alpha = 0.96f),
        radius = 12,
        elevation = 6
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onRecentre, modifier = Modifier.size(48.dp)) {
                Icon(
                    imageVector = AppIcons.Refresh,
                    contentDescription = stringResource(R.string.recentre),
                    tint = Land,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            PrimaryButton(
                text = stringResource(R.string.confirm_pin),
                onClick = onConfirm,
                enabled = enabled,
                height = 56,
                stateLabel = stateLabel,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun distanceLabel(distanceKm: Int, useMiles: Boolean): String = if (useMiles) {
    stringResource(
        R.string.distance_mi,
        Labels.groupNumber((distanceKm * GameConfig.MILES_PER_KM).roundToInt())
    )
} else {
    stringResource(R.string.distance_km, Labels.groupNumber(distanceKm))
}
