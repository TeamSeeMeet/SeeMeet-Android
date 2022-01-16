package org.seemeet.seemeet.ui.apply.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.data.local.ApplyFriendData
import org.seemeet.seemeet.databinding.ItemApplySearchFriendBinding

class ApplyFriendAdapter : RecyclerView.Adapter<ApplyFriendAdapter.ApplyFriendViewHolder>() {
    val applyfriendList = mutableListOf<ApplyFriendData>()

    private var listener: ((String) -> Unit)? = null
    fun setOnItemClickListener(listener: ((String) -> Unit)?) {
        this.listener = listener
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
                    listener?.invoke(data.name)
                }
            }
        }
    }
}