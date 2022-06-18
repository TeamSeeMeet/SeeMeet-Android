package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.request.mypage.RequestChangePW
import org.seemeet.seemeet.data.model.response.mypage.ResponseChangePW

class ChangePWViewModel(application: Application) : BaseViewModel(application) {

    private val _MyPageChangePWList = MutableLiveData<ResponseChangePW>()
    val MyPageChangePWList: LiveData<ResponseChangePW>
        get() = _MyPageChangePWList

    val newPW = MutableLiveData("")
    val tvWarningPW = MutableLiveData("")
    val checkPW = MutableLiveData("")
    val tvWarningCheckPW = MutableLiveData("")

    fun requestChangePW(
        password: String,
        passwordConfirm: String
    ) = viewModelScope.launch(exceptionHandler) {

        _MyPageChangePWList.postValue(
            RetrofitBuilder.mypageService.putChangePW(
                SeeMeetSharedPreference.getToken(),
                RequestChangePW(password, passwordConfirm)
            )
        )
    }
}