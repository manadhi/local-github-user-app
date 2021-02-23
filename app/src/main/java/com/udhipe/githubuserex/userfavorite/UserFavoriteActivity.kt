package com.udhipe.githubuserex.userfavorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.udhipe.githubuserex.R
import com.udhipe.githubuserex.app.GithExApplication
import com.udhipe.githubuserex.data.User
import com.udhipe.githubuserex.databinding.ActivityUserFavoriteBinding
import com.udhipe.githubuserex.sharedadapter.UserAdapter
import com.udhipe.githubuserex.userdetail.UserDetailActivity
import com.udhipe.githubuserex.userlist.UserListActivity

class UserFavoriteActivity : AppCompatActivity() {
    private val viewModel: UserFavoriteViewModel by viewModels {
        UserViewModelFactory((application as GithExApplication).repository)
    }

    private lateinit var binding: ActivityUserFavoriteBinding
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.favorite_user)

        val user = User("JakaYoYo", "Jaka Y.", "Jakarta", 2, "-", 10, 50, "")
        val user2 = User("Pakdhe", "Kuncoro", "Jawa Tengah", 5, "-", 1, 10, "")
        val user3 = User("Thunder", "Kuncoro", "Jawa Tengah", 5, "-", 1, 10, "")

//        userViewModel.addUser(user)
//        userViewModel.addUser(user2)
//        userViewModel.addUser(user3)

        binding.rvGithubUser.setHasFixedSize(true)

        setAdapter()

        viewModel.getFavoriteList().observe(this, Observer {
            userAdapter.setData(it)
        })


//        userViewModel.deleteUser(user)

//        userViewModel.deleteAllUser()
    }

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