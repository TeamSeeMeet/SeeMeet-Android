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
import org.seemeet.seemeet.data.model.request.invitation.RequestSendInvitationConfirm
import org.seemeet.seemeet.data.model.response.invitation.*
import retrofit2.HttpException

class SendViewModel(application: Application) : AndroidViewModel(application) {

    private val _sendInvitationData  = MutableLiveData<SendInvitationData>()
    val sendInvitationData : LiveData<SendInvitationData>
        get() = _sendInvitationData


    private val _sendInvitationDateList = MutableLiveData<List<SendInvitationDate>>()
    val sendInvitationDateList : LiveData<List<SendInvitationDate>>
        get() = _sendInvitationDateList

    private val _sendInvitationRejects = MutableLiveData<String>()
    val sendInvitationRejects : LiveData<String>
        get() = _sendInvitationRejects

    init {
        _sendInvitationData.value = SendInvitationData(
            "", listOf(SendGuest(-1, false, "")), SendHost(-1, ""),
            -1, -1,"","", isCancled = false, isConfirmed = false, isDeleted = false
        )
    }

<<<<<<< Updated upstream
    fun requestSendInvitationData(invitationId : Int) = viewModelScope.launch (Dispatchers.IO){
        try {
            val send = RetrofitBuilder.invitationService.getSendInvitationData(invitationId, SeeMeetSharedPreference.getToken()).data
            _sendInvitationData.postValue(send.invitation)
            _sendInvitationDateList.postValue(send.invitationDates)
        } catch (e: HttpException) {
            e.printStackTrace()
        }
=======
    fun requestSendInvitationData(invitationId : Int) = viewModelScope.launch (exceptionHandler){
        val send = RetrofitBuilder.invitationService.getSendInvitationData(invitationId, SeeMeetSharedPreference.getToken()).data
        _sendInvitationData.postValue(send.invitation)
        _sendInvitationDateList.postValue(send.invitationDates)
        _sendInvitationRejects.postValue(send.rejectGuests.joinToString(" ") { it.username })
>>>>>>> Stashed changes
    }

    fun requestSendConfirmInvitation(invitationId: Int, dateId : Int) = viewModelScope.launch (Dispatchers.IO){
        try {
            Log.d("**********sendId", invitationId.toString())
            val confirm = RequestSendInvitationConfirm(dateId)
            RetrofitBuilder.invitationService.setConfirmSendInvitation(invitationId, confirm, SeeMeetSharedPreference.getToken())
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }

    fun requestSendCancelInvitation(invitationId: Int) = viewModelScope.launch (Dispatchers.IO){
        try {
            RetrofitBuilder.invitationService.setCancelSendInvitation(invitationId, SeeMeetSharedPreference.getToken())
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }

}