
package com.cokiri.coinkiri.presentation.analysis

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.presentation.analysis.component.SelectCoinItemContent
import com.cokiri.coinkiri.presentation.analysis.component.SelectInvestmentOpinion
import com.cokiri.coinkiri.presentation.analysis.component.TargetPeriodContent
import com.cokiri.coinkiri.ui.component.CustomSnackbarHost
import com.cokiri.coinkiri.ui.component.OpinionCard
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AnalysisWriteScreen(
    navController: NavHostController,
    analysisViewModel: AnalysisViewModel = hiltViewModel()
) {

    val selectedCoinName by analysisViewModel.selectedCoinName.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = { AnalysisWriteTopBar(navController) },
        content = { paddingValues ->
            AnalysisWriteContent(
                paddingValues = paddingValues,
                selectedCoinName = selectedCoinName,
                onShowSnackbar = { message ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message)
                    }
                },
                analysisViewModel = analysisViewModel
            )
        },
        snackbarHost = { CustomSnackbarHost(snackbarHostState) }
    )
}


/**
 * 분석글 작성화면의 TopBar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisWriteTopBar(navController: NavHostController) {
    TopAppBar(
        title = { /*TODO*/ },
        navigationIcon = {
            TextButton(
                onClick = {
                    navController.popBackStack()
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
    selectedCoinName: String,
    onShowSnackbar: (String) -> Unit,
    analysisViewModel: AnalysisViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(paddingValues)
    ) {

        // 분석할 코인종목 선택
        SelectCoinItemContent(analysisViewModel = analysisViewModel)

        // 투자의견 선택(강력매수, 매수, 중립, 매도, 강력매도)
        SelectInvestmentOpinion(
            analysisViewModel = analysisViewModel,
            enabled = selectedCoinName.isNotEmpty(),
        ) {
            if (selectedCoinName.isEmpty()) {
                onShowSnackbar("종목을 먼저 선택하세요.")
            }
        }

        // 목표 기간 선택(1개월, 3개월, 6개월, 1년, 직접입력)
        TargetPeriodContent(
            analysisViewModel = analysisViewModel,
            enabled = selectedCoinName.isNotEmpty()
        ) {
            if (selectedCoinName.isEmpty()) {
                onShowSnackbar("종목을 먼저 선택하세요.")
            }
        }
    }
}
