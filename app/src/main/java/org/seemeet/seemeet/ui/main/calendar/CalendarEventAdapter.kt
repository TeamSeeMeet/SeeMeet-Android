package org.seemeet.seemeet.ui.main.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.databinding.CalendarEventItemBinding

class CalendarEventAdapter(val onClick: (CalendarEvent) -> Unit) :
    RecyclerView.Adapter<CalendarEventAdapter.ViewHolder>() {

    val events = mutableListOf<CalendarEvent>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CalendarEventItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    inner class ViewHolder(private val binding: CalendarEventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick(events[bindingAdapterPosition])
            }
        }

        fun bind(event: CalendarEvent) {
            binding.tvEventTitle.text = event.text
        }
    }
}