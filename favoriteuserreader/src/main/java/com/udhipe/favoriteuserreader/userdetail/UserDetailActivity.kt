package com.udhipe.favoriteuserreader.userdetail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.udhipe.favoriteuserreader.R
import com.udhipe.favoriteuserreader.app.FavUserApplication
import com.udhipe.favoriteuserreader.data.User
import com.udhipe.favoriteuserreader.databinding.ActivityUserDetailBinding
import com.udhipe.favoriteuserreader.userfavorite.UserFavoriteActivity

class UserDetailActivity : AppCompatActivity() {

    private val viewModel: UserDetailViewModel by viewModels {
        UserDetailViewModelFactory((application as FavUserApplication).repository)
    }

    private lateinit var binding: ActivityUserDetailBinding

    private var user: User? = null
    private var isFavorite: Boolean = false

//    private lateinit var viewModel: UserDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.shimmerScreen.visibility = View.VISIBLE
        binding.shimmerScreen.startShimmer()

        // update
        val pagerAdapter = FollowPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

//        viewModel = ViewModelProvider(
//            this,
//            ViewModelProvider.NewInstanceFactory()
//        ).get(UserViewModel::class.java)

        val userName = intent.getStringExtra(UserFavoriteActivity.USERNAME)
        userName?.let {
//            viewModel.setUserDetail(it)
            viewModel.setFollowerList(it)
            viewModel.setFollowingList(it)
        }

        viewModel.getUserDetail().observe(this, {
            if (it != null) {
                user = it
                showUserDetail(it)
            }
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getInfo(UserDetailViewModel.USER_DETAIL).observe(this, {
            when (it) {
                UserDetailViewModel.DATA_EXIST -> {
                }

                null, "", UserDetailViewModel.DATA_EMPTY -> Toast.makeText(
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
        })

        // favorite button
//        binding.btnFavorite.setOnClickListener {
//            if (isFavorite) {
//                user?.let { viewModel.deleteUser(it) }
//            } else {
//                user?.let { viewModel.addUser(it) }
//            }
//        }

        viewModel.isFavorite().observe(this, {
            isFavorite = it

            if (it) {
                binding.btnFavorite.setIconTintResource(R.color.red)
//                binding.btnFavorite.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_red)
            } else {
                binding.btnFavorite.setIconTintResource(R.color.white)
//                binding.btnFavorite.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
            }
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
            tvName.text = user?.name ?: "-"
            tvRepository.text = user?.repository.toString()
            tvFollower.text = user?.followers.toString()
            tvFollowing.text = user?.following.toString()
            tvCompany.text = user?.company ?: "-"
            tvLocation.text = user?.location ?: "-"

            Glide.with(root.context)
                .load(user?.avatar)
                .circleCrop()
                .placeholder(R.drawable.circle_grey)
                .into(imgUserAvatar)

        }
    }
}