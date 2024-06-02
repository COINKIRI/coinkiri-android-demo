package com.cokiri.coinkiri.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun FloatingActionMenu(
    isMenuExpanded: Boolean,
    onMenuToggle: () -> Unit,
    menuItems: List<Triple<String, ImageVector, () -> Unit>> = emptyList()
) {

    if (!isMenuExpanded) {
        FloatingActionButton(
            onClick = onMenuToggle,
            elevation = FloatingActionButtonDefaults.elevation(5.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add"
            )
        }
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.End,
        ) {
            menuItems.forEach { item ->
                FloatingActionButtonWithLabel(
                    label = item.first,
                    icon = item.second,
                    onClick = item.third
                )
            }
            FloatingActionButton(
                onClick = onMenuToggle
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        }
    }
}

@Composable
fun FloatingActionButtonWithLabel(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            fontSize = 10.sp
        )
        FloatingActionButton(
            onClick = onClick
        ) {
            Icon(
                icon,
                contentDescription = label
            )
        }
    }
}