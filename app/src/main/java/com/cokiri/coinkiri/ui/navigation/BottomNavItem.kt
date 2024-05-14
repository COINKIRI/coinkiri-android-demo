package com.cokiri.coinkiri.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.util.ANALYSIS
import com.cokiri.coinkiri.util.HOME
import com.cokiri.coinkiri.util.POST
import com.cokiri.coinkiri.util.PRICE
import com.cokiri.coinkiri.util.PROFILE

sealed class BottomNavItem(
    @StringRes val title : Int,
    @DrawableRes val icon : Int,
    val route : String
){
    object Analysis : BottomNavItem(R.string.analysis, R.drawable.ic_navi_analysis, ANALYSIS)
    object Post : BottomNavItem(R.string.post, R.drawable.ic_navi_post, POST)
    object Home : BottomNavItem(R.string.home, R.drawable.ic_navi_home, HOME)
    object Price : BottomNavItem(R.string.price, R.drawable.ic_navi_price, PRICE)
    object Profile : BottomNavItem(R.string.profile, R.drawable.ic_navi_profile, PROFILE)

}