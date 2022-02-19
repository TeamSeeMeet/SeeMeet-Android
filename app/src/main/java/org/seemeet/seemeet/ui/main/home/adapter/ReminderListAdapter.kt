package org.seemeet.seemeet.ui.main.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.ReminderData
import org.seemeet.seemeet.data.model.response.plan.ComePlanData
import org.seemeet.seemeet.databinding.ItemHomeReminderBinding

class ReminderListAdapter :RecyclerView.Adapter<ReminderListAdapter.ReminderViewHolder>() {

    private var reminderList = emptyList<ComePlanData>()

    class ReminderViewHolder(
        private val binding : ItemHomeReminderBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(comePlanData: ComePlanData){
            // item home reminder 레이아웃에 있는 data 값에 해당 데이터 삽입 (data Binding)
            binding.comePlanData= comePlanData

            // 만남이 가능한 사람이 주최자 외 1명일 경우. 혹은 주최자만 가능할 경우.
           if(comePlanData.count.toInt() <= 2){
                binding.ivHomeReminder.setImageResource(R.drawable.img_illust_2)
            }
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

    fun setReminder(comePlanList : List<ComePlanData>){
        this.reminderList = comePlanList
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