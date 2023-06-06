package com.slaviboy.zxcvbn.matches

class SpatialMatch(
    override val i: Int,
    override val j: Int,
    override val token: CharSequence,
    val graph: String,
    val turns: Int,
    val shiftedCount: Int
) : Match(i, j, token)