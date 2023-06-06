package com.slaviboy.zxcvbn.helpers

import com.slaviboy.zxcvbn.core.Context
import com.slaviboy.zxcvbn.core.Strength
import com.slaviboy.zxcvbn.guesses.EstimateGuess
import com.slaviboy.zxcvbn.matches.BruteforceMatch
import com.slaviboy.zxcvbn.matches.Match
import java.util.Calendar

object StrengthHelper {

    val REFERENCE_YEAR = Calendar.getInstance()[Calendar.YEAR]

    private const val MIN_GUESSES_BEFORE_GROWING_SEQUENCE = 10000
    private const val JS_NUMBER_MAX = 9007199254740991L

    fun log10(n: Double): Double {
        return Math.log(n) / Math.log(10.0)
    }

    fun mostGuessableMatchSequence(
        context: Context,
        password: CharSequence,
        matches: List<Match>,
        excludeAdditive: Boolean = false
    ): Strength {
        val n = password.length
        val matchesByJ: MutableList<MutableList<Match>> = ArrayList()
        for (i in 0 until n) {
            matchesByJ.add(ArrayList())
        }
        for (m in matches) {
            matchesByJ[m.j].add(m)
        }
        for (lst in matchesByJ) {
            lst.sortWith { m1, m2 -> m1.i - m2.i }
        }
        val optimal = Optimal(n)
        for (k in 0 until n) {
            for (m in matchesByJ[k]) {
                if (m.i > 0) {
                    for ((l) in optimal.m[m.i - 1]) {
                        update(context, password, m, l + 1, optimal, excludeAdditive)
                    }
                } else {
                    update(context, password, m, 1, optimal, excludeAdditive)
                }
            }
            bruteforceUpdate(context, password, k, optimal, excludeAdditive)
        }
        val optimalMatchSequence: List<Match> = unwind(n, optimal)
        val optimalL = optimalMatchSequence.size
        val guesses: Double = if (password.isEmpty()) 1.0 else optimal.g[n - 1][optimalL]!!
        return Strength(
            password = password,
            guesses = guesses,
            guessesLog10 = log10(guesses),
            sequence = optimalMatchSequence
        )
    }

    private fun unwind(n: Int, optimal: Optimal): List<Match> {
        val optimalMatchSequence: MutableList<Match> = mutableListOf()
        var k = n - 1
        if (0 <= k) {
            var l = 0
            var g = Double.POSITIVE_INFINITY
            for ((key, value) in optimal.g[k]) {
                if (value < g) {
                    l = key
                    g = value
                }
            }
            while (k >= 0) {
                val m: Match = optimal.m[k][l]!!
                optimalMatchSequence.add(0, m)
                k = m.i - 1
                l--
            }
        }
        return optimalMatchSequence
    }

    private fun makeBruteforceMatch(password: CharSequence, i: Int, j: Int): Match {
        return BruteforceMatch(
            i = i,
            j = j,
            token = password.subSequence(i, j + 1)
        )
    }

    private fun factorial(n: Int): Long {
        if (n < 2) return 1
        if (n > 19) return JS_NUMBER_MAX
        var f: Long = 1
        for (i in 2..n) f *= i.toLong()
        return f
    }

    private class Optimal(n: Int) {
        val m: MutableList<MutableMap<Int, Match>> = ArrayList()
        val pi: MutableList<MutableMap<Int, Double>> = ArrayList()
        val g: MutableList<MutableMap<Int, Double>> = ArrayList()

        init {
            for (i in 0 until n) {
                m.add(HashMap())
                pi.add(HashMap())
                g.add(HashMap())
            }
        }
    }

    private fun update(context: Context, password: CharSequence, m: Match, l: Int, optimal: Optimal, excludeAdditive: Boolean) {
        val k: Int = m.j
        var pi: Double = EstimateGuess(context, password).exec(m)
        if (l > 1) {
            pi *= optimal.pi[m.i - 1][l - 1]!!
        }
        if (java.lang.Double.isInfinite(pi)) {
            pi = Double.MAX_VALUE
        }
        var g = factorial(l) * pi
        if (java.lang.Double.isInfinite(g)) {
            g = Double.MAX_VALUE
        }
        if (!excludeAdditive) {
            g += Math.pow(MIN_GUESSES_BEFORE_GROWING_SEQUENCE.toDouble(), (l - 1).toDouble())
            if (java.lang.Double.isInfinite(g)) {
                g = Double.MAX_VALUE
            }
        }
        for ((key, value) in optimal.g[k]) {
            if (key > l) {
                continue
            }
            if (value <= g) {
                return
            }
        }
        optimal.g[k][l] = g
        optimal.m[k][l] = m
        optimal.pi[k][l] = pi
    }

    private fun bruteforceUpdate(context: Context, password: CharSequence, k: Int, optimal: Optimal, excludeAdditive: Boolean) {
        var m: Match = makeBruteforceMatch(password, 0, k)
        update(context, password, m, 1, optimal, excludeAdditive)
        for (i in 1..k) {
            m = makeBruteforceMatch(password, i, k)
            for ((l, last_m) in optimal.m[i - 1]) {
                if (last_m !is BruteforceMatch) {
                    update(context, password, m, l + 1, optimal, excludeAdditive)
                }
            }
        }
    }
}