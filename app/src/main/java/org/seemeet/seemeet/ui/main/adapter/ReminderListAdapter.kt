package org.seemeet.seemeet.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.data.local.ReminderData
import org.seemeet.seemeet.databinding.ItemHomeReminderBinding

class ReminderListAdapter :RecyclerView.Adapter<ReminderListAdapter.ReminderViewHolder>() {

    private var reminderList = emptyList<ReminderData>()

    class ReminderViewHolder(
        private val binding : ItemHomeReminderBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(reminderData: ReminderData){
            // item home reminder 레이아웃에 있는 data 값에 해당 데이터 삽입 (data Binding)
            binding.reminderData = reminderData
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val binding = ItemHomeReminderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ReminderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(reminderList[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int = reminderList.size

    fun setReminder(reminderList : List<ReminderData>){
        this.reminderList = reminderList
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener
}