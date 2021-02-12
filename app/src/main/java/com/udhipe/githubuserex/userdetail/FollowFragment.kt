package com.udhipe.githubuserex.userdetail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.udhipe.githubuserex.R
import com.udhipe.githubuserex.userlist.UserListActivity
import com.udhipe.githubuserex.sharedadapter.UserAdapter
import com.udhipe.githubuserex.databinding.FragmentFollowBinding
import com.udhipe.githubuserex.viewmodel.UserViewModel

private const val ARG_SECTION = "section_number"

class FollowFragment : Fragment() {
    private var binding: FragmentFollowBinding? = null
    private lateinit var userAdapter: UserAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowBinding.inflate(layoutInflater, container, false)

        binding?.rvFollow?.visibility = View.GONE
        binding?.shimmerScreen?.visibility = View.VISIBLE

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        userViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        ).get(UserViewModel::class.java)

        val index = arguments?.getInt(ARG_SECTION, 2)

        if (index != null) {
            userViewModel.getUserList(index)?.observe(this, {
                if (it != null) {
                    userAdapter.setData(it)
                }
            })

            userViewModel.getInfo(index).observe(this, {
                when (it) {
                    UserViewModel.DATA_EXIST -> {
                        binding?.tvInfo?.visibility = View.GONE
                    }

                    null, "", UserViewModel.DATA_EMPTY -> {

                        var info: Int = if (index == UserViewModel.FOLLOWER_LIST) {
                            R.string.no_follower
                        } else {
                            R.string.no_following
                        }

                        binding?.tvInfo?.text = getString(info)
                        binding?.tvInfo?.visibility = View.VISIBLE

                    }

                    else -> Toast.makeText(
                        context,
                        getString(R.string.something_is_wrong),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                binding?.shimmerScreen?.stopShimmer()
                binding?.shimmerScreen?.visibility = View.GONE
                binding?.rvFollow?.visibility = View.VISIBLE
            })
        }

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setAdapter() {
        val onItemCallBack = object : UserAdapter.OnItemClickCallback {
            override fun onItemClick(userName: String) {
                val intent = Intent(context, UserDetailActivity::class.java)
                intent.putExtra(UserListActivity.USERNAME, userName)
                startActivity(intent)
            }
        }

        binding?.rvFollow?.setHasFixedSize(true)
        binding?.rvFollow?.layoutManager = LinearLayoutManager(context)
        userAdapter = UserAdapter(onItemCallBack)
        binding?.rvFollow?.adapter = userAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance(index: Int) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION, index)
                }
            }
    }
}