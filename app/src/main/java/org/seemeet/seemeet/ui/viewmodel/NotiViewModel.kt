package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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

class NotiViewModel(application: Application) : AndroidViewModel(application) {

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
    fun requestAllInvitationList() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _invitationList.postValue(
                RetrofitBuilder.invitationService.getAllInvitationList(
                    SeeMeetSharedPreference.getToken()
                )
            )
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }

    fun setInviInProgressList() {
        _inviInProgressList.postValue(invitationList.value?.data?.invitations)
    }

    fun setInviDoneList() {
        _inviDoneList.postValue(invitationList.value?.data?.confirmedAndCanceld)
    }
}