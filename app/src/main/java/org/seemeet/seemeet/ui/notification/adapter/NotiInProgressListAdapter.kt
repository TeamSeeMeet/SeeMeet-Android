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

class NotiInProgressListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var inProgressList = emptyList<Invitation>()
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context

        if (viewType == OptionViewType.SEND) {
            return NotiInProgressViewHolder1(
                ItemNotificationSendBinding.inflate(
                    layoutInflater, parent, false
                )
            )
        }
        return NotiInProgressViewHolder2(
            ItemNotificationReceiveBinding.inflate(
                layoutInflater, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (!inProgressList[position].isReceived) {
            (holder as NotiInProgressViewHolder1).bind(inProgressList[position])
        } else {
            (holder as NotiInProgressViewHolder2).bind(inProgressList[position])
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    inner class NotiInProgressViewHolder1(private val binding: ItemNotificationSendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(inProgressData: Invitation) {
            binding.inProgressData = inProgressData

            // 보낸 신청 클릭리스너
            binding.clNotiSendBg.setOnClickListener {
                val intent = Intent(context, SendActivity::class.java)
                intent.putExtra("invitationId", inProgressData.id)
                context?.startActivity(intent)
            }

            // 이름 칩그룹
            binding.cgSendFriendList.removeAllViews()
            inProgressData.guests.forEach {
                binding.cgSendFriendList.addView(Chip(context).apply {

                    val name = it.username
                    text = if(name.length > 3) {
                        name.substring(0,2) + " ..."
                    } else {
                        it.username
                    }

                    if (it.isResponse) {
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

    inner class NotiInProgressViewHolder2(private val binding: ItemNotificationReceiveBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(inProgressData: Invitation) {
            binding.inProgressData = inProgressData

            // 받은 신청 클릭리스너
            binding.clNotiReceiveBg.setOnClickListener {
                val intent = Intent(context, ReceiveActivity::class.java)
                intent.putExtra("invitationId", inProgressData.id)
                context?.startActivity(intent)
            }

            // 이름 칩그룹
            binding.cgReceiveFriendList.removeAllViews()
            binding.cgReceiveFriendList.addView(Chip(context).apply {

                val name = inProgressData.host.username
                text = if(name.length > 3) {
                    name.substring(0,2) + " ..."
                } else {
                    inProgressData.host.username
                }

                setChipBackgroundColorResource(R.color.gray06)
                setTextAppearance(R.style.chipTextWhiteStyle)
                isCheckable = false
                isClickable = false
            })

        }
    }

    override fun getItemCount() = inProgressList.size

    override fun getItemViewType(position: Int): Int {
        return when (inProgressList[position].isReceived) {
            true -> OptionViewType.RECEIVE
            false -> OptionViewType.SEND
        }
    }

    fun setInProgressList(newList: List<Invitation>) {
        this.inProgressList = newList.sortedByDescending { it.createdAt }
        notifyDataSetChanged()
    }

    object OptionViewType {
        const val SEND = 1
        const val RECEIVE = 2
    }
}