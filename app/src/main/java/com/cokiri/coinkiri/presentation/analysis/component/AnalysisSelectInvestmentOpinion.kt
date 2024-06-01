package com.cokiri.coinkiri.presentation.analysis.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.presentation.analysis.AnalysisViewModel
import com.cokiri.coinkiri.ui.component.OpinionCard
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectInvestmentOpinion(
    enabled: Boolean = true,
    analysisViewModel: AnalysisViewModel,
    onClickWhenDisabled: () -> Unit = {}
) {
    val selectedInvestmentOption by analysisViewModel.selectedInvestmentOption.collectAsState()
    val selectedCoinPrevClosingPrice by analysisViewModel.selectedCoinPrevClosingPrice.collectAsState()

    var showTargetPriceBottomSheet by remember { mutableStateOf(false) }
    val targetPriceSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    if (showTargetPriceBottomSheet) {
        TargetPriceBottomSheet(
            coroutineScope = coroutineScope,
            sheetState = targetPriceSheetState,
            onDismissRequest = { coroutineScope.launch { showTargetPriceBottomSheet = false } }
        )
    }

    if (selectedInvestmentOption.isEmpty()) {
        OpinionSelectionCard(
            selectedInvestmentOption = selectedInvestmentOption,
            enabled = enabled,
            onOptionSelected = { option ->
                analysisViewModel.setSelectedInvestmentOption(option)
            },
            onClickWhenDisabled = onClickWhenDisabled
        )
    } else {
        Column {
            OpinionSelectionCard(
                selectedInvestmentOption = selectedInvestmentOption,
                enabled = enabled,
                onOptionSelected = { option ->
                    analysisViewModel.setSelectedInvestmentOption(option)
                },
                onClickWhenDisabled = onClickWhenDisabled,
                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
            )
            TargetPriceCard(
                selectedCoinPrevClosingPrice = selectedCoinPrevClosingPrice,
                onTargetPriceClick = {
                    coroutineScope.launch { showTargetPriceBottomSheet = true }
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TargetPriceBottomSheet(
    coroutineScope: CoroutineScope,
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = CoinkiriBackground,
    ) {
        CenterAlignedTopAppBar(
            title = { Text(text = "목표가격설정", fontSize = 18.sp) },
            modifier = Modifier.height(50.dp),
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(CoinkiriBackground),
            navigationIcon = {
                IconButton(onClick = { coroutineScope.launch { onDismissRequest() } }) {
                    Icon(painter = painterResource(id = R.drawable.ic_close), contentDescription = "닫기")
                }
            }
        )
        LazyColumn(
            modifier = Modifier
                .background(CoinkiriBackground)
                .fillMaxSize(),
        ) {
            items(30) {
                Text(text = "목표가격설정")
            }
        }
    }
}



@Composable
fun OpinionSelectionCard(
    selectedInvestmentOption: String,
    enabled: Boolean,
    onOptionSelected: (String) -> Unit,
    onClickWhenDisabled: () -> Unit,
    shape: Shape = RoundedCornerShape(10.dp)
) {
    Card(
        colors = CardDefaults.cardColors(CoinkiriBackground),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = shape,
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
                Text(text = "투자의견")
                Text(text = selectedInvestmentOption, fontWeight = FontWeight.Bold)
            }
            Text(text = "해당 코인의 매력도를 선택해주세요", fontSize = 12.sp)
            OpinionOptionsRow(
                selectedInvestmentOption = selectedInvestmentOption,
                enabled = enabled,
                onOptionSelected = onOptionSelected,
                onClickWhenDisabled = onClickWhenDisabled
            )
        }
    }
}



@Composable
fun OpinionOptionsRow(
    selectedInvestmentOption: String,
    enabled: Boolean,
    onOptionSelected: (String) -> Unit,
    onClickWhenDisabled: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 10.dp)
            .height(50.dp)
            .fillMaxWidth()
    ) {
        listOf("강력매도", "매도", "중립", "매수", "강력매수").forEachIndexed { index, option ->
            OpinionCard(
                text = option,
                isSelected = selectedInvestmentOption == option,
                onClick = {
                    if (enabled) onOptionSelected(option) else onClickWhenDisabled()
                },
                shape = when (index) {
                    0 -> RoundedCornerShape(bottomStart = 10.dp, topStart = 10.dp)
                    4 -> RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
                    else -> RectangleShape
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}



@Composable
fun TargetPriceCard(
    selectedCoinPrevClosingPrice: String,
    onTargetPriceClick: () -> Unit
) {
    val currentPrice by rememberUpdatedState(newValue = selectedCoinPrevClosingPrice)

    Card(
        colors = CardDefaults.cardColors(CoinkiriBackground),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(text = "목표가격")
                Spacer(modifier = Modifier.padding(1.dp))
                Text(text = "현재가 : $currentPrice", fontSize = 13.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "목표가격 선택")
                IconButton(onClick = onTargetPriceClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_analysis_keyboard_arrow_down),
                        contentDescription = ""
                    )
                }
            }
        }
    }
}