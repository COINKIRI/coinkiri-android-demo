package com.cokiri.coinkiri.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.presentation.login.LoginUiState
import com.cokiri.coinkiri.presentation.login.LoginViewModel
import com.cokiri.coinkiri.util.LOGIN
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    navController: NavHostController,
) {

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val loginUiState by loginViewModel.loginUiState.collectAsStateWithLifecycle()

    val memberInfo by profileViewModel.memberInfo.collectAsStateWithLifecycle()

    LaunchedEffect(loginUiState) {
        if (loginUiState is LoginUiState.Initial) {
            navController.navigate(LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CoinkiriBackground,
                    titleContentColor = CoinkiriBlack,
                ),
                actions = {
                    IconButton(
                        onClick = { showBottomSheet = true }
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = ""
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.profile),
                        fontSize = 18.sp,
                    )
                }
            )
        },
        content = {
            if (showBottomSheet) {
                ProfileBottomSheet(
                    sheetState = sheetState,
                    onDismissSheet = { showBottomSheet = false },
                    onLogoutClick = {
                        loginViewModel.kakaoLogout()
                        showBottomSheet = false
                    }
                )
            }
            Column(modifier = Modifier.padding(it)) {
                // Profile screen content goes here
                memberInfo?.let { it1 ->
                    Text(
                        text = it1.nickname
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBottomSheet(
    sheetState: SheetState,
    onDismissSheet: () -> Unit,
    onLogoutClick: () -> Unit     // 로그아웃 버튼 클릭 시 실행할 함수
) {
    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismissSheet,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .background(CoinkiriBackground)
                .padding(bottom = 35.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ModalBottomSheetBtn(
                text = "로그아웃",
                onClick = onLogoutClick
            )
        }
    }
}




@Composable
fun ModalBottomSheetBtn(
    text : String,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
    ) {
        Text(
            text = text
        )
    }
}