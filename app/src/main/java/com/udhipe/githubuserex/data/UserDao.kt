package com.udhipe.githubuserex.data

import android.database.Cursor
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table ORDER BY userName ASC")
    fun getAllList(): Cursor

    @Query("SELECT * FROM user_table WHERE userName= :specificUserName")
    fun getUserById(specificUserName: String): Cursor

    @Query("SELECT * FROM user_table ORDER BY userName ASC")
    fun getUserListAscending(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Delete
    suspend fun deleteOneUser(user: User)

    @Query("SELECT * FROM user_table WHERE userName= :specificUserName")
    suspend fun getUserByUserName(specificUserName: String): User?
}