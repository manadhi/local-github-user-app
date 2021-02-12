package com.udhipe.githubuserex.network

import com.udhipe.githubuserex.BuildConfig
import com.udhipe.githubuserex.data.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.ACCESS_TOKEN}")
    fun getUser(@Query("q") username: String): Call<UserResponse>

    @GET("users/{userName}")
    @Headers("Authorization: token ${BuildConfig.ACCESS_TOKEN}")
    fun getUserDetail(@Path("userName") userName: String): Call<User>

    @GET("users/{userName}/followers")
    @Headers("Authorization: token ${BuildConfig.ACCESS_TOKEN}")
    fun getUserFollower(@Path("userName") userName: String): Call<ArrayList<User>>

    @GET("users/{userName}/following")
    @Headers("Authorization: token ${BuildConfig.ACCESS_TOKEN}")
    fun getUserFollowing(@Path("userName") userName: String): Call<ArrayList<User>>
}