package org.seemeet.seemeet.ui.main.adapter

import android.view.LayoutInflater
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
    }

    override fun getItemCount(): Int = reminderList.size

    fun setReminder(reminderList : List<ReminderData>){
        this.reminderList = reminderList
        notifyDataSetChanged()
    }
}