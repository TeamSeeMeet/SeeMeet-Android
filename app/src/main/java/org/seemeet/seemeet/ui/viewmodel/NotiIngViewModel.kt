package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.seemeet.seemeet.data.local.NotificationIngData

class NotiIngViewModel(application: Application) : AndroidViewModel(application) {

    private val _ingList = MutableLiveData<List<NotificationIngData>>()
    val ingList : LiveData<List<NotificationIngData>>
        get() = _ingList


    fun setIngList() {
        _ingList.value = mutableListOf(
            NotificationIngData(
                "1일전", "받은 요청","친구의 요청에 답해보세요!", 2
            ),
            NotificationIngData(
                "1일전", "보낸 요청","친구 2명의 답변을 기다리고 있어요!", 1
            ),
            NotificationIngData(
                "1일전", "보낸 요청","친구 3명의 답변을 기다리고 있어요!", 1
            ),
            NotificationIngData(
                "1일전", "보낸 요청","친구가 답변을 모두 완료하였어요!", 1
            ),
            NotificationIngData(
                "1일전", "보낸 요청","친구의 답변을 기다리고 있어요!", 1
            ),
            NotificationIngData(
                "1일전", "받은 요청","친구의 요청에 답했어요!", 2
            ),

        )
    }


}