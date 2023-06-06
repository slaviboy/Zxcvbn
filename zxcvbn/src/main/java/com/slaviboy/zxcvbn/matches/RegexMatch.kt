package com.slaviboy.zxcvbn.matches

import java.util.regex.Matcher

class RegexMatch(
    override val i: Int,
    override val j: Int,
    override val token: CharSequence,
    val regexName: String,
    val regexMatch: Matcher
) : Match(i, j, token)