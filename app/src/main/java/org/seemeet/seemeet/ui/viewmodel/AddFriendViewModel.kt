package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.seemeet.seemeet.data.local.FriendIdData

class AddFriendViewModel(application: Application) : AndroidViewModel(application) {

    //리싸이클러뷰에 들어갈 리스트 변수
    private val _addFriendList = MutableLiveData<List<FriendIdData>>()
    val addFriendList : LiveData<List<FriendIdData>>
        get() = _addFriendList


    //임시로 넣을 더미데이터 셋팅. < 위의 리스트에 대입
    fun returnAddFriendList() {
        _addFriendList.value = mutableListOf(
            FriendIdData("김현아", "0925hyunah@gmail.com"),
            FriendIdData("김현아", "akxmcse@gmail.com"),
            FriendIdData("김현아", "akim.cse@ewhain.net"),
            )
    }


}