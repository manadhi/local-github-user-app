package com.udhipe.githubuserex.userfavorite

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.udhipe.githubuserex.R
import com.udhipe.githubuserex.app.GithExApplication
import com.udhipe.githubuserex.databinding.ActivityUserFavoriteBinding
import com.udhipe.githubuserex.sharedadapter.UserAdapter
import com.udhipe.githubuserex.userdetail.UserDetailActivity
import com.udhipe.githubuserex.userlist.UserListActivity

class UserFavoriteActivity : AppCompatActivity() {
    private val viewModel: UserFavoriteViewModel by viewModels {
        UserFavoriteViewModelFactory((application as GithExApplication).repository)
    }

    private lateinit var binding: ActivityUserFavoriteBinding
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.favorite_user)

        binding.rvGithubUser.setHasFixedSize(true)

        setAdapter()

        binding.shimmerScreen.visibility = View.VISIBLE
        viewModel.getFavoriteList().observe(this, Observer {
            userAdapter.setData(it)

            binding.shimmerScreen.stopShimmer()
            binding.shimmerScreen.visibility = View.GONE
            binding.rvGithubUser.visibility = View.VISIBLE
        })

//        viewModel.getFavoriteListCursor().observe(this, Observer {
//            Log.d("CURSSOORR", it.toString())
//        })

//        getAllUser()
    }

//    private fun getAllUser() {
//        GlobalScope.launch(Dispatchers.Main) {
//            val deferredUser = async(Dispatchers.IO) {
//                // CONTENT_URI = content://com.dicoding.picodiploma.mynotesapp/note
//                val cursor = contentResolver.query(UserProvider.CONTENT_URI, null, null, null, null)
//                Log.d("CURSSOORR", cursor.toString())
////                Log.d("CURSSOORR", cursor?.moveToNext().toString())
////                Log.d("CURSSOORR", cursor?.moveToNext().toString())
//
//                cursor?.apply {
//                    while (moveToNext()) {
//                        val username = getString(getColumnIndexOrThrow("userName"))
//                        val name = getString(getColumnIndexOrThrow("name"))
//                        val location = getString(getColumnIndexOrThrow("location"))
//                        val repository = getInt(getColumnIndexOrThrow("repository"))
//                        val company = getString(getColumnIndexOrThrow("company"))
//                        val followers = getInt(getColumnIndexOrThrow("followers"))
//                        val following = getInt(getColumnIndexOrThrow("following"))
//                        val avatar = getString(getColumnIndexOrThrow("avatar"))
//
//                        Log.d("CURSSOORR", username)
//                        Log.d("CURSSOORR", avatar)
//
//
////                        notesList.add(Note(id, title, description, date))
//                    }
//                }
//
//            }
//            val notes = deferredUser.await()
//        }
//    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun setAdapter() {
        val onItemCallBack = object : UserAdapter.OnItemClickCallback {
            override fun onItemClick(userName: String) {
                val intent = Intent(this@UserFavoriteActivity, UserDetailActivity::class.java)
                intent.putExtra(UserListActivity.USERNAME, userName)
                startActivity(intent)
            }
        }

        binding.rvGithubUser.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(onItemCallBack)
        binding.rvGithubUser.adapter = userAdapter
    }
}