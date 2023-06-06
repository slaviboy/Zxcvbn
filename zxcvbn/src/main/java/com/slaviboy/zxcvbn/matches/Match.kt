package com.slaviboy.zxcvbn.matches

abstract class Match(
    open val i: Int,
    open val j: Int,
    open val token: CharSequence
) {
    var guesses: Double? = null
    var guessesLog10: Double? = null

    fun tokenLength(): Int {
        return token.length
    }
}