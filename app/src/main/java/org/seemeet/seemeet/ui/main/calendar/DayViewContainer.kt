package org.seemeet.seemeet.ui.main.calendar

import android.view.View
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.ViewContainer
import org.seemeet.seemeet.databinding.ItemCalendarDateBinding

class DayViewContainer(view: View, val clickListener: (CalendarDay) -> Unit) : ViewContainer(view) {
    lateinit var day: CalendarDay // Will be set when this container is bound.
    val binding = ItemCalendarDateBinding.bind(view)

    init {
        view.setOnClickListener {
            clickListener(day)
        }
    }
}