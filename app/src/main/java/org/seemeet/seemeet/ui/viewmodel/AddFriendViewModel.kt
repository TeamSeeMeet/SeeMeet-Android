package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import android.text.Editable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.request.friend.RequestUserData
import org.seemeet.seemeet.data.model.response.friend.ResponseUserList
import retrofit2.HttpException

class AddFriendViewModel(application: Application) : AndroidViewModel(application) {
    private val _userList = MutableLiveData<ResponseUserList>()
    val userList : LiveData<ResponseUserList>
        get() = _userList

    //  유저 목록 요청하기
    fun requestUserList(email: Editable) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _userList.postValue(
                RetrofitBuilder.friendService.searchUserList(
                    SeeMeetSharedPreference.getToken(),
                    RequestUserData(email.toString())
                ))
        } catch (e: HttpException) {
        }
    }

    /* 더미
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
   */

}