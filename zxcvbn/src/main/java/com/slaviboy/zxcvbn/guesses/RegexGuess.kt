package com.slaviboy.zxcvbn.guesses

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.core.WipeableString
import com.slaviboy.zxcvbn.helpers.MathHelper.abs
import com.slaviboy.zxcvbn.helpers.MathHelper.max
import com.slaviboy.zxcvbn.helpers.MathHelper.pow
import com.slaviboy.zxcvbn.matches.Match
import com.slaviboy.zxcvbn.matches.RegexMatch

class RegexGuess(
    context: Context
) : Guess(context) {

    override fun exec(match: Match): Double {
        if (match !is RegexMatch) return 0.0
        if (CHAR_CLASS_BASES.containsKey(match.regexName)) {
            return pow(
                CHAR_CLASS_BASES[match.regexName]!!,
                match.tokenLength()
            )
        } else if (match.regexName == "recent_year") {
            var yearSpace = abs(parseInt(match.token) - REFERENCE_YEAR)
            yearSpace = max(yearSpace, Guess.MIN_YEAR_SPACE)
            return yearSpace
        }
        return 0.0
    }

    companion object {
        private val CHAR_CLASS_BASES: MutableMap<String, Int> = HashMap()

        init {
            CHAR_CLASS_BASES["alpha_lower"] = 26
            CHAR_CLASS_BASES["alpha_upper"] = 26
            CHAR_CLASS_BASES["alpha"] = 52
            CHAR_CLASS_BASES["alphanumeric"] = 62
            CHAR_CLASS_BASES["digits"] = 10
            CHAR_CLASS_BASES["symbols"] = 33
        }

        private fun parseInt(s: CharSequence): Int {
            var result = 0
            try {
                result = WipeableString.parseInt(s)
            } catch (e: NumberFormatException) {
                // ignore
            }
            return result
        }
    }
}