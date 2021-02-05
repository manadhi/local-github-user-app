package com.udhipe.githubuserex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.udhipe.githubuserex.databinding.ItemGithubUserBinding

class UserAdapter(
    private var onItemClickCallBack: OnItemClickCallback
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var listUser = arrayListOf<User>()

    interface OnItemClickCallback {
        fun onItemClick(userName: String)
    }

    fun setData(newListUser: ArrayList<User>) {
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
                    .into(imgUser)

                tvUsername.text = user.userName
                itemGithubUser.setOnClickListener { onItemClickCallBack.onItemClick(user.userName) }
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