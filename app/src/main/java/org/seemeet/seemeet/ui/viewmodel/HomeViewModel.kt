package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.ReminderData

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    //리싸이클러뷰에 들어갈 리스트 변수
    private val _reminderList = MutableLiveData<List<ReminderData>>()
    val reminderList : LiveData<List<ReminderData>>
        get() = _reminderList

    private val _home_msg = MutableLiveData<String>()
    val homeMsg : LiveData<String>
        get() = _home_msg

    //임시로 넣을 더미데이터 셋팅. < 위의 리스트에 대입
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

    fun setHomeMsg() {
        _home_msg.value = "아싸 오늘은\n 친구 만나는 날이다!"
   }


}