package com.udhipe.githubuserex.network

import com.google.gson.annotations.SerializedName
import com.udhipe.githubuserex.data.User

data class UserResponse(
    @SerializedName("total_count") val totalCount: Int,
    val items: ArrayList<User>
)