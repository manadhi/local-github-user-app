package com.udhipe.githubuserex.data

import androidx.annotation.WorkerThread
import com.udhipe.githubuserex.network.NetworkService
import com.udhipe.githubuserex.network.UserService
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao, private val networkservice: UserService) {
    val listUser: Flow<List<User>> = userDao.getUserListAscending()

    @WorkerThread
    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }

    @WorkerThread
    suspend fun deleteUser(user: User) {
        userDao.deleteOneUser(user)
    }

    @WorkerThread
    suspend fun deleteAllUser() {
        userDao.deleteAllUser()
    }

    @WorkerThread
    suspend fun getOneUser(userName: String): User? {
        return userDao.getUserByUserName(userName)
    }
}