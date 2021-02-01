package com.udhipe.githubuserex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.udhipe.githubuserex.databinding.ActivityUserDetailBinding

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showUserDetail()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    private fun showUserDetail() {
        if (intent.hasExtra("data")) {
            val user: User? = intent.getParcelableExtra("data")

            supportActionBar?.title = user?.userName

            with(binding) {
                tvUsername.text = user?.userName
                tvName.text = user?.name
                tvRepository.text = user?.repository
                tvFollower.text = user?.followers
                tvFollowing.text = user?.following
                tvCompany.text = user?.company
                tvLocation.text = user?.location

                Glide.with(root.context)
                    .load(user?.avatar)
                    .into(imgUserAvatar)
            }
        }
    }
}