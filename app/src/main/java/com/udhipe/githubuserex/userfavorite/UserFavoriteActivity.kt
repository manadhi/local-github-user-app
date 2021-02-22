package com.udhipe.githubuserex.userfavorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.udhipe.githubuserex.R
import com.udhipe.githubuserex.app.GithExApplication
import com.udhipe.githubuserex.data.User
import com.udhipe.githubuserex.databinding.ActivityUserFavoriteBinding
import com.udhipe.githubuserex.databinding.ActivityUserListBinding
import com.udhipe.githubuserex.sharedadapter.UserAdapter
import com.udhipe.githubuserex.userdetail.UserDetailActivity
import com.udhipe.githubuserex.userlist.UserListActivity
import com.udhipe.githubuserex.viewmodel.UserViewModel
import com.udhipe.githubuserex.viewmodel.UserViewModelFactory

class UserFavoriteActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as GithExApplication).repository, null)
    }

    private lateinit var binding: ActivityUserFavoriteBinding
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = User("JakaYoYo", "Jaka Y.", "Jakarta", 2, "-", 10, 50, "")
        val user2 = User("Pakdhe", "Kuncoro", "Jawa Tengah", 5, "-", 1, 10, "")
        val user3 = User("Thunder", "Kuncoro", "Jawa Tengah", 5, "-", 1, 10, "")

        userViewModel.addUser(user)
        userViewModel.addUser(user2)
        userViewModel.addUser(user3)

        binding.rvGithubUser.setHasFixedSize(true)

        setAdapter()

        userViewModel.getFavoriteList().observe(this, Observer {
            userAdapter.setData(it)
        })


//        userViewModel.deleteUser(user)

//        userViewModel.deleteAllUser()
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