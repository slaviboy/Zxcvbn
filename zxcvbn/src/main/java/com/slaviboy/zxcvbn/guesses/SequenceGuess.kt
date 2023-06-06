package com.slaviboy.zxcvbn.guesses

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.matches.Match
import com.slaviboy.zxcvbn.matches.SequenceMatch
import java.util.regex.Pattern

class SequenceGuess(
    context: Context
) : Guess(context) {

    override fun exec(match: Match): Double {
        if (match !is SequenceMatch) return 0.0
        val firstChr: Char = match.token[0]
        var baseGuesses: Double = if (START_POINTS.contains(firstChr)) {
            4.0
        } else {
            if (Pattern.compile("\\d").matcher(firstChr.toString()).find()) {
                10.0
            } else {
                26.0
            }
        }
        if (!match.ascending) baseGuesses *= 2.0
        return baseGuesses * match.tokenLength()
    }

    companion object {
        private val START_POINTS = listOf('a', 'A', 'z', 'Z', '0', '1', '9')
    }
}