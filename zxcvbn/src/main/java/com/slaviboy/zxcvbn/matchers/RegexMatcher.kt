package com.slaviboy.zxcvbn.matchers

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.core.WipeableString
import com.slaviboy.zxcvbn.matches.Match
import com.slaviboy.zxcvbn.matches.RegexMatch
import java.util.regex.Pattern

class RegexMatcher(
    context: Context
) : Matcher(context) {

    override fun execute(password: CharSequence): List<Match> {
        val matches: MutableList<Match> = ArrayList()
        for ((name, value) in REGEXEN) {
            val rxMatch = Pattern.compile(value).matcher(password)
            while (rxMatch.find()) {
                val token: CharSequence = WipeableString(rxMatch.group())
                matches.add(
                    RegexMatch(
                        rxMatch.start(),
                        rxMatch.start() + token.length - 1,
                        token,
                        name,
                        rxMatch
                    )
                )
            }
        }
        return this.sorted(matches)
    }

    companion object {
        private val REGEXEN: MutableMap<String, String> = hashMapOf(
            "recent_year" to "19\\d\\d|200\\d|201\\d|202\\d"
        )
    }
}