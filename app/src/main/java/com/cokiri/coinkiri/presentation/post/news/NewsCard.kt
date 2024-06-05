package com.cokiri.coinkiri.presentation.post.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cokiri.coinkiri.data.remote.model.NewsList
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.PretendardFont

@Composable
fun NewsCard(
    newsList: NewsList,
    newsCardClick: () -> Unit
) {

    val newsTitle = newsList.title
    val newsDescription = newsList.description
    val newsDate = newsList.pubDate

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        colors = CardDefaults.cardColors(CoinkiriWhite),
        onClick = newsCardClick
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            NewsTitle(newsTitle)
            NewsDescription(newsDescription)
            NewDate(newsDate)
        }
    }
}

@Composable
fun NewsTitle(newsTitle: String) {
    Text(
        text = newsTitle,
        fontFamily = PretendardFont,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
}

@Composable
fun NewsDescription(newsDescription: String) {
    Text(
        text = newsDescription,
        fontFamily = PretendardFont,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun NewDate(newsDate: String) {
    Text(
        text = newsDate,
        fontFamily = PretendardFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    )
}