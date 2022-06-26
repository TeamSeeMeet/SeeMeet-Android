package org.seemeet.seemeet.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.ToastCustomBinding

object CustomToast {

    var toast: Toast? = null
    fun createToast(context: Context, message: String): Toast? {
        val inflater = LayoutInflater.from(context)
        val binding: ToastCustomBinding =
            DataBindingUtil.inflate(inflater, R.layout.toast_custom, null, false)

        binding.tvSample.text = message

        if (toast == null) {
            toast = Toast(context)
        }

        toast?.setGravity(Gravity.CENTER, 0, 0)
        toast?.duration = Toast.LENGTH_SHORT
        toast?.view = binding.root

        return toast

    }
}