package com.udhipe.favoriteuserreader.network

import com.udhipe.favoriteuserreader.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {
    fun getNetworkService(): UserService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()

        return retrofit.create(UserService::class.java)
    }
}