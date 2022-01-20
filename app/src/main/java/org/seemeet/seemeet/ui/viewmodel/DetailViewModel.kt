package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.seemeet.seemeet.data.local.NotificationDoneData
import org.seemeet.seemeet.data.local.NotificationFriendData

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    //리싸이클러뷰에 들어갈 리스트 변수
    private val _detailFriendList = MutableLiveData<List<NotificationDoneData>>()
    val detailFriendList : LiveData<List<NotificationDoneData>>
        get() = _detailFriendList


    //임시로 넣을 더미데이터 셋팅. < 위의 리스트에 대입
    fun setDetailFriendList() {
        _detailFriendList.value = mutableListOf(
            NotificationDoneData(
                "1일전", true,"강화도 여행", listOf(
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", false),
                )
            ),
        )
    }


}