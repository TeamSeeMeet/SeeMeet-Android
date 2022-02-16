package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import retrofit2.HttpException
import java.net.SocketException
import java.net.UnknownHostException

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    private val _fetchState = MutableLiveData<Pair<Throwable, FetchState>>()
    val fetchState : LiveData<Pair<Throwable, FetchState>>
        get() = _fetchState

    protected val exceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        throwable.printStackTrace()

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