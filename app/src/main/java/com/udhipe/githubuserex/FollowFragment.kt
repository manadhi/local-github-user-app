package com.udhipe.githubuserex

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.udhipe.githubuserex.databinding.FragmentFollowBinding

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
                    binding?.shimmerScreen?.stopShimmer()
                    binding?.shimmerScreen?.visibility = View.GONE
                    binding?.rvFollow?.visibility = View.VISIBLE

                    userAdapter.setData(it)
                }
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