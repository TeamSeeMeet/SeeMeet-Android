package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.seemeet.seemeet.data.local.GuestData
import org.seemeet.seemeet.data.local.InviData
import org.seemeet.seemeet.data.local.UserData

class SendViewModel(application: Application) : AndroidViewModel(application) {

    private val _inviList = MutableLiveData<List<InviData>>()
    val inviList : LiveData<List<InviData>>
        get() = _inviList

    private val _guestList = MutableLiveData<List<GuestData>>()
    val guestList : LiveData<List<GuestData>>
        get() = _guestList

    private val _invititle = MutableLiveData<String>()
    val inviTitle : LiveData<String>
        get() = _invititle

    private val _desc = MutableLiveData<String>()
    val desc : LiveData<String>
        get() = _desc

    private val _guestCnt = MutableLiveData<String>()
    val guestCnt : LiveData<String>
        get() = _guestCnt

    fun setGuestCount( cnt : Int,  size : Int){
        _guestCnt.value = "$cnt/$size"
    }

    fun setSendInvitationList() {
        _invititle.value = "가나다라마바사아자차카타파하"
        _desc.value = "헬로헬로 헬로윈 푸펠푸푸렐 굴뚝마을 푸펠\n 나는 문어 꿈을 꾸는 문어 꿈 속에서는 무엇이든 될 수 있어~ 높은 산을 올라가면 나는 초록색 문어 횡단보도 건너가면 나는 줄무늬 문어 별빛가득 밤하늘을 날아가면 나는 오색찬란한 문어가 되는거야 아아아아아 아 아아아아 깊은 바다속은 무서워 너무 차갑고 때론 외롭기도 해애애애애 애 야 아아아아 아 " +
                "헬로헬로 헬로윈 푸펠푸푸렐 굴뚝마을 푸펠\n" +
                " 나는 문어 꿈을 꾸는 문어 꿈 속에서는 무엇이든 될 수 있어~ 높은 산을 올라가면 나는 초록색 문어 횡단보도 건너가면 나는 줄무늬 문어 별빛가득 밤하늘을 날아가면 나는 오색찬란한 문어가 되는거야 아아아아아 아 아아아아 깊은 바다속은 무서워 너무 차갑고 때론 외롭기도 해애애애애 애 야 아아아아 아 "

        _inviList.value = mutableListOf(
            InviData(
                1, "2021.12.23", "오전 11:00 ~ 오후 15:00",
                listOf(UserData(
                    "이동동", 10
                ))
            ),
            InviData(
                1, "2021.12.23", "오전 11:00 ~ 오후 16:00",
                listOf(UserData(
                    "이동동", 10
                ), UserData(
                    "박촤촤", 12
                ))
            ),
            InviData(
                1, "2021.12.23", "오전 11:00 ~ 오후 17:00",
                listOf(UserData(
                    "이동동", 10
                ), UserData(
                    "김마마", 11
                ), UserData(
                    "박촤촤", 12
                ) )
            ),
            InviData(
                1, "2021.12.23", "오전 11:00 ~ 오후 18:00",
                listOf()
            )
        )

        _guestList.value = mutableListOf(
            GuestData(
                10, "이동동", true
            ),
            GuestData(
                10, "김마마", true
            ),
            GuestData(
                10, "박촤촤", true
            )
        )
    }
}