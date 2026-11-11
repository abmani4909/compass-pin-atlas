package com.meridian.compasspin.presentation.menu

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meridian.compasspin.core.ui.AppIcons
import com.meridian.compasspin.R
import com.meridian.compasspin.core.di.AppViewModelFactory
import com.meridian.compasspin.core.theme.Accent
import com.meridian.compasspin.core.theme.Hairline
import com.meridian.compasspin.core.theme.InkText
import com.meridian.compasspin.core.theme.InverseText
import com.meridian.compasspin.core.theme.Land
import com.meridian.compasspin.core.ui.AtlasTile
import com.meridian.compasspin.core.ui.Labels
import com.meridian.compasspin.core.ui.PrimaryButton
import com.meridian.compasspin.core.ui.SecondaryButton
import com.meridian.compasspin.core.ui.SectionRule
import com.meridian.compasspin.core.ui.StatCard
import com.meridian.compasspin.data.sample.SampleData

@Composable
fun MenuScreen(
    onPlay: (String) -> Unit,
    onPassport: () -> Unit,
    onWeakSpots: () -> Unit,
    onStudy: () -> Unit,
    onHowToPlay: () -> Unit,
    onSettings: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = AppViewModelFactory())
    val state by menuViewModel.state.collectAsStateWithLifecycle()
    MenuContent(
        state = state,
        onPlay = onPlay,
        onPassport = onPassport,
        onWeakSpots = onWeakSpots,
        onStudy = onStudy,
        onHowToPlay = onHowToPlay,
        onSettings = onSettings
    )
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: (String) -> Unit,
    onPassport: () -> Unit,
    onWeakSpots: () -> Unit,
    onStudy: () -> Unit,
    onHowToPlay: () -> Unit,
    onSettings: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val headerFade by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    val heroFade by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 420, delayMillis = 80, easing = FastOutSlowInEasing)
    )

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
                            Color(0xE6EAF0E6),
                            Color(0xF2F2EAD8),
                            Color(0xE6EAF0E6)
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
                    .height(66.dp)
                    .alpha(headerFade),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.brand_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 22.sp,
                        color = InkText
                    )
                    Text(
                        text = stringResource(R.string.brand_sub),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        color = Land
                    )
                }
                IconButton(
                    onClick = onSettings,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = AppIcons.Settings,
                        contentDescription = stringResource(R.string.settings),
                        tint = Land,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            SectionRule()
            Spacer(modifier = Modifier.height(14.dp))

            StatStrip(state = state)

            HeroTile(
                fade = heroFade,
                onPlay = { onPlay(SampleData.WORLD) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            AtlasShelf(state = state, visible = visible, onPlay = onPlay)

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.passport),
                    onClick = onPassport,
                    height = 46,
                    leadingIcon = AppIcons.Star,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    text = stringResource(R.string.place_glossary),
                    onClick = onStudy,
                    height = 46,
                    leadingIcon = AppIcons.Search,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.weak_spots),
                    onClick = onWeakSpots,
                    height = 46,
                    leadingIcon = AppIcons.Place,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    text = stringResource(R.string.how_to_play),
                    onClick = onHowToPlay,
                    height = 46,
                    leadingIcon = AppIcons.Info,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatStrip(state: MenuUiState) {
    val grade = state.bestGrade
    val showStamps = state.stampsEarned > 0
    val showTrips = state.tripsCompleted > 0
    val shown = (if (showStamps) 1 else 0) + (if (grade != null) 1 else 0) + (if (showTrips) 1 else 0)
    if (shown < 2) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (showStamps) {
            StatCard(
                label = stringResource(R.string.stat_stamps),
                value = stringResource(R.string.stamp_value, state.stampsEarned, state.totalStamps),
                valueColor = Accent,
                modifier = Modifier.weight(1f)
            )
        }
        if (grade != null) {
            StatCard(
                label = stringResource(R.string.stat_best_grade),
                value = grade.letter,
                valueColor = Land,
                modifier = Modifier.weight(1f)
            )
        }
        if (showTrips) {
            StatCard(
                label = stringResource(R.string.stat_trips),
                value = Labels.groupNumber(state.tripsCompleted),
                valueColor = InkText,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun HeroTile(fade: Float, onPlay: () -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Land),
        border = BorderStroke(1.dp, Color(0x331E2B23)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .alpha(fade)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_1),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.world_atlas),
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 22.sp,
                    color = InverseText
                )
                Text(
                    text = stringResource(R.string.world_atlas_sub),
                    style = MaterialTheme.typography.labelSmall,
                    color = Hairline
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.menu_hint),
                    style = MaterialTheme.typography.labelSmall,
                    color = Hairline.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                PrimaryButton(
                    text = stringResource(R.string.play),
                    onClick = onPlay,
                    leadingIcon = AppIcons.PlayArrow,
                    height = 56
                )
            }
        }
    }
}

@Composable
private fun AtlasShelf(
    state: MenuUiState,
    visible: Boolean,
    onPlay: (String) -> Unit
) {
    val entries = state.shelf
    val notVisited = stringResource(R.string.not_visited)
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        entries.chunked(2).forEachIndexed { rowIndex, row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEachIndexed { columnIndex, entry ->
                    val grade = entry.grade
                    AtlasTile(
                        title = stringResource(Labels.atlasName(entry.atlas.id)),
                        badge = if (grade == null) notVisited else grade.letter,
                        earned = grade != null,
                        visible = visible,
                        delayMillis = 140 + (rowIndex * 2 + columnIndex) * 50,
                        onClick = { onPlay(entry.atlas.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
