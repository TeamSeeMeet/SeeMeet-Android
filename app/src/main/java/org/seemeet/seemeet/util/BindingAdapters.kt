package org.seemeet.seemeet.util

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.widget.ImageView
import android.widget.TextView

import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import org.seemeet.seemeet.R
import androidx.databinding.BindingAdapter
import org.seemeet.seemeet.data.local.UserData
import org.seemeet.seemeet.ui.main.calendar.CalendarEvent
import java.time.LocalDate

object BindingAdapters {
    //더미용
    @BindingAdapter("setSrc")
    @JvmStatic
    fun setSrc(imageView: ImageView, imgId: Int) {
        imageView.setImageResource(imgId)
    }
    
    @BindingAdapter("setDate")
    @JvmStatic
    fun setDate(textView: TextView, date : String){

    }


    @BindingAdapter("setNameBoldRecieved")
    @JvmStatic
    fun setNameBoldRecieved(textView: TextView, target: String) {
        val string = target + "님이 보냈어요"
        val word = target
        val start = 1
        val end = start + word.length

        val ss = SpannableStringBuilder(string)
        ss.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(RelativeSizeSpan(1.2f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = ss
    }

    @BindingAdapter("setResponseCnt")
    @JvmStatic
    fun setResponseCntPink(textView: TextView, target: String) {
        val word = target[0].toString()
        val start = 0
        val end = 1

        val ss = SpannableStringBuilder(target)
        ss.setSpan(ForegroundColorSpan(Color.parseColor("#FA555C")), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        ss.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(RelativeSizeSpan(1.2f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = ss
    }

    @BindingAdapter("setUserList")
    @JvmStatic
    fun setRespondents(textView: TextView, list : List<UserData>) {
        var text : String = " "
        list.forEach {
            text += it.name + "   "
        }
        textView.text = text
    }


    @JvmStatic
    @BindingAdapter("localDate")
    fun convertLocalDate(textview: TextView, text: String) {
        textview.text = text.TimeParsing()
    }

}