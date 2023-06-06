package com.slaviboy.zxcvbn.guesses

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.core.WipeableString
import com.slaviboy.zxcvbn.matches.DictionaryL33Match
import com.slaviboy.zxcvbn.matches.DictionaryMatch
import com.slaviboy.zxcvbn.matches.Match
import java.util.regex.Pattern

class DictionaryGuess(
    context: Context
) : Guess(context) {

    override fun exec(match: Match): Double {
        if (match !is DictionaryMatch) return 0.0
        val rank = match.rank.toDouble()
        //match.baseGuesses = rank
        val uppercaseVariations = uppercaseVariations(match)
        val l33tVariations = l33tVariations(match)
        val reversedVariations = if (match.reversed) {
            2.0
        } else {
            1.0
        }
        return (rank * uppercaseVariations * l33tVariations * reversedVariations)
    }

    fun uppercaseVariations(match: Match): Int {
        val word: CharSequence = match.token
        val lowercaseWord = WipeableString.lowerCase(word)
        if (ALL_LOWER.matcher(word).find(0) || lowercaseWord == word) return 1
        for (pattern in arrayOf(START_UPPER, END_UPPER, ALL_UPPER)) if (pattern.matcher(word).find()) return 2
        var u = 0
        var l = 0
        for (n in word.indices) {
            l += if (Character.isLowerCase(word[n])) 1 else 0
            u += if (Character.isUpperCase(word[n])) 1 else 0
        }
        var variations = 0
        for (i in 1..Math.min(u, l)) variations += nCk(u + l, i)
        lowercaseWord.wipe()
        return variations
    }

    fun l33tVariations(match: Match): Int {
        if (match !is DictionaryL33Match) return 1
        var variations = 1
        for ((subbed, unsubbed) in match.sub.entries) {
            var s = 0
            var u = 0
            val lower = WipeableString.lowerCase(match.token)
            for (chr in lower.charArray()) {
                if (chr == subbed) s++
                if (chr == unsubbed) u++
            }
            if (s == 0 || u == 0) {
                variations *= 2
            } else {
                val p = Math.min(u, s)
                var possibilities = 0
                for (i in 1..p) possibilities += nCk(u + s, i)
                variations *= possibilities
            }
        }
        return variations
    }

    companion object {
        val START_UPPER = Pattern.compile("^[A-Z][^A-Z]+$")
        val ALL_UPPER = Pattern.compile("^[^a-z]+$")
        private val END_UPPER = Pattern.compile("^[^A-Z]+[A-Z]$")
        private val ALL_LOWER = Pattern.compile("^[^A-Z]+$")
    }
}