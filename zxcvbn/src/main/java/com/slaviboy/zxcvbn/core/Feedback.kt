package com.slaviboy.zxcvbn.core

import android.content.res.Resources
import androidx.annotation.StringRes
import com.slaviboy.zxcvbn.R
import com.slaviboy.zxcvbn.guesses.DictionaryGuess
import com.slaviboy.zxcvbn.matches.DateMatch
import com.slaviboy.zxcvbn.matches.DictionaryMatch
import com.slaviboy.zxcvbn.matches.Match
import com.slaviboy.zxcvbn.matches.RegexMatch
import com.slaviboy.zxcvbn.matches.RepeatMatch
import com.slaviboy.zxcvbn.matches.SequenceMatch
import com.slaviboy.zxcvbn.matches.SpatialMatch

open class Feedback(
    private val warning: Int? = null,
    @StringRes open val suggestions: List<Int> = listOf()
) {
    fun getWarningText(resources: Resources): String {
        return warning?.let {
            resources.getString(it)
        } ?: ""
    }

    fun getSuggestionsText(resources: Resources): List<String> {
        val suggestionTexts: MutableList<String> = ArrayList(suggestions.size)
        for (suggestion in suggestions) {
            suggestionTexts.add(
                resources.getString(suggestion)
            )
        }
        return suggestionTexts
    }

    companion object {

        fun getFeedback(
            score: Int,
            sequence: List<Match>
        ): Feedback {
            if (sequence.isEmpty()) {
                return Feedback(
                    null,
                    listOf(
                        R.string.feedback_default_suggestions_useFewWords,
                        R.string.feedback_default_suggestions_noNeedSymbols
                    )
                )
            }
            if (score > 2) {
                return Feedback()
            }
            var longestMatch: Match = sequence[0]
            if (sequence.size > 1) {
                for (match in sequence.subList(1, sequence.size)) {
                    if (match.tokenLength() > longestMatch.tokenLength()) longestMatch = match
                }
            }
            return getMatchFeedback(longestMatch, sequence.size == 1)
        }

        private fun getMatchFeedback(
            match: Match,
            isSoleMatch: Boolean
        ): Feedback {
            return when (match) {
                is DictionaryMatch -> getDictionaryMatchFeedback(
                    match,
                    isSoleMatch
                )

                is SpatialMatch -> Feedback(
                    if (match.turns == 1) {
                        R.string.feedback_spatial_warning_straightRowsOfKeys
                    } else {
                        R.string.feedback_spatial_warning_shortKeyboardPatterns
                    },
                    listOf(
                        R.string.feedback_extra_suggestions_addAnotherWord,
                        R.string.feedback_spatial_suggestions_UseLongerKeyboardPattern
                    )
                )

                is RepeatMatch -> Feedback(
                    if (match.baseToken.length == 1) {
                        R.string.feedback_repeat_warning_likeAAA
                    } else {
                        R.string.feedback_repeat_warning_likeABCABCABC
                    },
                    listOf(
                        R.string.feedback_extra_suggestions_addAnotherWord,
                        R.string.feedback_repeat_suggestions_avoidRepeatedWords
                    )
                )

                is SequenceMatch -> Feedback(
                    R.string.feedback_sequence_warning_likeABCor6543,
                    listOf(
                        R.string.feedback_extra_suggestions_addAnotherWord,
                        R.string.feedback_sequence_suggestions_avoidSequences
                    )
                )

                is RegexMatch -> Feedback(
                    if (match.regexName == "recent_year") {
                        R.string.feedback_regex_warning_recentYears
                    } else {
                        null
                    },
                    listOf(
                        R.string.feedback_extra_suggestions_addAnotherWord,
                        R.string.feedback_regex_suggestions_avoidRecentYears
                    )
                )

                is DateMatch -> Feedback(
                    R.string.feedback_date_warning_dates,
                    listOf(
                        R.string.feedback_extra_suggestions_addAnotherWord,
                        R.string.feedback_date_suggestions_avoidDates
                    )
                )

                else -> Feedback(
                    null,
                    listOf(
                        R.string.feedback_extra_suggestions_addAnotherWord
                    )
                )
            }
        }

        private fun getDictionaryMatchFeedback(
            match: DictionaryMatch,
            isSoleMatch: Boolean
        ): Feedback {
            var warning: Int? = null
            if (match.dictionaryName == "passwords") {
                if (isSoleMatch && !match.l33t && !match.reversed) {
                    warning = if (match.rank <= 10) {
                        R.string.feedback_dictionary_warning_passwords_top10
                    } else if (match.rank <= 100) {
                        R.string.feedback_dictionary_warning_passwords_top100
                    } else {
                        R.string.feedback_dictionary_warning_passwords_veryCommon
                    }
                } else if (match.guessesLog10!! <= 4) {
                    warning = R.string.feedback_dictionary_warning_passwords_similar
                }
            } else if (match.dictionaryName == "english_wikipedia") {
                if (isSoleMatch) {
                    warning = R.string.feedback_dictionary_warning_englishWikipedia_itself
                }
            } else if (
                listOf("surnames", "male_names", "female_names").contains(match.dictionaryName)
            ) {
                warning = if (isSoleMatch) {
                    R.string.feedback_dictionary_warning_etc_namesThemselves
                } else {
                    R.string.feedback_dictionary_warning_etc_namesCommon
                }
            }
            val word: CharSequence = match.token
            val lower = WipeableString.lowerCase(word)
            val suggestions = mutableListOf(
                R.string.feedback_extra_suggestions_addAnotherWord
            )
            if (DictionaryGuess.START_UPPER.matcher(word).find()) {
                suggestions.add(R.string.feedback_dictionary_suggestions_capitalization)
            } else if (DictionaryGuess.ALL_UPPER.matcher(word).find() && lower != word) {
                suggestions.add(R.string.feedback_dictionary_suggestions_allUppercase)
            }
            if (match.reversed && match.tokenLength() >= 4) {
                suggestions.add(R.string.feedback_dictionary_suggestions_reversed)
            }
            if (match.l33t) {
                suggestions.add(R.string.feedback_dictionary_suggestions_l33t)
            }
            lower.wipe()
            return Feedback(warning, suggestions)
        }
    }
}