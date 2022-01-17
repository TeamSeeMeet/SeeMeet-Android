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
                "1일전", true,"강화도 여행", mutableListOf(
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", false),
                )
            ),
            NotificationDoneData(
                "1일전", false,"강화도 여행", mutableListOf(
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", false),
                )
            ),
            NotificationDoneData(
                "1일전", false,"강화도 여행", mutableListOf(
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", false),
                )
            ),
            NotificationDoneData(
                "1일전", true,"강화도 여행", mutableListOf(
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", false),
                )
            ),
        )
    }
}