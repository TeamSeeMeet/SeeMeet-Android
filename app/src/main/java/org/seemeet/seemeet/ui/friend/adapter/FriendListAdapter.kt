package org.seemeet.seemeet.ui.friend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.data.model.response.friend.FriendListData
import org.seemeet.seemeet.databinding.ItemFriendListBinding

class FriendListAdapter : RecyclerView.Adapter<FriendListAdapter.FriendViewHolder>() {
    private var searchWord: String = ""
    private var listener: ((FriendListData, Int) -> Unit)? = null
    private var friendList = emptyList<FriendListData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding =
            ItemFriendListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.onBind(friendList[position])
    }

    override fun getItemCount(): Int = friendList.size
    var mPosition = 0

    inner class FriendViewHolder(private val binding: ItemFriendListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: FriendListData) {
            binding.tvFriendName.text = data.username

            // 필터링
            if (data.username.startsWith(searchWord)) {
                binding.clFriendList.visibility = View.VISIBLE
                binding.clFriendList.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            } else {
                binding.clFriendList.visibility = View.GONE
                binding.clFriendList.layoutParams.height = 0
            }

            // 신청 보내기
            binding.ivAddMsg.setOnClickListener {
                val pos = adapterPosition
                mPosition = pos
                listener?.invoke(data, mPosition)
            }
        }
    }

    fun setOnItemClickListener(listener: ((FriendListData, Int) -> Unit)?) {
        this.listener = listener
    }

    fun setSearchWord(text: String) {
        searchWord = text
        notifyDataSetChanged()
    }

    fun setFriendList(friendList: List<FriendListData>) {
        this.friendList = friendList.sortedBy { it.username }
        notifyDataSetChanged()
    }
}