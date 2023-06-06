package com.slaviboy.zxcvbn.guesses

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.helpers.StrengthHelper
import com.slaviboy.zxcvbn.matches.BruteforceMatch
import com.slaviboy.zxcvbn.matches.DateMatch
import com.slaviboy.zxcvbn.matches.DictionaryMatch
import com.slaviboy.zxcvbn.matches.Match
import com.slaviboy.zxcvbn.matches.RegexMatch
import com.slaviboy.zxcvbn.matches.RepeatMatch
import com.slaviboy.zxcvbn.matches.SequenceMatch
import com.slaviboy.zxcvbn.matches.SpatialMatch

class EstimateGuess(
    context: Context,
    private val password: CharSequence
) : Guess(context) {

    override fun exec(match: Match): Double {
        if (match.guesses != null) return match.guesses!!
        var minGuesses = 1
        if (match.tokenLength() < password.length) {
            minGuesses = if (match.tokenLength() == 1) {
                MIN_SUBMATCH_GUESSES_SINGLE_CHAR
            } else {
                MIN_SUBMATCH_GUESSES_MULTI_CHAR
            }
        }
        val guess: Guess = when (match) {
            is BruteforceMatch -> BruteforceGuess(context)
            is DictionaryMatch -> DictionaryGuess(context)
            is SpatialMatch -> SpatialGuess(context)
            is RepeatMatch -> RepeatGuess(context)
            is SequenceMatch -> SequenceGuess(context)
            is RegexMatch -> RegexGuess(context)
            is DateMatch -> DateGuess(context)
            else -> BruteforceGuess(context)
        }
        match.guesses = Math.max(guess.exec(match), minGuesses.toDouble())
        match.guessesLog10 = StrengthHelper.log10(match.guesses!!)
        return match.guesses!!
    }
}