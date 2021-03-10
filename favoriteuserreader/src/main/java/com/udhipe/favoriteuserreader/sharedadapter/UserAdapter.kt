package com.udhipe.favoriteuserreader.sharedadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.udhipe.favoriteuserreader.R
import com.udhipe.favoriteuserreader.data.User
import com.udhipe.favoriteuserreader.databinding.ItemGithubUserBinding

class UserAdapter :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var listUser = arrayListOf<User>()

    interface OnItemClickCallback {
        fun onItemClick(userName: String)
    }

    fun setData(newListUser: List<User>) {
        listUser.clear()
        listUser.addAll(newListUser)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(private val binding: ItemGithubUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .circleCrop()
                    .placeholder(R.drawable.circle_grey)
                    .into(imgUser)

                tvUsername.text = user.userName
//                itemGithubUser.setOnClickListener { onItemClickCallBack.onItemClick(user.userName) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            ItemGithubUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size
}