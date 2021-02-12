package com.udhipe.githubuserex.userdetail

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.udhipe.githubuserex.R

class FollowPagerAdapter(private val mContext: Context, fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitles = intArrayOf(R.string.follower, R.string.following)

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return FollowFragment.newInstance(position + 2)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitles[position])
    }
}