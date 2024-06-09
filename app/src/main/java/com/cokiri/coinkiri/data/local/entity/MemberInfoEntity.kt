package com.cokiri.coinkiri.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "member_info")
data class MemberInfoEntity(
    @PrimaryKey val id: Long,
    val nickname: String,
    val exp: Int,
    val level: Int,
    val mileage: Int,
    val pic: String?,
    val statusMessage: String,
    val followingCount: Int,
    val followerCount: Int
)