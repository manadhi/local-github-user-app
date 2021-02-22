package com.udhipe.githubuserex.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
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
}