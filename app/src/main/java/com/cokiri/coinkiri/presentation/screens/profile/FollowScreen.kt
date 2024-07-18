package com.cokiri.coinkiri.presentation.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.PretendardFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowScreen(
    navController: NavHostController,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("팔로워", "팔로잉")

    Column {
        TopAppBar(
            title = { Text("팔로워 / 팔로잉") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "뒤로가기"
                    )
                }
            }
        )

        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> FollowersList()
            1 -> FollowingList()
        }
    }
}

@Composable
fun FollowersList() {
    LazyColumn {
        items(2) {
            FollowItem()
        }
    }
}

@Composable
fun FollowingList() {
    LazyColumn {
        items(1) {
            FollowItem()
        }
    }
}


@Preview
@Composable
fun FollowItem(
    profileImage: Painter = painterResource(id = R.drawable.ic_launcher_foreground)

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 3.dp),
        colors = CardDefaults.cardColors(CoinkiriWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(5.dp),
                ) {
                    Image(
                        painter = profileImage,
                        contentScale = ContentScale.Crop,
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(start = 5.dp, bottom = 3.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {

                    Text(
                        text = "Lv. 1",
                        fontFamily = PretendardFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )

                    Text(
                        text = "이름",
                        fontFamily = PretendardFont,
                        fontSize = 15.sp
                    )
                }
            }
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(CoinkiriPointGreen),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(10.dp)
            ) {
                Text("팔로우")
            }
        }
    }
}