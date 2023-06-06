package com.slaviboy.zxcvbn.entities

data class CrackTimeSeconds(
    var onlineThrottling100perHour: Double,
    var onlineNoThrottling10perSecond: Double,
    var offlineSlowHashing1e4perSecond: Double,
    var offlineFastHashing1e10PerSecond: Double
)