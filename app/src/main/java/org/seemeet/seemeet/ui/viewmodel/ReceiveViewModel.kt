package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.request.invitationResponse.RequestInvitationResponse
import org.seemeet.seemeet.data.model.response.invitation.ReceiveInvitationData
import org.seemeet.seemeet.data.model.response.invitation.ReceiveInvitationDate
import org.seemeet.seemeet.data.model.response.plan.PlanResponseData
import retrofit2.HttpException

class ReceiveViewModel(application: Application) : BaseViewModel(application) {

    private val _receiveInvitationData = MutableLiveData<ReceiveInvitationData>()
    val receiveInvitationData : LiveData<ReceiveInvitationData>
        get() = _receiveInvitationData

    private val _receiveInvitationDateList  = MutableLiveData<List<ReceiveInvitationDate>>()
    val receiveInvitationDateList : LiveData<List<ReceiveInvitationDate>>
        get() = _receiveInvitationDateList

    private val _receivePlanResponseList = MutableLiveData<List<PlanResponseData>>()
    val receivePlanResponseList : LiveData<List<PlanResponseData>>
        get() = _receivePlanResponseList

    fun requestReceiveInvitation(invitationId : Int) = viewModelScope.launch (exceptionHandler){
        Log.d("**********sendId", invitationId.toString())
        _receiveInvitationData.postValue(RetrofitBuilder.invitationService.getReceiveInvitationData(invitationId, SeeMeetSharedPreference.getToken()).data)
    }

    fun requestReceivePlanResponse(dateId : Int) = viewModelScope.launch(exceptionHandler){
        _receivePlanResponseList.postValue(RetrofitBuilder.planService.getPlanResponse(dateId, SeeMeetSharedPreference.getToken()).data)
    }

    fun requestReceiveYesInvitation(invitationId: Int, dateIds : List<Int>) = viewModelScope.launch (exceptionHandler){
        Log.d("**********invitationId", invitationId.toString())
        val requestYesInvitationResponse = RequestInvitationResponse(dateIds)
        RetrofitBuilder.invitationService.setYesReceiveInvitationResponse(invitationId, requestYesInvitationResponse, SeeMeetSharedPreference.getToken())
    }

    fun requestReceiveNoInvitation(invitationId: Int) = viewModelScope.launch (exceptionHandler){
        Log.d("**********invitationId", invitationId.toString())
        RetrofitBuilder.invitationService.setNoReceiveInvitationResponse(invitationId, SeeMeetSharedPreference.getToken())
    }

    var isClicked : MutableLiveData<Int> = MutableLiveData(0)

    fun setIsClicked(cb : ReceiveInvitationDate){
        if(cb.isSelected)
            isClicked.value = (isClicked.value)?.plus(1)
        else
            isClicked.value = (isClicked.value)?.minus(1)
    }

    fun setReceiveInvitationDate() {
        _receiveInvitationDateList.postValue(receiveInvitationData.value?.invitationDates)
    }

}