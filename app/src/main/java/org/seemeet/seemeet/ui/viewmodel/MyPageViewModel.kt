package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.request.register.RequestRegisterNameId
import org.seemeet.seemeet.data.model.response.register.ResponseRegisterNameId
import org.seemeet.seemeet.data.model.response.withdrawal.ResponseWithdrawal
import org.seemeet.seemeet.ui.registration.RegisterNameIdActivity

class MyPageViewModel(application: Application) : BaseViewModel(application) {

    private val _MyPageNameIdList = MutableLiveData<ResponseRegisterNameId>()
    val MyPageNameIdList: LiveData<ResponseRegisterNameId>
        get() = _MyPageNameIdList
    private val _MyPageWithdrawalList = MutableLiveData<ResponseWithdrawal>()
    val mypageName = MutableLiveData("")
    val mypageId = MutableLiveData("")
    val warning = MutableLiveData("")
    val temp = MutableLiveData<Boolean>(false)
    val status = MutableLiveData<Int>(0)

    private val _mypageStatus = MutableLiveData<Boolean>(false)
    val mypageStatus: LiveData<Boolean>
        get() = _mypageStatus

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

    fun check() {
        // 불가능한 문자 입력했던 기록이 있을 때 status 1
        if (temp.value == true) {
            status.value = 1
            warning.value = "아이디는 알파벳, 숫자, 밑줄, 마침표만 사용 가능해요"
            temp.value = false
        } else {
            // 길이가 0일 때 status 0
            if (mypageId.value?.length == 0) {
                status.value = 0
                warning.value = ""
            } else {
                // 불가능한 문자 입력했을 때 status 2
                if (!RegisterNameIdActivity.isIdFormat(mypageId.value.toString())) {
                    status.value = 2
                    temp.value = true
                } else {
                    // 길이가 7 미만일 때 status 0
                    if (mypageId.value?.length!! < 7) {
                        status.value = 0
                        warning.value = "7자 이상 써주세요"
                    } else {
                        // 모두 숫자로만 이루어져있을 때 status 0
                        if (RegisterNameIdActivity.isNumberFormat(mypageId.value.toString())) {
                            status.value = 0
                            warning.value = "숫자로만은 만들 수 없어요"
                        }

                        // 모두 _으로만 이루어져있을 때 status 0
                        else if (RegisterNameIdActivity.is_Format(mypageId.value.toString())) {
                            status.value = 0
                            warning.value = "_로만은 만들 수 없어요"
                        }

                        // 모두 .으로만 이루어져있을 때 status 0
                        else if (RegisterNameIdActivity.isdotFormat(mypageId.value.toString())) {
                            status.value = 0
                            warning.value = "마침표로만은 만들 수 없어요"
                        }

                        // 형식이 모두 올바를 때 status 3
                        else {
                            status.value = 3
                            warning.value = ""
                        }
                    }
                }
            }
        }
    }

    fun requestMyPageWithdrawal(
    ) = viewModelScope.launch(exceptionHandler) {
        _MyPageWithdrawalList.postValue(
            RetrofitBuilder.withdrawalService.putWithdrawal(
                SeeMeetSharedPreference.getToken()
            )
        )
    }
}