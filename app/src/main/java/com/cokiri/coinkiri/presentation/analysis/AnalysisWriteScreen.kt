package com.cokiri.coinkiri.presentation.analysis

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.presentation.analysis.component.SelectCoinItemContent
import com.cokiri.coinkiri.presentation.analysis.component.SelectInvestmentOpinion
import com.cokiri.coinkiri.presentation.analysis.component.TargetPeriodContent
import com.cokiri.coinkiri.ui.component.CustomSnackbarHost
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen
import com.cokiri.coinkiri.util.CREATE_POST_SCREEN_FOR_ANALYSIS
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AnalysisWriteScreen(
    navController: NavHostController,
    analysisViewModel: AnalysisViewModel = hiltViewModel()
) {

    val selectedCoinId by analysisViewModel.selectedCoinId.collectAsState()
    val selectedCoinPrevClosingPrice by analysisViewModel.selectedCoinPrevClosingPrice.collectAsState()

    val selectedInvestmentOption by analysisViewModel.selectedInvestmentOption.collectAsState()
    val selectedTargetPrice by analysisViewModel.selectedTargetPrice.collectAsState()

    val selectedTargetPeriod by analysisViewModel.selectedTargetPeriod.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = { AnalysisWriteTopBar(
            navController,
            analysisViewModel = analysisViewModel
        ) },
        content = { paddingValues ->
            AnalysisWriteContent(
                paddingValues = paddingValues,
                selectedCoinId = selectedCoinId,
                onShowSnackbar = { message ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message)
                    }
                },
                analysisViewModel = analysisViewModel
            )
        },
        bottomBar = { AnalysisWriteBottomBar(
            enabled = selectedCoinId != 0L &&
                    selectedCoinPrevClosingPrice.isNotEmpty() &&
                    selectedInvestmentOption.isNotEmpty() &&
                    selectedTargetPrice.isNotEmpty() &&
                    selectedTargetPeriod.isNotEmpty(),
            navController = navController
        ) },
        snackbarHost = { CustomSnackbarHost(snackbarHostState) }
    )
}


/**
 * 분석글 작성화면의 TopBar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisWriteTopBar(
    navController: NavHostController,
    analysisViewModel: AnalysisViewModel
) {
    var showDialog by remember { mutableStateOf(false) }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "취소 확인") },
            text = { Text("정말 취소하시겠습니까? 작성 중인 내용은 저장되지 않습니다.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        analysisViewModel.resetState()
                        navController.popBackStack()
                        showDialog = false
                    }
                ) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("취소")
                }
            }
        )
    }

    TopAppBar(
        title = { /*TODO*/ },
        navigationIcon = {
            TextButton(
                onClick = {
                    showDialog = true
                }
            ) {
                Text(text = "취소")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(CoinkiriBackground)
    )
}


/**
 * 분석글 작성화면의 Content
 * - 코인종목 선택
 * - 투자의견 선택
 * - 목표기간 선택
 */
@Composable
fun AnalysisWriteContent(
    paddingValues: PaddingValues,
    selectedCoinId: Long,
    onShowSnackbar: (String) -> Unit,
    analysisViewModel: AnalysisViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CoinkiriBackground)
            .padding(paddingValues)
    ) {

        // 분석할 코인종목 선택
        SelectCoinItemContent(
            analysisViewModel = analysisViewModel,
        )

        // 투자의견 선택(강력매수, 매수, 중립, 매도, 강력매도)
        SelectInvestmentOpinion(
            analysisViewModel = analysisViewModel,
            enabled = selectedCoinId != 0L,
        ) {
            if (selectedCoinId == 0L) {
                onShowSnackbar("종목을 먼저 선택하세요.")
            }
        }

        // 목표 기간 선택(1개월, 3개월, 6개월, 1년, 직접입력)
        TargetPeriodContent(
            analysisViewModel = analysisViewModel,
            enabled = selectedCoinId != 0L,
        ) {
            if (selectedCoinId == 0L) {
                onShowSnackbar("종목을 먼저 선택하세요.")
            }
        }
    }
}


/**
 * 분석글 작성화면의 BottomBar
 */
@Composable
fun AnalysisWriteBottomBar(
    enabled: Boolean,
    navController: NavHostController
    ) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(CoinkiriBackground)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                shape = RectangleShape,
                enabled = enabled
            ) {
                Text(text = "임시저장")
            }

            VerticalDivider()

            TextButton(
                onClick = { navController.navigate(CREATE_POST_SCREEN_FOR_ANALYSIS) },
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                shape = RectangleShape,
                enabled = enabled,
                colors = ButtonDefaults.textButtonColors(CoinkiriPointGreen, Color.White),
            ) {
                Text(text = "내용 작성하기")
            }
        }
    }
}
