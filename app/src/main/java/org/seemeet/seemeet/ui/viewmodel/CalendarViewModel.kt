package org.seemeet.seemeet.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.response.calendar.CalendarEvent
import java.time.LocalDate

class CalendarViewModel : ViewModel() {
    private val _calendarEventMap = MutableLiveData(listOf(CalendarEvent()).groupBy { it.date })
    val calendarEventMap: LiveData<Map<String, List<CalendarEvent>>>
        get() = _calendarEventMap


    private val _selectedEventList = MutableLiveData<List<CalendarEvent>>()
    val selectedEventList: LiveData<List<CalendarEvent>>
        get() = _selectedEventList

    fun getCalendarDate(year: String, month: String, day : LocalDate, push : Boolean) {
        viewModelScope.launch {
            try {
                val ob = RetrofitBuilder.calendarService.getFriendList(
                    SeeMeetSharedPreference.getToken(),
                    year,
                    month
                ).data
                _calendarEventMap.value = ob.groupBy { it.date }

                if(push)
                    getSelectedEvent(day.plusDays(1).toString())
                else
                    getSelectedEvent(day.toString())

            } catch (e: Exception) { }
        }
    }

    fun getSelectedEvent(date: String) {
        _selectedEventList.value = calendarEventMap.value?.get(date)
        selectedEventList.value?.get(0)?.let { Log.d("test", it.invitationTitle) }
    }
}