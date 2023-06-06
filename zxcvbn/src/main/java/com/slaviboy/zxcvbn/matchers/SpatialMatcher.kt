package com.slaviboy.zxcvbn.matchers

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.core.Keyboard
import com.slaviboy.zxcvbn.core.WipeableString
import com.slaviboy.zxcvbn.matches.Match
import com.slaviboy.zxcvbn.matches.SpatialMatch
import java.util.regex.Pattern

class SpatialMatcher(
    context: Context,
    keyboardMap: Map<String, Keyboard>?
) : Matcher(context) {

    private val shiftedRx = Pattern.compile("[~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:\"ZXCVBNM<>?]")
    private val keyboards: Map<String, Keyboard> = LinkedHashMap(keyboardMap)

    constructor(context: Context) : this(context, context.keyboardMap)

    override fun execute(password: CharSequence): List<Match> {
        val matches: MutableList<Match> = mutableListOf()
        for (keyboard in keyboards.values) {
            extend(matches, spatialMatchHelper(password, keyboard))
        }
        return this.sorted(matches)
    }

    private fun spatialMatchHelper(password: CharSequence, keyboard: Keyboard): List<Match> {
        val matches: MutableList<Match> = ArrayList()
        var i = 0
        while (i < password.length - 1) {
            var j = i + 1
            var lastDirection = 0
            var turns = 0
            var shiftedCount: Int = if (keyboard.isSlanted && shiftedRx.matcher(password[i].toString()).find()) {
                1
            } else {
                0
            }
            val graph = keyboard.adjacencyGraph
            while (true) {
                val prevChar = password[j - 1]
                var found = false
                var foundDirection: Int
                var curDirection = -1
                val adjacents = if (graph.containsKey(prevChar)) graph[prevChar]!! else emptyList<String>()
                if (j < password.length) {
                    val curChar = password[j]
                    for (adj in adjacents) {
                        curDirection += 1
                        if (adj != null && adj.contains(curChar.toString())) {
                            found = true
                            foundDirection = curDirection
                            if (adj.indexOf(curChar.toString()) == 1) {
                                shiftedCount += 1
                            }
                            if (lastDirection != foundDirection) {
                                turns += 1
                                lastDirection = foundDirection
                            }
                            break
                        }
                    }
                }
                if (found) {
                    j += 1
                } else {
                    if (j - i > 2) {
                        matches.add(
                            SpatialMatch(
                                i,
                                j - 1,
                                WipeableString.copy(password, i, j),
                                keyboard.name,
                                turns,
                                shiftedCount
                            )
                        )
                    }
                    i = j
                    break
                }
            }
        }
        return matches
    }
}