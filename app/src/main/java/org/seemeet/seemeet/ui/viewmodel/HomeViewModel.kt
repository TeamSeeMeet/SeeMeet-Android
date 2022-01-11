package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.ReminderData

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _reminderList = MutableLiveData<List<ReminderData>>()
    val reminderList : LiveData<List<ReminderData>>
        get() = _reminderList

    fun setReminderList() {
        _reminderList.value = mutableListOf(
            ReminderData(
                "대방어데이", R.drawable.circle_black, "1월 15일", "D-4"
            ),
            ReminderData(
                "대방어데이2", R.drawable.circle_black, "1월 16일", "D-5"
            ),
            ReminderData(
                "대방어데이3", R.drawable.circle_black, "1월 17일", "D-6"
            )

        )
    }


}