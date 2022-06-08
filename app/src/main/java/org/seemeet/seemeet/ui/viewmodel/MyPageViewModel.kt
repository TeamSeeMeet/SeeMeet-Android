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

class MyPageViewModel(application: Application) : BaseViewModel(application) {

    private val _MyPageNameIdList = MutableLiveData<ResponseRegisterNameId>()
    val MyPageNameIdList: LiveData<ResponseRegisterNameId>
        get() = _MyPageNameIdList
    val mypageName = MutableLiveData("")
    val mypageId = MutableLiveData("")

    fun requestMyPageNameIdList(
        name: String,
        nickname: String
    ) = viewModelScope.launch(exceptionHandler) {
        _MyPageNameIdList.postValue(
            RetrofitBuilder.registerService.putRegisterNameId(
                SeeMeetSharedPreference.getToken(),
                RequestRegisterNameId(name, nickname)
            )
        )
    }
}