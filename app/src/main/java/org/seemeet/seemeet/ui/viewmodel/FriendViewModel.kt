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
import org.seemeet.seemeet.data.local.FriendNameData
import org.seemeet.seemeet.data.model.response.friend.ResponseFriendList
import retrofit2.HttpException

class FriendViewModel(application: Application) : AndroidViewModel(application) {

    private val _friendList = MutableLiveData<ResponseFriendList>()
    val friendList : LiveData<ResponseFriendList>
        get() = _friendList

    //  친구 목록 가져오기
    fun requestFriendList()  = viewModelScope.launch(Dispatchers.IO) {
        try {
            _friendList.postValue(
                RetrofitBuilder.friendService.getFriendList(
                    SeeMeetSharedPreference.getToken()
                ))
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }

    // 더미
    //리싸이클러뷰에 들어갈 리스트 변수
    private val _nullFriendList = MutableLiveData<List<FriendNameData>>()
    val nullFriendList : LiveData<List<FriendNameData>>
        get() = _nullFriendList


    //임시로 넣을 더미데이터 셋팅. < 위의 리스트에 대입
    fun setNullFriendList() {
        _nullFriendList.value = mutableListOf(
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