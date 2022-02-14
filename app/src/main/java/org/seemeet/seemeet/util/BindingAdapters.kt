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
import androidx.databinding.BindingAdapter
import org.seemeet.seemeet.data.model.response.invitation.GuestX
import org.seemeet.seemeet.data.model.response.invitation.SendRespondent

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


    @BindingAdapter("removeSquareBrackets")
    @JvmStatic
    fun removeSquareBrackets(textView: TextView, target: String) {
        var text = target
        if(target.startsWith("["))
            text = text.substring(1)

        if(target.endsWith("]"))
            text = text.substring(0, text.length -1);

        textView.text = text
    }

    @BindingAdapter("setNameBoldRecieved")
    @JvmStatic
    fun setNameBoldRecieved(textView: TextView, target: String) {
        var text = target
        if(target.startsWith("["))
            text = text.substring(1)

        if(target.endsWith("]"))
            text = text.substring(0, text.length -1);

        val string = text + "님이 보냈어요"
        val word = text
        val start = 0
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
    fun setRespondents(textView: TextView, list : List<SendRespondent>) {
        var text : String = " "
        list.forEach {
            text += it.username + "   "
        }
        textView.text = text
    }


    @JvmStatic
    @BindingAdapter("localDate")
    fun convertLocalDate(textview: TextView, text: String) {
        textview.text = text.TimeParsing()
    }

    @JvmStatic
    @BindingAdapter("setMonthDayDate")
    fun setMonthDayDate(textview: TextView, text: String) {
        // O월 O일 꼴로 파싱함.
        textview.text = text.monthDayParsing()
    }

    @JvmStatic
    @BindingAdapter("setYearMonthDate")
    fun setYearMonthDate(textview: TextView, text: String) {
        // 0년 O월 O일 꼴로 파싱함.
        textview.text = text.YearMonthDayParsing()
    }

    @JvmStatic
    @BindingAdapter("setDday")
    fun setDday(textview: TextView, text: String) {
        text.calDday().run{
            if(text.calDday() == 0)
                textview.text = "D-DAY"
            else
                textview.text = "D-$this"
        }


    }

    @JvmStatic
    @BindingAdapter("setNotiDay")
    fun setNotiday(textview: TextView, text: String) {
         textview.text = "${text.setBetweenDays()}일 전"

    }

    @JvmStatic
    @BindingAdapter("setNotiSendMsg")
    fun setNotiSendMsg(textview: TextView, guests : List<GuestX>) {
        if(guests.size == 1){
            if(guests[0].isResponse)
                textview.text = "친구가 답변을 완료하였어요!"
            else
                textview.text = "친구의 답변을 기다리고 있어요!"
        }else {
            val response = guests.filter { it.isResponse }.size
            if(guests.size == response){
                textview.text = "친구가 모두 답변을 완료하였어요!"
            } else {
                var text = "친구 ${guests.size - response}명의 답변을 기다리고 있어요!"
                val start = text.indexOf("${guests.size - response}")
                val end = start + 1

                val ss = SpannableStringBuilder(text)
                ss.setSpan(ForegroundColorSpan(Color.parseColor("#FA555C")), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)

                textview.text = ss
            }
        }
    }

    @JvmStatic
    @BindingAdapter("setNotiReceiveMsg")
    fun setNotiReceiveMsg(textView: TextView, flag : Boolean){
        if(flag){
            textView.text = "친구의 요청에 답했어요!"
        } else {
            textView.text = "친구의 요청에 답해보세요!"
        }
    }

    @JvmStatic
    @BindingAdapter("setStartTime", "setEndTime")
    fun setStartEndTimeText(textView: TextView, start : String, end : String){
        val start = start.TimeParsing()
        val end = end.TimeParsing()

        textView.text = "$start ~ $end"
    }

    //이거 send 쪽 브랜치에 똑같이 있는 함수인데 가져다 사용함. 나중에 merge할때 잘 처리하자...
    @JvmStatic
    @BindingAdapter("setWordPinkAllText", "setPinkText")
    fun setWordPinkText(textView: TextView, text : String, pink : String) {
        val start = text.indexOf(pink)
        val end = start + pink.length

        val ss = SpannableStringBuilder(text)
        ss.setSpan(ForegroundColorSpan(Color.parseColor("#FA555C")), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        ss.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(RelativeSizeSpan(1.2f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = ss
    }



}