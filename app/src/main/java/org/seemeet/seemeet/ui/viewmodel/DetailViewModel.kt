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
import org.seemeet.seemeet.data.model.response.invitation.ResponseCanceledInvitation
import org.seemeet.seemeet.data.model.response.invitation.ResponseReceiveInvitation
import org.seemeet.seemeet.data.model.response.plan.ResponsePlanDetail
import retrofit2.HttpException

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _planDetail = MutableLiveData<List<ResponsePlanDetail>>()
    val planDetail: LiveData<List<ResponsePlanDetail>>
        get() = _planDetail

    private val _canceledDetail = MutableLiveData<List<ResponseCanceledInvitation>>()
    val canceledDetail: LiveData<List<ResponseCanceledInvitation>>
        get() = _canceledDetail

    private val _canceledHostDetail = MutableLiveData<List<ResponseReceiveInvitation>>()
    val canceledHostDetail: LiveData<List<ResponseReceiveInvitation>>
        get() = _canceledHostDetail

    fun requestPlanId(planId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _planDetail.postValue(listOf(RetrofitBuilder.planService.getPlanDetail(planId, SeeMeetSharedPreference.getToken())))
        } catch (e: HttpException) {
        }
    }

    fun requestInvitationId(invitationId : Int) = viewModelScope.launch (Dispatchers.IO){
        try {
            Log.d("**********invitationId", invitationId.toString())
            _canceledDetail.postValue(listOf(RetrofitBuilder.invitationService.getCanceledInvitationData(invitationId, SeeMeetSharedPreference.getToken())))
        } catch (e: HttpException) {
        }
    }

    fun requestInvitationDetail(invitationId : Int) = viewModelScope.launch (Dispatchers.IO){
        try {
            _canceledHostDetail.postValue(listOf(RetrofitBuilder.invitationService.getReceiveInvitationData(invitationId, SeeMeetSharedPreference.getToken())))
        } catch (e: HttpException) {
        }
    }

    fun deletePlan(planId: Int) = viewModelScope.launch (Dispatchers.IO){
        try {
            RetrofitBuilder.planService.deletePlan(planId, SeeMeetSharedPreference.getToken())
        } catch (e: HttpException) {
        }
    }
}