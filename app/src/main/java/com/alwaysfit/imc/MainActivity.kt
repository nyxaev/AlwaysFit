package com.alwaysfit.imc

import android.graphics.Color as CorAndroid
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(CorAndroid.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(CorAndroid.TRANSPARENT)
        )
        setContent {
            AlwaysFitTheme {
                TelaImc(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
