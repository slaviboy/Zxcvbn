package com.slaviboy.zxcvbn.matchers

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.core.WipeableString
import com.slaviboy.zxcvbn.helpers.StrengthHelper
import com.slaviboy.zxcvbn.matches.DateMatch
import com.slaviboy.zxcvbn.matches.Match
import java.util.Collections
import java.util.regex.Pattern

class DateMatcher(
    context: Context
) : Matcher(context) {

    private val maybe_date_no_separator = Pattern.compile("^\\d{4,8}$")
    private val maybe_date_with_separator = Pattern.compile("^(\\d{1,4})([\\s/\\\\_.-])(\\d{1,2})\\2(\\d{1,4})$")

    override fun execute(password: CharSequence): List<Match> {
        val matches: MutableList<Match> = ArrayList()
        for (i in 0..password.length - 4) {
            for (j in i + 3..i + 7) {
                if (j >= password.length) break
                val token = WipeableString.copy(password, i, j + 1)
                if (!maybe_date_no_separator.matcher(token).find()) {
                    token.wipe()
                    continue
                }
                val candidates: MutableList<Dmy> = ArrayList()
                for (date in DATE_SPLITS[token.length]!!) {
                    val k = date[0]
                    val l = date[1]
                    val ints: MutableList<Int> = ArrayList()
                    try {
                        ints.add(WipeableString.parseInt(token.subSequence(0, k)))
                        ints.add(WipeableString.parseInt(token.subSequence(k, l)))
                        ints.add(WipeableString.parseInt(token.subSequence(l, token.length)))
                    } catch (e: NumberFormatException) {
                        continue
                    }
                    val dmy = mapIntsToDmy(ints)
                    if (dmy != null) {
                        candidates.add(dmy)
                    }
                }
                if (candidates.isEmpty()) {
                    token.wipe()
                    continue
                }
                var bestCandidate = candidates[0]
                var minDistance = metric(candidates[0])
                for (candidate in candidates.subList(1, candidates.size)) {
                    val distance = metric(candidate)
                    if (distance < minDistance) {
                        bestCandidate = candidate
                        minDistance = distance
                    }
                }
                matches.add(
                    DateMatch(
                        i,
                        j,
                        token,
                        "",
                        bestCandidate.year,
                        bestCandidate.month,
                        bestCandidate.day
                    )
                )
            }
        }
        for (i in 0..password.length - 6) {
            for (j in i + 5..i + 9) {
                if (j >= password.length) break
                val token = WipeableString.copy(password, i, j + 1)
                val rxMatch = maybe_date_with_separator.matcher(token)
                if (!rxMatch.find()) {
                    token.wipe()
                    continue
                }
                val ints: MutableList<Int> = ArrayList()
                try {
                    ints.add(WipeableString.parseInt(rxMatch.group(1)))
                    ints.add(WipeableString.parseInt(rxMatch.group(3)))
                    ints.add(WipeableString.parseInt(rxMatch.group(4)))
                } catch (e: NumberFormatException) {
                    token.wipe()
                    continue
                }
                val dmy = mapIntsToDmy(ints)
                if (dmy == null) {
                    token.wipe()
                    continue
                }
                matches.add(
                    DateMatch(
                        i,
                        j,
                        token,
                        rxMatch.group(2),
                        dmy.year,
                        dmy.month,
                        dmy.day
                    )
                )
            }
        }
        val targetMatches: MutableList<Match> = ArrayList()
        for (match in matches) {
            var isSubmatch = false
            for (otherMatch in matches) {
                if (match == otherMatch) continue
                if (otherMatch.i <= match.i && otherMatch.j >= match.j) {
                    isSubmatch = true
                    break
                }
            }
            if (!isSubmatch) targetMatches.add(match)
        }
        return this.sorted(targetMatches)
    }

    private fun metric(candidate: Dmy): Int {
        return Math.abs(candidate.year - StrengthHelper.REFERENCE_YEAR)
    }

    private fun mapIntsToDmy(ints: List<Int>): Dmy? {
        if (ints[1] > 31 || ints[1] <= 0) {
            return null
        }
        var over12 = 0
        var over31 = 0
        var under1 = 0
        for (i in ints) {
            if (99 < i && i < DATE_MIN_YEAR || i > DATE_MAX_YEAR) {
                return null
            }
            if (i > 31) over31 += 1
            if (i > 12) over12 += 1
            if (i <= 0) under1 += 1
        }
        if (over31 >= 2 || over12 == 3 || under1 >= 2) {
            return null
        }
        val possibleYearSplits: MutableMap<Int, List<Int>> = HashMap()
        possibleYearSplits[ints[2]] = ints.subList(0, 1 + 1)
        possibleYearSplits[ints[0]] = ints.subList(1, 2 + 1)
        for ((y, rest) in possibleYearSplits) {
            if (DATE_MIN_YEAR <= y && y <= DATE_MAX_YEAR) {
                val dm = mapIntsToDm(rest)
                return if (dm != null) {
                    Dmy(dm.day, dm.month, y)
                } else {
                    null
                }
            }
        }
        for (possibleYearSplitRef in possibleYearSplits.entries) {
            var y = possibleYearSplitRef.key
            val rest = possibleYearSplitRef.value
            val dm = mapIntsToDm(rest)
            if (dm != null) {
                y = twoToFourDigitYear(y)
                return Dmy(dm.day, dm.month, y)
            }
        }
        return null
    }

    private fun mapIntsToDm(ints: List<Int>): Dm? {
        val copy: List<Int> = ArrayList(ints)
        Collections.reverse(copy)
        val refs: MutableList<List<Int>> = ArrayList()
        refs.add(ints)
        refs.add(copy)
        for (ref in refs) {
            val d = ref[0]
            val m = ref[1]
            if (1 <= d && d <= 31 && 1 <= m && m <= 12) {
                return Dm(d, m)
            }
        }
        return null
    }

    private fun twoToFourDigitYear(year: Int): Int {
        return if (year > 99) {
            year
        } else if (year > 50) {
            // 87 -> 1987
            year + 1900
        } else {
            // 15 -> 2015
            year + 2000
        }
    }

    private open class Dm(val day: Int, val month: Int)
    private class Dmy(day: Int, month: Int, val year: Int) : Dm(day, month)

    companion object {
        private const val DATE_MAX_YEAR = 2050
        private const val DATE_MIN_YEAR = 1000
        private val DATE_SPLITS: MutableMap<Int, ArrayList<Array<Int>>> = HashMap()

        init {
            DATE_SPLITS[4] = object : ArrayList<Array<Int>>() {
                init {
                    add(arrayOf(1, 2))
                    add(arrayOf(2, 3))
                }
            }
            DATE_SPLITS[5] = object : ArrayList<Array<Int>>() {
                init {
                    add(arrayOf(1, 3))
                    add(arrayOf(2, 3))
                }
            }
            DATE_SPLITS[6] = object : ArrayList<Array<Int>>() {
                init {
                    add(arrayOf(1, 2))
                    add(arrayOf(2, 4))
                    add(arrayOf(4, 5))
                }
            }
            DATE_SPLITS[7] = object : ArrayList<Array<Int>>() {
                init {
                    add(arrayOf(1, 3))
                    add(arrayOf(2, 3))
                    add(arrayOf(4, 5))
                    add(arrayOf(4, 6))
                }
            }
            DATE_SPLITS[8] = object : ArrayList<Array<Int>>() {
                init {
                    add(arrayOf(2, 4))
                    add(arrayOf(4, 6))
                }
            }
        }
    }
}