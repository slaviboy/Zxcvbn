package com.slaviboy.zxcvbn.guesses

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.core.Keyboard
import com.slaviboy.zxcvbn.matches.Match
import com.slaviboy.zxcvbn.matches.SpatialMatch

class SpatialGuess(
    context: Context
) : Guess(context) {

    override fun exec(match: Match): Double {
        if (match !is SpatialMatch) return 0.0
        val keyboard: Keyboard = context.keyboardMap[match.graph] ?: return 0.0
        val s: Int = keyboard.startingPositions
        val d: Double = keyboard.averageDegree
        var guesses = 0.0
        val l: Int = match.tokenLength()
        val t: Int = match.turns
        for (i in 2..l) {
            val possibleTurns = Math.min(t, i - 1)
            for (j in 1..possibleTurns) {
                guesses += nCk(i - 1, j - 1) * s * Math.pow(d, j.toDouble())
            }
        }
        if (match.shiftedCount > 0) {
            val shiftedCount: Int = match.shiftedCount
            val unshiftedCount: Int = match.tokenLength() - match.shiftedCount
            if (unshiftedCount == 0) {
                guesses *= 2.0
            } else {
                var shiftedVariations = 0
                for (i in 1..Math.min(shiftedCount, unshiftedCount)) {
                    shiftedVariations += nCk(shiftedCount + unshiftedCount, i)
                }
                guesses *= shiftedVariations.toDouble()
            }
        }
        return guesses
    }
}