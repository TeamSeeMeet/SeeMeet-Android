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
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.model.response.invitation.GuestX
import org.seemeet.seemeet.data.model.response.invitation.SendGuest
import org.seemeet.seemeet.data.model.response.invitation.SendRespondent

object BindingAdapters {
    //더미용
    @BindingAdapter("setSrc")
    @JvmStatic
    fun setSrc(imageView: ImageView, imgId: Int) {
        imageView.setImageResource(imgId)
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
        // yy-MM-dd를 O월 O일 꼴로 파싱함.
        textview.text = text.monthDayParsing()
    }

    @JvmStatic
    @BindingAdapter("setYearMonthDate")
    fun setYearMonthDate(textview: TextView, text: String) {
        //yy-MM-dd를 0년 O월 O일 꼴로 파싱함.
        textview.text = text.YearMonthDayParsing()
    }

    @JvmStatic
    @BindingAdapter("setYearMonthDayWithDot")
    fun setYearMonthDayWithDot(textview: TextView, text: String) {
        //yyyy-MM-dd'T'HH:mm:ss.SSS'Z 꼴의 date String값을 yyyy.mm.dd로 만듦
        textview.text = text.yearMonthDayWithDotParsing()
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
                val text = "친구 ${guests.size - response}명의 답변을 기다리고 있어요!"
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
        textView.text = "${start.TimeParsing()} ~ ${end.TimeParsing()}"
    }

    @JvmStatic
    @BindingAdapter("setSendGuestListForCnt")
    fun setSendInvitationResponseCnt(textView: TextView, guests : List<SendGuest>) {
        val text = guests.count{it.isResponse}.toString() + "/" + guests.size.toString()
        val pink = guests.count{it.isResponse}.toString()
        setWordPinkText(textView, text, pink)
    }

    @JvmStatic
    @BindingAdapter("setWordBoldAllText","setBoldText" )
    fun setWordBoldText(textView: TextView, text : String, bold : String) {
        val start = text.indexOf(bold)
        val end = start + bold.length

        val ss = SpannableStringBuilder(text)
        ss.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = ss
    }

    @JvmStatic
    @BindingAdapter("setWordBigAndWhiteText")
    fun setWordBigAndWhiteText(textView: TextView, text : String, white : String) {
        val start = text.indexOf(white)
        val end = start + white.length

        val ss = SpannableStringBuilder(text)
        ss.setSpan(ForegroundColorSpan(Color.WHITE), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        ss.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(RelativeSizeSpan(1.2f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = ss
    }

    @JvmStatic
    @BindingAdapter("setHomeBannerText", "setHomeBannerDay")
    fun setHomeBanner(textView: TextView, flag : Int, day : Int) {
        var text = ""
        var white = ""

        when (flag) {
            1 -> {
                text = "씨밋과 함께\n약속을 잡아볼까요?"
                white = "약속"
            }
            2 -> {
                text = "친구가 당신의 약속 신청을\n 기다리고 있어요!"
                white = "약속 신청"
            }
            3 -> {
                text = "아싸 오늘은\n친구 만나는 날이다!"
                white = "친구"
            }
            4 -> {
                text = "약속 잡기에\n딱 좋은 시기예요!"
                white = "딱 좋은"
            }
            5 -> {
                text = "친구와 만난지\n벌써 ${day}일이 지났어요!"
                white = "${day}일"
            }
            6 -> {
                text = "친구를 언제 만났는지\n기억도 안나요...."
                white = "기억도"
            }
        }

        setWordBigAndWhiteText(textView, text, white)
    }

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

    @JvmStatic
    @BindingAdapter("setHomeBannerImage")
    fun setHomeBannerImage(imageView: ImageView, flag : Int) {
       val imgId = when (flag) {
            1 -> R.drawable.img_illust_5
            2 -> R.drawable.img_illust_4
            3 -> R.drawable.img_illust_1
            4 -> R.drawable.img_illust_8
            5 -> R.drawable.img_illust_6
            6 -> R.drawable.img_illust_7
           else ->  R.drawable.img_illust_5
       }

        imageView.setImageResource(imgId)
    }
}