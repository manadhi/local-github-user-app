package com.udhipe.githubuserex.userlist

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.udhipe.githubuserex.R
import com.udhipe.githubuserex.about.AboutAppActivity
import com.udhipe.githubuserex.sharedadapter.UserAdapter
import com.udhipe.githubuserex.databinding.ActivityUserListBinding
import com.udhipe.githubuserex.setting.SettingActivity
import com.udhipe.githubuserex.userdetail.UserDetailActivity
import com.udhipe.githubuserex.userfavorite.UserFavoriteActivity
import com.udhipe.githubuserex.viewmodel.UserViewModel


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
            }
        })

        userViewModel.getInfo(UserViewModel.USER_LIST).observe(this, {
            when (it) {
                null, "", UserViewModel.DATA_EXIST -> binding.tvInfo.visibility = View.GONE

                UserViewModel.DATA_EMPTY -> {
                    binding.tvInfo.visibility = View.VISIBLE
                    binding.tvInfo.text = getString(R.string.can_not_find_user)
                }

                else -> Toast.makeText(this, getString(R.string.something_is_wrong), Toast.LENGTH_SHORT).show()
            }

            binding.shimmerScreen.stopShimmer()
            binding.shimmerScreen.visibility = View.GONE
            binding.rvGithubUser.visibility = View.VISIBLE
        })

        setListener()

    }

    private fun setListener() {
        userViewModel.getKeyWord().observe(this, {
            if (it != null) {
                binding.searchView.setQuery(it, false)
            }
        })

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(keyword: String?): Boolean {
                if (keyword != null && keyword.isNotEmpty()) {
                    hideKeyboard(this@UserListActivity)

                    binding.tvInfo.visibility = View.GONE
                    binding.rvGithubUser.visibility = View.GONE
                    binding.shimmerScreen.visibility = View.VISIBLE

                    userViewModel.setUserList(keyword)
                }
                return true
            }

            override fun onQueryTextChange(keyword: String?): Boolean {
                keyword?.let { userViewModel.setKeyword(it) }

                return true
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.app_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.license -> {
                OssLicensesMenuActivity.setActivityTitle(getString(R.string.license))
                startActivity(Intent(this, OssLicensesMenuActivity::class.java))
                true
            }

            R.id.about -> {
                startActivity(Intent(this, AboutAppActivity::class.java))
                true
            }

            R.id.setting -> {
                startActivity(Intent(this, SettingActivity::class.java))
//                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }

            R.id.favorite -> {
                startActivity(Intent(this@UserListActivity, UserFavoriteActivity::class.java))
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

    private fun hideKeyboard(activity: Activity) {
        val view = activity.findViewById<View>(android.R.id.content)
        if (view != null) {
            val imm: InputMethodManager =
                activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}