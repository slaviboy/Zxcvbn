package com.slaviboy.zxcvbn.matches

class DateMatch(
    override val i: Int,
    override val j: Int,
    override val token: CharSequence,
    val separator: String,
    val year: Int,
    val month: Int,
    val day: Int
) : Match(i, j, token)