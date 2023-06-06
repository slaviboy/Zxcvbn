package com.slaviboy.zxcvbn.guesses

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.matches.Match

abstract class Guess(
    val context: Context
) {
    abstract fun exec(match: Match): Double

    companion object {

        const val BRUTEFORCE_CARDINALITY: Int = 10
        const val MIN_SUBMATCH_GUESSES_SINGLE_CHAR: Int = 10
        const val MIN_SUBMATCH_GUESSES_MULTI_CHAR: Int = 50
        const val MIN_YEAR_SPACE: Int = 20
        const val REFERENCE_YEAR: Int = 2000

        fun nCk(n: Int, k: Int): Int {
            // http://blog.plover.com/math/choose.html
            var n = n
            if (k > n) return 0
            if (k == 0) return 1
            var r = 1
            for (d in 1..k) {
                r *= n
                r /= d
                n -= 1
            }
            return r
        }
    }
}