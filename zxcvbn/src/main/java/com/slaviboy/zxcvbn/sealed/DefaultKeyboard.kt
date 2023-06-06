package com.slaviboy.zxcvbn.sealed

import androidx.annotation.RawRes
import com.slaviboy.zxcvbn.R

sealed class DefaultKeyboard(
    open val name: String,
    @RawRes open val fileResId: Int
)

sealed class SlantedKeyboard(
    override val name: String,
    @RawRes override val fileResId: Int
) : DefaultKeyboard(name, fileResId) {
    object qwerty : SlantedKeyboard("qwerty", R.raw.qwerty)
    object dvorak : SlantedKeyboard("dvorak", R.raw.dvorak)
    object jis : SlantedKeyboard("jis", R.raw.jis)
}

sealed class AlignedKeyboard(
    override val name: String,
    @RawRes override val fileResId: Int
) : DefaultKeyboard(name, fileResId) {
    object keypad : AlignedKeyboard("keypad", R.raw.keypad)
    object macKeypad : AlignedKeyboard("mac_keypad", R.raw.mac_keypad)
}