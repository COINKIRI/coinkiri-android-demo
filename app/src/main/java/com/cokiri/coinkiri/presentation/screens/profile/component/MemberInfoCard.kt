package com.cokiri.coinkiri.presentation.screens.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.data.local.entity.MemberInfoEntity
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.PretendardFont
import com.cokiri.coinkiri.util.FOLLOW
import com.cokiri.coinkiri.util.byteArrayToPainter

@Composable
fun MemberInfoCard(
    navController: NavHostController,
    memberInfo: MemberInfoEntity?
) {


    val memberProfile = byteArrayToPainter(memberInfo?.pic)

    Card(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(5.dp),
        colors = CardDefaults.cardColors(CoinkiriWhite),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(15.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(5.dp),
                ) {
                    Image(
                        painter = memberProfile,
                        contentScale = ContentScale.Crop,
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(85.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        navController.navigate(FOLLOW)
                                    }
                                )
                            }
                    ) {
                        memberInfo?.let {
                            Text(it.followerCount.toString())
                        }
                        Text("팔로워")
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        navController.navigate(FOLLOW)
                                    }
                                )
                            }
                    ) {
                        memberInfo?.let {
                            Text(it.followingCount.toString())
                        }
                        Text("팔로잉")
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                memberInfo?.let {
                    Text(
                        text = "Lv." + it.level.toString(),
                        fontFamily = PretendardFont,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                    )
                    Text(
                        text = it.nickname,
                        fontFamily = PretendardFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                memberInfo?.let {
                    Text(
                        text = it.statusMessage,
                        fontFamily = PretendardFont,
                        fontWeight = FontWeight.Thin,
                        fontSize = 15.sp,
                    )
                }
            }
        }
    }
}


