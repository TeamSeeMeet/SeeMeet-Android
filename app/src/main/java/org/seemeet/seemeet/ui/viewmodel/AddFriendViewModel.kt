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
import org.seemeet.seemeet.data.model.request.friend.RequestAddFriendData
import org.seemeet.seemeet.data.model.request.friend.RequestUserData
import org.seemeet.seemeet.data.model.response.friend.ResponseAddFriendData
import org.seemeet.seemeet.data.model.response.friend.ResponseUserList
import retrofit2.HttpException

class AddFriendViewModel(application: Application) : AndroidViewModel(application) {
    private val _userList = MutableLiveData<ResponseUserList>()
    val userList : LiveData<ResponseUserList>
        get() = _userList

    private val _addFriend = MutableLiveData<ResponseAddFriendData>()
    val addFriend : LiveData<ResponseAddFriendData>
        get() = _addFriend


    //  유저 목록 요청하기
    fun requestUserList(nickname: Editable) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _userList.postValue(
                RetrofitBuilder.friendService.searchUserList(
                    SeeMeetSharedPreference.getToken(),
                    RequestUserData(nickname.toString()),
                )
            )
        } catch (e: HttpException) {
        }
    }

    // 친구 추가 요청하기
    fun requestAddFriend(nickname:String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _addFriend.postValue(
                RetrofitBuilder.friendService.addFriendData(
                    SeeMeetSharedPreference.getToken(),
                    RequestAddFriendData(nickname)
                ))
        } catch (e: HttpException) {
        }
    }
}