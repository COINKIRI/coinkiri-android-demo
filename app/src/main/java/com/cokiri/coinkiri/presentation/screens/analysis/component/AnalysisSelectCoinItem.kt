package com.cokiri.coinkiri.presentation.screens.analysis.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.domain.model.Coin
import com.cokiri.coinkiri.presentation.analysis.AnalysisViewModel
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.PretendardFont
import com.cokiri.coinkiri.util.byteArrayToPainter
import kotlinx.coroutines.launch


/**
 * 코인종목 선택 컴포넌트
 * 분석글 작성시 코인종목을 선택할 수 있는 컴포넌트
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCoinItemContent(
    analysisViewModel: AnalysisViewModel
) {
    val selectedCoinName by analysisViewModel.selectedCoinName.collectAsState()
    val selectedCoinMarketId by analysisViewModel.selectedCoinMarketId.collectAsState()
    val selectedCoinImagePainter by analysisViewModel.selectedCoinImagePainter.collectAsState()
    val selectedCoinPrevClosingPrice by analysisViewModel.selectedCoinPrevClosingPrice.collectAsState()

    val coinSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var showSelectCoinItemBottomSheet by remember { mutableStateOf(false) }

    val coinList by analysisViewModel.coinList.collectAsStateWithLifecycle()
    val singleCoinTicker by analysisViewModel.singleCoinTicker.collectAsStateWithLifecycle()

    // composable이 처음 실행될때 coinList가 비어있으면 fetchCoins()를 호출하여 코인목록을 가져옴
    LaunchedEffect(coinList) {
        if (coinList.isEmpty()) {
            analysisViewModel.fetchCoins()
        }
    }

    // 선택된 코인이 변경되면 해당 코인의 MarketId를 통해 Ticker를 가져옴
    LaunchedEffect(selectedCoinMarketId) {
        if (selectedCoinMarketId.isNotEmpty()) {
            analysisViewModel.observeCoinTicker(selectedCoinMarketId)
        }
    }

    // 선택된 코인의 ticker를 통해 해당 코인의 전일 종가를 가져옴
    LaunchedEffect(singleCoinTicker) {
        singleCoinTicker?.let {
            analysisViewModel.setPrevClosingPrice(it.formattedPrevClosingPrice)
        }
    }

    if (showSelectCoinItemBottomSheet) {
        CoinSelectionBottomSheet(
            coinSheetState = coinSheetState,
            coinList = coinList,
            onCoinSelected = { coinId, coinMarket, coinName, coinSymbolImage ->
                analysisViewModel.setSelectedCoin(coinId, coinMarket, coinName, coinSymbolImage)
                coroutineScope.launch {
                    showSelectCoinItemBottomSheet = false
                }
            },
            onClose = { coroutineScope.launch { showSelectCoinItemBottomSheet = false } }
        )
    }

    CoinSelectionCard(
        selectedCoinName = selectedCoinName,
        selectedCoinImagePainter = selectedCoinImagePainter,
        selectedCoinPrevClosingPrice = selectedCoinPrevClosingPrice,
        onClick = { showSelectCoinItemBottomSheet = true }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinSelectionBottomSheet(
    coinSheetState: SheetState,
    coinList: List<Coin>,
    onCoinSelected: (Long, String, String, Painter) -> Unit,
    onClose: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = coinSheetState,
        containerColor = CoinkiriWhite,
    ) {
        CenterAlignedTopAppBar(
            title = { Text(text = "코인선택", fontSize = 18.sp) },
            modifier = Modifier.height(50.dp),
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(CoinkiriWhite),
            navigationIcon = {
                IconButton(onClick = onClose) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "닫기"
                    )
                }
            }
        )
        LazyColumn(
            modifier = Modifier
                .background(CoinkiriWhite)
                .fillMaxSize(),
        ) {
            items(coinList.size) { index ->
                val coin = coinList[index]
                SelectCoinCard(
                    coin = coin,
                    onClick = { coinId, coinMarket, coinName, coinSymbolImage ->
                        onCoinSelected(coinId, coinMarket, coinName, coinSymbolImage)
                    }
                )
            }
        }
    }
}

@Composable
fun SelectCoinCard(
    coin: Coin,
    onClick: (Long, String, String, Painter) -> Unit
) {
    val coinId = coin.coinId
    val coinMarket = coin.krwMarket
    val coinName = coin.koreanName
    val byteArraySymbolImage = coin.symbolImage
    val coinSymbolImage = byteArrayToPainter(byteArraySymbolImage)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(CoinkiriWhite),
        onClick = {
            onClick(coinId, coinMarket, coinName, coinSymbolImage)
        }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CoinkiriWhite)
                    .padding(horizontal = 5.dp)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(CoinkiriWhite),
                    border = BorderStroke(1.dp, Color.LightGray),
                    elevation = CardDefaults.cardElevation(5.dp),
                ) {
                    Image(
                        painter = coinSymbolImage,
                        contentScale = ContentScale.Crop,
                        contentDescription = "coin Image",
                        modifier = Modifier.size(35.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                Text(
                    text = coinName,
                    fontSize = 18.sp,
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Bold
                )
            }
            HorizontalDivider()
        }
    }
}

@Composable
fun CoinSelectionCard(
    selectedCoinName: String,
    selectedCoinImagePainter: Painter?,
    selectedCoinPrevClosingPrice: String,
    onClick: () -> Unit
) {
    if (selectedCoinName.isNotEmpty()) {
        CoinDetailsCard(
            selectedCoinName = selectedCoinName,
            selectedCoinImagePainter = selectedCoinImagePainter,
            selectedCoinPrevClosingPrice = selectedCoinPrevClosingPrice,
            onClick = onClick
        )
    } else {
        SelectionPromptCard(onClick = onClick)
    }
}

@Composable
fun SelectionPromptCard(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(CoinkiriWhite)
    ) {
        Row(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "코인종목 선택")
            Icon(
                painter = painterResource(R.drawable.ic_analysis_keyboard_arrow_down),
                contentDescription = "종목선택"
            )
        }
    }
}

@Composable
fun CoinDetailsCard(
    selectedCoinName: String,
    selectedCoinImagePainter: Painter?,
    selectedCoinPrevClosingPrice: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        SelectionHeader(
            onClick = onClick,
            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
        )
        HorizontalDivider()
        SelectionDetails(
            selectedCoinImagePainter = selectedCoinImagePainter,
            selectedCoinName = selectedCoinName,
            selectedCoinPrevClosingPrice = selectedCoinPrevClosingPrice
        )
    }
}

@Composable
fun SelectionHeader(
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(10.dp)
) {
    Card(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = shape,
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(CoinkiriWhite)
    ) {
        Row(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "코인종목")
            Icon(
                painter = painterResource(R.drawable.ic_analysis_keyboard_arrow_down),
                contentDescription = "종목선택"
            )
        }
    }
}

@Composable
fun SelectionDetails(
    selectedCoinImagePainter: Painter?,
    selectedCoinName: String,
    selectedCoinPrevClosingPrice: String
) {
    Card(
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(CoinkiriWhite)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(CoinkiriWhite),
                    border = BorderStroke(1.dp, Color.LightGray),
                    elevation = CardDefaults.cardElevation(5.dp),
                ) {
                    selectedCoinImagePainter?.let {
                        Image(
                            painter = it,
                            contentScale = ContentScale.Crop,
                            contentDescription = "Coin Image",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                Text(selectedCoinName)
            }
            Text(
                text = "₩ $selectedCoinPrevClosingPrice",
                fontSize = 18.sp,
                fontFamily = PretendardFont,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
