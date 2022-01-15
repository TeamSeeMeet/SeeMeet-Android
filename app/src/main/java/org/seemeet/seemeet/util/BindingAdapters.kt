package org.seemeet.seemeet.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import org.seemeet.seemeet.ui.main.calendar.CalendarEvent
import java.time.LocalDate

object BindingAdapters {

    @BindingAdapter("setSrc")
    @JvmStatic
    fun setSrc(imageView: ImageView, imgId: Int) {
        imageView.setImageResource(imgId)
    }

<<<<<<< HEAD
    @JvmStatic
    @BindingAdapter("localDate")
    fun convertLocalDate(textview: TextView, text : String) {
        textview.text = text.TimeParsing()
    }
=======

>>>>>>> develop
}