package org.seemeet.seemeet.ui.apply.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.data.local.ApplyFriendData
import org.seemeet.seemeet.databinding.ItemApplySearchFriendBinding

class ApplyFriendAdapter : RecyclerView.Adapter<ApplyFriendAdapter.ApplyFriendViewHolder>() {

    val applyfriendList = mutableListOf<ApplyFriendData>()
    private var searchWord : String = ""

    private var listener: ((String) -> Unit)? = null
    fun setOnItemClickListener(listener: ((String) -> Unit)?) {
        this.listener = listener
    }

    var mPosition =0
    fun getPosition(): Int{
        return mPosition
    }
    fun addItem(data: ApplyFriendData){
        applyfriendList.add(data)
        notifyDataSetChanged()
    }
    fun removeItem(position:Int){
        if(position>=0){
            applyfriendList.removeAt(position)
            notifyDataSetChanged()
        }
    }
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
        fun onBind(data: ApplyFriendData) {
            binding.ivApplyProfile.setImageResource(data.profile)
            binding.tvApplyName.text = data.name
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    mPosition = pos
                    listener?.invoke(data.name)
                }
            }

            if(data.name.startsWith(searchWord)) {
                binding.itemApplySearchFriend.visibility= View.VISIBLE
                binding.itemApplySearchFriend.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            else {
                binding.itemApplySearchFriend.visibility = View.GONE
                binding.itemApplySearchFriend.layoutParams.height = 0
            }
        }
    }

    fun setSearchWord(text : String) {
        searchWord = text
        notifyDataSetChanged()
    }
}