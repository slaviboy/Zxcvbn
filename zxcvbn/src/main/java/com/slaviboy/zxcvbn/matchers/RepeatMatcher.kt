package com.slaviboy.zxcvbn.matchers

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.core.Matching
import com.slaviboy.zxcvbn.core.WipeableString
import com.slaviboy.zxcvbn.helpers.StrengthHelper
import com.slaviboy.zxcvbn.matches.Match
import com.slaviboy.zxcvbn.matches.RepeatMatch
import java.util.regex.Pattern

class RepeatMatcher(
    context: Context
) : Matcher(context) {

    override fun execute(password: CharSequence): List<Match> {
        val matches: MutableList<Match> = ArrayList()
        val greedy = Pattern.compile("(.+)\\1+")
        val lazy = Pattern.compile("(.+?)\\1+")
        val lazyAnchored = Pattern.compile("^(.+?)\\1+$")
        val passwordLength = password.length
        var lastIndex = 0
        while (lastIndex < passwordLength) {
            val greedyMatch = greedy.matcher(password)
            val lazyMatch = lazy.matcher(password)
            greedyMatch.region(lastIndex, passwordLength)
            lazyMatch.region(lastIndex, passwordLength)
            if (!greedyMatch.find()) break
            var match: java.util.regex.Matcher
            var baseToken: CharSequence
            if (greedyMatch.group(0).length > (if (lazyMatch.find()) lazyMatch.group(0).length else 0)) {
                match = greedyMatch
                val matcher = lazyAnchored.matcher(match.group(0))
                baseToken = if (matcher.find()) matcher.group(1) else match.group(0)
            } else {
                match = lazyMatch
                baseToken = match.group(1)
            }
            val i = match.start(0)
            val j = match.start(0) + match.group(0).length - 1
            val baseAnalysis = StrengthHelper.mostGuessableMatchSequence(
                context,
                baseToken,
                Matching(context, ArrayList()).omnimatch(baseToken)
            )
            val baseMatches = baseAnalysis.sequence
            val baseGuesses = baseAnalysis.guesses
            baseToken = WipeableString(baseToken)
            matches.add(
                RepeatMatch(
                    i,
                    j,
                    match.group(0),
                    baseToken,
                    baseGuesses,
                    baseMatches,
                    match.group(0).length / baseToken.length
                )
            )
            lastIndex = j + 1
        }
        return matches
    }
}