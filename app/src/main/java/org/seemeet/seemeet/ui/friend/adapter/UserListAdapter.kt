package org.seemeet.seemeet.ui.friend.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.seemeet.seemeet.data.model.response.friend.UserListData
import org.seemeet.seemeet.databinding.ItemUserListBinding

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {
    private var listener: ((UserListData, Int) -> Unit)? = null
    var userList = mutableListOf<UserListData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserListAdapter.UserViewHolder, position: Int) {
        holder.onBind(userList[position])
    }

    override fun getItemCount(): Int = userList.size
    var mPosition = 0

    inner class UserViewHolder(private val binding: ItemUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: UserListData) {
            binding.tvUserName.text = data.username
            binding.tvUserNickname.text = data.nickname
            Glide.with(itemView)
                .load(data.imgLink)
                .into(binding.ivProfileImage)

            // 친구 신청 보내기
            binding.ivAddFriend.setOnClickListener {
                val pos = adapterPosition
                mPosition = pos
                listener?.invoke(data, mPosition)

                binding.ivAddFriend.isSelected = true
            }
        }
    }

    fun setOnItemClickListener(listener: ((UserListData, Int) -> Unit)?) {
        this.listener = listener
    }

    fun setUserList(userData: UserListData) {
        userList.clear()
        userList.addAll(mutableListOf(userData))
        notifyDataSetChanged()
    }
}