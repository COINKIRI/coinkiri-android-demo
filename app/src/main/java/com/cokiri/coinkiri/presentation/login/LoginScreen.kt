package com.cokiri.coinkiri.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.ui.theme.PretendardFont
import com.cokiri.coinkiri.ui.theme.kakaoColor
import com.cokiri.coinkiri.util.HOME
import com.cokiri.coinkiri.util.LOGIN


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavController
) {

    val loginUiState by viewModel.loginUiState.collectAsStateWithLifecycle()

    LaunchedEffect(loginUiState) {
        if (loginUiState is LoginUiState.LogInSuccess) {
            navController.navigate(HOME) {
                popUpTo(LOGIN) { inclusive = true }
            }
        }
    }


    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "coinkiri",
                fontFamily = PretendardFont,
                fontWeight = FontWeight.Bold,
                fontSize = 60.sp
            )
            Image(
                painter = painterResource(id = R.drawable.ic_logo_coinkiri),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(155.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                KakaoLoginBtn { viewModel.kakaoLogin() }
            }
        }
    }
}


@Composable
fun KakaoLoginBtn(
    onClick: () -> Unit
) {
    Card(
        onClick = { onClick() },
        colors = CardDefaults.cardColors(kakaoColor),
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
            Text(
                text = "Kakao 로그인",
                fontFamily = PretendardFont,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )
        }
    }
}