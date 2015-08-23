package com.redo.redo

import android.util.Log

import java.util.Calendar
import java.util.Date
import java.lang.String

/**
 * Created by jakob on 14.08.15.
 */
public object TimeCalculator {




    public fun DelayAndTypeInMilSeconds(delay: Int, type: kotlin.String): Long {
        var year = 70
        var month = 0
        var day = 0
        if (type.compareTo("years", ignoreCase = true) == 0) {
            year += delay
        } else if (type.compareTo("months", ignoreCase = true) == 0) {
            month += delay
        } else if (type.compareTo("weeks", ignoreCase = true) == 0) {
            day += (delay * 7)
        } else if (type.compareTo("days", ignoreCase = true) == 0) {
            day += delay
        } else {
            Log.d("DelayAndTypeInMilSecond", "NOT_years_months_weeks_days")
        }
        /*
        Log.d("TYPE", type)
        Log.d("ZERO_DATE", (Date(70,0,0).getTime() + 90000000).toString())
        Log.d("years", year.toString())
        Log.d("months", month.toString())
        Log.d("days", day.toString())
*/
        return Date(year, month, day).getTime() + 90000000
    }

    public fun MilSecondsToDelayAndType(time: Long): DateUnitAndQuantity {
        Log.d("TIME", time.toString())
        val oneyear = Date(71, 0, 0).getTime() + 90000000
        Log.d("oneyear", oneyear.toString())
        val onemonth = Date(70, 1, 0).getTime() + 90000000
        Log.d("onemonth", onemonth.toString())
        val oneweek = Date(70, 0, 7).getTime() + 90000000
        Log.d("oneweek", oneweek.toString())
        val oneday = Date(70, 0, 1).getTime() + 90000000
        Log.d("oneday", oneday.toString())

        if (time > oneyear) {
            val result = (time / oneyear).toInt()
            return DateUnitAndQuantity("years", result)
        }
        if (time > onemonth) {
            val result = (time / onemonth).toInt()
            return DateUnitAndQuantity("month", result)
        }
        if (time > oneweek) {
            val result = (time / oneweek).toInt()
            return DateUnitAndQuantity("weeks", result)
        }
        if (time > oneday) {
            val result = (time / oneday).toInt()
            return DateUnitAndQuantity("days", result)
        }
        Log.i("NO_VALUE_FOUND_FOR ", java.lang.String.valueOf(time))
        return DateUnitAndQuantity("days", 0)
    }

    public fun TimeLeftSentence(lastdone: Long, delay: Int, type: kotlin.String): kotlin.String {
        Log.d("IN_TimeLeftSentence", "____________START___________")
        val today = Date().getTime()
        Log.d("today", today.toString())
        Log.d("lastdone", lastdone.toString())
        val time_elapsed = today - lastdone
        Log.d("diff", time_elapsed.toString())
        val timeleft = DelayAndTypeInMilSeconds(delay, type) - time_elapsed
        Log.d("timeleft", String.valueOf(timeleft))
        val sentence = "More than " + (MilSecondsToDelayAndType(timeleft).Quantity!!).toString() + " " + MilSecondsToDelayAndType(timeleft).Unit + " left"
        Log.d("SENTENCE", sentence)
        Log.d("OUT_TimeLeftSentence", "___________END_______________")
        return sentence
    }

    public fun TimePassedInPercentage(lastdone: Long, delay: Int, type: kotlin.String): Int {
        Log.d("TimePassedInPercentage", "____________START___________")
        Log.d("LASTDONE", String.valueOf(lastdone))
        val today = Date().getTime()
        Log.d("TODAY", String.valueOf(today))
        val time_elapsed = today - lastdone
        Log.d("DIFF", String.valueOf(time_elapsed))
        Log.d("DelayAndTypeInMilSecond", String.valueOf(DelayAndTypeInMilSeconds(delay, type)))
        val timeleft = DelayAndTypeInMilSeconds(delay, type) -  time_elapsed
        Log.d("TIMELEFT", String.valueOf(timeleft))
        val onePercentage = (DelayAndTypeInMilSeconds(delay, type)) / 100
        Log.d("ONEPERCANTAGE", String.valueOf(onePercentage))
        Log.d("PERCENTAGE", String.valueOf(timeleft / onePercentage))
        val percentage = 100 - (timeleft / onePercentage).toInt()
        Log.d("TimePassedInPercentage", "____________END___________")
        return percentage
    }
}
