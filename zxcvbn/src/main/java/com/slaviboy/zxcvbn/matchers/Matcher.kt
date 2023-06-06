package com.slaviboy.zxcvbn.matchers

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.core.WipeableString
import com.slaviboy.zxcvbn.matches.Match
import java.util.Collections

abstract class Matcher(
    val context: Context
) {
    abstract fun execute(
        password: CharSequence
    ): List<Match>

    protected fun sorted(matches: List<Match>): List<Match> {
        Collections.sort(matches) { o1, o2 ->
            val c = o1.i - o2.i
            if (c != 0) {
                c
            } else {
                o1.j - o2.j
            }
        }
        return matches
    }

    protected fun translate(
        string: CharSequence,
        chrMap: Map<Char, Char>
    ): CharSequence {
        val characters: MutableList<Char> = ArrayList()
        for (element in string) {
            characters.add(
                (if (chrMap.containsKey(element)) chrMap[element]
                else element)!!
            )
        }
        val sb = StringBuilder()
        for (c in characters) {
            sb.append(c)
        }
        val result = WipeableString(sb)
        WipeableString.wipeIfPossible(sb)
        return result
    }

    protected fun extend(
        lst: MutableList<Match>,
        lst2: List<Match>
    ): List<Match> {
        lst.addAll(lst2)
        return lst
    }
}