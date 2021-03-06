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

    suspend fun getUserDetailByUsername(userName: String, listener: Listener<User>) {
        val user: User? = getOneUser(userName)

        if (user != null) {
            listener.onSuccess(user, "success")
//            mUserDetail.postValue(finalUser)
//            mIsFavorite.postValue(true)
//            mUserDetailInfo.postValue(UserDetailViewModel.DATA_EXIST)
        } else {
//            mIsFavorite.postValue(false)
            getUserDetailRemote(userName, listener)
//            listener.onSuccess(user, "success")
        }
    }

    private fun getUserDetailRemote(userName: String, listener: Listener<User>) {
        lateinit var user: User

        networkservice.getUserDetail(userName).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        user = response.body()!!
                        listener.onSuccess(user, "success")
//                            mUserDetail.postValue(response.body())
//                            mUserDetailInfo.postValue(UserDetailViewModel.DATA_EXIST)
                    } else {
                        listener.onError("empty body")
//                            mUserDetailInfo.postValue(UserDetailViewModel.DATA_EMPTY)
                    }
                } else {
                    listener.onError("not successful")
//                        mUserDetailInfo.postValue(mNotSuccess)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                listener.onError(t.message.toString())
//                    mUserDetailInfo.postValue(t.message)
            }

        })
    }


}