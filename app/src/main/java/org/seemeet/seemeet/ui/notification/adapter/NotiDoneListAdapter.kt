package org.seemeet.seemeet.ui.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.data.local.NotificationDoneData
import org.seemeet.seemeet.databinding.ItemNotificationDoneBinding

class NotiDoneListAdapter :RecyclerView.Adapter<NotiDoneListAdapter.NotiDoneViewHolder>() {

    private var doneList = emptyList<NotificationDoneData>()

    class NotiDoneViewHolder(
        private val binding : ItemNotificationDoneBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(doneData: NotificationDoneData){
            binding.doneData = doneData
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiDoneViewHolder {
        val binding = ItemNotificationDoneBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return NotiDoneViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotiDoneViewHolder, position: Int) {
        holder.bind(doneList[position])
    }

    override fun getItemCount(): Int = doneList.size

    fun setDone(doneList : List<NotificationDoneData>){
        this.doneList = doneList
        notifyDataSetChanged()
    }
}