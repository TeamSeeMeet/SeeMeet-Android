package org.seemeet.seemeet.util

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Duration
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

fun String.TimeParsing(): String {
    var date = ""
    var aa = ""
    val split = this.split(":")

    aa = if(split[0].toInt()<12) "오전"
    else "오후"

    if(split[0].toInt()>12)
        date = "${split[0].toInt()-12}:${split[1]}"
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

fun String.monthDayParsing() : String {
   val date = this.split("-")
    return "${date[1]}월 ${date[2]}일"
}
fun String.calDday() : Int {
    val today = LocalDate.now().dayOfMonth
    val date = this.split("-")[2].toInt()

    return date - today
}

fun String.dateParsingIso() : LocalDateTime {
    val transFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    return LocalDateTime.parse(this, transFormat)
}
fun String.setBetweenDays() : Long {
    val created = this.dateParsingIso()
    val today = LocalDateTime.now()

     return ChronoUnit.DAYS.between(created, today)
}