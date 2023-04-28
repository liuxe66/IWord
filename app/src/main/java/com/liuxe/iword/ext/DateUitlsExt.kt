package com.liuxe.iword.ext

/**
 *Created by Liuxe on 2023/3/25 21:13
 *desc : 日期拓展
 */

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

private val getTimeError = "获取日期异常"
private val ymdhms = "yyyy-MM-dd HH:mm:ss"
private val ymdhms2 = "yyyyMMddHHmmss"
private val hms = "HH:mm:ss"
private val hm = "HH:mm"
private val ymd = "yyyy-MM-dd" //
private val am = "AM" // 上午
private val pm = "PM" // 下午


@SuppressLint("SimpleDateFormat")
fun getDataYMD() = System.currentTimeMillis().getDateYMD()

@SuppressLint("SimpleDateFormat")
fun getDataYMDHMS() = System.currentTimeMillis().getDateYMDHMS()

/**
 * 直接通过long 获取转换为yyyy-MM-dd格式的时间
 */
@SuppressLint("SimpleDateFormat")
fun Long.getDateYMD(): String {
    val sdf = SimpleDateFormat(ymd)
    sdf.timeZone = TimeZone.getTimeZone("GMT+8")
    return try {
        sdf.format(this)
    } catch (e: Exception) {
        sdf.format(System.currentTimeMillis())
    }
}

/**
 * 直接通过long 获取转换为yyyy-MM-dd HH:mm:ss格式的时间
 */
@SuppressLint("SimpleDateFormat")
fun Long.getDateYMDHMS(): String {
    val sdf = SimpleDateFormat(ymdhms)
    sdf.timeZone = TimeZone.getTimeZone("GMT+8")
    return try {
        sdf.format(this)
    } catch (e: Exception) {
        sdf.format(System.currentTimeMillis())
    }
}

@SuppressLint("SimpleDateFormat")
fun Long.getDateHM(): String {
    val sdf = SimpleDateFormat(hm)
    sdf.timeZone = TimeZone.getTimeZone("GMT+8")
    return try {
        sdf.format(this)
    } catch (e: Exception) {
        sdf.format(System.currentTimeMillis())
    }
}

/**
 * 自定义获取返回的类型
 */
fun getDate(type: Int): String {
    return System.currentTimeMillis().getDate(type)
}

/**
 * 自定义返回的类型
 * 1 :[ymdhms]
 * 2 :[hms]
 * 3 :[hm]
 */
@SuppressLint("SimpleDateFormat")
fun Long.getDate(type: Int = 1): String {
    val sdf = if (type == 1) {
        SimpleDateFormat(ymdhms)
    } else if (type == 2) {
        SimpleDateFormat(hms)
    } else if (type == 3) {
        SimpleDateFormat(hm)
    } else {
        SimpleDateFormat(ymdhms)
    }
    sdf.timeZone = TimeZone.getTimeZone("GMT+8")
    return try {
        sdf.format(this)
    } catch (e: Exception) {
        sdf.format(System.currentTimeMillis())
    }
}

// 时间转换为long 格式要求为 ymdhms
@SuppressLint("SimpleDateFormat")
fun String.changeToYMDHMSString(): String =
    "${SimpleDateFormat(ymdhms).parse(this)?.time ?: getTimeError}"

@SuppressLint("SimpleDateFormat")
fun String.changeToYMDString(): String =
    "${SimpleDateFormat(ymd).parse(this)?.time ?: getTimeError}"

@SuppressLint("SimpleDateFormat")
fun String.changeToHMString(): String =
    "${SimpleDateFormat(hm).parse(this)?.time ?: getTimeError}"

/**
 * 转换时间为long
 * 需要格式为 ymdhms 要知道每个信息才可以正确转换为long
 */
@SuppressLint("SimpleDateFormat")
fun String.changeYMDHMSTimeToLong(): Long {
    val simple = SimpleDateFormat(ymdhms)
    val date = simple.parse(this)
    return date.time
}

/**
 * string 秒换算为时分秒
 */
fun String.secondChangeHourMinuteSecond(needHour: Boolean = true) =
    this.toInt().secondChangeHourMinuteSecond(needHour)

/**
 * int 秒换算为时分秒
 */
fun Int.secondChangeHourMinuteSecond(needHour: Boolean = true): String {
    var h = 0
    var d = 0
    var s = 0
    val temp = this % 3600
    if (this > 3600) {
        h = this / 3600
        if (temp != 0) {
            if (temp > 60) {
                d = temp / 60
                if (temp % 60 != 0) {
                    s = temp % 60
                }
            } else {
                s = temp
            }
        }
    } else { // 小于1小时
        // 当前时间除以60 获取当前是多少分钟
        d = this / 60
        if (this % 60 != 0) {
            s = this % 60
        }
    }
    val hStrings = if (h < 10) {
        "0${h}"
    } else {
        "$h"
    }
    val dStrings = if (d < 10) {
        "0${d}"
    } else {
        "$d"
    }
    val sStrings = if (s < 10) {
        "0${s}"
    } else {
        "$s"
    }
    return if (needHour)
        "${hStrings}:${dStrings}:${sStrings}"
    else
        "${dStrings}:${sStrings}"
}


/**
 * 计算两个日期所差的天数
 * 返回为int = 0 则同一天
 *          < 0 为前一天
 * GyDateUtilKT.differDay(c1.getTimeInMillis(), c2.getTimeInMillis());
 * infix 在java中调用和正常调用是一样的
 */
infix fun Long.differDay(milliseconds2: Long): Int {
    val dar1 = Calendar.getInstance()
    dar1.timeInMillis = this
    val dar2 = Calendar.getInstance()
    dar2.timeInMillis = milliseconds2
    // 先判断是否同年
    val y1 = dar1.get(Calendar.YEAR)
    val y2 = dar2.get(Calendar.YEAR)
    val d1 = dar1.get(Calendar.DAY_OF_YEAR)
    val d2 = dar2.get(Calendar.DAY_OF_YEAR)
    return if (y1 - y2 > 0) {
        d1 - d2 + dar2.getActualMaximum(Calendar.DAY_OF_YEAR)
    } else if (y1 - y2 < 0) {
        d1 - d2 - dar1.getActualMaximum(Calendar.DAY_OF_YEAR)
    } else {
        d1 - d2
    }
}

/**
 * 计算两个日期所差的小时数
 * GyDateUtilKT.differHours(c1.getTimeInMillis(), c2.getTimeInMillis());
 * infix 在java中调用和正常调用是一样的
 */
infix fun Long.differHours(date2: Long): Int {
    val dar1 = Calendar.getInstance()
    dar1.timeInMillis = this
    val dar2 = Calendar.getInstance()
    dar2.timeInMillis = date2
    val h1: Int = dar1.get(Calendar.HOUR_OF_DAY)
    val h2: Int = dar2.get(Calendar.HOUR_OF_DAY)
    val day = this differDay date2
    return h1 - h2 + day * 24
}

/**
 * 计算两个日期所差的分钟数
 * GyDateUtilKT.differMinutes(c1.getTimeInMillis(), c2.getTimeInMillis());
 * infix 在java中调用和正常调用是一样的
 */
infix fun Long.differMinutes(date2: Long): Int {
    val dar1 = Calendar.getInstance()
    dar1.timeInMillis = this
    val dar2 = Calendar.getInstance()
    dar2.timeInMillis = date2
    val m1: Int = dar1.get(Calendar.MINUTE)
    val m2: Int = dar2.get(Calendar.MINUTE)
    val h = this differHours date2
    return m1 - m2 + h * 60
}

/**
 * 获取周几
 * Monday周一 以下方法以此类推
 * 带时分秒的获取出来的日期 他的日期都是你某个时间段调用的日期
 */
fun getMondayYMDHMS() = Calendar.MONDAY.getDayOfWeek(ymdhms)
fun getTuesdayYMDHMS() = Calendar.TUESDAY.getDayOfWeek(ymdhms)
fun getWenesdayYMDHMS() = Calendar.WEDNESDAY.getDayOfWeek(ymdhms)
fun getThursdayYMDHMS() = Calendar.THURSDAY.getDayOfWeek(ymdhms)
fun getFridayYMDHMS() = Calendar.FRIDAY.getDayOfWeek(ymdhms)
fun getSaturdayYMDHMS() = Calendar.SATURDAY.getDayOfWeek(ymdhms)
fun getSundayYMDHMS() = Calendar.SUNDAY.getDayOfWeek(ymdhms)

fun getMondayYMD() = Calendar.MONDAY.getDayOfWeek(ymd)
fun getTuesdayYMD() = Calendar.TUESDAY.getDayOfWeek(ymd)
fun getWenesdayYMD() = Calendar.WEDNESDAY.getDayOfWeek(ymd)
fun getThursdayYMD() = Calendar.THURSDAY.getDayOfWeek(ymd)
fun getFridayYMD() = Calendar.FRIDAY.getDayOfWeek(ymd)
fun getSaturdayYMD() = Calendar.SATURDAY.getDayOfWeek(ymd)
fun getSundayYMD() = Calendar.SUNDAY.getDayOfWeek(ymd)

/**
 * 获取本月的第一天
 */
@SuppressLint("SimpleDateFormat")
fun getFirstDayInMonth(): String {
    return tryCatch {
        val c = GregorianCalendar()
        val simple = SimpleDateFormat(ymd)
        c.set(GregorianCalendar.DAY_OF_MONTH, 1)
        simple.format(c.time)
    }
}

/**
 * 获取本月最后一天
 */
@SuppressLint("SimpleDateFormat")
fun getLastDayInMonth() = tryCatch {
    val c = GregorianCalendar()
    val simple = SimpleDateFormat(ymd)
    c.set(Calendar.DATE, 1)
    c.roll(Calendar.DATE, -1)
    simple.format(c.time)
}



/**
 * 获取本周的某一天
 */
@SuppressLint("SimpleDateFormat")
private fun Int.getDayOfWeek(format: String): String {
    return tryCatch {
        val c = GregorianCalendar()
        val simple = SimpleDateFormat(format)
        val week = c.get(Calendar.DAY_OF_WEEK)
        if (week == this) {
            simple.format(c.time)
        } else {
            var offectDay = this - week
            if (this == Calendar.SUNDAY) {
                offectDay = 7 - abs(offectDay)
            }
            c.add(Calendar.DATE, offectDay)
            simple.format(c.time)
        }
    }
}

/**
 * 检测当前是否是闰年
 * (year能被4整除 并且 不能被100整除) 或者 year能被400整除,则该年为闰年.
 */
fun Int.checkIsLeapYear() = this % 4 == 0 && this % 400 != 0 || this % 400 == 0

fun String.checkIsLeapYear() = this.toInt().checkIsLeapYear()

/**
 * 根据当前日期判断上午还是下午
 */
@SuppressLint("SimpleDateFormat")
fun String.getTimeQuantumYMDHMS(): String {
    val date = SimpleDateFormat(ymdhms).parse(this) ?: return getTimeError
    return if (date.hours >= 12) pm else am
}

/**
 * 严格要求时间格式为[ymdhms]
 */
@SuppressLint("SimpleDateFormat")
fun String.getDynamicTime(): String {
    val simple = SimpleDateFormat(ymdhms)
    val c1 = Calendar.getInstance()
    val c2 = Calendar.getInstance()
    return tryCatch {
        c1.time = Date()
        c2.time = simple.parse(this)
        val d = c1.timeInMillis differDay c2.timeInMillis
        if (d == 0) {
            val h = c1.timeInMillis differHours c2.timeInMillis
            if (h > 0) {
                return "今天${getStringByFormat(2)}"
                // return "${h}小时前"
            } else if (h < 0) {
                // return "${abs(h)}小时后"
            } else if (h == 0) {
                val m = c1.timeInMillis differMinutes c2.timeInMillis
                if (m > 0) {
                    return "${m}分钟前"
                } else if (m < 0) {
                    // return "${abs(m)}分钟后"
                } else {
                    return "刚刚"
                }
            }
        } else if (d > 0) {
            if (d == 1) {
                return "昨天${getStringByFormat(2)}"
            } else if (d == 2) {
                return "前天${getStringByFormat(2)}"
            } else {
                return "${getStringByFormat()}"
            }
        } else if (d < 0) {
            if (d == -1) {
                //  return "明天${getStringByFormat()}"
            } else if (d == -2) {
                // return "后天${getStringByFormat()}"
            } else {
                // return "${abs(d)}天后${getStringByFormat()}"
            }
        }
        getStringByFormat()
    }


}

/**
 * @param type 要返回数据格式类型: 需要传入一个YMDHMS 会返回 type类型的
 */
@SuppressLint("SimpleDateFormat")
fun String.getStringByFormat(type: Int = 1): String {
    val long = this.changeYMDHMSTimeToLong()
    return long.getDate(type)
}

@SuppressLint("SimpleDateFormat")
fun String.getTimeQuantumHMS(): String {
    val date = SimpleDateFormat(hms).parse(this) ?: return getTimeError
    return if (date.hours >= 12) pm else am
}


/**
 * 获取距离当天晚上00:00:00的时间
 * 分割方式以':'冒号分割
 * 可以用解构去解析
 *
 */
fun getTodayLastTime(): String {
    return tryCatch {
        val simpleDateFormat = SimpleDateFormat(ymdhms2)
        val currentTime = simpleDateFormat.format(Date())
        val substring = currentTime.substring(0, 8) + "235959"
        val nowTime = simpleDateFormat.parse(currentTime).time / 1000
        val endTime = simpleDateFormat.parse(substring).time / 1000
        val totalTime = endTime - nowTime
        val hour = Math.floor((totalTime / 3600).toDouble()).toInt()
        val minute = Math.floor(((totalTime - hour * 3600) / 60).toDouble()).toInt()
        val second = Math.floor((totalTime - hour * 3600 - minute * 60).toDouble()).toInt()
        "$hour:$minute:$second"
    }
}


private inline fun tryCatch(block: () -> String): String {
    return try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        getTimeError
    }
}





