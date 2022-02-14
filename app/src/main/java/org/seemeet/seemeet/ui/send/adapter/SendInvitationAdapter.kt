package org.seemeet.seemeet.ui.send.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.data.model.response.invitation.SendInvitationDate
import org.seemeet.seemeet.databinding.ItemSendTimelistBinding

class SendInvitationAdapter : RecyclerView.Adapter<SendInvitationAdapter.SendInviViewHolder>(){

    private var dateList = emptyList<SendInvitationDate>()

    class SendInviViewHolder(
        private val binding : ItemSendTimelistBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(data : SendInvitationDate){
            binding.dateData = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendInviViewHolder {
        val binding = ItemSendTimelistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return SendInviViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SendInviViewHolder, position: Int) {
        holder.bind(dateList[position])
    }

    override fun getItemCount(): Int = dateList.size

    override fun getItemId(position: Int): Long = position.toLong()

    fun setInviList(dateList : List<SendInvitationDate>){
        this.dateList = dateList
        notifyDataSetChanged()
    }

}