package com.slaviboy.zxcvbn

import android.content.res.Resources
import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.core.Dictionary
import com.slaviboy.zxcvbn.core.Feedback
import com.slaviboy.zxcvbn.core.Keyboard
import com.slaviboy.zxcvbn.core.Matching
import com.slaviboy.zxcvbn.core.Strength
import com.slaviboy.zxcvbn.helpers.AttackTimesHelper.estimateAttackTimes
import com.slaviboy.zxcvbn.helpers.DefaultDictionaryHelper
import com.slaviboy.zxcvbn.helpers.DefaultKeyboardHelper
import com.slaviboy.zxcvbn.helpers.StrengthHelper
import com.slaviboy.zxcvbn.matches.Match

class Zxcvbn(
    resources: Resources,
    val dictionaryMap: Map<String, Dictionary> = DefaultDictionaryHelper.loadAllAsMap(resources),
    val keyboardMap: Map<String, Keyboard> = DefaultKeyboardHelper.loadAllAsMap(resources),
    val context: Context = Context(dictionaryMap, keyboardMap)
) {
    fun measure(
        password: CharSequence,
        sanitizedInputs: List<String>? = null
    ): Strength {
        val lowerSanitizedInputs = sanitizedInputs?.map {
            it.lowercase()
        } ?: emptyList()
        val start = time()
        val matching = createMatching(lowerSanitizedInputs)
        val matches: List<Match> = matching.omnimatch(password)
        val strength = StrengthHelper.mostGuessableMatchSequence(context, password, matches)
        val end = time() - start
        val attackTimes = estimateAttackTimes(strength.guesses)
        strength.apply {
            calcTime = end
            crackTimeSeconds = attackTimes.crackTimeSeconds
            crackTimesDisplay = attackTimes.crackTimesDisplay
            score = attackTimes.score
            feedback = Feedback.getFeedback(strength.score, strength.sequence)
        }
        return strength
    }

    fun createMatching(lowerSanitizedInputs: List<String>): Matching {
        return Matching(context, lowerSanitizedInputs)
    }

    private fun time(): Long {
        return System.nanoTime()
    }
}