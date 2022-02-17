package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.request.register.RequestRegisterList
import org.seemeet.seemeet.data.model.response.register.ResponseRegisterList
import retrofit2.HttpException

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val _registerList = MutableLiveData<ResponseRegisterList>()
    private val _status = MutableLiveData<Boolean>()
    val status : LiveData<Boolean>
        get() = _status

    fun requestRegisterList(username : String, email : String, password : String, passwordConfirm : String)
        = viewModelScope.launch {
            try {
                _registerList.postValue(
                    RetrofitBuilder.registerService.postRegister(
                        RequestRegisterList(username,email,password,passwordConfirm)
                    )
                )
                _status.postValue(true)
            }catch (e: HttpException){
                _status.value = false
            }
        }
}