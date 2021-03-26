package com.udhipe.githubuserex.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import com.udhipe.githubuserex.data.UserDao
import com.udhipe.githubuserex.data.UserDatabase

class UserProvider : ContentProvider() {

    companion object {
        // Defines the database name
        private const val DBNAME = "user_database"

        private const val AUTHORITY = "com.udhipe.githubuserex.provider"
        private const val USER_TABLE = "user_table"

        private const val USER = 1
        private const val USER_ID = 2

        private val mURIMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            mURIMatcher.addURI(AUTHORITY, USER_TABLE, USER)
            mURIMatcher.addURI(AUTHORITY, "$USER_TABLE/#", USER_ID)
        }
    }

    // Defines a handle to the Room database
    private lateinit var userDatabase: UserDatabase

    // Defines a Data Access Object to perform the database operations
    private var userDao: UserDao? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Implement this to handle requests to insert a new row.")
    }

    override fun onCreate(): Boolean {
        // Creates a new database object.
        userDatabase = Room.databaseBuilder(context!!, UserDatabase::class.java, DBNAME).build()

        // Gets a Data Access Object to perform the database operations
        userDao = userDatabase.userDao()

        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (mURIMatcher.match(uri)) {
            USER -> userDao?.getAllList()
            USER_ID -> userDao?.getUserById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}