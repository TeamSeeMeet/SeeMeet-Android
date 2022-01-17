package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.seemeet.seemeet.data.local.FriendNameData

class FriendViewModel(application: Application) : AndroidViewModel(application) {

    //리싸이클러뷰에 들어갈 리스트 변수
    private val _friendList = MutableLiveData<List<FriendNameData>>()
    val friendList : LiveData<List<FriendNameData>>
        get() = _friendList


    //임시로 넣을 더미데이터 셋팅. < 위의 리스트에 대입
    fun setFriendList() {
        _friendList.value = mutableListOf(
            FriendNameData("구건모"),
            FriendNameData("김인환"),
            FriendNameData("김준희"),
            FriendNameData("김현아"),
            FriendNameData("남지윤"),
            FriendNameData("박익범"),
            FriendNameData("손시형"),
            FriendNameData("엄희수"),
            FriendNameData("오수린"),
            FriendNameData("유가영"),
            FriendNameData("이동기"),
            FriendNameData("이유정"),
            FriendNameData("이유진"),
            FriendNameData("정재용"),
            FriendNameData("최유림"),
        )
    }


}