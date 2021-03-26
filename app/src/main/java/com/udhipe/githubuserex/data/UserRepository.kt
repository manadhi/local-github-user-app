package com.udhipe.githubuserex.data

import androidx.annotation.WorkerThread
import com.udhipe.githubuserex.network.UserService
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val userDao: UserDao, private val networkService: UserService) {
    val listUser: Flow<List<User>> = userDao.getUserListAscending()


    interface Listener<T> {
        fun onSuccess(data: T, message: String)
        fun onError(message: String)
    }

    private val mSuccess = "success"
    private val mEmptyData = "empty data"
    private val mEmptyBody = "empty body"
    private val mNotSuccess = "not success"

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
        networkService.getUser(userName).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val userList = response.body()?.items

                        if (userList == null || userList.size == 0) {
                            listener.onError(mEmptyData)
                        } else {
                            listener.onSuccess(userList, mSuccess)
                        }
                    } else {
                        listener.onError(mEmptyBody)
                    }
                } else {
                    listener.onError(mNotSuccess)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t.message?.let { listener.onError(it) }
            }

        })
    }

    suspend fun getUserDetailByUsername(userName: String, listener: Listener<User>) {
        val user: User? = getOneUser(userName)

        if (user != null) {
            listener.onSuccess(user, mSuccess)
        } else {
            getUserDetailRemote(userName, listener)
        }
    }

    private fun getUserDetailRemote(userName: String, listener: Listener<User>) {
        lateinit var user: User

        networkService.getUserDetail(userName).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        user = response.body()!!
                        listener.onSuccess(user, mSuccess)
                    } else {
                        listener.onError(mEmptyBody)
                    }
                } else {
                    listener.onError(mNotSuccess)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                listener.onError(t.message.toString())
            }

        })
    }

    fun getFollowerListByUsername(userName: String, listener: Listener<ArrayList<User>>) {
        networkService.getUserFollower(userName).enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val followerList = response.body()

                        listener.onSuccess(followerList!!, mSuccess)

                        if (followerList.size == 0) {
                            listener.onError(mEmptyData)
                        }

                    } else {
                        listener.onError(mEmptyBody)
                    }
                } else {
                    listener.onError(mNotSuccess)
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                listener.onError(t.message.toString())
            }
        })
    }

    fun getFollowingListByUsername(userName: String, listener: Listener<ArrayList<User>>) {
        networkService.getUserFollowing(userName).enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val followingList = response.body()
                        listener.onSuccess(followingList!!, mSuccess)

                        if (followingList.size == 0) {
                            listener.onError(mEmptyData)
                        }

                    } else {
                        listener.onError(mEmptyBody)
                    }
                } else {
                    listener.onError(mNotSuccess)
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                listener.onError(t.message.toString())
            }

        })
    }


}