package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.request.register.RequestRegisterList
import org.seemeet.seemeet.data.model.response.register.ResponseRegisterList
import retrofit2.HttpException

class RegisterViewModel(application: Application) : BaseViewModel(application) {
    private val _registerList = MutableLiveData<ResponseRegisterList>()
    val registerList: LiveData<ResponseRegisterList>
        get() = _registerList
    private val _status = MutableLiveData<Boolean>()
    val registerEmail = MutableLiveData("")
    val registerPw = MutableLiveData("")
    val registerCheckpw = MutableLiveData("")
    val tvWarningEmail = MutableLiveData("")
    val tvWarningPw = MutableLiveData("")
    val tvWarningCheckpw = MutableLiveData("")

    val status: LiveData<Boolean>
        get() = _status

    fun requestRegisterList(
        email: String,
        password: String,
        passwordConfirm: String
    ) = viewModelScope.launch(exceptionHandler) {
        try {
            _registerList.postValue(
                RetrofitBuilder.registerService.postRegister(
                    RequestRegisterList(email, password, passwordConfirm)
                )
            )
            _status.postValue(true)
        } catch (e: HttpException) {
            _status.value = false
        }
    }
}