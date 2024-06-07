package com.cokiri.coinkiri.presentation.profile.component

import android.util.Log
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cokiri.coinkiri.R
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


    //val memberProfile = memberInfo?.pic?.let { byteArrayToPainter(it) }

    Card(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(10.dp),
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
//                        painter = memberProfile,
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
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


@Preview
@Composable
fun MemberInfoCardPreview() {
    val navController = rememberNavController() // 임시 NavController 생성
    MemberInfoCard(
        memberInfo = MemberInfoEntity(
            1,
            "nickname",
            1,
            1,
            1,
            "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",
            "statusMessage",
            1,
            1,
        ),
        navController = navController
    )
}





