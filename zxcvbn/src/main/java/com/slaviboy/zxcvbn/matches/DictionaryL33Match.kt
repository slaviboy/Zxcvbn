package com.slaviboy.zxcvbn.matches

class DictionaryL33Match(
    override val i: Int,
    override val j: Int,
    override val token: CharSequence,
    override val matchedWord: CharSequence,
    override val rank: Int,
    override val dictionaryName: String,
    override val reversed: Boolean,
    override val l33t: Boolean,
    val sub: Map<Char, Char>,
    val subDisplay: String
) : DictionaryMatch(i, j, token, matchedWord, rank, dictionaryName, reversed, l33t)