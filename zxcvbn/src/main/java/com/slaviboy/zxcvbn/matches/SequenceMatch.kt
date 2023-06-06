package com.slaviboy.zxcvbn.matches

class SequenceMatch(
    override val i: Int,
    override val j: Int,
    override val token: CharSequence,
    val sequenceName: String,
    val sequenceSpace: Int,
    val ascending: Boolean
) : Match(i, j, token)