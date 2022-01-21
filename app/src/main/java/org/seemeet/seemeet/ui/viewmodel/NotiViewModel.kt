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

    private val _inviIngList = MutableLiveData<List<Invitation>>()
    val inviIngList: LiveData<List<Invitation>>
        get() = _inviIngList

    private val _inviDoneList = MutableLiveData<List<ConfirmedAndCanceld>>()
    val inviDoneList: LiveData<List<ConfirmedAndCanceld>>
        get() = _inviDoneList


    //약속 내역 모두 가져오는 함수
    fun requestAllInvitaionList() = viewModelScope.launch(Dispatchers.IO) {
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

    fun setInviIngList() {
        _inviIngList.postValue(invitationList.value?.data?.invitations)
    }

    fun setInviDoneList() {
        _inviDoneList.postValue(invitationList.value?.data?.confirmedAndCanceld)
    }

    // 더미 + notiIng과 notiDone의 viewModel 하나로 합체시킴.
    /*
    private val _doneList = MutableLiveData<List<NotificationDoneData>>()
    val doneList : LiveData<List<NotificationDoneData>>
        get() = _doneList

    fun setDoneList() {
        _doneList.value = mutableListOf(
            NotificationDoneData(
                "1일전", true,"강화도 여행", mutableListOf(
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", false),
                )
            ),
            NotificationDoneData(
                "1일전", false,"강화도 여행", mutableListOf(
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", false),
                )
            ),
            NotificationDoneData(
                "1일전", false,"강화도 여행", mutableListOf(
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", false),
                )
            ),
            NotificationDoneData(
                "1일전", true,"강화도 여행", mutableListOf(
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", false),
                )
            ),
        )
    }

 private val _ingList = MutableLiveData<List<NotificationIngData>>()
 val ingList : LiveData<List<NotificationIngData>>
     get() = _ingList*/

    /*fun setIngList() {
        _ingList.value = mutableListOf(
            NotificationIngData(
                "1일전", "받은 요청","친구의 요청에 답해보세요!", 2, mutableListOf(
                    NotificationFriendData("김준희", true),
                )
            ),
            NotificationIngData(
                "1일전", "보낸 요청","친구 2명의 답변을 기다리고 있어요!", 1, mutableListOf(
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", false),
                    NotificationFriendData("김준희", false))
            ),
            NotificationIngData(
                "1일전", "보낸 요청","친구 3명의 답변을 기다리고 있어요!", 1, mutableListOf(
                    NotificationFriendData("김준희", false),
                    NotificationFriendData("김준희", false),
                    NotificationFriendData("김준희", false))
            ),
            NotificationIngData(
                "1일전", "보낸 요청","친구가 답변을 모두 완료하였어요!", 1, mutableListOf(
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", true))
            ),
            NotificationIngData(
                "1일전", "보낸 요청","친구의 답변을 기다리고 있어요!", 1, mutableListOf(
                    NotificationFriendData("김준희", false))
            ),
            NotificationIngData(
                "1일전", "받은 요청","친구의 요청에 답했어요!", 2, mutableListOf(
                    NotificationFriendData("김준희", true))
            ),

        )
    }*/


}