package com.meridian.compasspin.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.meridian.compasspin.R

object AppIcons {
    val ArrowBack: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_arrow_back)
    val Refresh: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_refresh)
    val PlayArrow: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_play_arrow)
    val Info: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_info)
    val Place: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_place)
    val Search: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_search)
    val Settings: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_settings)
    val Star: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_star)
    val Close: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_close)
}
