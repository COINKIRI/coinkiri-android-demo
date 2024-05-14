package com.cokiri.coinkiri.ui.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.BottomNavigation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen

@Composable
fun BottomNavBar(
    navController: NavController,
    tabs: List<BottomNavItem>
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    BottomNavigation(
        backgroundColor = CoinkiriBackground
    ) {

        tabs.forEach { tab ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = tab.icon),
                        contentDescription = stringResource(id = tab.title),
                        modifier = Modifier
                            .width(25.dp)
                            .height(25.dp)
                    )
                },
                label = {
                    Text(
                        stringResource(id = tab.title),
                        fontSize = 9.sp
                    )
                },
                selected = currentRoute == tab.route,
                onClick = {
                    navController.navigate(tab.route) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                alwaysShowLabel = false
            )
        }
    }
}