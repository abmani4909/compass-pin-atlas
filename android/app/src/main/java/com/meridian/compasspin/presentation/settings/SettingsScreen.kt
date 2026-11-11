package com.meridian.compasspin.presentation.settings

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meridian.compasspin.R
import com.meridian.compasspin.core.di.AppViewModelFactory
import com.meridian.compasspin.core.theme.Accent
import com.meridian.compasspin.core.theme.Hairline
import com.meridian.compasspin.core.theme.InkText
import com.meridian.compasspin.core.theme.InverseText
import com.meridian.compasspin.core.theme.Land
import com.meridian.compasspin.core.theme.PaperSurface
import com.meridian.compasspin.core.ui.SecondaryButton
import com.meridian.compasspin.core.ui.SectionRule
import com.meridian.compasspin.presentation.dialog.ConfirmDialog

@Composable
fun SettingsScreen(onMenu: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelFactory())
    val settings by settingsViewModel.settings.collectAsStateWithLifecycle()
    val resetVisible by settingsViewModel.resetVisible.collectAsStateWithLifecycle()

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
                            Color(0xE6F2EAD8),
                            Color(0xF2EAF0E6)
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
                Text(
                    text = stringResource(R.string.settings),
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 22.sp,
                    color = InkText,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(R.drawable.icon_home),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(26.dp)
                )
            }
            SectionRule()

            SettingRow(label = stringResource(R.string.settings_units)) {
                SegmentedPair(
                    firstLabel = stringResource(R.string.unit_km),
                    secondLabel = stringResource(R.string.unit_miles),
                    secondSelected = settings.useMiles,
                    onFirst = { settingsViewModel.onUnitSelected(false) },
                    onSecond = { settingsViewModel.onUnitSelected(true) }
                )
            }
            SectionRule()

            SettingRow(label = stringResource(R.string.settings_borders)) {
                SegmentedPair(
                    firstLabel = stringResource(R.string.borders_easy),
                    secondLabel = stringResource(R.string.borders_hard),
                    secondSelected = settings.hardBorders,
                    onFirst = { settingsViewModel.onBordersSelected(false) },
                    onSecond = { settingsViewModel.onBordersSelected(true) }
                )
            }
            SectionRule()

            SettingRow(label = stringResource(R.string.settings_sound)) {
                val soundLabel = stringResource(
                    if (settings.soundEnabled) R.string.state_on else R.string.state_off
                )
                Switch(
                    checked = settings.soundEnabled,
                    onCheckedChange = { value -> settingsViewModel.onSoundToggled(value) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = PaperSurface,
                        checkedTrackColor = Land,
                        uncheckedThumbColor = Land,
                        uncheckedTrackColor = Hairline
                    ),
                    modifier = Modifier.semantics { stateDescription = soundLabel }
                )
            }
            SectionRule()

            Spacer(modifier = Modifier.height(18.dp))
            SecondaryButton(
                text = stringResource(R.string.reset_progress),
                onClick = { settingsViewModel.onResetRequested() },
                contentColor = Accent
            )

            Spacer(modifier = Modifier.weight(1f))
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }

    if (resetVisible) {
        ConfirmDialog(
            title = stringResource(R.string.reset_title),
            message = stringResource(R.string.reset_message),
            confirmText = stringResource(R.string.reset),
            dismissText = stringResource(R.string.cancel),
            onConfirm = { settingsViewModel.onResetConfirmed() },
            onDismiss = { settingsViewModel.onResetDismissed() }
        )
    }
}

@Composable
private fun SettingRow(label: String, control: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = InkText,
            modifier = Modifier.weight(1f)
        )
        control()
    }
}

@Composable
private fun SegmentedPair(
    firstLabel: String,
    secondLabel: String,
    secondSelected: Boolean,
    onFirst: () -> Unit,
    onSecond: () -> Unit
) {
    Row(
        modifier = Modifier.width(180.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SecondaryButton(
            text = firstLabel,
            onClick = onFirst,
            height = 44,
            contentColor = if (secondSelected) Land else InverseText,
            containerColor = if (secondSelected) PaperSurface.copy(alpha = 0.55f) else Land,
            modifier = Modifier.weight(1f)
        )
        SecondaryButton(
            text = secondLabel,
            onClick = onSecond,
            height = 44,
            contentColor = if (secondSelected) InverseText else Land,
            containerColor = if (secondSelected) Land else PaperSurface.copy(alpha = 0.55f),
            modifier = Modifier.weight(1f)
        )
    }
}
