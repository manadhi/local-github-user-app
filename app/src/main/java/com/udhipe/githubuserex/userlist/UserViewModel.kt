package com.udhipe.githubuserex.userlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udhipe.githubuserex.data.User
import com.udhipe.githubuserex.data.UserRepository
import com.udhipe.githubuserex.network.NetworkService

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    companion object {
        const val USER_LIST = 1
        const val DATA_EMPTY = "data empty"
        const val DATA_EXIST = "data exist"
    }

    private val mEmptyBody = "empty body"
    private val mNotSuccess = "not success"

    private val mUserService = NetworkService.getNetworkService()
    private val mUserList = MutableLiveData<ArrayList<User>>()
    private val mUserListInfo = MutableLiveData<String>()
    private val mUserDetailInfo = MutableLiveData<String>()
    private val mKeyword = MutableLiveData<String>()

    fun setKeyword(keyword: String) {
        mKeyword.postValue(keyword)
    }

    fun getKeyWord(): LiveData<String> {
        return mKeyword
    }

    fun setUserList(userName: String) {
        repository.getUserbyUsername(userName, object : UserRepository.Listener<ArrayList<User>> {
            override fun onSuccess(data: ArrayList<User>, message: String) {
                mUserList.postValue(data)
                mUserListInfo.postValue(DATA_EXIST)
            }

            override fun onError(message: String) {
                if (message.equals("empty data", true)) {
                    mUserListInfo.postValue(DATA_EMPTY)
                } else {
                    mUserListInfo.postValue(message)
                }
            }

        })



/*        mUserService.getUser(userName).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val userList = response.body()?.items

                        if (userList == null || userList.size == 0) {
                            mUserListInfo.postValue(DATA_EMPTY)
                        } else {
                            mUserList.postValue(userList!!)
                            mUserListInfo.postValue(DATA_EXIST)
                        }
                    } else {
                        mUserListInfo.postValue(mEmptyBody)
                    }
                } else {
                    mUserListInfo.postValue(mNotSuccess)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                mUserListInfo.postValue(t.message)
            }

        })*/
    }

    fun getUserList(category: Int): LiveData<ArrayList<User>>? {
        return when (category) {
            USER_LIST -> mUserList
            else -> null
        }
    }

    fun getInfo(category: Int): LiveData<String> {
        return when (category) {
            USER_LIST -> mUserListInfo
            else -> mUserDetailInfo
        }
    }

    class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}