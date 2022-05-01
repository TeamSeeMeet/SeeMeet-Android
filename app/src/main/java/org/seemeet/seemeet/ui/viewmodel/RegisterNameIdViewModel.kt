package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.request.register.RequestRegisterNameId
import org.seemeet.seemeet.data.model.response.register.ResponseRegisterNameId
import org.seemeet.seemeet.ui.registration.RegisterNameIdActivity
import retrofit2.HttpException

class RegisterNameIdViewModel(application: Application) : BaseViewModel(application) {
    private val _registerNameIdList = MutableLiveData<ResponseRegisterNameId>()
    val registerName = MutableLiveData("")
    val tvWarningId = MutableLiveData("")
    val registerId = MutableLiveData("")
    val temp = MutableLiveData<Boolean>(false)

    val status = MutableLiveData<Int>(0)

    fun requestRegisterNameIdList(
        name: String,
        nickname: String
    ) = viewModelScope.launch(exceptionHandler) {
        try {
            _registerNameIdList.postValue(
                RetrofitBuilder.registerService.putRegisterNameId(
                    SeeMeetSharedPreference.getToken(),
                    RequestRegisterNameId(name, nickname)
                )
            )
        } catch (e: HttpException) {
            Log.e("network error", e.toString())
        }
    }

    fun check() {
        // 불가능한 문자 입력했던 기록이 있을 때 status 1
        if (temp.value == true) {
            status.value = 1
            tvWarningId.value = "아이디는 알파벳, 숫자, 밑줄, 마침표만 사용 가능해요"
            temp.value = false
        } else {
            // 길이가 0일 때 status 0
            if (registerId.value?.length == 0) {
                status.value = 0
                tvWarningId.value = ""
            } else {
                // 불가능한 문자 입력했을 때 status 2
                if (!RegisterNameIdActivity.isIdFormat(registerId.value.toString())) {
                    status.value = 2
                    temp.value = true
                } else {
                    // 길이가 7 미만일 때 status 0
                    if (registerId.value?.length!! < 7) {
                        status.value = 0
                        tvWarningId.value = "7자 이상 써주세요"
                    } else {
                        // 모두 숫자로만 이루어져있을 때 status 0
                        if (RegisterNameIdActivity.isNumberFormat(registerId.value.toString())) {
                            status.value = 0
                            tvWarningId.value = "숫자로만은 만들 수 없어요"
                        }

                        // 모두 _으로만 이루어져있을 때 status 0
                        else if (RegisterNameIdActivity.is_Format(registerId.value.toString())) {
                            status.value = 0
                            tvWarningId.value = "_로만은 만들 수 없어요"
                        }

                        // 모두 .으로만 이루어져있을 때 status 0
                        else if (RegisterNameIdActivity.isdotFormat(registerId.value.toString())) {
                            status.value = 0
                            tvWarningId.value = "마침표로만은 만들 수 없어요"
                        }

                        // 형식이 모두 올바를 때 status 3
                        else {
                            status.value = 3
                            tvWarningId.value = ""
                        }
                    }
                }
            }
        }
    }
}