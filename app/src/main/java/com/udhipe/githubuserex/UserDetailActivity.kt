package com.udhipe.githubuserex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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

        binding.shimmerScreen.visibility = View.VISIBLE
        binding.shimmerScreen.startShimmer()

//        binding.originView.visibility = View.GONE

        // update
        val pagerAdapter = FollowPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        userViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserViewModel::class.java)

        val userName = intent.getStringExtra(UserListActivity.USERNAME)
        userName?.let {
            userViewModel.setUserDetail(it)
            userViewModel.setFollowerList(it)
            userViewModel.setFollowingList(it)
        }

        userViewModel.getUserDetail().observe(this, {
            if (it != null) {
//                binding.shimmerScreen.stopShimmer()
//                binding.shimmerScreen.visibility = View.GONE
//                binding.originView.visibility = View.VISIBLE
                showUserDetail(it)
            }
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userViewModel.getInfo(UserViewModel.USER_DETAIL).observe(this, {
            when (it) {
                UserViewModel.DATA_EXIST -> {}

                null, "", UserViewModel.DATA_EMPTY -> Toast.makeText(
                    this,
                    getString(R.string.can_not_find_user_detail),
                    Toast.LENGTH_SHORT
                ).show()

                else -> Toast.makeText(
                    this,
                    getString(R.string.something_is_wrong),
                    Toast.LENGTH_SHORT
                ).show()
            }

            binding.shimmerScreen.stopShimmer()
            binding.shimmerScreen.visibility = View.GONE
//            binding.originView.visibility = View.VISIBLE
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

    private fun showUserDetail(user: User?) {
        supportActionBar?.title = user?.userName

        with(binding) {
            tvName.text = user?.name?:"-"
            tvRepository.text = user?.repository.toString()
            tvFollower.text = user?.followers.toString()
            tvFollowing.text = user?.following.toString()
            tvCompany.text = user?.company?:"-"
            tvLocation.text = user?.location?:"-"

            Glide.with(root.context)
                .load(user?.avatar)
                .circleCrop()
                .placeholder(R.drawable.circle_grey)
                .into(imgUserAvatar)

        }
    }
}