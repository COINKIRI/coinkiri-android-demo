package com.cokiri.coinkiri.presentation.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.PretendardFont
import com.cokiri.coinkiri.util.PROFILE
import com.cokiri.coinkiri.util.byteArrayToPainter

@Composable
fun ProfileModifyScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {

    val isLoading by profileViewModel.isLoading.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    val memberInfo by profileViewModel.memberInfo.collectAsStateWithLifecycle()
    val memberName = memberInfo?.nickname
    val memberStatusMessage = memberInfo?.statusMessage
    val memberProfile = memberInfo?.pic?.let { byteArrayToPainter(it) }

    val nickName by profileViewModel.nickName.collectAsStateWithLifecycle()
    val statusMessage by profileViewModel.statusMessage.collectAsStateWithLifecycle()

//    val context = LocalContext.current
//
//    var imageUri by remember { mutableStateOf<Uri?>(null) }
//
//    val imagePickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        imageUri = uri
//    }
//
//    LaunchedEffect(imageUri) {
//        imageUri?.let { uri ->
//            val base64 = loadImageAndConvertToBase64(context, uri)
//            profileViewModel.onProfileImageChanged(base64)
//        }
//    }

    //TODO: 뒤로가기버튼 클릭시 다이얼로그 추가


    Scaffold(
        topBar = {
            ProfileModifyTopBar(
                text = "내정보 수정",
                backClick = {
                    showDialog = true
                }
            )
        },
        content = { paddingValues ->
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if (memberProfile != null) {
                    ProfileModifyContent(
                        memberName = memberName,
                        memberStatusMessage = memberStatusMessage,
                        memberProfile = memberProfile,
                        paddingValues = paddingValues,
                        nickNameValue = nickName,
                        statusMessageValue = statusMessage,
                        onNickNameChange = { newNickName -> profileViewModel.onNickNameChanged(newNickName) },
                        onStatusMessageChange = { newStatusMessage -> profileViewModel.onStatusMessageChanged(newStatusMessage) },
            //                    onProfileImageClick = { imagePickerLauncher.launch("image/*") }
                    )
                }
            }

            if (showDialog) {
                CanselAlertDialog(
                    onDismissRequest = {
                        showDialog = false
                    },
                    confirmClick = {
                        showDialog = false
                        navController.popBackStack()
                    },
                    dismissClick = {
                        showDialog = false
                    }
                )
            }
        },
        bottomBar = {
            ProfileModifyBottomBar(
                saveClick = {
                    profileViewModel.submitMemberInfo()
                    navController.navigate(PROFILE)
                }
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileModifyTopBar(
    text: String,
    backClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text)
        },
        navigationIcon = {
            IconButton(
                onClick = backClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = null
                )
            }
        },
    )
}


@Composable
fun ProfileModifyContent(
    paddingValues: PaddingValues,
    nickNameValue: String,
    statusMessageValue: String,
    onNickNameChange: (String) -> Unit,
    onStatusMessageChange: (String) -> Unit,
    memberName: String?,
    memberStatusMessage: String?,
    memberProfile: BitmapPainter,
//    onProfileImageClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(top = 30.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // 프로필 이미지
        Card(
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(5.dp),
//            onClick = onProfileImageClick
        ) {
            Image(
                painter = memberProfile,
                contentScale = ContentScale.Crop,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(100.dp)
            )
        }
        Spacer(modifier = Modifier.padding(10.dp))
        HorizontalDivider(modifier = Modifier.padding(10.dp))


        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        )
        {
            TextField(
                value = nickNameValue,
                placeholder = { Text(memberName ?: "닉네임을 입력해주세요") },
                onValueChange = onNickNameChange,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            )

            TextField(
                value = statusMessageValue,
                placeholder = { Text(memberStatusMessage ?: "소개글을 입력해주세요") },
                onValueChange = onStatusMessageChange,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            )
        }
    }
}


/**
 * 내정보 수정 하단바
 */
@Composable
fun ProfileModifyBottomBar(
    saveClick: () -> Unit
) {
    BottomAppBar(
        windowInsets = BottomAppBarDefaults.windowInsets
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CoinkiriWhite),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = saveClick,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(CoinkiriPointGreen)
            ) {
                Text("저장하기")
            }
        }
    }
}


/**
 * 내정보 수정 취소 다이얼로그
 */
@Composable
fun CanselAlertDialog(
    onDismissRequest: () -> Unit,
    confirmClick: () -> Unit,
    dismissClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                "내정보 수정을 취소하시겠습니까?",
                fontFamily = PretendardFont,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        },
        confirmButton = {
            TextButton(
                onClick = confirmClick
            ) {
                Text(
                    "수정 취소",
                    fontFamily = PretendardFont,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Red
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = dismissClick
            ) {
                Text("계속 수정")
            }
        }
    )
}
