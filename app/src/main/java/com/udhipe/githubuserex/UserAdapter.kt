package com.udhipe.githubuserex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.udhipe.githubuserex.databinding.ItemGithubUserBinding

class UserAdapter(private val listUser: ArrayList<User>,
                  private var onItemClickCallBack: OnItemClickCallback
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    interface OnItemClickCallback {
        fun onItemClick(data: User)
    }

    inner class UserViewHolder(private val binding: ItemGithubUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .into(imgUser)

                tvUsername.text = user.userName
                itemGithubUser.setOnClickListener { onItemClickCallBack.onItemClick(user) }
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