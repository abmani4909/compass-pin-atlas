package com.meridian.compasspin.presentation.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.meridian.compasspin.core.theme.Accent
import com.meridian.compasspin.core.theme.InkText
import com.meridian.compasspin.core.theme.Land
import com.meridian.compasspin.core.theme.PaperSurface

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(10.dp),
        containerColor = PaperSurface,
        titleContentColor = InkText,
        textContentColor = Land,
        iconContentColor = Accent,
        tonalElevation = 0.dp,
        title = {
            Text(text = title, style = MaterialTheme.typography.titleLarge, color = InkText)
        },
        text = {
            Text(text = message, style = MaterialTheme.typography.bodyMedium, color = Land)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    style = MaterialTheme.typography.labelMedium,
                    color = Accent
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = dismissText,
                    style = MaterialTheme.typography.labelMedium,
                    color = Land
                )
            }
        }
    )
}
