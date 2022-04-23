package org.seemeet.seemeet.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData

class FirstApplyViewModel (application: Application) : BaseViewModel(application) {
    val toWho = MutableLiveData("")
    val title = MutableLiveData("")
    val detail = MutableLiveData("")

}