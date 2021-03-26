package com.udhipe.githubuserex.userdetail

import androidx.lifecycle.*
import com.udhipe.githubuserex.data.User
import com.udhipe.githubuserex.data.UserRepository
import kotlinx.coroutines.launch

class UserDetailViewModel(private val repository: UserRepository) : ViewModel() {

    companion object {
        const val FOLLOWER_LIST = 2
        const val FOLLOWING_LIST = 3
        const val USER_DETAIL = 4
        const val DATA_EMPTY = "data empty"
        const val DATA_EXIST = "data exist"
    }

    private val mEmptyBody = "empty body"

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
        user.isFavorite = 1
        mIsFavorite.postValue(true)
        repository.addUser(user)
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        user.isFavorite = 0
        mIsFavorite.postValue(false)
        repository.deleteUser(user)
    }

    fun setUserDetail(userName: String) = viewModelScope.launch {
        repository.getUserDetailByUsername(userName, object : UserRepository.Listener<User> {
            override fun onSuccess(data: User, message: String) {
                mUserDetail.postValue(data)

                if (data.isFavorite == 0) {
                    mIsFavorite.postValue(false)
                } else {
                    mIsFavorite.postValue(true)
                }

                mUserDetailInfo.postValue(DATA_EXIST)
            }

            override fun onError(message: String) {
                if (message.equals(mEmptyBody, true)) {
                    mUserDetailInfo.postValue(DATA_EMPTY)
                } else {
                    mUserDetailInfo.postValue(message)
                }
            }

        })
    }

    /** --------------------------------------------------------------------- */

    fun isFavorite(): LiveData<Boolean> {
        return mIsFavorite
    }

    fun setFollowerList(userName: String) {
        repository.getFollowerListByUsername(
            userName,
            object : UserRepository.Listener<ArrayList<User>> {
                override fun onSuccess(data: ArrayList<User>, message: String) {
                    mFollowerList.postValue(data)
                    mFollowerInfo.postValue(DATA_EXIST)
                }

                override fun onError(message: String) {
                    if (message.equals("empty data", true)) {
                        mFollowerInfo.postValue(DATA_EMPTY)
                    } else {
                        mFollowerInfo.postValue(message)
                    }
                }
            })
    }

    fun setFollowingList(userName: String) {
        repository.getFollowingListByUsername(
            userName,
            object : UserRepository.Listener<ArrayList<User>> {
                override fun onSuccess(data: ArrayList<User>, message: String) {
                    mFollowingList.postValue(data)
                    mFollowingInfo.postValue(DATA_EXIST)
                }

                override fun onError(message: String) {
                    if (message.equals("empty data", true)) {
                        mFollowingInfo.postValue(DATA_EMPTY)
                    } else {
                        mFollowingInfo.postValue(message)
                    }
                }

            })
    }

    fun getUserList(category: Int): LiveData<ArrayList<User>>? {
        return when (category) {
            FOLLOWER_LIST -> mFollowerList
            FOLLOWING_LIST -> mFollowingList
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
            USER_DETAIL -> mUserDetailInfo
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