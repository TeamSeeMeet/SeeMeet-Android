package org.seemeet.seemeet.ui.notification.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.NotificationDoneData
import org.seemeet.seemeet.databinding.ItemNotificationDoneBinding
import org.seemeet.seemeet.ui.detail.DetailActivity

class NotiDoneListAdapter :RecyclerView.Adapter<NotiDoneListAdapter.NotiDoneViewHolder>() {

    private var doneList = mutableListOf<NotificationDoneData>()
    private var context : Context? = null

    inner class NotiDoneViewHolder(
        private val binding : ItemNotificationDoneBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(doneData: NotificationDoneData) {
            binding.doneData = doneData

            binding.ivDeleteList.setOnClickListener {
                Log.d("*************************", "클릭")
            }
            
            binding.ivDetail.setOnClickListener {
                val intent = Intent( context, DetailActivity::class.java)
                context?.startActivity(intent)
            }

            doneData.nameList.forEach{
                binding.cgDoneFriendList.addView(Chip(context).apply{
                    text = it.name
                    if(it.response){
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
                Log.d("**********************받은이", it.name)
            }

            if(doneData.isCanceled){
                binding.tvConfirmOrCancel.text = context?.getResources()?.getString(R.string.noti_cancel)
                binding.ivDetail.visibility = View.GONE
            } else{
                binding.tvConfirmOrCancel.text = context?.getResources()?.getString(R.string.noti_confirm)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiDoneViewHolder {
        val binding = ItemNotificationDoneBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        context = parent.context
        return NotiDoneViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotiDoneViewHolder, position: Int) {
        holder.bind(doneList[position])
    }

    override fun getItemCount(): Int = doneList.size

    fun setDone(doneList : MutableList<NotificationDoneData>){
        this.doneList = doneList
        notifyDataSetChanged()
    }

}