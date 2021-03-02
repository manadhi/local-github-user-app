package com.udhipe.favoriteuserreader.app

import android.app.Application
import com.udhipe.favoriteuserreader.data.UserRepository
import com.udhipe.favoriteuserreader.network.NetworkService

class FavUserApplication : Application() {
//    private val database by lazy { UserDatabase.getDatabase(this) }
    private val networkService by lazy { NetworkService.getNetworkService() }
    val repository by lazy {UserRepository(networkService)}
}