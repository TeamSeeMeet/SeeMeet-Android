package org.seemeet.seemeet.ui.apply.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.data.model.response.calendar.InvitationPlan
import org.seemeet.seemeet.databinding.ItemPickerEventBinding
import org.seemeet.seemeet.util.BindingRecyclerViewAdapter

class PickerEventAdapter() :
    RecyclerView.Adapter<PickerEventAdapter.ViewHolder>(),
    BindingRecyclerViewAdapter<List<InvitationPlan>> {

    var eventList = mutableListOf<InvitationPlan>()
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPickerEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(eventList[position])
    }

    override fun getItemCount(): Int = eventList.size

    inner class ViewHolder(private val binding: ItemPickerEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: InvitationPlan) {
            binding.apply {
                invitationPlan = event
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setData(data: List<InvitationPlan>) {
        eventList = data.toMutableList()
        notifyDataSetChanged()
    }
}