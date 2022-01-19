package org.seemeet.seemeet.ui.apply

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.ActivitySecondApplyBinding
import org.seemeet.seemeet.databinding.ItemPickerDateBinding
import org.seemeet.seemeet.ui.apply.adapter.PickerEventAdapter
import org.seemeet.seemeet.ui.main.calendar.CalendarEvent
import org.seemeet.seemeet.ui.main.calendar.UserData
import org.seemeet.seemeet.util.daysOfWeekFromLocale
import org.seemeet.seemeet.util.makeInVisible
import org.seemeet.seemeet.util.makeVisible
import org.seemeet.seemeet.util.setTextColorRes
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class SecondApplyActivity : AppCompatActivity() {

    private val binding : ActivitySecondApplyBinding by lazy {
        ActivitySecondApplyBinding.inflate(layoutInflater)
    }

    private val eventsAdapter = PickerEventAdapter()

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
    private val dayFormatter = DateTimeFormatter.ofPattern("EEE")
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val selectionFormatter = DateTimeFormatter.ofPattern("MMM d일 EEE요일")

    //private val events = mutableMapOf<LocalDate, List<CalendarEvent>>()
    private val events = dummyDate().groupBy { it.date }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initClickListener()

        binding.apply {
            rvCalendarEvent.adapter = eventsAdapter
        }

        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        binding.calendar.apply {
            setup(currentMonth.minusMonths(0), currentMonth.plusMonths(100), daysOfWeek.first())
            scrollToDate(today)
        }

        if (savedInstanceState == null) {
            binding.calendar.post {
                // Show today's events initially.
                selectDate(today)
            }
        }

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = ItemPickerDateBinding.bind(view)

            init {
                view.setOnClickListener {
                    when (day.owner) {
                        DayOwner.THIS_MONTH -> {
                            if (day.date.isAfter(today) || day.date.isEqual(today))
                                selectDate(day.date)
                        }
                    }
                }
            }
        }

        binding.calendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textDate = container.binding.tvDate
                val textDay = container.binding.tvDay
                val layoutDate = container.binding.layoutDate

                val dotView = container.binding.viewDot

                textDate.text = day.date.dayOfMonth.toString()
                textDay.text = dayFormatter.format(day.date)

                if (day.owner == DayOwner.THIS_MONTH) {
                    val isEvent = events[day.date.toString()]
                    textDate.makeVisible()
                    when (day.date) {
                        today -> {
                            textDate.setTextColorRes(R.color.white)
                            textDay.setTextColorRes(R.color.white)
                            layoutDate.setBackgroundResource(R.drawable.rectangle_pink_10)
                        }
                        selectedDate -> {
                            textDate.setTextColorRes(R.color.white)
                            textDay.setTextColorRes(R.color.white)
                            layoutDate.setBackgroundResource(R.drawable.rectangle_black_radius_16)
                        }
                        else -> {
                            textDate.setTextColorRes(R.color.black)
                            textDay.setTextColorRes(R.color.black)
                            layoutDate.setBackgroundResource(R.drawable.rectangle_white_radius_10)
                            dotView.isVisible = events[day.date.toString()].orEmpty().isNotEmpty()
                        }
                    }

                } else {
                    textDate.setTextColorRes(R.color.silver_chalice)
                    textDay.setTextColorRes(R.color.silver_chalice)
                    layoutDate.setBackgroundResource(R.drawable.rectangle_white_radius_10)
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
        }
    }

    private fun initClickListener() {
        binding.ivX.setOnClickListener {
            finish()
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
            eventList.addAll(this@SecondApplyActivity.events[date.toString()].orEmpty())
            if (events[date.toString()].orEmpty().isEmpty()) {
                binding.tvEmptyPromise.visibility = View.VISIBLE
                binding.rvCalendarEvent.visibility = View.GONE
            } else {
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

        list.add(CalendarEvent(1, "대방어대방어", "2022-01-18", "11:00", "13:00", userData))
        list.add(CalendarEvent(1, "대방어대방어", "2022-01-19", "11:00", "13:00", userData))
        list.add(CalendarEvent(1, "대방어대방어", "2022-01-19", "11:00", "13:00", userData))
        return list
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SecondApplyActivity::class.java)
            context.startActivity(intent)
        }
    }
}