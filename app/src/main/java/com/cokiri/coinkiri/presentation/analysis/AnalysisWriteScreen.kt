package com.cokiri.coinkiri.presentation.analysis

import android.annotation.SuppressLint
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.ui.component.SelectCoinCard
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AnalysisWriteScreen(
    navController: NavHostController,
    analysisViewModel: AnalysisViewModel = hiltViewModel()
) {


    Scaffold(
        topBar = {
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
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CoinkiriBackground)
                    .padding(paddingValues)
            ) {
                SelectCoinItem(analysisViewModel)
                SelectOpinion()
                TargetPeriod()
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCoinItem(analysisViewModel: AnalysisViewModel) {

    var selectCoin by remember { mutableStateOf("") }
    var selectedCoinImage by remember { mutableStateOf<Painter?>(null) }
    val coinSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var showSelectCoinItemBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        analysisViewModel.loadCoins()
    }

    val coinList by analysisViewModel.coinList.collectAsStateWithLifecycle()
    Log.d("SelectCoinItem", "coinList $coinList")

    if (showSelectCoinItemBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { coroutineScope.launch { showSelectCoinItemBottomSheet = false } },
            sheetState = coinSheetState,
            containerColor = CoinkiriBackground,
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "코인선택",
                        fontSize = 18.sp
                    )
                },
                modifier = Modifier.height(50.dp),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(CoinkiriBackground),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                showSelectCoinItemBottomSheet = false
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "닫기"
                        )
                    }
                }
            )
            LazyColumn(
                modifier = Modifier
                    .background(CoinkiriBackground)
                    .fillMaxSize(),
            ) {
                items(coinList.size) { index ->
                    val selectCoinList = coinList[index]
                    Log.d("selectCoinList", "selectCoinList $selectCoinList")
                    SelectCoinCard(
                        onclick = { coinName, coinSymbolImage ->
                            selectCoin = coinName
                            selectedCoinImage = coinSymbolImage
                            coroutineScope.launch { showSelectCoinItemBottomSheet = false }
                        },
                        selectCoinList = selectCoinList
                    )
                }
            }
        }
    }



    if (selectCoin.isNotEmpty()) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Card(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                elevation = CardDefaults.cardElevation(3.dp),
                colors = CardDefaults.cardColors(CoinkiriBackground)

            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("코인종목")
                    IconButton(
                        onClick = { coroutineScope.launch { showSelectCoinItemBottomSheet = true } },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_analysis_keyboard_arrow_down),
                            contentDescription = "종목선택"
                        )
                    }
                }
            }
            HorizontalDivider()
            Card(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
                elevation = CardDefaults.cardElevation(3.dp),
                colors = CardDefaults.cardColors(CoinkiriBackground)

            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Card(
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(CoinkiriBackground),
                        border = BorderStroke(1.dp, Color.LightGray),
                        elevation = CardDefaults.cardElevation(5.dp),
                    ) {
                        selectedCoinImage?.let {
                            Image(
                                painter = it,
                                contentScale = ContentScale.Crop,
                                contentDescription = "Coin Image",
                                modifier = Modifier
                                    .size(35.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                    Text(selectCoin)
                }
            }
        }
    }
    else{
        Card(
            modifier = Modifier
                .padding(10.dp)
                .height(50.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(3.dp),
            colors = CardDefaults.cardColors(CoinkiriBackground)

        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("코인종목")
                IconButton(
                    onClick = { coroutineScope.launch { showSelectCoinItemBottomSheet = true } },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_analysis_keyboard_arrow_down),
                        contentDescription = "종목선택"
                    )
                }
            }

        }
    }
}


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TargetPeriod() {
    var targetPeriod by remember { mutableStateOf("") }
    val targetPeriodSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var showTargetPeriodBottomSheet by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    fun calculateTargetDate(period: String): String {
        val currentDate = LocalDate.now()
        val targetDate = when (period) {
            "1개월" -> currentDate.plusMonths(1)
            "3개월" -> currentDate.plusMonths(3)
            "6개월" -> currentDate.plusMonths(6)
            "1년" -> currentDate.plusYears(1)
            "직접입력" -> selectedDate
            else -> currentDate
        }
        return targetDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
    }

    if (showTargetPeriodBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { coroutineScope.launch { showTargetPeriodBottomSheet = false } },
            sheetState = targetPeriodSheetState,
            containerColor = CoinkiriBackground,
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
                                selectedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                                targetPeriod = calculateTargetDate("직접입력")
                                coroutineScope.launch { showTargetPeriodBottomSheet = false }
                            }
                        }
                    },
                    modifier = Modifier.wrapContentSize()
                )
            }
        }
    }

    Log.d("test", targetPeriod)

    Card(
        colors = CardDefaults.cardColors(CoinkiriBackground),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
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
                    text = targetPeriod,
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
                OpinionCard(
                    text = "1개월",
                    isSelected = targetPeriod == "1개월",
                    onClick = { targetPeriod = calculateTargetDate("1개월") },
                    shape = RoundedCornerShape(bottomStart = 10.dp, topStart = 10.dp),
                    modifier = Modifier.weight(1f)
                )
                OpinionCard(
                    text = "3개월",
                    isSelected = targetPeriod == "3개월",
                    onClick = { targetPeriod = calculateTargetDate("3개월") },
                    shape = RectangleShape,
                    modifier = Modifier.weight(1f)
                )
                OpinionCard(
                    text = "6개월",
                    isSelected = targetPeriod == "6개월",
                    onClick = { targetPeriod = calculateTargetDate("6개월") },
                    shape = RectangleShape,
                    modifier = Modifier.weight(1f)
                )
                OpinionCard(
                    text = "1년",
                    isSelected = targetPeriod == "1년",
                    onClick = { targetPeriod = calculateTargetDate("1년") },
                    shape = RectangleShape,
                    modifier = Modifier.weight(1f)
                )
                OpinionCard(
                    text = "직접입력",
                    isSelected = targetPeriod == "직접입력",
                    onClick = {
                        targetPeriod = calculateTargetDate("직접입력")
                        coroutineScope.launch { showTargetPeriodBottomSheet = true }
                    },
                    shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
                    modifier = Modifier.weight(1f)
                )
            }

        }
    }
}


@Preview
@Composable
fun SelectOpinion() {

    var selectedOption by remember { mutableStateOf("") }

    Log.d("test", selectedOption)

    Card(
        colors = CardDefaults.cardColors(CoinkiriBackground),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
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
                Text(
                    text = selectedOption,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "해당 코인의 매력도를 선택해주세요",
                fontSize = 12.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .height(50.dp)
                    .fillMaxWidth()
            ) {
                OpinionCard(
                    text = "매도",
                    isSelected = selectedOption == "매도",
                    onClick = { selectedOption = "매도" },
                    shape = RoundedCornerShape(bottomStart = 10.dp, topStart = 10.dp),
                    modifier = Modifier.weight(1f)
                )
                OpinionCard(
                    text = "중립",
                    isSelected = selectedOption == "중립",
                    onClick = { selectedOption = "중립" },
                    shape = RectangleShape,
                    modifier = Modifier.weight(1f)
                )
                OpinionCard(
                    text = "매수",
                    isSelected = selectedOption == "매수",
                    onClick = { selectedOption = "매수" },
                    shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
fun OpinionCard(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    shape: Shape,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(CoinkiriBackground),
        border = BorderStroke(1.dp, if (isSelected) CoinkiriPointGreen else Color.LightGray),
        shape = shape,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = text)
        }
    }
}
