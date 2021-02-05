package com.udhipe.githubuserex

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udhipe.githubuserex.network.NetworkService
import com.udhipe.githubuserex.network.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {

    companion object {
        public final val USER_LIST = 1
        public final val FOLLOWER_LIST = 2
        public final val FOLLOWING_LIST = 3
    }

    private val mUserService = NetworkService.getNetworkService()
    private val mUserList = MutableLiveData<ArrayList<User>>()
    private val mFollowerList = MutableLiveData<ArrayList<User>>()
    private val mFollowingList = MutableLiveData<ArrayList<User>>()
    private val mUserDetail = MutableLiveData<User>()
    private val mInfo = MutableLiveData<String>()

    fun setUserList(userName: String) {
        mUserService.getUser(userName).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        mUserList.postValue(response.body()?.items)
                    } else {
                        mInfo.postValue("empty body")
                    }
                } else {
                    mInfo.postValue("not success")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                mInfo.postValue(t.message)
            }

        })
    }

    fun setFollowerList(userName: String) {
        mUserService.getUserFollower(userName).enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        mFollowerList.postValue(response.body())
                    } else {
                        mInfo.postValue("empty body")
                    }
                } else {
                    mInfo.postValue("not success")
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                mInfo.postValue(t.message)
            }
        })
    }

    fun setFollowingList(userName: String) {
        mUserService.getUserFollowing(userName).enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        mFollowingList.postValue(response.body())
                    } else {
                        mInfo.postValue("empty body")
                    }
                } else {
                    mInfo.postValue("not success")
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                mInfo.postValue(t.message)
            }

        })
    }

    fun setUserDetail(userName: String) {
        mUserService.getUserDetail(userName).enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        mUserDetail.postValue(response.body())
                    } else {
                        mInfo.postValue("empty body")
                    }
                } else {
                    mInfo.postValue("not success")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                mInfo.postValue(t.message)
            }

        })
    }

    fun getUserList(category: Int): LiveData<ArrayList<User>>? {
        return when (category) {
            USER_LIST -> mUserList
            FOLLOWER_LIST -> mFollowerList
            FOLLOWING_LIST -> mFollowingList
            else -> null
        }
    }

    fun getUserDetail(): LiveData<User> {
        return mUserDetail
    }

}