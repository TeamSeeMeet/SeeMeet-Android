package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
//import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//import okhttp3.ResponseBody
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.request.login.RequestLoginList
//import org.seemeet.seemeet.data.model.response.login.ResponseErrorLoginList
import org.seemeet.seemeet.data.model.response.login.ResponseLoginList
import retrofit2.HttpException

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val _loginList = MutableLiveData<ResponseLoginList>()
    val loginList: LiveData<ResponseLoginList>
        get() = _loginList

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean>
        get() = _loginStatus

    /*
    private val _errorloginList = MutableLiveData<ResponseErrorLoginList>()
    val errorloginList: LiveData<ResponseErrorLoginList>
        get() = _errorloginList
     */

    var errorMessage: String = ""

    fun requestLoginList(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _loginList.postValue(
                RetrofitBuilder.loginService.postLogin(
                    RequestLoginList(email, password)
                )
            )
            _loginStatus.postValue(true)
        } catch (e: HttpException) {
            //requestErrorLoginList(e.response()!!.errorBody()!!)
            errorMessage = e.response()!!.errorBody()!!.string().split("\"")[7]
            _loginStatus.postValue(false)
        }
    }

    /*
    fun requestErrorLoginList(errorBody: ResponseBody)
        =viewModelScope.launch {
            try{
                Log.d("error try", "1")
                _errorloginList.postValue(
                    RetrofitBuilder.seeMeetRetrofit.responseBodyConverter<ResponseErrorLoginList>(
                        ResponseErrorLoginList::class.java,
                        ResponseErrorLoginList::class.java.annotations
                    ).convert(errorBody)
                )
                Log.d("error try", "2")
            }catch (e: HttpException){
            }
    }*/
}