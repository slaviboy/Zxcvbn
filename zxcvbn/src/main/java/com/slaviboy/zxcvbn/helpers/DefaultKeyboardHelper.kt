package com.slaviboy.zxcvbn.helpers

import android.content.res.Resources
import com.slaviboy.zxcvbn.core.Keyboard
import com.slaviboy.zxcvbn.graphBuilders.AlignedAdjacentGraphBuilder
import com.slaviboy.zxcvbn.graphBuilders.SlantedAdjacentGraphBuilder
import com.slaviboy.zxcvbn.sealed.AlignedKeyboard
import com.slaviboy.zxcvbn.sealed.DefaultKeyboard
import com.slaviboy.zxcvbn.sealed.SlantedKeyboard
import java.io.IOException

object DefaultKeyboardHelper {

    /*private fun load(resources: Resources, defaultKeyboard: DefaultKeyboard): Keyboard {
        val text: String = LoaderHelper
            .load(resources, defaultKeyboard.name, defaultKeyboard.fileResId)
        val adjacentGraphBuilder = when (defaultKeyboard) {
            is SlantedKeyboard -> SlantedAdjacentGraphBuilder(text)
            is AlignedKeyboard -> AlignedAdjacentGraphBuilder(text)
        }
        return Keyboard(defaultKeyboard.name, adjacentGraphBuilder)
    }*/

    private fun loadAll(
        resources: Resources
    ): List<Keyboard> {
        return emptyList()
        /*return arrayOf(
            SlantedKeyboard.qwerty,
            SlantedKeyboard.dvorak,
            SlantedKeyboard.jis,
            AlignedKeyboard.keypad,
            AlignedKeyboard.macKeypad
        ).map {
            load(resources, it)
        }*/
    }

    @Throws(IOException::class)
    fun loadAllAsMap(
        resources: Resources
    ): MutableMap<String, Keyboard> {
        val keyboardMap: MutableMap<String, Keyboard> = LinkedHashMap()
        for (keyboard in loadAll(resources)) {
            keyboardMap[keyboard.name] = keyboard
        }
        return keyboardMap
    }
}