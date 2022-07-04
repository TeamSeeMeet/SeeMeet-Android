package org.seemeet.seemeet.ui.apply

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.utils.yearMonth
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.ApplyFriendData
import org.seemeet.seemeet.data.local.StartEndDateData
import org.seemeet.seemeet.databinding.ActivitySecondApplyBinding
import org.seemeet.seemeet.ui.apply.adapter.PickerEventAdapter
import org.seemeet.seemeet.ui.apply.adapter.SelectedDateAdapter
import org.seemeet.seemeet.ui.viewmodel.SecondApplyViewModel
import org.seemeet.seemeet.util.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

class SecondApplyActivity : AppCompatActivity() {

    private val binding: ActivitySecondApplyBinding by lazy {
        ActivitySecondApplyBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: SecondApplyViewModel

    private val eventsAdapter = PickerEventAdapter()
    private val selectedAdapter = SelectedDateAdapter { date, size ->
        viewModel.removeSelectedDate(date)
        binding.tvSelectedCount.text = size.toString()
    }

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA)
    private val dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.KOREA)
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.KOREA)
    private val selectionFormatter = DateTimeFormatter.ofPattern("MMM d일 EEE요일", Locale.KOREA)

    private var applyDate = today.toString()
    private var applyStartTime = defaultTime(0, 3)
    private var applyEndTime = defaultTime(1, 3)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val userDataList =
            intent.getSerializableExtra("chipFriendData") as ArrayList<ApplyFriendData>
        val title = intent.getStringExtra("title")!!
        val desc = intent.getStringExtra("Desc")!!
        val viewModelFactory = SecondApplyViewModelFactory(userDataList, title, desc)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SecondApplyViewModel::class.java)

        binding.apply {
            this.secondApplyViewModel = viewModel
            lifecycleOwner = this@SecondApplyActivity
            rvCalendarEvent.adapter = eventsAdapter
            rvSelectedDate.adapter = selectedAdapter
        }

        setObserveViewModel()
        initDefaultText()
        initListener()
        setCalendar()
    }

    private fun initListener() {
        initClickListener()
        initSwitchCheckListener()
        initTimepickerListener()
    }

    private fun initClickListener() {
        binding.ivX.setOnClickListener {
            finish()
        }
        binding.btnAdd.setOnClickListener {
            if (checkStartEndTime())
                addSelectedDate()
            else
                CustomToast.createToast(this, "종료시간을 확인해주세요")!!.show()
        }
    }

    private fun setObserveViewModel() {
        viewModel.applyStatus.observe(this) {
            if (it) {
                Toast.makeText(this@SecondApplyActivity, "약속신청을 완료했어요.", Toast.LENGTH_LONG)
                    .show()
                finish()
            }
        }
        viewModel.calendarEventMap.observe(this) {
            binding.calendar.notifyCalendarChanged()
        }
    }

    private fun initDefaultText() {
        binding.tvSelectedDateTop.text = today.toString()
        binding.tvStartTimeTop.text = defaultTime(0, 2)
        binding.tvEndTimeTop.text = defaultTime(1, 2)

        binding.tvStartTimepicker.text = defaultTime(0, 1)
        binding.tvFinishTimepicker.text = defaultTime(1, 1)
    }

    @SuppressLint("SetTextI18n")
    private fun setCalendar() {
        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()

        binding.calendar.apply {
            setup(currentMonth.minusMonths(0), currentMonth.plusMonths(100), daysOfWeek.first())
            scrollToDate(today)
        }
        binding.calendar.post {
            selectDate(today)
        }

        binding.calendar.dayBinder =
            object : DayBinder<PickerDateViewContainer> {

                override fun create(view: View) = PickerDateViewContainer(view) { day ->
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (day.date.isAfter(today) || day.date.isEqual(today))
                            selectDate(day.date)
                    } else if (day.owner == DayOwner.NEXT_MONTH) {
                        selectDate(day.date)
                    }
                }

                override fun bind(container: PickerDateViewContainer, day: CalendarDay) {
                    container.day = day

                    val textDate = container.binding.tvDate
                    val textDay = container.binding.tvDay
                    val layoutDate = container.binding.layoutDate
                    val dotView = container.binding.viewDot

                    textDate.text = day.date.dayOfMonth.toString()
                    textDay.text = dayFormatter.format(day.date)

                    if (day.date.isAfter(today) || day.date.isEqual(today)) {
                        textDate.makeVisible()
                        when (day.date) {
                            today -> {
                                textDate.setTextColorRes(R.color.white)
                                textDay.setTextColorRes(R.color.white)
                                layoutDate.setBackgroundResource(R.drawable.rectangle_pink01_10)
                            }
                            selectedDate -> {
                                textDate.setTextColorRes(R.color.white)
                                textDay.setTextColorRes(R.color.white)
                                layoutDate.setBackgroundResource(R.drawable.rectangle_black_radius_10)
                            }
                            else -> {
                                textDate.setTextColorRes(R.color.black)
                                textDay.setTextColorRes(R.color.black)
                                layoutDate.setBackgroundResource(R.drawable.rectangle_white_radius_10)
                            }
                        }
                        if (!viewModel.calendarEventMap.value.isNullOrEmpty()) {
                            dotView.isVisible =
                                viewModel.calendarEventMap.value!![day.date.toString()].orEmpty()
                                    .isNotEmpty()
                        } else {
                            dotView.makeInVisible()
                        }
                    } else {
                        textDate.setTextColorRes(R.color.silver_chalice)
                        textDay.setTextColorRes(R.color.silver_chalice)
                        layoutDate.setBackgroundResource(R.drawable.rectangle_gray02_10)
                        dotView.makeInVisible()
                    }
                }
            }

        binding.calendar.monthScrollListener =
            {
                val firstDate = it.weekDays.first().first().date
                val lastDate = it.weekDays.last().last().date
                if (firstDate.yearMonth == lastDate.yearMonth) {
                    binding.tvCurrentDate.text =
                        firstDate.yearMonth.year.toString() + "년" + monthTitleFormatter.format(
                            firstDate
                        )
                } else {
                    viewModel.getCalendarDate("" + it.year, "" + it.month)
                    if (firstDate.year == lastDate.year) {
                        binding.tvCurrentDate.text = firstDate.yearMonth.year.toString() + "년" +
                                "${monthTitleFormatter.format(firstDate)} " +
                                "- ${monthTitleFormatter.format(lastDate)}"
                    } else {
                        binding.tvCurrentDate.text =
                            "${firstDate.yearMonth.year}년 ${monthTitleFormatter.format(firstDate)} " +
                                    "- ${lastDate.yearMonth.year}년 ${
                                        monthTitleFormatter.format(
                                            lastDate
                                        )
                                    }"
                    }
                }
            }
        viewModel.getCalendarDate("" + today.year, "" + today.monthValue)
    }

    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.calendar.notifyDateChanged(it) }
            binding.calendar.notifyDateChanged(date)
            updateAdapterForDate(date)

            applyDate = dateFormatter.format(date)
            binding.tvSelectedDateTop.text = dateFormatter.format(date)
        }
    }

    private fun updateAdapterForDate(date: LocalDate) {
        viewModel.getSelectedEvent(date.toString())
        updateEventViewVisibility(viewModel.selectedEventList.value.isNullOrEmpty())
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

    private fun checkStartEndTime(): Boolean {
        return applyStartTime < applyEndTime
    }

    private fun addSelectedDate() {
        if (selectedAdapter.selectedDateList.size < 4) {
            viewModel.addSelectedDate(StartEndDateData(applyDate, applyStartTime, applyEndTime))
            binding.tvSelectedCount.text = viewModel.selectedDateList.value?.size.toString()
        }
    }

    //맨 첨에 현재 시간에 따라 디폴트 시작 시간, 종료 시간 setting
    @SuppressLint("SimpleDateFormat")
    private fun defaultTime(i: Int, type: Int): String {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minutes = cal.get(Calendar.MINUTE)
        //현재 시간이 cal에 있음

        if (i == 0) {
            if (minutes in 0..29) {
                //시간을 1 더하고 분은 00
                //11시부터 11시 29까지 -> 12시 00분
                cal.set(Calendar.HOUR_OF_DAY, hour + 1)
                cal.set(Calendar.MINUTE, 0)
            } else {
                //시간을 1 더하고 분은 30
                //11시 30분부터 11시 59분까지 -> 12시 30분
                cal.set(Calendar.HOUR_OF_DAY, hour + 1)
                cal.set(Calendar.MINUTE, 30)
            }
        } else if (i == 1) {
            if (minutes in 0..29) {
                //시간을 1 더하고 분은 00
                //11시부터 11시 29까지 -> 12시 00분
                cal.set(Calendar.HOUR_OF_DAY, hour + 2)
                cal.set(Calendar.MINUTE, 0)
            } else {
                //시간을 1 더하고 분은 30
                //11시 30분부터 11시 59분까지 -> 12시 30분
                cal.set(Calendar.HOUR_OF_DAY, hour + 2)
                cal.set(Calendar.MINUTE, 30)
            }
        }
        return when (type) {
            1 -> SimpleDateFormat("aa hh:mm").format(cal.time)
            2 -> SimpleDateFormat("aa hh:mm").format(cal.time)
            else -> {
                SimpleDateFormat("hh:mm").format(cal.time)
                    .timeToDate(SimpleDateFormat("aa").format(cal.time))
            }
        }
    }

    private fun initSwitchCheckListener() {
        //  스위치를 클릭했을때
        binding.switchAllday.setOnCheckedChangeListener { _, onSwitch ->
            //  스위치가 켜지면
            if (onSwitch) {
                binding.tvStartTimepicker.text = "오전 00:00"
                binding.tvFinishTimepicker.text = "오후 11:59"
                binding.tvStartTimeTop.text = "오전 00:00"
                binding.tvEndTimeTop.text = "오후 11:59"
                applyStartTime = "00:00"
                applyEndTime = "23:59"
            }
            //  스위치가 꺼지면
            else {
                binding.tvStartTimepicker.text = defaultTime(0, 1) //처음 딱 들어갔을 때는 디폴트 타임으로 띄우기
                binding.tvFinishTimepicker.text = defaultTime(1, 1)
                binding.tvStartTimeTop.text = defaultTime(0, 2)
                binding.tvEndTimeTop.text = defaultTime(1, 2)
                applyStartTime = defaultTime(0, 3)
                applyEndTime = defaultTime(1, 3)
            }
        }
    }

    private fun initTimepickerListener() {
        binding.tvStartTimepicker.setOnClickListener {
            var ampm = binding.tvStartTimepicker.text.substring(0, 2)
            var hour = binding.tvStartTimepicker.text.substring(3, 5)
            var min = binding.tvStartTimepicker.text.substring(6, 8)
            if (ampm.equals("오후")) {
                if (!hour.equals("12")) {
                    hour = (hour.toInt() + 12).toString()
                }
            } else {
                if (hour.equals("12")) {
                    hour = "0"
                }
            }
            getTime(binding.tvStartTimepicker, this@SecondApplyActivity, hour, min, 0)
        }

        binding.tvFinishTimepicker.setOnClickListener {
            var ampm = binding.tvFinishTimepicker.text.substring(0, 2)
            var hour = binding.tvFinishTimepicker.text.substring(3, 5)
            var min = binding.tvFinishTimepicker.text.substring(6, 8)
            if (ampm.equals("오후")) {
                if (!hour.equals("12")) {
                    hour = (hour.toInt() + 12).toString()
                }
            } else {
                if (hour.equals("12")) {
                    hour = "0"
                }
            }
            getTime(binding.tvFinishTimepicker, this@SecondApplyActivity, hour, min, 1)
        }
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun getTime(textView: TextView, context: Context, hour: String, min: String, i: Int) {
        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->

            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            var min_to_string = minute.toString()
            if (hour == 0) {
                if (minute < 10) {
                    min_to_string = "0" + minute.toString()
                }
                textView.text = "오전 00:$min_to_string"

                if (i == 0) {
                    binding.tvStartTimeTop.text = "오전 00:00"
                    applyStartTime = "00:00"
                } else {
                    binding.tvEndTimeTop.text = "오전 00:00"
                    applyEndTime = "00:00"
                }
                //타임피커에서 선택한 시간 받아서 텍스트에 넣어줌
            } else {
                textView.text =
                    SimpleDateFormat("aa hh:mm").format(cal.time) //타임피커에서 선택한 시간 받아서 텍스트에 넣어줌

                //TODO : 상단 시간팅 텍스트뷰 세팅 및, 시간변수 세팅

                if (i == 0) {
                    binding.tvStartTimeTop.text = SimpleDateFormat("aa hh:mm").format(cal.time)
                    applyStartTime = SimpleDateFormat("hh:mm").format(cal.time)
                        .timeToDate(SimpleDateFormat("aaa").format(cal.time))
                } else {
                    binding.tvEndTimeTop.text = SimpleDateFormat("aa hh:mm").format(cal.time)
                    applyEndTime = SimpleDateFormat("hh:mm").format(cal.time)
                        .timeToDate(SimpleDateFormat("aaa").format(cal.time))
                }

            }
        }

        val tp = TimePickerDialog(context, timeSetListener, hour.toInt(), min.toInt(), false)
        if (i == 0) {//다이얼로그 위치 조정
            tp.window?.attributes?.x = 100
            tp.window?.attributes?.y = 98
        } else {
            tp.window?.attributes?.x = 100
            tp.window?.attributes?.y = 160
        }
        tp.show()//타임피커 다이얼로그 띄우기
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SecondApplyActivity::class.java)
            context.startActivity(intent)
        }
    }
}