package com.cokiri.coinkiri.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.data.local.entity.MemberInfoEntity
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.util.FOLLOW

@Composable
fun MemberInfoCard(
    memberInfo: MemberInfoEntity?,
    navController: NavHostController
) {



    Card(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(10.dp),
        colors = CardDefaults.cardColors(CoinkiriBackground),
        shape = CardDefaults.shape,
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
                verticalAlignment = Alignment.Bottom
            ) {
                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(5.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(50.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 10.dp, bottom = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    if (memberInfo != null) {
                        Text(
                            text = "Lv." + memberInfo.level.toString(),
                        )
                        Text(
                            text = memberInfo.nickname,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(vertical = 15.dp, horizontal = 10.dp),
                shape = CutCornerShape(topEnd = 10.dp),
            ) {
                if (memberInfo != null) {
                    Text(
                        text = memberInfo.statusMessage,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxWidth(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            TextButton(
                onClick = {navController.navigate(FOLLOW)},
                text = "팔로워"
            )
            TextButton(
                onClick = {navController.navigate(FOLLOW)},
                text = "팔로잉"
            )
        }
    }
}



@Composable
fun TextButton(
    onClick: () -> Unit,
    text: String
) {
    TextButton(
        onClick = onClick,
    ) {
        Text(
            text = "$text 0",
        )
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
            "",
            "statusMessage",
            1,
            1,
        ),
        navController = navController
    )
}