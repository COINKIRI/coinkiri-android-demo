package com.cokiri.coinkiri.presentation.screens.analysis.component

import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.cokiri.coinkiri.presentation.screens.analysis.AnalysisViewModel
import com.cokiri.coinkiri.ui.component.OpinionCard
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 목표기간 설정 컴포넌트
 * 분석글 작성 시 목표기간을 설정하는 컴포넌트
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TargetPeriodContent(
    analysisViewModel: AnalysisViewModel,
    enabled: Boolean = true,
    onClickWhenDisabled: () -> Unit = {}
) {
    val selectedTargetPeriod by analysisViewModel.selectedTargetPeriod.collectAsState()
    val selectedDate by analysisViewModel.selectedDate.collectAsState()

    val targetPeriodSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var showTargetPeriodBottomSheet by remember { mutableStateOf(false) }

    fun calculateTargetDate(period: String, date: LocalDate? = null): String {
        val currentDate = LocalDate.now()
        val targetDate = when (period) {
            "1개월" -> currentDate.plusMonths(1)
            "3개월" -> currentDate.plusMonths(3)
            "6개월" -> currentDate.plusMonths(6)
            "1년" -> currentDate.plusYears(1)
            "직접입력" -> date ?: selectedDate ?: currentDate
            else -> currentDate
        }
        return targetDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
    }

    if (showTargetPeriodBottomSheet) {
        TargetPeriodBottomSheet(
            targetPeriodSheetState = targetPeriodSheetState,
            onDateSelected = { date ->
                coroutineScope.launch {
                    analysisViewModel.setSelectedDate(date)
                    val targetDate = calculateTargetDate("직접입력", date)
                    analysisViewModel.setTargetPeriod(targetDate)
                    targetPeriodSheetState.hide()
                    showTargetPeriodBottomSheet = false
                }
            },
            onDismissRequest = {
                coroutineScope.launch {
                    targetPeriodSheetState.hide()
                    showTargetPeriodBottomSheet = false
                }
            }
        )
    }

    TargetPeriodCard(
        selectedTargetPeriod = selectedTargetPeriod,
        onPeriodSelected = { period ->
            if (enabled) {
                coroutineScope.launch {
                    val targetDate = calculateTargetDate(period)
                    analysisViewModel.setTargetPeriod(targetDate)
                }
            } else {
                onClickWhenDisabled()
            }
        },
        onDirectInputClick = {
            if (enabled) coroutineScope.launch {
                showTargetPeriodBottomSheet = true
            } else onClickWhenDisabled()
        }
    )
}


/**
 * 사용자가 직접 날짜를 선택할 수 있는 BottomSheet
 * DatePicker를 사용하여 날짜를 선택하고, 선택한 날짜를 AnalysisViewModel에 저장
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TargetPeriodBottomSheet(
    targetPeriodSheetState: SheetState,
    onDateSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = targetPeriodSheetState,
        containerColor = CoinkiriWhite,
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 50.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AndroidView(
                factory = { context ->
                    DatePicker(context).apply {
                        minDate = System.currentTimeMillis() // 오늘 날짜 이후만 선택 가능
                        setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                            onDateSelected(LocalDate.of(year, monthOfYear + 1, dayOfMonth))
                        }
                    }
                },
                modifier = Modifier.wrapContentSize()
            )
        }
    }
}


/**
 * 목표기간을 설정하는 카드
 * 목표기간을 선택하거나(1개월, 3개월, 6개월, 1년) 사용자가 직접입력할 수 있는 카드
 */
@Composable
fun TargetPeriodCard(
    selectedTargetPeriod: String,
    onPeriodSelected: (String) -> Unit,
    onDirectInputClick: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(CoinkiriWhite),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "목표기간 설정")
                Text(
                    text = selectedTargetPeriod,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "투자의견의 유효기간을 선택해주세요",
                fontSize = 12.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .height(50.dp)
                    .fillMaxWidth()
            ) {
                val periods = listOf("1개월", "3개월", "6개월", "1년", "직접입력")
                periods.forEach { period ->
                    val shape = when (period) {
                        "1개월" -> RoundedCornerShape(bottomStart = 10.dp, topStart = 10.dp)
                        "직접입력" -> RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
                        else -> RectangleShape
                    }
                    OpinionCard(
                        text = period,
                        isSelected = selectedTargetPeriod == period,
                        onClick = {
                            if (period == "직접입력") onDirectInputClick() else onPeriodSelected(period)
                        },
                        shape = shape,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}
