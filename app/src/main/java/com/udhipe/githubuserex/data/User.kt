package com.udhipe.githubuserex.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    @SerializedName("login")
    val userName: String,

    @ColumnInfo(defaultValue = "")
    val name: String?,

    @ColumnInfo(defaultValue = "")
    val location: String?,

    @SerializedName("public_repos")
    val repository: Int,

    @ColumnInfo(defaultValue = "")
    val company: String?,

    val followers: Int,

    val following: Int,

    @SerializedName("avatar_url")
    val avatar: String,

    // for decide this user is favorite user or not
    // default value is false = not favorite user
    var isFavorite: Int
)