package com.udhipe.githubuserex.app

import android.app.Application
import com.udhipe.githubuserex.data.UserDatabase
import com.udhipe.githubuserex.data.UserRepository
import com.udhipe.githubuserex.network.NetworkService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class GithExApplication : Application() {
    private val database by lazy { UserDatabase.getDatabase(this) }
    private val networkService by lazy { NetworkService.getNetworkService() }
    val repository by lazy { UserRepository(database.userDao(), networkService) }
}