package org.seemeet.seemeet.ui.friend.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.data.local.FriendData
import org.seemeet.seemeet.databinding.ItemFriendListBinding

class FriendAdapter : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>(){
    val friendList = mutableListOf<FriendData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding = ItemFriendListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.onBind(friendList[position])
    }

    override fun getItemCount(): Int = friendList.size

    class FriendViewHolder(private val binding: ItemFriendListBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(data: FriendData) {
            binding.tvFriendName.text = data.name
        }
    }
}