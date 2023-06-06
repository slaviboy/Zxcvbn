package com.slaviboy.zxcvbn.core

import com.slaviboy.zxcvbn.entities.CrackTimeSeconds
import com.slaviboy.zxcvbn.entities.CrackTimesDisplay
import com.slaviboy.zxcvbn.matches.DictionaryMatch
import com.slaviboy.zxcvbn.matches.Match
import com.slaviboy.zxcvbn.matches.RepeatMatch

data class Strength(
    var password: CharSequence,
    var guesses: Double,
    var guessesLog10: Double,
    var sequence: List<Match>
) {
    lateinit var crackTimeSeconds: CrackTimeSeconds
    lateinit var crackTimesDisplay: CrackTimesDisplay
    lateinit var feedback: Feedback

    var score: Int = 0
    var calcTime: Long = 0

    /**
     * Attempts to wipe any sensitive content from the object.
     */
    fun wipe() {
        WipeableString.wipeIfPossible(password)
        for (match in sequence) {
            WipeableString.wipeIfPossible(match.token)
            if (match is RepeatMatch) {
                WipeableString.wipeIfPossible(match.baseToken)
            }
            if (match is DictionaryMatch) {
                WipeableString.wipeIfPossible(match.matchedWord)
            }
        }
    }
}