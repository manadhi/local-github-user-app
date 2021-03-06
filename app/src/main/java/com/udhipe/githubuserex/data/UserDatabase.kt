package com.udhipe.githubuserex.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [User::class], version = 6, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var DBINSTANCE: UserDatabase? =null
        private val DBNAME = "user_database"

//        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL(
//                    "ALTER TABLE user_table ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 1"
//                )
//            }
//        }

//        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("CREATE TABLE new_user_table (" +
//                        "id INTEGER PRIMARY KEY NOT NULL," +
//                        "tag TEXT NOT NULL DEFAULT '')");
//                database.execSQL("INSERT INTO new_Song (id, tag) " +
//                        "SELECT id, tag FROM Song");
//                database.execSQL("DROP TABLE user_table");
//                database.execSQL("ALTER TABLE new_user_table RENAME TO user_table");
//            }
//        }

        fun getDatabase(context: Context): UserDatabase {
            return DBINSTANCE ?: synchronized(this) {
                val dbInstansce = Room.databaseBuilder(context, UserDatabase::class.java, DBNAME)
//                    .addMigrations(MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .build()

                DBINSTANCE = dbInstansce
                return dbInstansce
            }
        }
    }
}