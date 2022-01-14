package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.seemeet.seemeet.data.local.CheckboxData
import org.seemeet.seemeet.data.local.ReceiveData
import org.seemeet.seemeet.data.local.ReceiverData

class ReceiveViewModel(application: Application) : AndroidViewModel(application) {

    private val _checkboxList = MutableLiveData<List<CheckboxData>>()
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

    fun setReceiveData(){
        _checkboxList.value = mutableListOf(
            CheckboxData(
            "2021.12.23", "오전 11 : 00 ~ 오후 04 : 00",
            true),
            CheckboxData(
                "2021.12.23", "오전 12 : 00 ~ 오후 05 : 00",
                true),
            CheckboxData(
                "2021.12.23", "오전 10 : 00 ~ 오후 03 : 00",
                true),
            CheckboxData(
                "2021.12.23", "오전 09 : 00 ~ 오후 02 : 00",
                true)
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

    }
}