package org.seemeet.seemeet.ui.receive.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.ScheduleData
import org.seemeet.seemeet.databinding.ItemReceiveScheduleBinding
import org.seemeet.seemeet.databinding.ItemReceiveScheduleGrayBinding


class ReceiveSchduleListAdapter :RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var scheduleList = emptyList<ScheduleData>()
    private var context : Context? = null
    //일단 viewtype을

    inner class ReceiveSchduleViewHolder1(
        private val binding : ItemReceiveScheduleBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(scheduleData: ScheduleData) {
            binding.scheduleData = scheduleData
            binding.cgRecieveTogether.removeAllViews()
            scheduleData.together.forEach{
                binding.cgRecieveTogether.addView(Chip(context).apply{
                    text = it

                    setChipBackgroundColorResource(R.color.white)
                    setTextAppearance(R.style.chipTextPinkStyle)
                    chipStrokeWidth = 1.0F
                    isCheckable = false
                    setChipStrokeColorResource(R.color.pink01)

                })
            }
        }
    }
    inner class ReceiveSchduleViewHolder2(
        private val binding : ItemReceiveScheduleGrayBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(scheduleData: ScheduleData) {
            binding.scheduleData = scheduleData

        }
    }

    override fun getItemViewType(position: Int): Int {
        return scheduleList[position].id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1 ){
            val binding = ItemReceiveScheduleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            context = parent.context
            return ReceiveSchduleViewHolder1(binding)
        } else {
            val binding = ItemReceiveScheduleGrayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            context = parent.context
            return ReceiveSchduleViewHolder2(binding)
        }

    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(scheduleList[position].id){
            OptionViewType.TOGETHER ->
                (holder as ReceiveSchduleViewHolder1).bind(scheduleList[position])
            OptionViewType.ALONE ->
                (holder as ReceiveSchduleViewHolder2).bind(scheduleList[position])
        }

    }

    override fun getItemCount(): Int = scheduleList.size

    fun setSchedule(scheduleList : List<ScheduleData>){
        this.scheduleList = scheduleList
        notifyDataSetChanged()
    }


    object OptionViewType {
        const val TOGETHER = 1
        const val ALONE = 2
    }

}
