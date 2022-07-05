package org.seemeet.seemeet.ui.apply.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.model.response.friend.FriendListData
import org.seemeet.seemeet.databinding.ItemApplySearchFriendBinding

class ApplyFriendAdapter : RecyclerView.Adapter<ApplyFriendAdapter.ApplyFriendViewHolder>() {

    private val applyfriendList = mutableListOf<FriendListData>()
    private var searchWord: String = ""
    private var listener: ((FriendListData) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ApplyFriendViewHolder {
        val binding = ItemApplySearchFriendBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ApplyFriendViewHolder(binding)
    }

    override fun getItemCount(): Int = applyfriendList.size

    override fun onBindViewHolder(holder: ApplyFriendViewHolder, position: Int) {
        holder.onBind(applyfriendList[position])
    }

    inner class ApplyFriendViewHolder(private val binding: ItemApplySearchFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: FriendListData) {
            if (data.imgLink == "null" || data.imgLink.isNullOrEmpty()) {
                Glide.with(itemView).load(R.drawable.ic_img_profile).circleCrop()
                    .into(binding.ivApplyProfile)
            } else {
                Glide.with(itemView).load(data.imgLink!!.toUri()).circleCrop()
                    .into(binding.ivApplyProfile)
            }

            binding.tvApplyName.text = data.username
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    mPosition = pos
                    listener?.invoke(data)
                }
            }

            if (data.username.startsWith(searchWord)) {
                binding.itemApplySearchFriend.visibility = View.VISIBLE
                binding.itemApplySearchFriend.layoutParams.height =
                    ViewGroup.LayoutParams.WRAP_CONTENT
            } else {
                binding.itemApplySearchFriend.visibility = View.GONE
                binding.itemApplySearchFriend.layoutParams.height = 0
            }
        }
    }

    fun setOnItemClickListener(listener: ((FriendListData) -> Unit)?) {
        this.listener = listener
    }

    fun setSearchWord(text: String) {
        searchWord = text
        notifyDataSetChanged()
    }


    fun setFriend(friendList: List<FriendListData>) {
        this.applyfriendList.removeAll(this.applyfriendList)
        this.applyfriendList.addAll(friendList)
        sortItem()
        notifyDataSetChanged()
    }

    var mPosition = 0
    fun getPosition(): Int {
        return mPosition
    }

    fun addItem(data: FriendListData) {
        applyfriendList.add(data)
        notifyDataSetChanged()
    }

    fun sortItem() {
        applyfriendList.sortBy { it.username }
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position >= 0) {
            applyfriendList.removeAt(position)
            notifyDataSetChanged()
        }
    }
}