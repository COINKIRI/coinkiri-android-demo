package com.cokiri.coinkiri.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite

@Composable
fun HomeScreen(

) {
    Scaffold(
        topBar = {
            HomeTopBar()
        },
        content = {paddingValues ->
            HomeContent(
                paddingValues
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    TopAppBar(
        title = {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo_coinkrir),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(120.dp)
                )
        },
        colors = TopAppBarDefaults.topAppBarColors(CoinkiriWhite),
        windowInsets = TopAppBarDefaults.windowInsets
    )
}


@Composable
fun HomeContent(
    paddingValues: PaddingValues
) {
    LazyColumn(
        contentPadding = paddingValues
    ) {
        items(100) {
            Text(text = "Item $it")
        }
    }
}