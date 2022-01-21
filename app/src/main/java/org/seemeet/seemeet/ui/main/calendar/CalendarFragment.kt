package org.seemeet.seemeet.ui.main.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import com.kizitonwose.calendarview.utils.yearMonth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.seemeet.seemeet.R
import org.seemeet.seemeet.SeeMeetApplication
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.response.calendar.CalendarEvent
import org.seemeet.seemeet.data.model.response.calendar.UserData
import org.seemeet.seemeet.databinding.FragmentCalendarBinding
import org.seemeet.seemeet.databinding.ItemCalendarDateBinding
import org.seemeet.seemeet.ui.detail.DetailActivity
import org.seemeet.seemeet.util.daysOfWeekFromLocale
import org.seemeet.seemeet.util.makeInVisible
import org.seemeet.seemeet.util.makeVisible
import org.seemeet.seemeet.util.setTextColorRes
import java.lang.Exception
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    val binding get() = _binding!!

    private val eventsAdapter = CalendarEventAdapter {
        DetailActivity.start(requireContext(), it.planId)
    }

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val selectionFormatter = DateTimeFormatter.ofPattern("MMM d일 EEE요일")

    //private val events = mutableMapOf<LocalDate, List<CalendarEvent>>()
    private var events = dummyDate().groupBy { it.date }

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
            val binding = ItemCalendarDateBinding.bind(view)

            init {
                view.setOnClickListener {
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
                            textView.setBackgroundResource(R.drawable.oval_pink)
                            dotView.isVisible = events[day.date.toString()].orEmpty().isNotEmpty()
                        }
                        selectedDate -> {
                            textView.setTextColorRes(R.color.white)
                            textView.setBackgroundResource(R.drawable.oval_black)
                            dotView.isVisible = events[day.date.toString()].orEmpty().isNotEmpty()
                        }
                        else -> {
                            textView.setTextColorRes(R.color.black)
                            textView.background = null
                            dotView.isVisible = events[day.date.toString()].orEmpty().isNotEmpty()
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
                binding.tvCurrentYear.text = it.yearMonth.year.toString() + "년"
                binding.tvCurrentMonth.text = monthTitleFormatter.format(it.yearMonth)
            } else {
                val firstDate = it.weekDays.first().first().date
                val lastDate = it.weekDays.last().last().date
                if (firstDate.yearMonth == lastDate.yearMonth) {
                    binding.tvCurrentYear.text = firstDate.yearMonth.year.toString() + "년"
                    binding.tvCurrentMonth.text = monthTitleFormatter.format(firstDate)
                } else {
                    binding.tvCurrentMonth.text =
                        "${monthTitleFormatter.format(firstDate)} - ${
                            monthTitleFormatter.format(
                                lastDate
                            )
                        }"
                    if (firstDate.year == lastDate.year) {
                        binding.tvCurrentYear.text = firstDate.yearMonth.year.toString() + "년"
                    } else {
                        binding.tvCurrentYear.text =
                            "${firstDate.yearMonth.year} - ${lastDate.yearMonth.year}" + "년"
                    }
                }
            }
            getCalendarDate(""+it.year,""+it.month)
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
            eventList.clear()
            eventList.addAll(this@CalendarFragment.events[date.toString()].orEmpty())
            if(events[date.toString()].orEmpty().isEmpty()) {
                binding.tvEmptyPromise.visibility = View.VISIBLE
                binding.rvCalendarEvent.visibility = View.GONE
            }else {
                binding.tvEmptyPromise.visibility = View.GONE
                binding.rvCalendarEvent.visibility = View.VISIBLE
            }
            notifyDataSetChanged()
        }
        binding.tvSelectedDay.text = selectionFormatter.format(date)
    }

    private fun dummyDate(): List<CalendarEvent> {
        val list = mutableListOf<CalendarEvent>()
        val currentMonth = YearMonth.now()
        val userData = mutableListOf<UserData>()
        userData.add(UserData(1, "이동기"))
        userData.add(UserData(2, "이동기"))
        userData.add(UserData(3, "이동기"))

        list.add(CalendarEvent(1, "대방어대방어", "2022-01-00", "11:00", "13:00", userData))
        return list
    }

    private fun getCalendarDate(year : String, month : String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val ob =RetrofitBuilder.calendarService.getFriendList(SeeMeetSharedPreference.getToken(),year, month).data
                events = ob.groupBy { it.date }
                binding.calendar.notifyCalendarChanged()
                Log.d("testtt",ob.size.toString())
            }catch (e : Exception) {
                Log.d("testtt",e.toString())
            }

        }
    }
}