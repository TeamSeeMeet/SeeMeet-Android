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
import org.seemeet.seemeet.data.model.response.invitation.ConfirmedAndCanceld
import org.seemeet.seemeet.data.model.response.plan.LastPlanData
import org.seemeet.seemeet.data.model.response.plan.ResponseComePlanList
import retrofit2.HttpException
import java.time.LocalDate
import java.time.YearMonth

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _comePlanList = MutableLiveData<ResponseComePlanList>()
    val comePlanList : LiveData<ResponseComePlanList>
        get() = _comePlanList

    private val _homeBanner = MutableLiveData<Int>()
    val homeBanner : LiveData<Int>
        get() = _homeBanner
    var day : Int = 0

    private val _friendList = MutableLiveData<ResponseFriendList>()
    val friendList : LiveData<ResponseFriendList>
        get() = _friendList

    private val _lastPlan = MutableLiveData<LastPlanData>()
    val lastPlan : LiveData<LastPlanData>
        get() = _lastPlan


    private val _lastConfirmedInvitation = MutableLiveData<ConfirmedAndCanceld>()
    val lastConfirmedInvitation : LiveData<ConfirmedAndCanceld>
        get() = _lastConfirmedInvitation


    //서버통신
    //  친구 수 가져오기.
    fun requestFriendList()  = viewModelScope.launch(Dispatchers.IO) {
        try {
            _friendList.postValue(RetrofitBuilder.friendService.getFriendList(getToken()))
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }

    // 다가오는 약속
    fun requestComePlanList() = viewModelScope.launch (Dispatchers.IO){
        try {
            val month = YearMonth.now().monthValue
            val year = YearMonth.now().year
            _comePlanList.postValue(RetrofitBuilder.planService.getComePlan(year, month, getToken()))
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }

    // 확정된 약속 중 최근 거 하나 가져오기.
    fun requestLastConfirmedInvitation() = viewModelScope.launch (Dispatchers.IO){
        try {
            _lastConfirmedInvitation.postValue(RetrofitBuilder.invitationService.
            getAllInvitationList(getToken()).data.confirmedAndCanceld[0])
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }

    // 마지막 약속 가지고 오기
    fun requestLastPlanData() = viewModelScope.launch (Dispatchers.IO){
        try {
            val month = YearMonth.now().monthValue
            val year = YearMonth.now().year
           /* val day = LocalDate.now().dayOfMonth*/
            val day = 22
            _lastPlan.postValue(RetrofitBuilder.planService.getLastPlan(year, month, day, getToken()).data)
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }


    //더미

    //리싸이클러뷰에 들어갈 리스트 변수
    private val _reminderList = MutableLiveData<List<ReminderData>>()
    val reminderList : LiveData<List<ReminderData>>
        get() = _reminderList

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


}