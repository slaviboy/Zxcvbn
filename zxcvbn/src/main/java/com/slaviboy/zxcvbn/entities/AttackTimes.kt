package com.slaviboy.zxcvbn.entities

data class AttackTimes(
    var crackTimeSeconds: CrackTimeSeconds,
    var crackTimesDisplay: CrackTimesDisplay,
    var score: Int
)