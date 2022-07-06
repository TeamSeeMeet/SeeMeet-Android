package org.seemeet.seemeet.ui.main.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.FragmentCalendarBinding
import org.seemeet.seemeet.ui.detail.DetailActivity
import org.seemeet.seemeet.ui.viewmodel.CalendarViewModel
import org.seemeet.seemeet.util.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarFragment : Fragment() {
    lateinit var binding: FragmentCalendarBinding

    val calendarViewModel: CalendarViewModel by viewModels()

    private val eventsAdapter = CalendarEventAdapter {
        DetailActivity.start(requireContext(), it.planId)
    }

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    private lateinit var currentCalendarMonth: CalendarMonth

    private var push = false
    private var start = true

    private val monthTitleFormatter = DateTimeFormatter.ofPattern("M월")
    private val selectionFormatter = DateTimeFormatter.ofPattern("M월 d일 E요일", Locale.KOREAN)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)

        binding.appbarCalendar.setPadding(0, (22 + getStatusBarHeight(requireContext())), 0, 0)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.calendarViewModel = calendarViewModel
        binding.rvCalendarEvent.adapter = eventsAdapter

        arguments?.let {
            if (it.getBoolean("tomorrow")) {
                push = true
            }
        }
        calendarViewModel.calendarEventMap.observe(viewLifecycleOwner) {
            binding.calendar.notifyCalendarChanged()
            if (start && it.isNotEmpty()) {
                if (push) {
                    selectDate(today.plusDays(1))
                    push = false
                } else {
                    selectDate(today)
                }
                start = false
            }
        }

        setCalendar()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!isHidden) {
            calendarViewModel.getCalendarDate(
                "" + currentCalendarMonth.year,
                "" + currentCalendarMonth.month
            )
        }
    }

    private fun setCalendar() {
        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth =
            if (push && YearMonth.now().atEndOfMonth() == today) {
                YearMonth.now().plusMonths(1)
            } else {
                YearMonth.now()
            }

        binding.calendar.apply {
            itemAnimator = null
            setup(currentMonth.minusMonths(100), currentMonth.plusMonths(100), daysOfWeek.first())
            scrollToMonth(currentMonth)
        }

        binding.calendar.dayBinder = object : DayBinder<DayViewContainer> {

            override fun create(view: View) = DayViewContainer(view) { day ->
                when (day.owner) {
                    DayOwner.THIS_MONTH -> {
                        selectDate(day.date)
                    }
                    DayOwner.NEXT_MONTH -> {
                        this@CalendarFragment.binding.calendar.findFirstVisibleMonth()?.let {
                            this@CalendarFragment.binding.calendar.smoothScrollToMonth(it.yearMonth.next)
                        }
                    }
                    else -> {
                        this@CalendarFragment.binding.calendar.findFirstVisibleMonth()?.let {
                            this@CalendarFragment.binding.calendar.smoothScrollToMonth(it.yearMonth.previous)
                        }
                    }
                }
            }

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.tvDate
                val dotView = container.binding.viewDot

                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.makeVisible()
                    when (day.date) {
                        LocalDate.now() -> {
                            textView.setTextColorRes(R.color.white)
                            textView.setBackgroundResource(R.drawable.oval_pink)
                        }
                        selectedDate -> {
                            textView.setTextColorRes(R.color.white)
                            textView.setBackgroundResource(R.drawable.oval_black)

                        }
                        else -> {
                            textView.setTextColorRes(R.color.black)
                            textView.background = null
                        }
                    }
                    if (!calendarViewModel.calendarEventMap.value.isNullOrEmpty())
                        dotView.isVisible =
                            calendarViewModel.calendarEventMap.value!![day.date.toString()].orEmpty()
                                .isNotEmpty()
                    else dotView.makeInVisible()

                } else {
                    textView.setTextColorRes(R.color.silver_chalice)
                    textView.background = null
                    dotView.makeInVisible()
                }
            }
        }

        binding.calendar.monthScrollListener = {
            binding.tvCurrentYear.text = it.yearMonth.year.toString() + "년"
            binding.tvCurrentMonth.text = monthTitleFormatter.format(it.yearMonth)
            currentCalendarMonth = it
            calendarViewModel.getCalendarDate("" + it.year, "" + it.month)
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
        calendarViewModel.getSelectedEvent(date.toString())
        updateEventViewVisibility(calendarViewModel.selectedEventList.value.isNullOrEmpty())
        binding.tvSelectedDay.text = selectionFormatter.format(date)
    }

    private fun updateEventViewVisibility(isEmpty: Boolean) {
        if (isEmpty) {
            binding.tvEmptyPromise.visibility = View.VISIBLE
            binding.rvCalendarEvent.visibility = View.GONE
        } else {
            binding.tvEmptyPromise.visibility = View.GONE
            binding.rvCalendarEvent.visibility = View.VISIBLE
        }
    }
}