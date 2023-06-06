package com.slaviboy.zxcvbn.matchers

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.core.L33tSubDict
import com.slaviboy.zxcvbn.core.WipeableString
import com.slaviboy.zxcvbn.matches.DictionaryL33Match
import com.slaviboy.zxcvbn.matches.DictionaryMatch
import com.slaviboy.zxcvbn.matches.Match

class L33tMatcher(
    context: Context,
    private val rankedDictionaries: Map<String, Map<String, Int>>
) : Matcher(context) {

    @JvmOverloads
    fun relevantL33tSubTable(password: CharSequence, table: Map<Char, List<Char>> = L33T_TABLE): Map<Char, List<Char>> {
        val passwordChars = HashMap<Char?, Boolean>()
        for (element in password) {
            passwordChars[element] = true
        }
        val subTable: MutableMap<Char, List<Char>> = HashMap()
        for ((letter, subs) in table) {
            val relevantSubs: MutableList<Char> = ArrayList()
            for (sub in subs) {
                if (passwordChars.containsKey(sub)) {
                    relevantSubs.add(sub)
                }
            }
            if (relevantSubs.size > 0) {
                subTable[letter] = relevantSubs
            }
        }
        return subTable
    }

    override fun execute(password: CharSequence): List<Match> {
        val matches: MutableList<Match> = ArrayList()
        val subTable = relevantL33tSubTable(password)
        val l33tSubs = L33tSubDict(subTable)
        for (sub in l33tSubs) {
            if (sub.isEmpty()) break
            val subbedPassword = translate(password, sub)
            for (match in DictionaryMatcher(context, rankedDictionaries).execute(subbedPassword) as List<DictionaryMatch>) {
                val token = WipeableString.copy(password, match.i, match.j + 1)
                val lower = WipeableString.lowerCase(token)
                if (lower.equals(match.matchedWord)) {
                    token.wipe()
                    lower.wipe()
                    continue
                }
                val matchSub: MutableMap<Char, Char> = HashMap()
                for ((subbedChr, chr) in sub) {
                    if (token.indexOf(subbedChr) != -1) {
                        matchSub[subbedChr] = chr
                    }
                }
                val subDisplays: MutableList<String> = ArrayList()
                for ((k, v) in matchSub) {
                    subDisplays.add(String.format("%s -> %s", k, v))
                }
                val subDisplay = subDisplays.joinToString { "," }
                matches.add(
                    DictionaryL33Match(
                        match.i,
                        match.j,
                        token,
                        match.matchedWord,
                        match.rank,
                        match.dictionaryName,
                        match.reversed,
                        true,
                        matchSub,
                        subDisplay
                    )
                )
                // Don't wipe token as the Match needs it
                lower.wipe()
            }
        }
        val lst: MutableList<Match> = ArrayList()
        for (match in matches) if (match.tokenLength() > 1) lst.add(match)
        return this.sorted(lst)
    }

    companion object {
        private val L33T_TABLE: Map<Char, List<Char>> = hashMapOf(
            'a' to listOf('4', '@'),
            'b' to listOf('8'),
            'c' to listOf('(', '{', '[', '<'),
            'e' to listOf('3'),
            'g' to listOf('6', '9'),
            'i' to listOf('1', '!', '|'),
            'l' to listOf('1', '|', '7'),
            'o' to listOf('0'),
            's' to listOf('$', '5'),
            't' to listOf('+', '7'),
            'x' to listOf('%'),
            'z' to listOf('2')
        )
    }
}