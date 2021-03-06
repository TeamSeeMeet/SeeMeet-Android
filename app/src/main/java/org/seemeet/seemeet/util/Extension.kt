package org.seemeet.seemeet.util

import android.content.Context
import android.content.res.Configuration
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.Button
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import org.seemeet.seemeet.R
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.*


fun daysOfWeekFromLocale(): Array<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()
    // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
    // Only necessary if firstDayOfWeek != DayOfWeek.MONDAY which has ordinal 0.
    if (firstDayOfWeek != DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInVisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun Button.activeBtn(){
    setBackgroundResource(R.drawable.rectangle_pink01_10)
    isClickable = true // 버튼 클릭할수 있게
    isEnabled = true // 버튼 활성화
}

fun Button.inactiveBtn(color: Int) {
    setBackgroundResource(color)
    isClickable = false // 버튼 클릭할수 없게
    isEnabled = false // 버튼 비활성화
}

fun String.TimeParsing(): String {
    var date = ""
    var aa = ""
    val split = this.split(":")

    aa = if (split[0].toInt() < 12) "오전"
    else "오후"

    if (split[0].toInt() > 12)
        date = "${split[0].toInt() - 12}:${split[1]}"
    else
        date = this

    return "$aa $date"
}

fun String.timeToDate(aa: String): String {
    var date = ""
    val split = this.split(":")
    date = if (aa == "오후" && split[0].toInt() < 12) {
        "${split[0].toInt() + 12}:${split[1]}"
    } else {
        this
    }
    return date
}

fun LocalDate.stringParsing(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy:MMM:d")
    return formatter.format(this)
}

internal fun Context.getDrawableCompat(@DrawableRes drawable: Int) =
    ContextCompat.getDrawable(this, drawable)

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

internal fun TextView.setTextColorRes(@ColorRes color: Int) =
    setTextColor(context.getColorCompat(color))

fun String.YearMonthDayParsing(): String {
    val date = this.split("-")
    return "${date[0]}년 ${date[1]}월 ${date[2]}일"
}

fun String.monthDayParsing(): String {
    val date = this.split("-")
    return "${date[1]}월 ${date[2]}일"
}

fun String.yearMonthDayWithDotParsing() : String{
    //yyyy-MM-dd'T'HH:mm:ss.SSS'Z 꼴의 date String값을 yyyy.mm.dd로 만듦
    val date = this.split("-")
    return "${date[0]}.${date[1]}.${date[2].substring(0, 2)}"
}

fun String.calDday(): Int {
    val comeDay = LocalDate.parse(this, DateTimeFormatter.ISO_DATE).atStartOfDay()
    val now = LocalDateTime.now()
    val today = LocalDateTime.of(now.year, now.month, now.dayOfMonth, 0, 0 , 0)

    return ChronoUnit.DAYS.between(today, comeDay).toInt()
}

fun String.dateParsingIso(): LocalDateTime {
    val transFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    return LocalDateTime.parse(this, transFormat)
}

fun String.setBetweenDays(): Long {
    val created = this.dateParsingIso()
    val now = LocalDateTime.now()
    val today = LocalDateTime.of(now.year, now.month, now.dayOfMonth, 0, 0 , 0)

    return ChronoUnit.DAYS.between(created, today)
}

fun String.setBetweenDays2(): Long {
    val created = LocalDate.parse(this, DateTimeFormatter.ISO_DATE)
    val now = LocalDateTime.now()
    val today = LocalDateTime.of(now.year, now.month, now.dayOfMonth, 0, 0 , 0)

    return ChronoUnit.DAYS.between(created, today)
}

/*
fun changeStatusBarColor(colorId : Int, activity : Activity, mContext : Context){
    val mWindow = activity.window
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        mWindow.statusBarColor = mContext.resources.getColor(colorId, null)
    } else {
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        mWindow.statusBarColor = ContextCompat.getColor(activity, R.color.pink01)
    }
}
*/

fun getStatusBarHeight(context: Context): Int {
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")

    return if (resourceId > 0) {
        context.resources.getDimensionPixelSize(resourceId)
    } else {
        0
    }
}

fun getNaviBarHeight(c: Context): Int {
    val result = 0
    val hasMenuKey = ViewConfiguration.get(c).hasPermanentMenuKey()
    val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)

    if (!hasMenuKey && !hasBackKey) {
        //The device has a navigation bar
        val orientation: Int = c.resources.configuration.orientation
        val resourceId: Int = c.resources.getIdentifier(
                if (orientation == Configuration.ORIENTATION_PORTRAIT) "navigation_bar_height" else "navigation_bar_width",
                "dimen",
                "android"
            )

        if (resourceId > 0) {
            return c.resources.getDimensionPixelSize(resourceId)
        }
    }
    return result
}