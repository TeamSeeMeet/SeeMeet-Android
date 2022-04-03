package org.seemeet.seemeet.ui.apply

import android.view.View
import android.view.ViewGroup
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.ViewContainer
import org.seemeet.seemeet.databinding.ItemPickerDateBinding

class PickerDateViewContainer (view: View, val clickListener: (CalendarDay) -> Unit) : ViewContainer(view) {
    lateinit var day: CalendarDay // Will be set when this container is bound.
    val binding = ItemPickerDateBinding.bind(view)

    init {
        view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        view.setOnClickListener {
            clickListener(day)
        }
    }
}