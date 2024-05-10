package com.cokiri.coinkiri.feature.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.cokiri.coinkiri.core.designsystem.theme.CoinkiriandroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoinkiriandroidTheme {
                MainScreen()
            }
        }
    }
}
