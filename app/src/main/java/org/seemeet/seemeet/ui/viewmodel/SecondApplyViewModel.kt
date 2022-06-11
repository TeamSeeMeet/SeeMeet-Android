package org.seemeet.seemeet.ui.viewmodel

import android.util.Log
import androidx.databinding.InverseMethod
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.local.ApplyFriendData
import org.seemeet.seemeet.data.local.StartEndDateData
import org.seemeet.seemeet.data.model.request.invitation.RequestApplyInvitation
import org.seemeet.seemeet.data.model.response.calendar.InvitationPlan
import org.seemeet.seemeet.util.ListLiveData

class SecondApplyViewModel(
    friendList: ArrayList<ApplyFriendData>,
    title: String,
    desc: String
) : ViewModel() {
    val friendList = friendList
    val title = title
    val desc = desc

    private val _calendarEventMap = MutableLiveData(listOf(InvitationPlan()).groupBy { it.date })
    val calendarEventMap: LiveData<Map<String, List<InvitationPlan>>>
        get() = _calendarEventMap

    private val _selectedEventList = MutableLiveData<List<InvitationPlan>>()
    val selectedEventList: LiveData<List<InvitationPlan>>
        get() = _selectedEventList

    val selectedDateList = ListLiveData<StartEndDateData>()

    private val _applyStatus = MutableLiveData<Boolean>()
    val applyStatus: LiveData<Boolean>
        get() = _applyStatus

    fun getCalendarDate(year: String, month: String) {
        viewModelScope.launch {
            try {
                val ob = RetrofitBuilder.calendarService.getInvitationPlan(
                    SeeMeetSharedPreference.getToken(),
                    year,
                    month
                ).data
                _calendarEventMap.value = ob.groupBy { it.date }
            } catch (e: Exception) {
            }
        }
    }

    fun getSelectedEvent(date: String) {
        _selectedEventList.value = calendarEventMap.value?.get(date)
    }

    fun addSelectedDate(date: StartEndDateData) {
        selectedDateList.add(date)
    }

    fun removeSelectedDate(date: StartEndDateData) {
        selectedDateList.remove(date)
    }

    fun tryApply() {
        Log.d(TAG, "test0")
        val dateList = mutableListOf<String>()
        val startTimeList = mutableListOf<String>()
        val endTimeList = mutableListOf<String>()

        if (selectedDateList.value.isNullOrEmpty()) {
            Log.d(TAG, "test11")
            return
        } else {
            Log.d(TAG, "test1")
            selectedDateList.value!!.forEach {
                dateList.add(it.date)
                startTimeList.add(it.start)
                endTimeList.add(it.end)
            }
        }
        viewModelScope.launch {
            try {
                val ob = RequestApplyInvitation(
                    friendList,
                    title,
                    desc,
                    dateList,
                    startTimeList,
                    endTimeList
                )
                RetrofitBuilder.invitationService.postApplyInvitation(
                    SeeMeetSharedPreference.getToken(),
                    ob
                )
                _applyStatus.value = true
                Log.d(TAG, "test2")
            } catch (e: Exception) {
                _applyStatus.value = false
                Log.d(TAG, e.toString())
            }
        }
    }

    companion object {
        const val TAG = "SecondApplyViewModel"
    }
}