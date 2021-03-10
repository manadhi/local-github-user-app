package com.udhipe.favoriteuserreader.userfavorite

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.udhipe.favoriteuserreader.R
import com.udhipe.favoriteuserreader.app.FavUserApplication
import com.udhipe.favoriteuserreader.data.User
import com.udhipe.favoriteuserreader.databinding.ActivityUserFavoriteBinding
import com.udhipe.favoriteuserreader.sharedadapter.UserAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserFavoriteActivity : AppCompatActivity() {

    val AUTHORITY = "com.udhipe.githubuserex.provider"
    private val USER_TABLE = "user_table"
    val CONTENT_URI: Uri = Uri.parse("content://" + AUTHORITY + "/" + USER_TABLE)

    companion object {
        const val USERNAME = "userName"
    }

    private val viewModel: UserFavoriteViewModel by viewModels {
        UserFavoriteViewModelFactory((application as FavUserApplication).repository)
    }

    private lateinit var binding: ActivityUserFavoriteBinding
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvGithubUser.visibility = View.GONE
        binding.shimmerScreen.visibility = View.VISIBLE
        binding.shimmerScreen.startShimmer()

//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.favorite_user)

        binding.rvGithubUser.setHasFixedSize(true)

        setAdapter()

//        viewModel.getFavoriteList().observe(this, Observer {
//            userAdapter.setData(it)
//        })

//        viewModel.getFavoriteListCursor().observe(this, Observer {
//            Log.d("CURSSOORR", it.toString())
//        })

//        val favoriteUserList = getAllUser()

//        Log.d("FAVUSSERR", favoriteUserList.toString())
        getAllUser()
    }

    private fun getAllUser() {
        var favoriteUserList = ArrayList<User>()

        GlobalScope.launch(Dispatchers.Main) {
            val deferredUser = async(Dispatchers.IO) {
                // CONTENT_URI = content://com.dicoding.picodiploma.mynotesapp/note
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                Log.d("CURSSOORR", cursor.toString())
//                Log.d("CURSSOORR", cursor?.moveToNext().toString())
//                Log.d("CURSSOORR", cursor?.moveToNext().toString())

                cursor?.apply {
                    while (moveToNext()) {
                        val username = getString(getColumnIndexOrThrow("userName"))
                        val name = getString(getColumnIndexOrThrow("name"))
                        val location = getString(getColumnIndexOrThrow("location"))
                        val repository = getInt(getColumnIndexOrThrow("repository"))
                        val company = getString(getColumnIndexOrThrow("company"))
                        val followers = getInt(getColumnIndexOrThrow("followers"))
                        val following = getInt(getColumnIndexOrThrow("following"))
                        val avatar = getString(getColumnIndexOrThrow("avatar"))
                        val isFavorite = getInt(getColumnIndexOrThrow("isFavorite"))

                        val user = User(username, name, location, repository, company, followers, following, avatar, isFavorite)

                        favoriteUserList.add(user)

                        Log.d("CURSSOORR", username)
                        Log.d("CURSSOORR", avatar)


//                        notesList.add(Note(id, title, description, date))
                    }
                }

            }
            val result = deferredUser.await()
            Log.d("FAVUSSERR", "after get deferredUser = " + favoriteUserList.toString())
            userAdapter.setData(favoriteUserList)

            binding.shimmerScreen.stopShimmer()
            binding.shimmerScreen.visibility = View.GONE
            binding.rvGithubUser.visibility = View.VISIBLE
        }

        Log.d("FAVUSSERR", "after async = " + favoriteUserList.toString())
    }


//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            android.R.id.home -> {
//                finish()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//
//    }

    private fun setAdapter() {
//        val onItemCallBack = object : UserAdapter.OnItemClickCallback {
//            override fun onItemClick(userName: String) {
//                val intent = Intent(this@UserFavoriteActivity, UserDetailActivity::class.java)
//                intent.putExtra(USERNAME, userName)
//                startActivity(intent)
//            }
//        }

        binding.rvGithubUser.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter()
        binding.rvGithubUser.adapter = userAdapter
    }
}