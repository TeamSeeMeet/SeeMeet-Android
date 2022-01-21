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
import org.seemeet.seemeet.data.model.response.plan.ResponsePlanDetail
import retrofit2.HttpException

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    //리싸이클러뷰에 들어갈 리스트 변수
    private val _planDetail = MutableLiveData<List<ResponsePlanDetail>>()
    val planDetail: LiveData<List<ResponsePlanDetail>>
        get() = _planDetail

    fun requestPlanId(planId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _planDetail.postValue(listOf(RetrofitBuilder.planService.getPlanDetail(planId, SeeMeetSharedPreference.getToken())))
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }

    /*
    //임시로 넣을 더미데이터 셋팅. < 위의 리스트에 대입
    fun setDetailFriendList() {
        _detailFriendList.value = mutableListOf(
            NotificationDoneData(
                "1일전", true,"강화도 여행", listOf(
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", true),
                    NotificationFriendData("김준희", false),
                )
            ),
        )
    }
*/

}