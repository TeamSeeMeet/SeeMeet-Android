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
import retrofit2.HttpException

class ChangePWViewModel(application: Application) : BaseViewModel(application) {

    private val _MyPageChangePWList = MutableLiveData<ResponseChangePW>()
    private val _status = MutableLiveData<Boolean>()
    val newPW = MutableLiveData("")
    val tvWarningPW = MutableLiveData("")
    val checkPW = MutableLiveData("")
    val tvWarningCheckPW = MutableLiveData("")
    val status: LiveData<Boolean>
        get() = _status

    fun requestChangePW(
        password: String,
        passwordConfirm: String
    ) = viewModelScope.launch(exceptionHandler) {
        try {
            _MyPageChangePWList.postValue(
                RetrofitBuilder.mypageService.putChangePW(
                    SeeMeetSharedPreference.getToken(),
                    RequestChangePW(password, passwordConfirm)
                )
            )
            _status.value = true
        } catch (e: HttpException) {
            _status.value = false
        }
    }
}