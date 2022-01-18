package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.SeeMeetSharedPreference.getToken
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.local.ReminderData
import org.seemeet.seemeet.data.model.response.friend.ResponseFriendList
import retrofit2.HttpException

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    //리싸이클러뷰에 들어갈 리스트 변수
    private val _reminderList = MutableLiveData<List<ReminderData>>()
    val reminderList : LiveData<List<ReminderData>>
        get() = _reminderList

    private val _homeBanner = MutableLiveData<Int>()
    val homeBanner : LiveData<Int>
        get() = _homeBanner

    private val _friendList = MutableLiveData<ResponseFriendList>()
    val friendList : LiveData<ResponseFriendList>
        get() = _friendList

    var day : Int = 0


    //서버통신 _ 친구 수 가져오기.
    fun requestFriendList()  = viewModelScope.launch(Dispatchers.IO) {
        try {
            Log.d("friend token firend", getToken())
            _friendList.postValue(RetrofitBuilder.friendService.getFriendList(getToken()))
        } catch (e: HttpException) {

        }
    }

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

    fun setHomeBanner(flag : Int) {
        // 이친구는 확정된 약속을 기준으로 함.
        // 일단 플래그값으로 상황 구별해서 더미 넣어보기 _ 이후에는 flag 지우고 여기서 위에서 통신한 값을 가지고 계산해서 넣도록 하자!!
        // 1. 맨 처음 (친구도 없고 약속 잡은 적도 없음)
        // 2. 약속 처음 ( 친구는 있고, 약속 잡은 적은 없음)
        // 3. 오늘이 친구 만나는 날!
        // 4 . 다가오는 약속이 있는 경우
        // 5. 다가오는 약속도 없는 경우
            // 5는 확정 약속이 지난 일을 기준으로 나눔.

        _homeBanner.value = 1
   }

}