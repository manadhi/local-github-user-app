package com.udhipe.githubuserex

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.udhipe.githubuserex.databinding.ActivityUserListBinding


class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding
    private lateinit var userAdapter: UserAdapter

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvGithubUser.setHasFixedSize(true)

        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)

        setAdapter()

        val keyword = "jaka"
        userViewModel.setUserList(keyword)

        userViewModel.getUserList(UserViewModel.USER_LIST)?.observe(this, {
            if (it != null) {
                userAdapter.setData(it)
            }
        })

//        userList.addAll(getListUser())


//        showList(userList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.app_menu, menu)
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

/*    private fun getListUser(): ArrayList<User> {
        val listName = resources.getStringArray(R.array.name)
        val listUserName = resources.getStringArray(R.array.username)
        val listLocation = resources.getStringArray(R.array.location)
        val listRepository = resources.getStringArray(R.array.repository)
        val listCompany = resources.getStringArray(R.array.company)
        val listFollower = resources.getStringArray(R.array.followers)
        val listFollowing = resources.getStringArray(R.array.following)
        val listImage = resources.obtainTypedArray(R.array.avatar)

        val userList = ArrayList<User>()
        for (position in listName.indices) {
            val user = User(
                listUserName[position],
                listName[position],
                listLocation[position],
                listRepository[position],
                listCompany[position],
                listFollower[position],
                listFollowing[position],
                listImage.getResourceId(position, -1)
            )

            userList.add(user)
        }

        return userList
    }*/

/*
    private fun showList(userList: ArrayList<User>) {
        val onItemCallBack = object : UserAdapter.OnItemClickCallback {
            override fun onItemClick(data: User) {
                val intent = Intent(this@UserListActivity, UserDetailActivity::class.java)
//                intent.putExtra("data", data)
                startActivity(intent)
            }
        }

        binding.rvGithubUser.layoutManager = LinearLayoutManager(this)
        val userAdapter = UserAdapter(userList, onItemCallBack)
        binding.rvGithubUser.adapter = userAdapter
    }
*/

    private fun setAdapter() {
        val onItemCallBack = object : UserAdapter.OnItemClickCallback {
            override fun onItemClick(data: User) {
//                val intent = Intent(this@UserListActivity, UserDetailActivity::class.java)
//                intent.putExtra("data", data)
//                startActivity(intent)
            }
        }

        binding.rvGithubUser.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(onItemCallBack)
        binding.rvGithubUser.adapter = userAdapter
    }

}