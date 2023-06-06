package com.slaviboy.zxcvbn.entities

data class CrackTimesDisplay(
    var onlineThrottling100perHour: String,
    var onlineNoThrottling10perSecond: String,
    var offlineSlowHashing1e4perSecond: String,
    var offlineFastHashing1e10PerSecond: String
)