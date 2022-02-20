package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.response.friend.FriendListData
import retrofit2.HttpException

class ApplyViewModel(application: Application) : BaseViewModel(application) {

    private val _friendList = MutableLiveData<List<FriendListData>>()
    val friendList: LiveData<List<FriendListData>>
        get() = _friendList

    //친구 목록 가져오기
    fun requestFriendList() = viewModelScope.launch(exceptionHandler) {
        try {
            _friendList.postValue(
                RetrofitBuilder.friendService.getFriendList(
                    SeeMeetSharedPreference.getToken()
                ).data //data.id, data.name, data.email 으로 받아
            )
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }
}