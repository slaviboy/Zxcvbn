package com.slaviboy.zxcvbn.helpers

import android.content.res.Resources
import com.slaviboy.zxcvbn.core.Dictionary
import com.slaviboy.zxcvbn.sealed.DefaultDictionary
import java.io.IOException

object DefaultDictionaryHelper {

    private fun load(resources: Resources, defaultDictionary: DefaultDictionary): Dictionary {
        val words: List<String> = LoaderHelper
            .load(resources, defaultDictionary.name, defaultDictionary.fileResId)
            .split("\n")
        return Dictionary(defaultDictionary.name, words)
    }

    private fun loadAll(
        resources: Resources
    ): List<Dictionary> {
        return arrayOf(
            DefaultDictionary.usTvAndFilm,
            DefaultDictionary.englishWikipedia,
            DefaultDictionary.passwords,
            DefaultDictionary.surnames,
            DefaultDictionary.maleNames,
            DefaultDictionary.femaleNames
        ).map {
            load(resources, it)
        }
    }

    @Throws(IOException::class)
    fun loadAllAsMap(
        resources: Resources
    ): MutableMap<String, Dictionary> {
        val dictionaryMap: MutableMap<String, Dictionary> = LinkedHashMap()
        for (dictionary in loadAll(resources)) {
            dictionaryMap[dictionary.name] = dictionary
        }
        return dictionaryMap
    }
}