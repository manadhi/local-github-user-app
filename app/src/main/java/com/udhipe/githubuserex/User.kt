package com.udhipe.githubuserex

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login") val userName: String,
    val name: String,
    val location: String,
    @SerializedName("public_repos") val repository: Int,
    val company: String,
    val followers: Int,
    val following: Int,
    @SerializedName("avatar_url") val avatar: String
)