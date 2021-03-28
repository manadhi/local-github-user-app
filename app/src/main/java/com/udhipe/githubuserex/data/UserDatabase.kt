package com.udhipe.githubuserex.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 7, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var DBINSTANCE: UserDatabase? =null
        private const val DBNAME = "user_database"

        fun getDatabase(context: Context): UserDatabase {
            return DBINSTANCE ?: synchronized(this) {
                val dbInstansce = Room.databaseBuilder(context, UserDatabase::class.java, DBNAME)
                    .fallbackToDestructiveMigration()
                    .build()

                DBINSTANCE = dbInstansce
                return dbInstansce
            }
        }
    }
}