package org.seemeet.seemeet.ui.friend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.data.local.FriendNameData
import org.seemeet.seemeet.databinding.ItemFriendListBinding

class FriendListAdapter : RecyclerView.Adapter<FriendListAdapter.FriendViewHolder>() {
    private var searchWord : String = ""
    private var friendList = emptyList<FriendNameData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding = ItemFriendListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.onBind(friendList[position])
    }

    override fun getItemCount(): Int = friendList.size

    inner class FriendViewHolder(private val binding: ItemFriendListBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(data: FriendNameData) {
            binding.tvFriendName.text = data.name

            if(data.name.startsWith(searchWord)) {
                binding.clFriendList.visibility = View.VISIBLE
                binding.clFriendList.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            else {
                binding.clFriendList.visibility = View.GONE
                binding.clFriendList.layoutParams.height = 0
            }
        }
    }

    fun setSearchWord(text : String) {
        searchWord = text
        notifyDataSetChanged()
    }

    fun setFriendList(friendList : List<FriendNameData>){
        this.friendList = friendList
        notifyDataSetChanged()
    }
}