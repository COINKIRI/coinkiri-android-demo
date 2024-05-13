package com.cokiri.coinkiri.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.cokiri.coinkiri.presentation.login.KakaoViewModel
import com.cokiri.coinkiri.presentation.login.LoginScreen
import com.cokiri.coinkiri.ui.theme.CoinkiriandroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val kakaoViewModel: KakaoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoinkiriandroidTheme {
            //LoginScreen(kakaoViewModel)
                MainScreen()
            }
        }
    }
}