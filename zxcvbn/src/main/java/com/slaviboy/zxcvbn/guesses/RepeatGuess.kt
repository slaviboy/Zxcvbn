package com.slaviboy.zxcvbn.guesses

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.matches.Match
import com.slaviboy.zxcvbn.matches.RepeatMatch

class RepeatGuess(
    context: Context
) : Guess(context) {

    override fun exec(match: Match): Double {
        if (match !is RepeatMatch) return 0.0
        return match.baseGuesses * match.repeatCount
    }
}