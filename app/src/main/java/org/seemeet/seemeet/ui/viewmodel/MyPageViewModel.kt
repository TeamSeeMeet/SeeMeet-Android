package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.request.mypage.RequestChangePush
import org.seemeet.seemeet.data.model.request.register.RequestRegisterNameId
import org.seemeet.seemeet.data.model.response.mypage.ResponseMyPageProfile
import org.seemeet.seemeet.data.model.response.register.ResponseRegisterNameId
import org.seemeet.seemeet.data.model.response.withdrawal.ResponseWithdrawal
import org.seemeet.seemeet.ui.registration.RegisterNameIdActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageViewModel(application: Application) : BaseViewModel(application) {

    private val _MyPageNameIdList = MutableLiveData<ResponseRegisterNameId>()
    val MyPageNameIdList: LiveData<ResponseRegisterNameId>
        get() = _MyPageNameIdList
    private val _MyPageWithdrawalList = MutableLiveData<ResponseWithdrawal>()
    val mypageName = MutableLiveData("")
    val mypageId = MutableLiveData("")
    val warning = MutableLiveData("")
    val status = MutableLiveData(false)
    val id_cursorPos = MutableLiveData(0)
    val id_upperCase = MutableLiveData(false)

    private val _mypageStatus = MutableLiveData<Boolean>(false)
    val mypageStatus: LiveData<Boolean>
        get() = _mypageStatus

    private val _profileStatus = MutableLiveData<Int>(-1)
    val profileStatus: LiveData<Int>
        get() = _profileStatus

    fun requestMypageProfile(body: MultipartBody.Part) {
        RetrofitBuilder.mypageService.postProfile(
            SeeMeetSharedPreference.getToken(),
            body
        ).enqueue(object : Callback<ResponseMyPageProfile> {
            override fun onFailure(call: Call<ResponseMyPageProfile>, t: Throwable) {
                _profileStatus.value = 0
            }

            override fun onResponse(
                call: Call<ResponseMyPageProfile>,
                response: Response<ResponseMyPageProfile>
            ) {
                _profileStatus.value = 1
            }
        })
    }

    fun requestMyPageNameIdList(
        name: String,
        nickname: String
    ) = viewModelScope.launch(exceptionHandler) {
        _MyPageNameIdList.postValue(
            RetrofitBuilder.registerService.putRegisterNameId(
                SeeMeetSharedPreference.getToken(),
                RequestRegisterNameId(name, nickname)
            )
        )
        _mypageStatus.value = true
    }

    fun requestMyPageWithdrawal(
    ) = viewModelScope.launch(exceptionHandler) {
        _MyPageWithdrawalList.postValue(
            RetrofitBuilder.withdrawalService.putWithdrawal(
                SeeMeetSharedPreference.getToken()
            )
        )
    }

    //fcm ?????? ?????? ??????
    fun requestPushTokenNull() = viewModelScope.launch(exceptionHandler) {
        RetrofitBuilder.mypageService.postChangePush(
            SeeMeetSharedPreference.getToken(),
            RequestChangePush(SeeMeetSharedPreference.getPushOn(), null)
        )
    }

    fun check() {
        // ???????????? ????????? ????????? 0??? ??? status false
        if (mypageName.value?.length == 0) {
            status.value = false
            warning.value = "????????? ??????????????????"
        } else if (mypageId.value?.length == 0) {
            status.value = false
            warning.value = "???????????? ??????????????????"
        } else {
            // ????????? 7 ????????? ??? status false
            if (mypageId.value?.length!! < 7) {
                status.value = false
                warning.value = "???????????? 7??? ?????? ??????????????????"
            } else {
                // ???????????? ?????? ???????????? ??? status false
                if (!RegisterNameIdActivity.isIdFormat(mypageId.value.toString())) {
                    status.value = false
                    warning.value = "???????????? ?????????, ??????, ??????, ???????????? ?????? ????????????"
                } else {
                    // ?????? ???????????? ?????????????????? ??? status false
                    if (RegisterNameIdActivity.isNumberFormat(mypageId.value.toString())) {
                        status.value = false
                        warning.value = "???????????? ??????????????? ?????? ??? ?????????"
                    }
                    // ?????? _????????? ?????????????????? ??? status false
                    else if (RegisterNameIdActivity.is_Format(mypageId.value.toString())) {
                        status.value = false
                        warning.value = "???????????? _????????? ?????? ??? ?????????"
                    }
                    // ?????? .????????? ?????????????????? ??? status false
                    else if (RegisterNameIdActivity.isdotFormat(mypageId.value.toString())) {
                        status.value = false
                        warning.value = "???????????? ?????????????????? ?????? ??? ?????????"
                    }
                    // ????????? ?????? ????????? ??? status true
                    else {
                        status.value = true
                        warning.value = ""
                    }
                }
            }
        }
    }
}