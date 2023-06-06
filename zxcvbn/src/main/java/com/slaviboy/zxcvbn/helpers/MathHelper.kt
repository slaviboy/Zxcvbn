package com.slaviboy.zxcvbn.helpers

object MathHelper {

    fun min(a: Int, b: Int): Double {
        return Math.min(a, b).toDouble()
    }

    fun max(a: Int, b: Int): Double {
        return Math.max(a, b).toDouble()
    }

    fun max(a: Double, b: Int): Double {
        return Math.max(a, b.toDouble())
    }

    fun max(a: Int, b: Double): Double {
        return Math.max(a.toDouble(), b)
    }

    fun max(a: Double, b: Double): Double {
        return Math.max(a, b)
    }

    fun pow(a: Int, b: Int): Double {
        return Math.pow(a.toDouble(), b.toDouble())
    }

    fun abs(a: Int): Double {
        return Math.abs(a).toDouble()
    }
}