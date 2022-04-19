package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import retrofit2.HttpException
import java.net.SocketException
import java.net.UnknownHostException

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    // 예외 난 거 저장하는 변수
    private val _fetchState = MutableLiveData<Pair<Throwable, FetchState>>()
    val fetchState : LiveData<Pair<Throwable, FetchState>>
        get() = _fetchState

    //코루틴 예외처리 핸들러
    protected val exceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        throwable.printStackTrace()
        //각각의 에러 상황에서 값 설정. 400~500대 에러는 HttpException으로 처리 될 것.
        when(throwable){
            is SocketException -> _fetchState.postValue(Pair(throwable, FetchState.BAD_INTERNET))
            is HttpException -> _fetchState.postValue(Pair(throwable, FetchState.PARSE_ERROR))
            is UnknownHostException -> _fetchState.postValue(Pair(throwable, FetchState.WRONG_CONNECTION))
            else -> _fetchState.postValue(Pair(throwable, FetchState.FAIL))
        }
    }

    enum class FetchState {
        BAD_INTERNET, PARSE_ERROR, WRONG_CONNECTION, FAIL
    }

}