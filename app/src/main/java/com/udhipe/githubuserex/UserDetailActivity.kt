package com.udhipe.githubuserex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.udhipe.githubuserex.databinding.ActivityUserDetailBinding

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserViewModel::class.java)

        val userName = intent.getStringExtra(UserListActivity.USERNAME)
        userName?.let { userViewModel.setUserDetail(it) }

        userViewModel.getUserDetail().observe(this, {
            if (it != null) {
                showUserDetail(it)
            }
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // update
        val pagerAdapter = FollowPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

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

    private fun showUserDetail(user: User) {
        supportActionBar?.title = user.userName

        with(binding) {
            tvName.text = user.name
            tvRepository.text = user.repository.toString()
            tvFollower.text = user.followers.toString()
            tvFollowing.text = user.following.toString()
            tvCompany.text = user.company
            tvLocation.text = user.location

            Glide.with(root.context)
                .load(user.avatar)
                .circleCrop()
                .into(imgUserAvatar)

        }
    }
}