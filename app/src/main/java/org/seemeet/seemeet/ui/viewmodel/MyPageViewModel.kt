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
import org.seemeet.seemeet.data.model.response.withdrawal.ResponseWithdrawal
import org.seemeet.seemeet.ui.registration.RegisterNameIdActivity

class MyPageViewModel(application: Application) : BaseViewModel(application) {

    private val _MyPageNameIdList = MutableLiveData<ResponseRegisterNameId>()
    val MyPageNameIdList: LiveData<ResponseRegisterNameId>
        get() = _MyPageNameIdList
    private val _MyPageWithdrawalList = MutableLiveData<ResponseWithdrawal>()
    val mypageName = MutableLiveData("")
    val mypageId = MutableLiveData("")

    private val _mypageStatus = MutableLiveData<Boolean>(false)
    val mypageStatus: LiveData<Boolean>
        get() = _mypageStatus

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
        _mypageStatus.value = true
    }

    fun requestMyPageWithdrawal(
    ) = viewModelScope.launch(exceptionHandler){
        _MyPageWithdrawalList.postValue(
            RetrofitBuilder.withdrawalService.putWithdrawal(
                SeeMeetSharedPreference.getToken()
            )
        )
    }
}