package com.udhipe.githubuserex.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    @SerializedName("login")
    val userName: String,

    val name: String,
    val location: String,

    @SerializedName("public_repos")
    val repository: Int,

    val company: String,
    val followers: Int,
    val following: Int,

    @SerializedName("avatar_url")
    val avatar: String
)