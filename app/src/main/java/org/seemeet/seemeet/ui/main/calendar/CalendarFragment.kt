package org.seemeet.seemeet.ui.main.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.CalendarDateItemBinding
import org.seemeet.seemeet.databinding.FragmentCalendarBinding
import org.seemeet.seemeet.util.daysOfWeekFromLocale
import org.seemeet.seemeet.util.makeInVisible
import org.seemeet.seemeet.util.makeVisible
import org.seemeet.seemeet.util.setTextColorRes
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class CalendarFragment : Fragment() {
    private var _binding : FragmentCalendarBinding? = null
    val binding get() = _binding!!

    private val eventsAdapter = CalendarEventAdapter {
        //TODO : Create item click logic
    }

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val selectionFormatter = DateTimeFormatter.ofPattern("MMM d일 EEE요일")
    private val events = mutableMapOf<LocalDate, List<CalendarEvent>>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvCalendarEvent.adapter = eventsAdapter
        }

        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        binding.calendar.apply {
            setup(currentMonth.minusMonths(100), currentMonth.plusMonths(100), daysOfWeek.first())
            scrollToMonth(currentMonth)
        }

        if (savedInstanceState == null) {
            binding.calendar.post {
                // Show today's events initially.
                selectDate(today)
            }
        }

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = CalendarDateItemBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        selectDate(day.date)
                    }
                }
            }
        }

        binding.calendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.tvDate
                val dotView = container.binding.viewDot

                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.makeVisible()
                    when (day.date) {
                        today -> {
                            textView.setTextColorRes(R.color.white)
                            textView.setBackgroundResource(R.drawable.oval_black)
                            dotView.makeInVisible()
                        }
                        selectedDate -> {
                            textView.setTextColorRes(R.color.white)
                            textView.setBackgroundResource(R.drawable.oval_pink)
                            dotView.makeInVisible()
                        }
                        else -> {
                            textView.setTextColorRes(R.color.black)
                            textView.background = null
                            dotView.isVisible = events[day.date].orEmpty().isNotEmpty()
                        }
                    }
                } else {
                    textView.setTextColorRes(R.color.silver_chalice)
                    textView.background = null
                    dotView.makeInVisible()
                }
            }
        }

        binding.calendar.monthScrollListener = {
            if (binding.calendar.maxRowCount == 6) {
                binding.tvCurrentYear.text = it.yearMonth.year.toString()
                binding.tvCurrentMonth.text = monthTitleFormatter.format(it.yearMonth)
            } else {
                val firstDate = it.weekDays.first().first().date
                val lastDate = it.weekDays.last().last().date
                if (firstDate.yearMonth == lastDate.yearMonth) {
                    binding.tvCurrentYear.text = firstDate.yearMonth.year.toString()
                    binding.tvCurrentMonth.text = monthTitleFormatter.format(firstDate)
                } else {
                    binding.tvCurrentMonth.text =
                        "${monthTitleFormatter.format(firstDate)} - ${
                            monthTitleFormatter.format(
                                lastDate
                            )
                        }"
                    if (firstDate.year == lastDate.year) {
                        binding.tvCurrentYear.text = firstDate.yearMonth.year.toString()
                    } else {
                        binding.tvCurrentYear.text =
                            "${firstDate.yearMonth.year} - ${lastDate.yearMonth.year}"
                    }
                }
            }


        }
    }

    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.calendar.notifyDateChanged(it) }
            binding.calendar.notifyDateChanged(date)
            updateAdapterForDate(date)
        }
    }

    private fun updateAdapterForDate(date: LocalDate) {
        eventsAdapter.apply {
            events.clear()
            events.addAll(this@CalendarFragment.events[date].orEmpty())
            notifyDataSetChanged()
        }
        binding.tvSelectedDay.text = selectionFormatter.format(date)
    }

}