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
import org.seemeet.seemeet.ui.registration.RegisterNameIdActivity

class RegisterNameIdViewModel(application: Application) : BaseViewModel(application) {
    private val _registerNameIdList = MutableLiveData<ResponseRegisterNameId>()
    val registerNameIdList: LiveData<ResponseRegisterNameId>
        get() = _registerNameIdList
    val registerName = MutableLiveData("")
    val tvWarningId = MutableLiveData("")
    val registerId = MutableLiveData("")
    val id_cursorPos = MutableLiveData(0)
    val id_upperCase = MutableLiveData(false)
    val status = MutableLiveData<Boolean>(false)

    private val _registerStatus = MutableLiveData<Boolean>(false)
    val registerStatus: LiveData<Boolean>
        get() = _registerStatus

    fun requestRegisterNameIdList(
        name: String,
        nickname: String
    ) = viewModelScope.launch(exceptionHandler) {
        _registerNameIdList.postValue(
            RetrofitBuilder.registerService.putRegisterNameId(
                SeeMeetSharedPreference.getToken(),
                RequestRegisterNameId(name, nickname)
            )
        )
        _registerStatus.value = true
    }

    fun check() {
        // 길이가 0일 때
        if (registerId.value?.length == 0) {
            status.value = false
            tvWarningId.value = ""
        } else {
            // 길이가 7 미만일 때
            if (registerId.value?.length!! < 7) {
                status.value = false
                tvWarningId.value = "7자 이상 써주세요"
            } else {
                // 모두 숫자로만 이루어져있을 때
                if (RegisterNameIdActivity.isNumberFormat(registerId.value.toString())) {
                    status.value = false
                    tvWarningId.value = "숫자로만은 만들 수 없어요"
                }
                // 모두 _으로만 이루어져있을 때
                else if (RegisterNameIdActivity.is_Format(registerId.value.toString())) {
                    status.value = false
                    tvWarningId.value = "_로만은 만들 수 없어요"
                }
                // 모두 .으로만 이루어져있을 때
                else if (RegisterNameIdActivity.isdotFormat(registerId.value.toString())) {
                    status.value = false
                    tvWarningId.value = "마침표로만은 만들 수 없어요"
                }
                // 형식이 모두 올바를 때만 status=true
                else {
                    status.value = true
                    tvWarningId.value = ""
                }
            }
        }
    }
}