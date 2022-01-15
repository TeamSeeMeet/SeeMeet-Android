package org.seemeet.seemeet.ui.notification.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.NotificationIngData
import org.seemeet.seemeet.databinding.ItemNotificationReceiveBinding
import org.seemeet.seemeet.databinding.ItemNotificationSendBinding

class NotiIngListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ingList = mutableListOf<NotificationIngData>()
    private var context : Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == OptionViewType.SEND) {
            context = parent.context
            return NotiIngViewHolder1(
                ItemNotificationSendBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        context = parent.context
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


    inner class NotiIngViewHolder1(private val binding: ItemNotificationSendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ingData: NotificationIngData) {
            binding.ingData = ingData

            ingData.nameList.forEach{
                binding.cgSendFriendList.addView(Chip(context).apply{
                    text = it.name
                    if(it.response){
                        setChipBackgroundColorResource(R.color.pink01)
                        setTextAppearance(R.style.chipTextWhiteStyle)
                    } else {
                        setChipBackgroundColorResource(R.color.white)
                        setTextAppearance(R.style.chipTextPinkStyle)
                        chipStrokeWidth = 1.0F
                        setChipStrokeColorResource(R.color.pink01)
                    }
                    isCheckable = false
                    isClickable = false
                })
                Log.d("**********************받은이", it.name)
            }
        }
    }

    inner class NotiIngViewHolder2(private val binding: ItemNotificationReceiveBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ingData: NotificationIngData) {
            binding.ingData = ingData

            ingData.nameList.forEach{
                binding.cgReceiveFriendList.addView(Chip(context).apply{
                    text = it.name
                    setChipBackgroundColorResource(R.color.gray06)
                    setTextAppearance(R.style.chipTextWhiteStyle)
                    isCheckable = false
                    isClickable = false
                })
                Log.d("**********************받은이", it.name)
            }
        }
    }

    object OptionViewType {
        const val SEND = 1
        const val RECEIVE = 2
    }
}