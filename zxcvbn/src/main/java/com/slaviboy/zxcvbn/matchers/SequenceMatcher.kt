package com.slaviboy.zxcvbn.matchers

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.core.WipeableString
import com.slaviboy.zxcvbn.matches.Match
import com.slaviboy.zxcvbn.matches.SequenceMatch
import java.util.regex.Pattern

class SequenceMatcher(
    context: Context
) : Matcher(context) {

    override fun execute(password: CharSequence): List<Match> {
        val matches: MutableList<Match> = ArrayList()
        // Identifies sequences by looking for repeated differences in unicode codepoint.
        // this allows skipping, such as 9753, and also matches some extended unicode sequences
        // such as Greek and Cyrillic alphabets.
        //
        // for example, consider the input 'abcdb975zy'
        //
        // password: a   b   c   d   b    9   7   5   z   y
        // index:    0   1   2   3   4    5   6   7   8   9
        // delta:      1   1   1  -2  -41  -2  -2  69   1
        //
        // expected result:
        // [(i, j, delta), ...] = [(0, 3, 1), (5, 7, -2), (8, 9, 1)]
        if (password.length == 1) return matches
        var i = 0
        var lastDelta: Int? = null
        val wipeable = WipeableString(password)
        for (k in 1 until password.length) {
            val delta = wipeable.codePointAt(k) - wipeable.codePointAt(k - 1)
            if (lastDelta == null) {
                lastDelta = delta
            }
            if (delta == lastDelta) continue
            val j = k - 1
            val match = update(password, i, j, lastDelta)
            if (match != null) matches.add(match)
            i = j
            lastDelta = delta
        }
        wipeable.wipe()
        val match = update(password, i, password.length - 1, lastDelta)
        if (match != null) matches.add(match)
        return matches
    }

    companion object {
        private const val MAX_DELTA = 5
        private fun update(password: CharSequence, i: Int, j: Int, delta: Int?): Match? {
            var match: Match? = null
            if ((j - i > 1 || delta != null) && Math.abs(delta!!) == 1) {
                val token: CharSequence
                if (0 < Math.abs(delta!!) && Math.abs(delta) <= MAX_DELTA) {
                    token = WipeableString.copy(password, i, j + 1)
                    val sequenceName: String
                    val sequenceSpace: Int
                    if (Pattern.compile("^[a-z]+$").matcher(token).find()) {
                        sequenceName = "lower"
                        sequenceSpace = 26
                    } else if (Pattern.compile("^[A-Z]+$").matcher(token).find()) {
                        sequenceName = "upper"
                        sequenceSpace = 26
                    } else if (Pattern.compile("^\\d+$").matcher(token).find()) {
                        sequenceName = "digits"
                        sequenceSpace = 10
                    } else {
                        // conservatively stick with roman alphabet size.
                        // (this could be improved)
                        sequenceName = "unicode"
                        sequenceSpace = 26
                    }
                    match = SequenceMatch(
                        i,
                        j,
                        token,
                        sequenceName,
                        sequenceSpace,
                        delta > 0
                    )
                }
            }
            return match
        }
    }
}