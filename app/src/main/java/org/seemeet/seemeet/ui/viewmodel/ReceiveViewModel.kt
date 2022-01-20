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
import org.seemeet.seemeet.data.local.*
import org.seemeet.seemeet.data.model.response.invitation.InvitationListData
import org.seemeet.seemeet.data.model.response.invitation.ReceiveInvitation
import org.seemeet.seemeet.data.model.response.invitation.ReceiveInvitationData
import org.seemeet.seemeet.data.model.response.invitation.ReceiveInvitationDate
import org.seemeet.seemeet.data.model.response.plan.PlanResponseData
import org.seemeet.seemeet.ui.receive.adapter.ReceiveCheckListAdapter
import retrofit2.HttpException

class ReceiveViewModel(application: Application) : AndroidViewModel(application) {

    private val _receiveInvitationData = MutableLiveData<ReceiveInvitationData>()
    val receiveInvitationData : LiveData<ReceiveInvitationData>
        get() = _receiveInvitationData

    private val _receiveInvitationDateList  = MutableLiveData<List<ReceiveInvitationDate>>()
    val receiveInvitationDateList : LiveData<List<ReceiveInvitationDate>>
        get() = _receiveInvitationDateList

    private val _receivePlanResponseList = MutableLiveData<List<PlanResponseData>>()
    val receivePlanResponseList : LiveData<List<PlanResponseData>>
        get() = _receivePlanResponseList


    fun requestReceiveInvitation(invitationId : Int) = viewModelScope.launch (Dispatchers.IO){
        try {
            Log.d("**********sendId", invitationId.toString())
            _receiveInvitationData.postValue(RetrofitBuilder.invitationService.getReceiveInvitationData(invitationId, SeeMeetSharedPreference.getToken()).data)
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }

    fun requestReceivePlanResponse(dateId : Int) = viewModelScope.launch(Dispatchers.IO){
        try{
            _receivePlanResponseList.postValue(RetrofitBuilder.planService.getPlanResponse(dateId, SeeMeetSharedPreference.getToken()).data)
        } catch (e : HttpException){
            e.printStackTrace()
        }
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


    // 더미용
   /* private val _checkboxList = MutableLiveData<List<CheckboxData>>()
    val checkboxList : LiveData<List<CheckboxData>>
        get() = _checkboxList

    private val _receiverList = MutableLiveData<List<ReceiverData>>()
    val recieverList : LiveData<List<ReceiverData>>
        get() = _receiverList

    private val _request = MutableLiveData<String>()
    val request : LiveData<String>
        get() = _request

    private val _title = MutableLiveData<String>()
    val title : LiveData<String>
        get() = _title

    private val _content = MutableLiveData<String>()
    val content : LiveData<String>
        get() = _content

    private val _flag = MutableLiveData<Boolean>()
    val flag : LiveData<Boolean>
        get() = _flag

    private val _clickedList = MutableLiveData<List<ScheduleData>>()
    val clickedList : LiveData<List<ScheduleData>>
        get() = _clickedList

    fun setReceiveData(){
        _checkboxList.value = mutableListOf(
            CheckboxData(
                1,
            "2021.12.23", "오전 11 : 00 ~ 오후 04 : 00",
            false),
            CheckboxData(2,
                "2021.12.23", "오전 12 : 00 ~ 오후 05 : 00",
                false),
            CheckboxData(3,
                "2021.12.23", "오전 10 : 00 ~ 오후 03 : 00",
                false),
            CheckboxData(4,
                "2021.12.23", "오전 09 : 00 ~ 오후 02 : 00",
                false)
        )

        _receiverList.value = mutableListOf(
            ReceiverData(
                "김동동", true
            ),
            ReceiverData(
                "이솜솜", false
            ),
            ReceiverData(
                "이씨밋", false
            ),
            ReceiverData(
                "김광개토대왕", false
            ),
            ReceiverData(
                "박훈민정음혜래본", false
            )
        )
        _title.value = mutableSetOf<String>("대방어데이").toString()
        _content.value = mutableSetOf<String>("야그들아 대방어먹자야그들아 대방어먹자야그들아 대방어먹자야그들아 대방어먹자야그들아 대방어먹자야그들아 대방어먹자야그들아 대방어먹자야그들아 대방어먹자야그들아 대방어먹자야그들아 대방어먹자야그들아 대방어먹자야그들아 대방어먹자야그들아 대방어먹자야그들아 대방어먹자야그들아 대방어먹자야그들아 대방어먹자야그들").toString()
        _request.value =  mutableSetOf<String>("김준희").toString()
        _flag.value =  false

    }

    fun setSheduleListData(){
        _clickedList.value = mutableListOf(
            ScheduleData(
                1, "2021.12.23",
                "대방어데이1", mutableListOf("김준희", "김준희", "김준희"),
                "오전 11 : 00 ~ 오후 04 : 00"
            ),
            ScheduleData(
                1, "2021.12.23",
                "대방어데이2", mutableListOf("김준희4", "김준희5", "김준희6"),
                "오전 11 : 00 ~ 오후 04 : 00"
            ),
            ScheduleData(
                2, "2021.12.23",
                "대방어데이3", mutableListOf("김준희1", "김준희2", "김준희3"),
                "오전 11 : 00 ~ 오후 04 : 00"
            ),
            ScheduleData(
                2, "2021.12.23",
                "대방어데이4", mutableListOf("김준희4", "김준희5", "김준희6"),
                "오전 11 : 00 ~ 오후 04 : 00"
            )
        )
    }
*/
}