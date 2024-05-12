package com.cokiri.coinkiri.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun LoginScreen(kakaoViewModel: KakaoViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            KakaoLoginBtn(kakaoViewModel = kakaoViewModel)
            KakaoLogoutBtn(kakaoViewModel = kakaoViewModel)
            NaverLoginBtn()
        }
    }
}

@Composable
fun KakaoLoginBtn(kakaoViewModel: KakaoViewModel) {
    Card(
        onClick = { kakaoViewModel.kakaologin() },
        colors = CardDefaults.cardColors(Color.Yellow),
        elevation = CardDefaults.cardElevation(pressedElevation = 3.dp),
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Kakao Login")
        }
    }
}

@Composable
fun KakaoLogoutBtn(kakaoViewModel: KakaoViewModel) {
    Card(
        onClick = { kakaoViewModel.kakaoLogout()},
        colors = CardDefaults.cardColors(Color.Yellow),
        elevation = CardDefaults.cardElevation(pressedElevation = 3.dp),
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Kakao Logout")
        }
    }
}


@Composable
fun NaverLoginBtn() {
    Card(
        onClick = { /* Handle Naver Login Click */ },
        colors = CardDefaults.cardColors(Color.Green),
        elevation = CardDefaults.cardElevation(pressedElevation = 3.dp),
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "네이버 로그인")
        }
    }
}
