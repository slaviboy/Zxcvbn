package com.slaviboy.zxcvbn.guesses

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.helpers.MathHelper.abs
import com.slaviboy.zxcvbn.helpers.MathHelper.max
import com.slaviboy.zxcvbn.matches.DateMatch
import com.slaviboy.zxcvbn.matches.Match

class DateGuess(
    context: Context
) : Guess(context) {

    override fun exec(match: Match): Double {
        if (match !is DateMatch) return 0.0
        val yearSpace = max(
            abs(match.year - REFERENCE_YEAR),
            MIN_YEAR_SPACE
        )
        var guesses = yearSpace * 365.0
        if (match.separator.isNotEmpty()) {
            guesses *= 4.0
        }
        return guesses
    }
}