package com.slaviboy.zxcvbn.matchers

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.core.WipeableString
import com.slaviboy.zxcvbn.matches.DictionaryMatch
import com.slaviboy.zxcvbn.matches.Match

class ReverseDictionaryMatcher(
    context: Context,
    val rankedDictionaries: Map<String, Map<String, Int>> = mapOf()
) : Matcher(context) {

    override fun execute(password: CharSequence): List<Match> {
        val reversedPassword = WipeableString.reversed(password)
        val matches: MutableList<Match> = ArrayList()
        val list = DictionaryMatcher(context, rankedDictionaries)
            .execute(reversedPassword) as List<DictionaryMatch>
        for (match in list) {
            matches.add(
                DictionaryMatch(
                    password.length - 1 - match.j,
                    password.length - 1 - match.i,
                    WipeableString.reversed(match.token),
                    match.matchedWord,
                    match.rank,
                    match.dictionaryName,
                    true,
                    false
                )
            )
        }
        return this.sorted(matches)
    }
}