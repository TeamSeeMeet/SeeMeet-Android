package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.request.login.RequestLoginList
import org.seemeet.seemeet.data.model.response.login.ResponseLoginList
import retrofit2.HttpException

class LoginViewModel(application: Application) : BaseViewModel(application) {

    var loginEmail = MutableLiveData("")
    val loginPw = MutableLiveData("")
    //private val pattern: Pattern = Patterns.EMAIL_ADDRESS

    private val _loginList = MutableLiveData<ResponseLoginList>()
    val loginList: LiveData<ResponseLoginList>
        get() = _loginList

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean>
        get() = _loginStatus

    var errorMessage: String = ""

    fun requestLoginList(email: String, password: String) =
        viewModelScope.launch(exceptionHandler) {
            try {
                _loginList.postValue(
                    RetrofitBuilder.loginService.postLogin(
                        RequestLoginList(email, password)
                    )
                )
                _loginStatus.postValue(true)
            } catch (e: HttpException) {
                errorMessage = e.response()!!.errorBody()!!.string().split("\"")[7]
                _loginStatus.postValue(false)
            }
        }

    //fun isNullOrBlank() = loginEmail.value.isNullOrBlank() || loginPw.value.isNullOrBlank() || !pattern.matcher(loginEmail.value).matches()
}