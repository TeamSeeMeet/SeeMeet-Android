package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import org.seemeet.seemeet.ui.registration.RegisterNameIdActivity

class RegisterNameIdViewModel(application: Application) : BaseViewModel(application) {
    val registerName = MutableLiveData("")
    val tvWarningId = MutableLiveData("")
    val registerId = MutableLiveData("")
    val temp = MutableLiveData<Boolean>(false)

    val status = MutableLiveData<Int>(0)

    fun check() {
        // 불가능한 문자 입력했던 기록이 있을 때 status 1
        if (temp.value == true) {
            status.value = 1
            temp.value = false
        } else {
            // 길이가 0일 때 status 0
            if (registerId.value?.length == 0) {
                status.value = 0
            } else {
                // 불가능한 문자 입력했을 때 status 7
                if (!RegisterNameIdActivity.isIdFormat(registerId.value.toString())) {
                    status.value = 7
                    temp.value = true
                } else {
                    // 길이가 7 미만일 때 status 2
                    if (registerId.value?.length!! < 7) {
                        status.value = 2
                    } else {
                        // 모두 숫자로만 이루어져있을 때 status 3
                        if (RegisterNameIdActivity.isNumberFormat(registerId.value.toString()))
                            status.value = 3

                        // 모두 _으로만 이루어져있을 때 status 4
                        else if (RegisterNameIdActivity.is_Format(registerId.value.toString()))
                            status.value = 4

                        // 모두 .으로만 이루어져있을 때 status 5
                        else if (RegisterNameIdActivity.isdotFormat(registerId.value.toString()))
                            status.value = 5

                        // 형식이 모두 올바를 때 status 6
                        else
                            status.value = 6
                    }
                }
            }
        }
    }
}