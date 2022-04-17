package org.seemeet.seemeet.ui.notification.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.model.response.invitation.Invitation
import org.seemeet.seemeet.databinding.ItemNotificationReceiveBinding
import org.seemeet.seemeet.databinding.ItemNotificationSendBinding
import org.seemeet.seemeet.ui.receive.ReceiveActivity
import org.seemeet.seemeet.ui.send.SendActivity

class NotiIngListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ingList = mutableListOf<Invitation>()
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
        when (!ingList[position].isReceived) {
            true -> {
                (holder as NotiIngViewHolder1).bind(ingList[position])
            }
            false -> {
                (holder as NotiIngViewHolder2).bind(ingList[position])
            }
        }
    }

    override fun getItemCount() = ingList.size

    override fun getItemViewType(position: Int): Int {
        return when(ingList[position].isReceived){
            true -> OptionViewType.RECEIVE
            false -> OptionViewType.SEND
        }
    }

    fun setIng(newList: List<Invitation>) {
        ingList.clear()
        ingList.addAll(newList)
        notifyDataSetChanged()
    }


    inner class NotiIngViewHolder1(private val binding: ItemNotificationSendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ingData: Invitation) {
            binding.ingData = ingData

            binding.clNotiSendBg.setOnClickListener{
                val intent = Intent( context, SendActivity::class.java)
                intent.putExtra("invitationId", ingData.id)
                context?.startActivity(intent)
            }

            binding.cgSendFriendList.removeAllViews()
            ingData.guests.forEach{
                binding.cgSendFriendList.addView(Chip(context).apply{
                    text = it.username
                    if(it.isResponse){
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
            }
        }
    }

    inner class NotiIngViewHolder2(private val binding: ItemNotificationReceiveBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ingData: Invitation) {
            binding.ingData = ingData

            binding.clNotiReceiveBg.setOnClickListener{
                val intent = Intent( context, ReceiveActivity::class.java)
                intent.putExtra("invitationId", ingData.id)
                context?.startActivity(intent)
            }

            binding.cgReceiveFriendList.removeAllViews()
            binding.cgReceiveFriendList.addView(Chip(context).apply{
                text = ingData.host.username
                setChipBackgroundColorResource(R.color.gray06)
                setTextAppearance(R.style.chipTextWhiteStyle)
                isCheckable = false
                isClickable = false
            })

        }
    }

    object OptionViewType {
        const val SEND = 1
        const val RECEIVE = 2
    }
}