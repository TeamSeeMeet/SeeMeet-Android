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
import org.seemeet.seemeet.data.model.response.invitation.ConfirmedAndCanceld
import org.seemeet.seemeet.databinding.ItemNotificationDoneBinding
import org.seemeet.seemeet.ui.detail.DetailActivity

class NotiDoneListAdapter :RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val doneList = mutableListOf<ConfirmedAndCanceld>()
    private var context : Context? = null

    inner class NotiDoneViewHolder(
        private val binding : ItemNotificationDoneBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(doneData: ConfirmedAndCanceld) {
            binding.doneData = doneData

            binding.ivDeleteList.setOnClickListener {
                Log.d("*************************", "클릭")
            }
            
            binding.ivDetail.setOnClickListener {
                val intent = Intent( context, DetailActivity::class.java)
                intent.putExtra("planId", doneData.planId)
                context?.startActivity(intent)
            }

            binding.cgDoneFriendList.removeAllViews()
            doneData.guests.forEach{
                binding.cgDoneFriendList.addView(Chip(context).apply{
                    text = it.username
                    if(it.isResponse){
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
                Log.d("**********************받은이", it.username)
            }

            if(doneData.isCancled){
                binding.tvConfirmOrCancel.text = context?.getResources()?.getString(R.string.noti_cancel)
                binding.ivDetail.visibility = View.GONE
            } else{
                binding.tvConfirmOrCancel.text = context?.getResources()?.getString(R.string.noti_confirm)
            }
        }
    }

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

    override fun getItemCount() = doneList.size

    fun setDone(newList: List<ConfirmedAndCanceld>){
        doneList.clear()
        doneList.addAll(newList)
        notifyDataSetChanged()
    }

}