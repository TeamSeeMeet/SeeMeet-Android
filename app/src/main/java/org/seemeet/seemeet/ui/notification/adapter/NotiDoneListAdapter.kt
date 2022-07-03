package org.seemeet.seemeet.ui.notification.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.model.response.invitation.ConfirmedAndCanceld
import org.seemeet.seemeet.databinding.ItemNotificationDoneBinding
import org.seemeet.seemeet.ui.detail.DetailActivity

class NotiDoneListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var doneList = emptyList<ConfirmedAndCanceld>()
    private var context: Context? = null
    private var listener: ((ConfirmedAndCanceld, Int) -> Unit)? = null
    var mPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemNotificationDoneBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        context = parent.context
        return NotiDoneViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NotiDoneViewHolder).bind(doneList[position])
    }

    override fun getItemId(position: Int): Long = position.toLong()

    inner class NotiDoneViewHolder(private val binding: ItemNotificationDoneBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(doneData: ConfirmedAndCanceld) {
            binding.doneData = doneData

            // 완료 내역 삭제 버튼
            binding.ivDeleteList.setOnClickListener {
                val pos = adapterPosition
                mPosition = pos
                listener?.invoke(doneData, mPosition)
            }

            // 약속 상세
            binding.ivDetail.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("planId", doneData.planId)
                intent.putExtra("id", doneData.id)
                context?.startActivity(intent)
            }

            // 이름 칩그룹
            binding.cgDoneFriendList.removeAllViews()
            doneData.guests.forEach {
                binding.cgDoneFriendList.addView(Chip(context).apply {

                    val name = it.username
                    text = if(name.length > 3) {
                        name.substring(0,2) + " ..."
                    } else {
                        it.username
                    }

                    if (it.isResponse) {
                        setChipBackgroundColorResource(R.color.gray02)
                        setTextAppearance(R.style.chipTextBlackStyle)
                    } else {
                        setChipBackgroundColorResource(R.color.white)
                        setTextAppearance(R.style.chipTextGrayStyle)
                        chipStrokeWidth = 1.0F
                        setChipStrokeColorResource(R.color.gray04)
                    }
                    isCheckable = false
                    isClickable = false
                })
            }


            //n일
            binding.tvDoneDayNum.text = doneData.days.toString()

            // 약속 확정 or 취소
            if (doneData.isCanceled) {
                binding.tvConfirmOrCancel.text =
                    context?.getResources()?.getString(R.string.noti_cancel)
            } else {
                binding.tvConfirmOrCancel.text =
                    context?.getResources()?.getString(R.string.noti_confirm)
            }
        }
    }

    fun setOnItemClickListener(listener: ((ConfirmedAndCanceld, Int) -> Unit)?) {
        this.listener = listener
    }

    override fun getItemCount() = doneList.size

    fun setDone(newList: List<ConfirmedAndCanceld>) {
        this.doneList = newList.sortedBy {it.days}
        notifyDataSetChanged()
    }
}