package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.response.invitation.ConfirmedAndCanceld
import org.seemeet.seemeet.data.model.response.invitation.Invitation
import org.seemeet.seemeet.data.model.response.invitation.ResponseInvitationList
import retrofit2.HttpException

class NotiViewModel(application: Application) : BaseViewModel(application) {

    private val _invitationList = MutableLiveData<ResponseInvitationList>()
    val invitationList: LiveData<ResponseInvitationList>
        get() = _invitationList

    private val _inviInProgressList = MutableLiveData<List<Invitation>>()
    val inviInProgressList: LiveData<List<Invitation>>
        get() = _inviInProgressList

    private val _inviDoneList = MutableLiveData<List<ConfirmedAndCanceld>>()
    val inviDoneList: LiveData<List<ConfirmedAndCanceld>>
        get() = _inviDoneList

    private val _doneList = MutableLiveData<ResponseInvitationList>()
    val doneList : LiveData<ResponseInvitationList>
        get() = _doneList


    //약속 내역 모두 가져오는 함수
    fun requestAllInvitationList() = viewModelScope.launch(exceptionHandler) {
        _invitationList.postValue(
            RetrofitBuilder.invitationService.getAllInvitationList(
                SeeMeetSharedPreference.getToken()
            )
        )
    }

    fun deleteInvitation(Id: Int) = viewModelScope.launch (Dispatchers.IO){
        try {
            RetrofitBuilder.invitationService.deleteInvitation(Id, SeeMeetSharedPreference.getToken())
        } catch (e: HttpException) {
        }
    }

    fun setInviInProgressList() {
        _inviInProgressList.postValue(invitationList.value?.data?.invitations)
    }

    fun setInviDoneList() {
        _inviDoneList.postValue(invitationList.value?.data?.confirmedAndCanceld)
    }
}