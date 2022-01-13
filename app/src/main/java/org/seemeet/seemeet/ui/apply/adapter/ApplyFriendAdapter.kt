package org.seemeet.seemeet.ui.apply.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.ApplyFriendData
import org.seemeet.seemeet.databinding.ItemApplySearchFriendBinding

class ApplyFriendAdapter : RecyclerView.Adapter<ApplyFriendAdapter.ApplyFriendViewHolder>(){
    val applyfriendList = mutableListOf<ApplyFriendData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplyFriendViewHolder {
        val binding = ItemApplySearchFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApplyFriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApplyFriendViewHolder, position: Int) {
        holder.onBind(applyfriendList[position])
    }

    override fun getItemCount(): Int = applyfriendList.size

    class ApplyFriendViewHolder(private val binding: ItemApplySearchFriendBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(data: ApplyFriendData) {
            //binding.ivApplyProfile.setImageResource(R.drawable.ic_x)
            binding.tvApplyName.text = data.name
        }
    }
}