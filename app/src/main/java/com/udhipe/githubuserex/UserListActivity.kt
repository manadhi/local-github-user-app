package com.udhipe.githubuserex

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.udhipe.githubuserex.databinding.ActivityUserListBinding


class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding
    private lateinit var userAdapter: UserAdapter

    private lateinit var userViewModel: UserViewModel

    companion object {
        const val USERNAME = "userName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvGithubUser.setHasFixedSize(true)

        userViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserViewModel::class.java)

        setAdapter()

        userViewModel.getUserList(UserViewModel.USER_LIST)?.observe(this, {
            if (it != null) {
                userAdapter.setData(it)

                binding.shimmerScreen.stopShimmer()
                binding.shimmerScreen.visibility = View.GONE
                binding.rvGithubUser.visibility = View.VISIBLE
            }
        })

    }

    override fun onResume() {
        super.onResume()

//        binding.shimmerScreen.startShimmer()
    }

    override fun onPause() {
        super.onPause()

//        binding.shimmerScreen.stopShimmer()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.app_menu, menu)

        // search
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(keyword: String?): Boolean {
                if (keyword != null && keyword.isNotEmpty()) {
                    binding.rvGithubUser.visibility = View.GONE
                    binding.shimmerScreen.visibility = View.VISIBLE

                    userViewModel.setUserList(keyword)
                }
                return true
            }

            override fun onQueryTextChange(keyword: String?): Boolean {
                return false
            }

        })


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.license -> {
                startActivity(Intent(this, OssLicensesMenuActivity::class.java))
                true
            }
            R.id.about -> {
                startActivity(Intent(this, AboutAppActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setAdapter() {
        val onItemCallBack = object : UserAdapter.OnItemClickCallback {
            override fun onItemClick(userName: String) {
                val intent = Intent(this@UserListActivity, UserDetailActivity::class.java)
                intent.putExtra(USERNAME, userName)
                startActivity(intent)
            }
        }

        binding.rvGithubUser.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(onItemCallBack)
        binding.rvGithubUser.adapter = userAdapter
    }

}