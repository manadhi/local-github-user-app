package com.udhipe.githubuserex.data

import android.database.Cursor
import androidx.annotation.WorkerThread
import com.udhipe.githubuserex.network.UserService
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val userDao: UserDao, private val networkservice: UserService) {
    val listUser: Flow<List<User>> = userDao.getUserListAscending()


    interface Listener<T> {
        fun onSuccess(data: T, message: String)
        fun onError(message: String)
    }

    // web service
    lateinit var listUserByUsername: ArrayList<User>
    lateinit var listUserInfo: String

    private val mEmptyBody = "empty body"
    private val mNotSuccess = "not success"

    // test content provider
    @WorkerThread
    suspend fun getAllUser(): Cursor {
        return userDao.getAllList()
    }

//    val listUserForWidget: List<User> = userDao.getUserListAscendingWidget()

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

    // get data from web service
    fun getUserbyUsername(userName: String, listener: Listener<ArrayList<User>>) {
        networkservice.getUser(userName).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val userList = response.body()?.items

                        if (userList == null || userList.size == 0) {
//                            mUserListInfo.postValue(UserViewModel.DATA_EMPTY)
//                            listUserInfo = "empty data"
                            listener.onError("empty data")
                        } else {
//                            listUserInfo = "success"
//                            listUserByUsername = userList
                            listener.onSuccess(userList, "success")
//                            mUserList.postValue(userList!!)
//                            mUserListInfo.postValue(UserViewModel.DATA_EXIST)
                        }
                    } else {
//                        listUserInfo = "empty body"
                        listener.onError("empty body")
//                        mUserListInfo.postValue(mEmptyBody)
                    }
                } else {
                    listener.onError("not success")
//                    listUserInfo = "not success"
//                    mUserListInfo.postValue(mNotSuccess)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
//                listUserInfo = t.message.toString()
                t.message?.let { listener.onError(it) }
//                mUserListInfo.postValue(t.message)
            }

        })
    }

}