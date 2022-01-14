package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.seemeet.seemeet.data.local.NotificationDoneData

class NotiDoneViewModel(application: Application) : AndroidViewModel(application) {

    private val _doneList = MutableLiveData<List<NotificationDoneData>>()
    val doneList : LiveData<List<NotificationDoneData>>
        get() = _doneList


    fun setDoneList() {
        _doneList.value = mutableListOf(
            NotificationDoneData(
                "1일전", "약속 취소","강화도 여행"
            ),
            NotificationDoneData(
                "1일전", "약속 확정","강화도 여행"
            ),
            NotificationDoneData(
                "1일전", "약속 확정","강화도 여행"
            ),
            NotificationDoneData(
                "1일전", "약속 취소","강화도 여행"
            ),
        )
    }


}