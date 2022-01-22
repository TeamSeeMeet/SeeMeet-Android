package org.seemeet.seemeet.ui.apply

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.local.ApplyFriendData
import org.seemeet.seemeet.data.local.StartEndDateData
import org.seemeet.seemeet.data.model.response.calendar.CalendarEvent
import org.seemeet.seemeet.data.model.response.calendar.UserData
import org.seemeet.seemeet.data.model.request.invitation.RequestApplyInvitation
import org.seemeet.seemeet.databinding.ActivitySecondApplyBinding
import org.seemeet.seemeet.databinding.ItemPickerDateBinding
import org.seemeet.seemeet.ui.apply.adapter.PickerEventAdapter
import org.seemeet.seemeet.ui.apply.adapter.SelectedDateAdapter
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

    private val eventsAdapter = PickerEventAdapter()
    private val selectedAdapter = SelectedDateAdapter()

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val dayFormatter = DateTimeFormatter.ofPattern("EEE")
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val selectionFormatter = DateTimeFormatter.ofPattern("MMM d일 EEE요일")

    private val events = dummyDate().groupBy { it.date }

    private var applyDate = today.toString()
    private var applyStartTime = defaultTime(0,3)
    private var applyEndTime = defaultTime(1,3)

    private lateinit var userDataList : ArrayList<ApplyFriendData>
    private lateinit var title : String
    private lateinit var desc : String

    private var dateList = mutableListOf<String>()
    private var startTimeList = mutableListOf<String>()
    private var endTimeList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tvSelectedDateTop.text = today.toString()
        binding.tvStartTimeTop.text = defaultTime(0,2)
        binding.tvEndTimeTop.text = defaultTime(1,2)

        binding.tvStartTimepicker.text = defaultTime(0,1) //처음 딱 들어갔을 때는 디폴트 타임으로 띄우기
        binding.tvFinishTimepicker.text = defaultTime(1,1)
        initClickListener()

        userDataList = intent.getSerializableExtra("chipFriendData") as ArrayList<ApplyFriendData>
        title = intent.getStringExtra("title")!!
        desc = intent.getStringExtra("Desc")!!

        binding.apply {
            rvCalendarEvent.adapter = eventsAdapter
            rvSelectedDate.adapter = selectedAdapter

            selectedAdapter.setDeleteListener {
                binding.tvSelectedCount.text = selectedAdapter.selectedDateList.size.toString()
            }

            btnAdd.setOnClickListener {
                addSelectedDate()
            }
            btnApply.setOnClickListener {
                if(selectedAdapter.selectedDateList.isNotEmpty()) {
                    getSelectedDate()
                    try {
                        tryApply()
                        Toast.makeText(this@SecondApplyActivity,"약속신청을 완료했어요.",Toast.LENGTH_LONG).show()
                        finish()
                    }catch (e : Exception){}
                }
            }
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
                view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

                view.setOnClickListener {
                    when (day.owner) {
                        DayOwner.THIS_MONTH -> {
                            if (day.date.isAfter(today) || day.date.isEqual(today))
                                selectDate(day.date)
                        }
                        DayOwner.NEXT_MONTH -> {
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

                if (day.date.isAfter(today) || day.date.isEqual(today)) {
                    textDate.makeVisible()
                    when (day.date) {
                        today -> {
                            textDate.setTextColorRes(R.color.white)
                            textDay.setTextColorRes(R.color.white)
                            layoutDate.setBackgroundResource(R.drawable.rectangle_pink_10)
                            dotView.isVisible = events[day.date.toString()].orEmpty().isNotEmpty()
                        }
                        selectedDate -> {
                            textDate.setTextColorRes(R.color.white)
                            textDay.setTextColorRes(R.color.white)
                            layoutDate.setBackgroundResource(R.drawable.rectangle_black_radius_10)
                            dotView.isVisible = events[day.date.toString()].orEmpty().isNotEmpty()
                        }
                        else -> {
                            textDate.setTextColorRes(R.color.black)
                            textDay.setTextColorRes(R.color.black)
                            layoutDate.setBackgroundResource(R.drawable.rectangle_white_radius_10)
                            dotView.isVisible = events[day.date.toString()].orEmpty().isNotEmpty()
                        }
                    }
                }
                else {
                    textDate.setTextColorRes(R.color.silver_chalice)
                    textDay.setTextColorRes(R.color.silver_chalice)
                    layoutDate.setBackgroundResource(R.drawable.rectangle_gray02_10)
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
        //  스위치를 클릭했을때
        binding.switchAllday.setOnCheckedChangeListener { CompoundButton, onSwitch ->
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
                binding.tvStartTimepicker.text = defaultTime(0,1) //처음 딱 들어갔을 때는 디폴트 타임으로 띄우기
                binding.tvFinishTimepicker.text = defaultTime(1,1)
                binding.tvStartTimeTop.text = defaultTime(0,2)
                binding.tvEndTimeTop.text = defaultTime(1,2)
                applyStartTime = defaultTime(0,3)
                applyEndTime = defaultTime(1,3)
            }
        }

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
            Log.d("tests", ampm + "  " + hour + "  " + min)
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
            Log.d("tests", ampm + "  " + hour + "  " + min)
            getTime(binding.tvFinishTimepicker, this@SecondApplyActivity, hour, min, 1)

        }
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

    //맨 첨에 현재 시간에 따라 디폴트 시작 시간, 종료 시간 setting
    fun defaultTime(i: Int, type: Int): String {
        val cal = Calendar.getInstance() //
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minutes = cal.get(Calendar.MINUTE)
        //현재 시간이 cal에 있음

        if (i == 0) {
            if (minutes >= 0 && minutes < 30) {
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
            if (minutes >= 0 && minutes < 30) {
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
        return if (type == 1) SimpleDateFormat("aa hh:mm").format(cal.time)
        else if(type ==2) SimpleDateFormat("aa hh:mm").format(cal.time)
        else {
            SimpleDateFormat("hh:mm").format(cal.time).timeToDate(SimpleDateFormat("aa").format(cal.time))
        }
    }

    fun getTime(textView: TextView, context: Context, hour: String, min: String, i: Int) {
        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->

            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            var min_to_string = minute.toString()
            if (hour == 0) {
                if(minute<10) {
                    min_to_string = "0" + minute.toString()
                }
                textView.text = "오전 00:" + min_to_string

                if(i==0) {
                    binding.tvStartTimeTop.text = "오전 00:00"
                    applyStartTime = "00:00"
                }else{
                    binding.tvEndTimeTop.text = "오전 00:00"
                    applyEndTime = "00:00"
                }
                //타임피커에서 선택한 시간 받아서 텍스트에 넣어줌
            } else {
                textView.text =
                    SimpleDateFormat("aa hh:mm").format(cal.time) //타임피커에서 선택한 시간 받아서 텍스트에 넣어줌

                //TODO : 상단 시간팅 텍스트뷰 세팅 및, 시간변수 세팅

                if(i==0) {
                    binding.tvStartTimeTop.text = SimpleDateFormat("aa hh:mm").format(cal.time)
                    applyStartTime = SimpleDateFormat("hh:mm").format(cal.time).timeToDate(SimpleDateFormat("aaa").format(cal.time))
                }else{
                    binding.tvEndTimeTop.text = SimpleDateFormat("aa hh:mm").format(cal.time)
                    applyEndTime = SimpleDateFormat("hh:mm").format(cal.time).timeToDate(SimpleDateFormat("aaa").format(cal.time))
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
        tp.show()//타임피커 다이얼로그 띄우

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
        val userData = mutableListOf<UserData>()
        userData.add(UserData(1, "이동기"))
        userData.add(UserData(2, "이동기"))
        userData.add(UserData(3, "이동기"))

        list.add(CalendarEvent(1, "대방어대방어", "2022-01-00", "11:00", "13:00", userData))
        return list
    }

    private fun addSelectedDate() {
        if (selectedAdapter.selectedDateList.size<4) {
            selectedAdapter.addItem(StartEndDateData(applyDate, applyStartTime, applyEndTime))
            binding.tvSelectedCount.text = selectedAdapter.selectedDateList.size.toString()
        }
    }

    private fun getSelectedDate() {
        selectedAdapter.selectedDateList.forEach {
            dateList.add(it.date)
            startTimeList.add(it.start)
            endTimeList.add(it.end)
        }
    }

    private fun tryApply() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val ob = RequestApplyInvitation(userDataList,title,desc,dateList,startTimeList,endTimeList)
                RetrofitBuilder.invitationService.postApplyInvitation(SeeMeetSharedPreference.getToken(),ob)
            }catch (e: Exception){
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SecondApplyActivity::class.java)
            context.startActivity(intent)
        }
    }
}