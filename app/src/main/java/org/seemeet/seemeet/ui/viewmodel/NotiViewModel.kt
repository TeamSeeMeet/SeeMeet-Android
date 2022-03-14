package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.response.invitation.ConfirmedAndCanceld
import org.seemeet.seemeet.data.model.response.invitation.Invitation
import org.seemeet.seemeet.data.model.response.invitation.ResponseInvitationList

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


    //약속 내역 모두 가져오는 함수
    fun requestAllInvitationList() = viewModelScope.launch(exceptionHandler) {
        _invitationList.postValue(
            RetrofitBuilder.invitationService.getAllInvitationList(
                SeeMeetSharedPreference.getToken()
            )
        )
    }

    fun setInviInProgressList() {
        _inviInProgressList.postValue(invitationList.value?.data?.invitations)
    }

    fun setInviDoneList() {
        _inviDoneList.postValue(invitationList.value?.data?.confirmedAndCanceld)
    }
}