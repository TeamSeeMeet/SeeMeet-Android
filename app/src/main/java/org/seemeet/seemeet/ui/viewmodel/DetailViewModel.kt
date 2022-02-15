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
}