package com.udhipe.githubuserex.userdetail

import androidx.lifecycle.*
import com.udhipe.githubuserex.data.User
import com.udhipe.githubuserex.data.UserRepository
import com.udhipe.githubuserex.network.NetworkService
import com.udhipe.githubuserex.userfavorite.UserFavoriteViewModel
import com.udhipe.githubuserex.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(private val repository: UserRepository) : ViewModel() {

    companion object {
        const val FOLLOWER_LIST = 2
        const val FOLLOWING_LIST = 3
        const val USER_DETAIL = 4
        const val DATA_EMPTY = "data empty"
        const val DATA_EXIST = "data exist"
    }

    private val mEmptyBody = "empty body"
    private val mNotSuccess = "not success"

    private val mUserService = NetworkService.getNetworkService()
    private val mFollowerList = MutableLiveData<ArrayList<User>>()
    private val mFollowingList = MutableLiveData<ArrayList<User>>()
    private val mUserDetail = MutableLiveData<User>()
    private val mUserDetailInfo = MutableLiveData<String>()
    private val mFollowerInfo = MutableLiveData<String>()
    private val mFollowingInfo = MutableLiveData<String>()

    private val mIsFavorite = MutableLiveData<Boolean>()

    /**
     * Launching a new coroutine to manipulate the data in a non-blocking way
     */

    fun addUser(user: User) = viewModelScope.launch {
        mIsFavorite.postValue(true)
        repository.addUser(user)
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        mIsFavorite.postValue(false)
        repository.deleteUser(user)
    }

    fun setUserDetail(userName: String) = viewModelScope.launch {
        val user: User? = repository.getOneUser(userName)

        if (user != null) {
            val finalUser: User = user
            mUserDetail.postValue(finalUser)
            mIsFavorite.postValue(true)
            mUserDetailInfo.postValue(DATA_EXIST)
        } else {
            mIsFavorite.postValue(false)
            mUserService.getUserDetail(userName).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            mUserDetail.postValue(response.body())
                            mUserDetailInfo.postValue(DATA_EXIST)
                        } else {
                            mUserDetailInfo.postValue(DATA_EMPTY)
                        }
                    } else {
                        mUserDetailInfo.postValue(mNotSuccess)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    mUserDetailInfo.postValue(t.message)
                }

            })
        }
    }

    /** --------------------------------------------------------------------- */

//    fun setFavorite(state: Boolean) {
//        mIsFavorite.postValue(state)
//    }

    fun isFavorite(): LiveData<Boolean> {
        return mIsFavorite
    }

    fun setFollowerList(userName: String) {
        mUserService.getUserFollower(userName).enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val followerList = response.body()
                        mFollowerList.postValue(followerList!!)

                        if (followerList.size == 0) {
                            mFollowerInfo.postValue(DATA_EMPTY)
                        } else {
                            mFollowerInfo.postValue(DATA_EXIST)
                        }
                    } else {
                        mFollowerInfo.postValue(mEmptyBody)
                    }
                } else {
                    mFollowerInfo.postValue(mNotSuccess)
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                mFollowerInfo.postValue(t.message)
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
                        val followingList = response.body()
                        mFollowingList.postValue(followingList!!)

                        if (followingList.size == 0) {
                            mFollowingInfo.postValue(DATA_EMPTY)
                        } else {
                            mFollowingInfo.postValue(DATA_EXIST)
                        }

                    } else {
                        mFollowingInfo.postValue(mEmptyBody)
                    }
                } else {
                    mFollowingInfo.postValue(mNotSuccess)
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                mFollowingInfo.postValue(t.message)
            }

        })
    }

    fun getUserList(category: Int): LiveData<ArrayList<User>>? {
        return when (category) {
            UserViewModel.FOLLOWER_LIST -> mFollowerList
            UserViewModel.FOLLOWING_LIST -> mFollowingList
            else -> null
        }
    }


    fun getUserDetail(): LiveData<User> {
        return mUserDetail
    }

    fun getInfo(category: Int): LiveData<String> {
        return when (category) {
            FOLLOWER_LIST -> mFollowerInfo
            FOLLOWING_LIST -> mFollowingInfo
            else -> mUserDetailInfo
        }
    }
}

class UserDetailViewModelFactory(private val repository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}