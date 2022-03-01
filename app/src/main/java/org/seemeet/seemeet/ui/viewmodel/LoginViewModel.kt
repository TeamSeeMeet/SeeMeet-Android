package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.request.login.RequestLoginList
import org.seemeet.seemeet.data.model.response.login.ResponseLoginList

class LoginViewModel(application: Application) : BaseViewModel(application) {

    var loginEmail = MutableLiveData("")
    val loginPw = MutableLiveData("")

    private val _loginList = MutableLiveData<ResponseLoginList>()
    val loginList: LiveData<ResponseLoginList>
        get() = _loginList

    fun requestLoginList(email: String, password: String) =
        viewModelScope.launch(exceptionHandler) {
            _loginList.postValue(
                RetrofitBuilder.loginService.postLogin(
                    RequestLoginList(email, password)
                )
            )
        }
}