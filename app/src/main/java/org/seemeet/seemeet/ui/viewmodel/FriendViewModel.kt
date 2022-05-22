package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.response.friend.ResponseFriendList

class FriendViewModel(application: Application) : BaseViewModel(application) {

    private val _friendList = MutableLiveData<ResponseFriendList>()
    val friendList: LiveData<ResponseFriendList>
        get() = _friendList

    //  친구 목록 가져오기
    fun requestFriendList() = viewModelScope.launch(exceptionHandler) {
        _friendList.postValue(
            RetrofitBuilder.friendService.getFriendList(
                SeeMeetSharedPreference.getToken()
            )
        )
    }
}