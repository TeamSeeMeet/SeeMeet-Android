package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.seemeet.seemeet.data.SeeMeetSharedPreference.getToken
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.request.mypage.RequestChangePush
import org.seemeet.seemeet.data.model.response.friend.ResponseFriendList
import org.seemeet.seemeet.data.model.response.mypage.ResponseChangePush
import org.seemeet.seemeet.data.model.response.plan.LastPlanData
import org.seemeet.seemeet.data.model.response.plan.ResponseComePlanList
import retrofit2.HttpException
import java.time.LocalDate
import java.time.YearMonth

class HomeViewModel(application: Application) : BaseViewModel(application) {

    private val _comePlanList = MutableLiveData<ResponseComePlanList>()
    val comePlanList: LiveData<ResponseComePlanList>
        get() = _comePlanList

    private val _homeBanner = MutableLiveData<Int>()
    val homeBanner: LiveData<Int>
        get() = _homeBanner

    private val _homeBannerDay = MutableLiveData<Int>()
    val homeBannerDay: LiveData<Int>
        get() = _homeBannerDay

    private val _friendList = MutableLiveData<ResponseFriendList>()
    val friendList: LiveData<ResponseFriendList>
        get() = _friendList

    private val _lastPlan = MutableLiveData<LastPlanData>()
    val lastPlan: LiveData<LastPlanData>
        get() = _lastPlan

    private val _pushOn = MutableLiveData<ResponseChangePush>()
    val pushOn: LiveData<ResponseChangePush>
        get() = _pushOn

    init {
        _homeBanner.value = 1
        _homeBannerDay.value = -1
    }


    //서버통신
    //  친구 수 가져오기.
    fun requestFriendList() = viewModelScope.launch(exceptionHandler) {
        _friendList.postValue(RetrofitBuilder.friendService.getFriendList(getToken()))
    }

    // 다가오는 약속
    fun requestComePlanList() = viewModelScope.launch(exceptionHandler) {
        val month = YearMonth.now().monthValue
        val year = YearMonth.now().year
        _comePlanList.postValue(RetrofitBuilder.planService.getComePlan(year, month, getToken()))
    }


    // 마지막 약속 가지고 오기
    fun requestLastPlanData() = viewModelScope.launch(exceptionHandler) {
        val month = YearMonth.now().monthValue
        val year = YearMonth.now().year
        val day = LocalDate.now().dayOfMonth
        _lastPlan.postValue(
            RetrofitBuilder.planService.getLastPlan(
                year,
                month,
                day,
                getToken()
            ).data
        )

    }

    //home 베너 이미지랑 텍스트 설정.
    fun setHomeBannerFlagAndDay(flag: Int, day: Int) {
        _homeBanner.postValue(flag)
        _homeBannerDay.postValue(day)
    }

    //푸시알림 on/off 설정
    fun setPushNotification(push: Boolean, fcm: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _pushOn.postValue(
                RetrofitBuilder.mypageService.postChangePush(
                    getToken(),
                    RequestChangePush(push, fcm)
                ))
        } catch (e: HttpException) {
        }
    }
}