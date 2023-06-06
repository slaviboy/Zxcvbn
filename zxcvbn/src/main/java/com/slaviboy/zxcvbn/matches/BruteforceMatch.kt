package com.slaviboy.zxcvbn.matches

class BruteforceMatch(
    override val i: Int,
    override val j: Int,
    override val token: CharSequence
) : Match(i, j, token)