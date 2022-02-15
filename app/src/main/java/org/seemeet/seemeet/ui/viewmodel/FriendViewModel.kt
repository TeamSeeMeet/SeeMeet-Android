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
import org.seemeet.seemeet.data.model.response.friend.ResponseFriendList
import retrofit2.HttpException

class FriendViewModel(application: Application) : AndroidViewModel(application) {

    private val _friendList = MutableLiveData<ResponseFriendList>()
    val friendList: LiveData<ResponseFriendList>
        get() = _friendList

    //  친구 목록 가져오기
    fun requestFriendList() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _friendList.postValue(
                RetrofitBuilder.friendService.getFriendList(
                    SeeMeetSharedPreference.getToken()
                )
            )
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }
}