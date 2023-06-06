package com.slaviboy.zxcvbn.core

import com.slaviboy.zxcvbn.matchers.OmnibusMatcher
import com.slaviboy.zxcvbn.matches.Match

class Matching(
    private val context: Context,
    orderedList: List<String>
) {
    private val rankedDictionaries: Map<String, Map<String, Int>>

    init {
        val dictionaryMap: MutableMap<String, Dictionary> = LinkedHashMap(context.dictionaryMap)
        dictionaryMap["user_inputs"] = Dictionary("user_inputs", orderedList)
        rankedDictionaries = buildRankedDictionaryMap(dictionaryMap)
    }

    fun omnimatch(password: CharSequence): List<Match> {
        return OmnibusMatcher(context, rankedDictionaries).execute(password)
    }

    private fun buildRankedDictionaryMap(
        dictionaryMap: Map<String, Dictionary>
    ): Map<String, Map<String, Int>> {
        val rankedDictionaries: MutableMap<String, Map<String, Int>> = HashMap()
        for (dictionary in dictionaryMap.values) {
            rankedDictionaries[dictionary.name] = dictionary.rankedDictionary
        }
        return rankedDictionaries
    }
}