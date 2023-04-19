package com.liuxe.iword.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/**
 *  author : liuxe
 *  date : 2023/3/29 16:11
 *  description :
 */
object TimestampUtils {
    fun getTimeString(timestamp: Long): String? {
        var result = ""
        val weekNames = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")
        val hourTimeFormat = "HH:mm"
        val monthTimeFormat = "M月d日 HH:mm"
        val yearTimeFormat = "yyyy年M月d日 HH:mm"
        return try {
            val todayCalendar: Calendar = Calendar.getInstance()
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp
            result = if (todayCalendar.get(Calendar.YEAR) === calendar.get(Calendar.YEAR)) { //当年
                if (todayCalendar.get(Calendar.MONTH) === calendar.get(Calendar.MONTH)) { //当月
                    val temp: Int =
                        todayCalendar.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH)
                    when (temp) {
                        0 -> getTime(timestamp, hourTimeFormat)
                        1 -> "昨天 " + getTime(timestamp, hourTimeFormat)
                        2, 3, 4, 5, 6 -> {
                            val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK)
                            weekNames[dayOfWeek - 1].toString() + " " + getTime(
                                timestamp,
                                hourTimeFormat
                            )
                        }
                        else -> getTime(timestamp, monthTimeFormat)
                    }
                } else {
                    getTime(timestamp, monthTimeFormat)
                }
            } else {
                getTime(timestamp, yearTimeFormat)
            }
            result
        } catch (e: Exception) {
            Log.e("getTimeString", ""+e.message)
            ""
        }
    }

    fun getTime(time: Long, pattern: String?): String {
        val date = Date(time)
        return dateFormat(date, pattern)
    }

    fun dateFormat(date: Date?, pattern: String?): String {
        val format = SimpleDateFormat(pattern)
        return format.format(date)
    }

}