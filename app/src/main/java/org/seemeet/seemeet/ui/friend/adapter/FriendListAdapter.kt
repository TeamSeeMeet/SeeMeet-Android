package org.seemeet.seemeet.ui.friend.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.data.local.FriendNameData
import org.seemeet.seemeet.databinding.ItemFriendListBinding

class FriendListAdapter : RecyclerView.Adapter<FriendListAdapter.FriendViewHolder>(){
    private var friendList = emptyList<FriendNameData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding = ItemFriendListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.onBind(friendList[position])
    }

    override fun getItemCount(): Int = friendList.size

    class FriendViewHolder(private val binding: ItemFriendListBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(data: FriendNameData) {
            binding.tvFriendName.text = data.name
        }
    }

    fun setFriendList(friendList : List<FriendNameData>){
        this.friendList = friendList
        notifyDataSetChanged()
    }
}