package com.slaviboy.zxcvbn.guesses

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.helpers.MathHelper.max
import com.slaviboy.zxcvbn.helpers.MathHelper.pow
import com.slaviboy.zxcvbn.matches.Match

class BruteforceGuess(
    context: Context
) : Guess(context) {

    override fun exec(match: Match): Double {
        var guesses = pow(
            BRUTEFORCE_CARDINALITY,
            match.tokenLength()
        )
        if (guesses.isInfinite()) {
            guesses = Double.MAX_VALUE
        }
        val minGuesses = if (match.tokenLength() == 1) {
            MIN_SUBMATCH_GUESSES_SINGLE_CHAR + 1.0
        } else {
            MIN_SUBMATCH_GUESSES_MULTI_CHAR + 1.0
        }
        return max(guesses, minGuesses)
    }
}