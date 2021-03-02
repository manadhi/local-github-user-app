package com.udhipe.favoriteuserreader.data

import com.udhipe.favoriteuserreader.network.UserService

class UserRepository(private val networkservice: UserService) {
//    val listUser: Flow<List<User>> = userDao.getUserListAscending()

    // test content provider
//    @WorkerThread
//    suspend fun getAllUser(): Cursor {
//        return userDao.getAllList()
//    }
//
////    val listUserForWidget: List<User> = userDao.getUserListAscendingWidget()
//
//    @WorkerThread
//    suspend fun addUser(user: User) {
//        userDao.addUser(user)
//    }
//
//    @WorkerThread
//    suspend fun deleteUser(user: User) {
//        userDao.deleteOneUser(user)
//    }
//
//    @WorkerThread
//    suspend fun deleteAllUser() {
//        userDao.deleteAllUser()
//    }
//
//    @WorkerThread
//    suspend fun getOneUser(userName: String): User? {
//        return userDao.getUserByUserName(userName)
//    }
}