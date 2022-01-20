package org.seemeet.seemeet.ui.registration

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.ToastCustomBinding

object CustomToast {

    fun createToast(context: Context, message: String): Toast? {
        val inflater = LayoutInflater.from(context)
        val binding: ToastCustomBinding =
            DataBindingUtil.inflate(inflater, R.layout.toast_custom, null, false)

        binding.tvSample.text = message

        return Toast(context).apply {
            setGravity( Gravity.CENTER, 0,0)
            duration = Toast.LENGTH_SHORT
            view = binding.root
        }
    }
}