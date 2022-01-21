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
import org.seemeet.seemeet.data.model.response.friend.FriendListData
import retrofit2.HttpException

class ApplyViewModel(application: Application) : AndroidViewModel(application) {

    private val _friendList = MutableLiveData<List<FriendListData>>()
    val friendList: LiveData<List<FriendListData>>
        get() = _friendList


    //서버통신
    fun requestFriendList() = viewModelScope.launch(Dispatchers.IO) {
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