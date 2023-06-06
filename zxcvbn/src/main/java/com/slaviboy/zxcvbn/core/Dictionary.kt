package com.slaviboy.zxcvbn.core

class Dictionary(
    val name: String,
    frequencies: List<String>
) {
    val rankedDictionary: Map<String, Int> = toRankedDictionary(frequencies)

    private fun toRankedDictionary(frequencies: List<String>): Map<String, Int> {
        val result: MutableMap<String, Int> = HashMap()
        var i = 1 // rank starts at 1, not 0
        for (word in frequencies) {
            result[word] = i
            i++
        }
        return result
    }
}