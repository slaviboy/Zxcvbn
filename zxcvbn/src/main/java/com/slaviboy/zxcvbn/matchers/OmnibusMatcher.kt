package com.slaviboy.zxcvbn.matchers

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.matches.Match

class OmnibusMatcher(
    context: Context,
    val dictionaries: Map<String, Map<String, Int>> = HashMap()
) : Matcher(context) {

    override fun execute(password: CharSequence): List<Match> {
        val matchers: MutableList<Matcher> = arrayListOf(
            DictionaryMatcher(context, dictionaries),
            ReverseDictionaryMatcher(context, dictionaries),
            L33tMatcher(context, dictionaries),
            SpatialMatcher(context),
            RepeatMatcher(context),
            SequenceMatcher(context),
            RegexMatcher(context),
            DateMatcher(context)
        )
        val matches: MutableList<Match> = ArrayList()
        for (matcher in matchers) {
            matches.addAll(matcher.execute(password))
        }
        return this.sorted(matches)
    }
}