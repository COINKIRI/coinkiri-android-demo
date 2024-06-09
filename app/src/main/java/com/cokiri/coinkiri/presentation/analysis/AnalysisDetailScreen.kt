package com.cokiri.coinkiri.presentation.analysis

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.data.remote.model.analysis.AnalysisDetailResponseDto
import com.cokiri.coinkiri.data.remote.model.analysis.Coin
import com.cokiri.coinkiri.presentation.comment.CommentScreen
import com.cokiri.coinkiri.ui.component.detail.DetailAuthorProfile
import com.cokiri.coinkiri.ui.component.detail.DetailBottomAppBar
import com.cokiri.coinkiri.ui.component.detail.DetailContentSection
import com.cokiri.coinkiri.ui.component.detail.DetailTitleSection
import com.cokiri.coinkiri.ui.component.detail.DetailTopAppBar
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.PretendardFont
import com.cokiri.coinkiri.util.byteArrayToPainter
import com.cokiri.coinkiri.viewmodel.LikeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisDetailScreen(
    navController: NavHostController,
    stringPostId: String,
    analysisViewModel: AnalysisViewModel = hiltViewModel(),
    likeViewModel: LikeViewModel = hiltViewModel()
) {

    val postId = stringPostId.toLong()
    var webViewInstance: WebView? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    LaunchedEffect(postId) {
        analysisViewModel.fetchAnalysisDetail(postId)
    }

    // 초기에 좋아요 상태를 확인
    LaunchedEffect(postId) {
        likeViewModel.checkLike(postId) { }
    }

    val isLiked by likeViewModel.isLiked.collectAsState()
    Log.d("AnalysisDetailScreen", "isLiked: $isLiked")

    val analysisDetailResponseDto by analysisViewModel.analysisDetail.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        LaunchedEffect(Unit) {
            sheetState.expand()
        }
    }

    Scaffold(
        topBar = {
            DetailTopAppBar(
                backClick = {
                    webViewInstance?.let { webView ->
                        (webView.parent as? ViewGroup)?.removeView(webView)
                        webView.clearCache(true)
                        webView.destroy()
                        webViewInstance = null
                    }
                    navController.popBackStack()
                }
            )
        },
        content = { paddingValues ->
            AnalysisDetailContent(
                paddingValues = paddingValues,
                analysisDetailResponseDto = analysisDetailResponseDto,
                context = context,
                webView = { webView ->
                    webViewInstance = webView
                }
            )
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { coroutineScope.launch { showBottomSheet = false } },
                    sheetState = sheetState,
                    containerColor = CoinkiriWhite,
                ) {
                    /**
                     * 댓글 작성화면
                     */
                    CommentScreen(
                        closeClick = { coroutineScope.launch { showBottomSheet = false } },
                        postId = postId
                    )
                }
            }
        },
        bottomBar = {
            DetailBottomAppBar(
                clickComment = { coroutineScope.launch { showBottomSheet = true } },
                clickLike = { likeViewModel.toggleLike(postId) },
                isLiked = isLiked,
            )
        }
    )
}




@Composable
fun AnalysisDetailContent(
    paddingValues: PaddingValues,
    analysisDetailResponseDto: AnalysisDetailResponseDto?,
    context: Context,
    webView: (WebView) -> Unit
) {
    when {
        analysisDetailResponseDto != null -> {
            val postDetailResponseDto = analysisDetailResponseDto.postDetailResponseDto
            val coin = analysisDetailResponseDto.coin
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(CoinkiriBackground)
            ) {
                item {
                    DetailTitleSection(postDetailResponseDto)
                    Column(
                        modifier = Modifier
                            .background(CoinkiriWhite),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        CoinCard(coin)
                        OptionCard(analysisDetailResponseDto)
                    }
                    DetailContentSection(postDetailResponseDto, context) {
                        webView(it)
                    }
                    DetailAuthorProfile(postDetailResponseDto)
                }
            }
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CoinkiriWhite),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }


}


@Composable
fun CoinCard(
    coin: Coin
) {

    val coinName = coin.koreanName
    val byteArrayCoinSymbol = coin.symbolImage
    val coinSymbol = byteArrayToPainter(byteArrayCoinSymbol)
    val coinMarket = coin.market

    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(top = 10.dp),
        colors = CardDefaults.cardColors(CoinkiriBackground)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Card(
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(5.dp),
            ) {
                Image(
                    painter = coinSymbol,
                    contentScale = ContentScale.Crop,
                    contentDescription = "Coin Image",
                    modifier = Modifier
                        .size(45.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 15.dp)
            ) {
                Text(
                    coinName,
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    coinMarket,
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .padding(start = 20.dp)
            ) {
                Text(
                    "11,000",
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    "증가율",
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}


@Composable
fun OptionCard(
    analysisDetailResponseDto: AnalysisDetailResponseDto
) {

    val investmentOption = analysisDetailResponseDto.investmentOption           // 투자의견
    val coinPrevClosingPrice = analysisDetailResponseDto.coinPrevClosingPrice   // 글 작성 시점의 종가
    val targetPeriod = analysisDetailResponseDto.targetPeriod                   // 목표기간
    val targetPrice = analysisDetailResponseDto.targetPrice                     // 목표가격


    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(CoinkiriBackground)
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    investmentOption,
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        "분석 목표기간",
                        fontFamily = PretendardFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    Text(
                        "$targetPeriod (123일 남음)",
                        fontFamily = PretendardFont,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

            Row(
                modifier = Modifier
                    .padding(vertical = 3.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "목표 가격",
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
                Text(
                    targetPrice
                )
            }
            Row(
                modifier = Modifier
                    .padding(vertical = 3.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "현재 기대 수익률",
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
                Text("11,111")
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

            Row(
                modifier = Modifier
                    .padding(vertical = 3.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "분석시점 주가",
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
                Text(
                    coinPrevClosingPrice
                )
            }
            Row(
                modifier = Modifier
                    .padding(vertical = 3.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "분석 이후 수익률",
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
                Text("11,111")
            }
        }
    }
}