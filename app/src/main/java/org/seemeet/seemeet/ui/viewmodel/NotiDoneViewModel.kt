package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.seemeet.seemeet.data.local.NotificationDoneData
import org.seemeet.seemeet.data.local.NotificationFriendData

class NotiDoneViewModel(application: Application) : AndroidViewModel(application) {

    private val _doneList = MutableLiveData<List<NotificationDoneData>>()
    val doneList : LiveData<List<NotificationDoneData>>
        get() = _doneList


    fun setDoneList() {
        _doneList.value = mutableListOf(
            NotificationDoneData(
                "1일전", "받은 요청","친구의 요청에 답해보세요!", mutableListOf(
                    NotificationFriendData("김준희", true),
                )
            ),
        )
    }
}