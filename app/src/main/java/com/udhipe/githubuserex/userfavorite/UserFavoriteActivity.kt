package com.udhipe.githubuserex.userfavorite

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
        viewModel.getFavoriteList().observe(this, {
            userAdapter.setData(it)

            binding.shimmerScreen.stopShimmer()
            binding.shimmerScreen.visibility = View.GONE
            binding.rvGithubUser.visibility = View.VISIBLE
        })
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