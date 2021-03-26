package com.udhipe.githubuserex.userfavorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.udhipe.githubuserex.data.User
import com.udhipe.githubuserex.data.UserRepository

class UserFavoriteViewModel(
    repository: UserRepository
) : ViewModel() {

    private val mFavoriteList: LiveData<List<User>> = repository.listUser.asLiveData()

    fun getFavoriteList(): LiveData<List<User>> {
        return mFavoriteList
    }

}

class UserFavoriteViewModelFactory(private val repository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserFavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserFavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}