package org.seemeet.seemeet.util

import android.graphics.Color
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

object BindingAdapters {
    @BindingAdapter("setSrc")
    @JvmStatic
    fun setSrc(imageView: ImageView, imgId: Int) {
        imageView.setImageResource(imgId)
    }

    @BindingAdapter( value = ["dividerHeight", "dividerPadding", "dividerColor"], requireAll = false )
    @JvmStatic
    fun RecyclerView.setDivider(dividerHeight: Float?, dividerPadding: Float?, @ColorInt dividerColor: Int?) {
        val decoration = CustomDecoration(
            height = dividerHeight ?: 0f,
            padding = dividerPadding ?: 0f,
            color = dividerColor ?: Color.TRANSPARENT
        )
        addItemDecoration(decoration)
    }
}