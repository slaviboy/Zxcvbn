package com.slaviboy.zxcvbn.matches

open class DictionaryMatch(
    override val i: Int,
    override val j: Int,
    override val token: CharSequence,
    open val matchedWord: CharSequence,
    open val rank: Int,
    open val dictionaryName: String,
    open val reversed: Boolean,
    open val l33t: Boolean
) : Match(i, j, token)