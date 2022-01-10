package org.seemeet.seemeet.ui.friend.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.data.local.FriendData
import org.seemeet.seemeet.databinding.ItemFriendRequestListBinding

class FriendRequestAdapter : RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder>(){
    val friendRequestList = mutableListOf<FriendData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val binding = ItemFriendRequestListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendRequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        holder.onBind(friendRequestList[position])
    }

    override fun getItemCount(): Int = friendRequestList.size

    class FriendRequestViewHolder(private val binding: ItemFriendRequestListBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(data: FriendData) {
            binding.tvFriendName.text = data.name
        }
    }
}