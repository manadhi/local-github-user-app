package com.udhipe.githubuserex.userfavorite

import android.database.Cursor
import android.view.View
import androidx.lifecycle.*
import com.udhipe.githubuserex.data.User
import com.udhipe.githubuserex.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.ArrayList

class UserFavoriteViewModel(
    private val repository: UserRepository
) : ViewModel() {

    companion object {
        const val DATA_EMPTY = "data empty"
        const val DATA_EXIST = "data exist"
    }

    private val mEmptyBody = "empty body"
    private val mNotSuccess = "not success"

    private val mFavoriteList: LiveData<List<User>> = repository.listUser.asLiveData()

    private val favoriteCursor = MutableLiveData<Cursor>()

    /**
     * Launching a new coroutine to manipulate the data in a non-blocking way
     */
//    fun getAllUser() = viewModelScope.launch {
//        favoriteCursor.postValue(repository.getAllUser())
//    }

    fun getFavoriteListCursor(): LiveData<Cursor> {
        return favoriteCursor
    }

    fun addUser(user: User) = viewModelScope.launch {
        repository.addUser(user)
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        repository.deleteUser(user)
    }

    fun deleteAllUser() = viewModelScope.launch {
        repository.deleteAllUser()
    }

    /**
     * ======================================================================
     */

    fun getFavoriteList(): LiveData<List<User>> {
        return mFavoriteList
    }

}

class UserFavoriteViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserFavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserFavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}