package com.cokiri.coinkiri.presentation.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cokiri.coinkiri.ui.component.ModalBottomSheetBtn
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBottomSheet(
    sheetState: SheetState,
    onDismissSheet: () -> Unit,
    onLogoutClick: () -> Unit,
    onModifyClick: () -> Unit
) {
    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismissSheet,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .background(CoinkiriWhite)
                .padding(bottom = 35.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ModalBottomSheetBtn(
                text = "내정보 수정",
                onClick = onModifyClick
            )
            ModalBottomSheetBtn(
                text = "로그아웃",
                onClick = onLogoutClick
            )
        }
    }
}
