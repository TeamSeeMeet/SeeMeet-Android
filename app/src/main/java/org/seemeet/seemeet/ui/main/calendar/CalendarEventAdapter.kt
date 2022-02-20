package org.seemeet.seemeet.ui.main.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.model.response.calendar.CalendarEvent
import org.seemeet.seemeet.databinding.ItemCalendarEventBinding
import org.seemeet.seemeet.util.BindingRecyclerViewAdapter

class CalendarEventAdapter(val clickListener: (CalendarEvent) -> Unit) :
    RecyclerView.Adapter<CalendarEventAdapter.ViewHolder>(),
    BindingRecyclerViewAdapter<List<CalendarEvent>> {

    var eventList = emptyList<CalendarEvent>()
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCalendarEventBinding.inflate(
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

    inner class ViewHolder(private val binding: ItemCalendarEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: CalendarEvent) {
            binding.apply {
                cgFriendList.removeAllViews()
                calendarEvent = event
                event.users.forEach {
                    cgFriendList.addView(Chip(context).apply {
                        text = it.username
                        setChipBackgroundColorResource(R.color.white)
                        setTextAppearance(R.style.calendarChipTextPinkStyle)
                        chipStrokeWidth = 1.0F
                        isCheckable = false
                        setChipStrokeColorResource(R.color.pink01)
                    })
                }
                root.setOnClickListener {
                    clickListener(event)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setData(data: List<CalendarEvent>) {
        eventList = data
        notifyDataSetChanged()
    }
}