package com.slaviboy.zxcvbn.matches

class RepeatMatch(
    override val i: Int,
    override val j: Int,
    override val token: CharSequence,
    val baseToken: CharSequence,
    var baseGuesses: Double,
    val baseMatches: List<Match>?,
    val repeatCount: Int
) : Match(i, j, token)