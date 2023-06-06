package com.slaviboy.zxcvbn.helpers

import com.slaviboy.zxcvbn.entities.AttackTimes
import com.slaviboy.zxcvbn.entities.CrackTimeSeconds
import com.slaviboy.zxcvbn.entities.CrackTimesDisplay
import java.math.BigDecimal

object AttackTimesHelper {

    fun estimateAttackTimes(guesses: Double): AttackTimes {
        val crackTimeSeconds = CrackTimeSeconds(
            divide(guesses, 100.0 / 3600.0),
            (guesses / 10.0),
            (guesses / 1e4),
            (guesses / 1e10)
        )
        val crackTimesDisplay = CrackTimesDisplay(
            displayTime(crackTimeSeconds.onlineThrottling100perHour),
            displayTime(crackTimeSeconds.onlineNoThrottling10perSecond),
            displayTime(crackTimeSeconds.offlineSlowHashing1e4perSecond),
            displayTime(crackTimeSeconds.offlineFastHashing1e10PerSecond)
        )
        return AttackTimes(crackTimeSeconds, crackTimesDisplay, guessesToScore(guesses))
    }

    fun guessesToScore(guesses: Double): Int {
        val DELTA = 5
        return if (guesses < 1e3 + DELTA) 0
        else if (guesses < 1e6 + DELTA) 1
        else if (guesses < 1e8 + DELTA) 2
        else if (guesses < 1e10 + DELTA) 3
        else 4
    }

    fun displayTime(seconds: Double): String {
        val minute = 60.0
        val hour = minute * 60
        val day = hour * 24
        val month = day * 31
        val year = month * 12
        val century = year * 100
        return if (seconds < 1) format(null, "less than a second")
        else if (seconds < minute) format(seconds, "%s second")
        else if (seconds < hour) format(divide(seconds, minute), "%s minute")
        else if (seconds < day) format(divide(seconds, hour), "%s hour")
        else if (seconds < month) format(divide(seconds, day), "%s day")
        else if (seconds < year) format(divide(seconds, month), "%s month")
        else if (seconds < century) format(divide(seconds, year), "%s year")
        else format(null, "centuries")
    }

    private fun format(number: Double?, text: String): String {
        var text = text
        if (number != null) {
            text = String.format(text, Math.round(number)) + if (number != 1.0) "s" else ""
        }
        return text
    }

    private fun divide(dividend: Double, divisor: Double): Double {
        val dividendDecimal = BigDecimal(dividend)
        val divisorDecimal = BigDecimal(divisor)
        return dividendDecimal.divide(divisorDecimal, BigDecimal.ROUND_HALF_DOWN).toDouble()
    }
}