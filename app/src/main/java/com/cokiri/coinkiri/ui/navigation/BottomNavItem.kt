package com.cokiri.coinkiri.ui.navigation

import com.cokiri.coinkiri.R

sealed class BottomNavItem(
    val title : Int,
    val icon : Int,
    val route : String
){
    object Analysis : BottomNavItem(
        title = R.string.analysis,
        icon = R.drawable.ic_navi_analysis,
        route = ANALYSIS
    )
    object Post : BottomNavItem(
        title = R.string.post,
        icon = R.drawable.ic_navi_post,
        route = POST
    )

    object Home : BottomNavItem(
        title = R.string.home,
        icon = R.drawable.ic_navi_home,
        route = HOME
    )

    object Price : BottomNavItem(
        title = R.string.price,
        icon = R.drawable.ic_navi_price,
        route = PRICE
    )

    object Profile : BottomNavItem(
        title = R.string.profile,
        icon = R.drawable.ic_navi_profile,
        route = PROFILE
    )

}