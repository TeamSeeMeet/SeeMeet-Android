package org.seemeet.seemeet.ui.apply

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.seemeet.seemeet.data.local.ApplyFriendData
import org.seemeet.seemeet.ui.viewmodel.SecondApplyViewModel

class SecondApplyViewModelFactory(
    private val friendList: ArrayList<ApplyFriendData>,
    private val title: String,
    private val desc: String
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SecondApplyViewModel::class.java)) {
            return SecondApplyViewModel(friendList, title, desc) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}