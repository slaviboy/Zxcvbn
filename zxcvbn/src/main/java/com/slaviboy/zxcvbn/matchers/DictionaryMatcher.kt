package com.slaviboy.zxcvbn.matchers

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.core.WipeableString
import com.slaviboy.zxcvbn.matches.DictionaryMatch
import com.slaviboy.zxcvbn.matches.Match

class DictionaryMatcher(
    context: Context,
    val rankedDictionaries: Map<String, Map<String, Int>> = HashMap()
) : Matcher(context) {

    override fun execute(password: CharSequence): List<Match> {
        val matches: MutableList<Match> = ArrayList()
        val len = password.length
        val passwordLower: WipeableString = WipeableString.lowerCase(password)
        for ((dictionaryName, rankedDict) in rankedDictionaries) {
            for (i in 0 until len) {
                for (j in i until len) {
                    val word: CharSequence = passwordLower.subSequence(i, j + 1)
                    if (rankedDict.containsKey(word)) {
                        val rank = rankedDict[word]!!
                        val token = WipeableString.copy(password, i, j + 1)
                        matches.add(
                            DictionaryMatch(
                                i,
                                j,
                                token,
                                word,
                                rank,
                                dictionaryName,
                                false,
                                false
                            )
                        )
                    }
                }
            }
        }
        passwordLower.wipe()
        return sorted(matches)
    }
}