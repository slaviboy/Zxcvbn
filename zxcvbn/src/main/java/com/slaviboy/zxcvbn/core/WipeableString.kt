package com.slaviboy.zxcvbn.core

import java.nio.CharBuffer
import java.util.Arrays

/**
 * A character sequence with many attributes of Strings, but that can have its content wiped.
 */
class WipeableString() : CharSequence {

    private var content: CharArray? = null
    private var hash = 0

    /**
     * Returns true if the wipeable string has been wiped.
     */
    var isWiped = false
        private set

    override val length: Int
        get() = if (content == null) 0 else content!!.size

    override fun get(index: Int): Char {
        return content!![index]
    }

    /**
     * Creates a new wipeable string, copying the content from the specified source.
     */
    constructor(source: CharSequence) : this() {
        content = CharArray(source.length)
        for (n in content!!.indices) {
            content!![n] = source[n]
        }
    }

    /**
     * Creates a new wipeable string, copying the content from the specified source.
     */
    constructor(source: CharArray) : this() {
        content = Arrays.copyOf(source, source.size)
    }

    override fun subSequence(startIndex: Int, endIndex: Int): WipeableString {
        return WipeableString(Arrays.copyOfRange(content, startIndex, endIndex))
    }

    /**
     * Wipe the content of the wipeable string.
     *
     * Overwrites the content buffer with spaces, then replaces the buffer with an empty one.
     */
    fun wipe() {
        Arrays.fill(content, ' ')
        hash = 0
        content = CharArray(0)
        isWiped = true
    }

    /**
     * Returns the position of the first match of the specified character (indexed from 0).
     */
    fun indexOf(character: Char): Int {
        for (n in content!!.indices) {
            if (content!![n] == character) {
                return n
            }
        }
        return -1
    }

    /**
     * Returns the nth Unicode code point.
     */
    fun codePointAt(index: Int): Int {
        // Copy the implementation from String
        if (index < 0 || (index >= content!!.size)) {
            throw StringIndexOutOfBoundsException(index)
        }
        return Character.codePointAt(content, index, content!!.size)
    }

    /**
     * Returns a copy of the content as a char array.
     */
    fun charArray(): CharArray {
        return Arrays.copyOf(content, content!!.size)
    }

    override fun toString(): String {
        return String(content!!)
    }

    override fun hashCode(): Int {
        // Reproduce the same hash as String
        var h = hash
        if (h == 0 && content!!.size > 0) {
            val `val` = content
            for (i in content!!.indices) {
                h = 31 * h + `val`!![i].code
            }
            hash = h
        }
        return h
    }

    override fun equals(obj: Any?): Boolean {
        // Use an algorithm that matches any CharSequence (including Strings) with identical content.
        if (obj == null) {
            return false
        }
        if (obj === this) {
            return true
        }
        if (obj is CharSequence) {
            val other = obj
            if (other.length != length) {
                return false
            }
            for (n in 0 until length) {
                if (get(n) != other[n]) {
                    return false
                }
            }
            return true
        }
        return false
    }

    companion object {

        /**
         * Returns a new wipeable string with the specified content forced into lower case.
         */
        fun lowerCase(source: CharSequence?): WipeableString {
            assert(source != null)
            val chars = CharArray(source!!.length)
            for (n in source.indices) {
                chars[n] = source[n].lowercaseChar()
            }
            return WipeableString(chars)
        }

        /**
         * Returns a new wipeable string with the specified content but with the order of the characters reversed.
         */
        fun reversed(source: CharSequence?): WipeableString {
            assert(source != null)
            val length = source!!.length
            val chars = CharArray(length)
            for (n in source.indices) {
                chars[n] = source[length - n - 1]
            }
            return WipeableString(chars)
        }

        /**
         * Returns a copy of a portion of a character sequence as a wipeable string.
         */
        fun copy(source: CharSequence, start: Int, end: Int): WipeableString {
            return WipeableString(source.subSequence(start, end))
        }

        /**
         * Trims whitespace from a CharSequence.
         *
         * If there is no trailing whitespace then the original value is returned.
         * If there is trailing whitespace then the content (without that trailing
         * whitespace) is copied into a new WipeableString.
         */
        fun trimTrailingWhitespace(s: CharSequence): CharSequence {
            if (!Character.isWhitespace(s[s.length - 1])) {
                return s
            }
            var length = s.length
            while (length > 0 && Character.isWhitespace(s[length - 1])) {
                length--
            }
            return copy(s, 0, length)
        }

        /**
         * A version of Integer.parse(String) that accepts CharSequence as parameter.
         */
        @JvmOverloads
        @Throws(NumberFormatException::class)
        fun parseInt(s: CharSequence?, radix: Int = 10): Int {
            var s = s ?: throw NumberFormatException("null")
            s = trimTrailingWhitespace(s)
            if (radix < Character.MIN_RADIX) {
                throw NumberFormatException(
                    "radix " + radix +
                            " less than Character.MIN_RADIX"
                )
            }
            if (radix > Character.MAX_RADIX) {
                throw NumberFormatException(
                    ("radix " + radix +
                            " greater than Character.MAX_RADIX")
                )
            }
            var result = 0
            var negative = false
            var i = 0
            val len = s.length
            var limit = -Int.MAX_VALUE
            val multmin: Int
            var digit: Int
            if (len > 0) {
                val firstChar = s[0]
                if (firstChar < '0') { // Possible leading "+" or "-"
                    if (firstChar == '-') {
                        negative = true
                        limit = Int.MIN_VALUE
                    } else if (firstChar != '+') throw NumberFormatException("For input string: \"$s\"")
                    if (len == 1) throw NumberFormatException("For input string: \"$s\"")
                    i++
                }
                multmin = limit / radix
                while (i < len) {
                    // Accumulating negatively avoids surprises near MAX_VALUE
                    digit = s[i++].digitToIntOrNull(radix) ?: -1
                    if (digit < 0) {
                        throw NumberFormatException("For input string: \"$s\"")
                    }
                    if (result < multmin) {
                        throw NumberFormatException("For input string: \"$s\"")
                    }
                    result *= radix
                    if (result < limit + digit) {
                        throw NumberFormatException("For input string: \"$s\"")
                    }
                    result -= digit
                }
            } else {
                throw NumberFormatException("For input string: \"$s\"")
            }
            return if (negative) result else -result
        }

        /**
         * Wipes the content of the specified character sequence if possible.
         *
         * The following types can be wiped...
         * WipeableString
         * StringBuilder
         * StringBuffer
         * CharBuffer (if not readOnly)
         */
        fun wipeIfPossible(text: CharSequence?) {
            if (text == null) return
            if (text is WipeableString) {
                text.wipe()
            } else if (text is StringBuilder) {
                for (n in text.indices) {
                    text.setCharAt(n, ' ')
                }
                text.setLength(0)
            } else if (text is StringBuffer) {
                for (n in text.indices) {
                    text.setCharAt(n, ' ')
                }
                text.setLength(0)
            } else if (text is CharBuffer) {
                if (!text.isReadOnly) {
                    for (n in text.indices) {
                        text.put(n, ' ')
                    }
                }
            }
        }
    }
}