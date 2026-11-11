package com.meridian.compasspin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.meridian.compasspin.core.di.ServiceLocator
import com.meridian.compasspin.core.theme.AppTheme
import com.meridian.compasspin.presentation.app.AppRoot

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ServiceLocator.initialize(applicationContext)
        setContent {
            AppTheme {
                AppRoot()
            }
        }
    }
}
