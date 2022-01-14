package org.seemeet.seemeet.ui.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.data.local.NotificationIngData
import org.seemeet.seemeet.databinding.ItemNotificationReceiveBinding
import org.seemeet.seemeet.databinding.ItemNotificationSendBinding

class NotiIngListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ingList = mutableListOf<NotificationIngData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == OptionViewType.SEND) {
            return NotiIngViewHolder1(
                ItemNotificationSendBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        return NotiIngViewHolder2(
            ItemNotificationReceiveBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (ingList[position].viewType) {
            OptionViewType.SEND -> {
                (holder as NotiIngViewHolder1).bind(ingList[position])
            }
            OptionViewType.RECEIVE -> {
                (holder as NotiIngViewHolder2).bind(ingList[position])
            }
        }
    }

    override fun getItemCount() = ingList.size

    override fun getItemViewType(position: Int): Int {
        return ingList[position].viewType
    }

    fun setIng(newList: List<NotificationIngData>) {
        ingList.clear()
        ingList.addAll(newList)
        notifyDataSetChanged()
    }


    class NotiIngViewHolder1(private val binding: ItemNotificationSendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ingData: NotificationIngData) {
            binding.ingData = ingData
        }
    }

    class NotiIngViewHolder2(private val binding: ItemNotificationReceiveBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ingData: NotificationIngData) {
            binding.ingData = ingData
        }
    }

    object OptionViewType {
        const val SEND = 1
        const val RECEIVE = 2
    }
}