package com.slaviboy.zxcvbn.graphBuilders

abstract class AdjacentGraphBuilder(
    private val layout: String
) {
    /**
     * builds an adjacency graph as a dictionary: {character: [adjacent_characters]}.
     * adjacent characters occur in a clockwise order.
     * for example:
     * on qwerty layout, 'g' maps to ['fF', 'tT', 'yY', 'hH', 'bB', 'vV']
     * on keypad layout, '7' maps to [None, None, None, '=', '8', '5', '4', None]         *
     */
    fun build(): Map<Char, List<String?>> {
        val positionTable = buildPositionTable(layout)
        val adjacencyGraph: MutableMap<Char, List<String?>> = HashMap()
        for ((position, value) in positionTable) {
            for (key in value.toCharArray()) {
                val adjacencies: MutableList<String?> = ArrayList()
                for (coord in getAdjacentCoords(position)) {
                    adjacencies.add(positionTable[coord])
                }
                adjacencyGraph[key] = adjacencies
            }
        }
        return adjacencyGraph
    }

    private fun buildPositionTable(layout: String): Map<Position, String> {
        val positionTable: MutableMap<Position, String> = HashMap()
        val tokens = split(layout, WHITESPACE_SPLIT_MATCHER)
        val tokenSize = tokens[0].length
        val xUnit = tokenSize + 1
        for (token in tokens) {
            assert(token.length == tokenSize) { String.format("token [%s] length mismatch:\n%s", token, layout) }
        }
        var y = 1
        for (line in split(layout, NEW_LINE_SPLIT_MATCHER)) {
            // the way I illustrated keys above, each qwerty row is indented one space in from the last
            val slant = calcSlant(y)
            for (token in split(line, WHITESPACE_SPLIT_MATCHER)) {
                val index = line.indexOf(token) - slant
                val x = index / xUnit
                val remainder = index % xUnit
                assert(remainder == 0) { String.format("unexpected x offset [%d] for %s in:\n%s", x, token, layout) }
                positionTable[Position(x, y)] = token
            }
            y++
        }
        return positionTable
    }

    abstract fun getAdjacentCoords(position: Position): List<Position>
    abstract fun calcSlant(y: Int): Int
    abstract val isSlanted: Boolean

    private interface SplitMatcher {
        fun match(c: Char): Boolean
    }

    class Position(val x: Int, val y: Int) {

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }

        override fun equals(o: Any?): Boolean {
            if (this === o) return true
            if (o !is Position) return false
            val position = o
            return x == position.x && y == position.y
        }

        override fun toString(): String {
            return "[$x,$y]"
        }
    }

    companion object {
        private val WHITESPACE_SPLIT_MATCHER: SplitMatcher = object : SplitMatcher {
            override fun match(c: Char): Boolean {
                return Character.isWhitespace(c)
            }
        }
        private val NEW_LINE_SPLIT_MATCHER: SplitMatcher = object : SplitMatcher {
            override fun match(c: Char): Boolean {
                return c == '\n'
            }
        }

        private fun split(str: String, splitMatcher: SplitMatcher): List<String> {
            val len = str.length
            val list: MutableList<String> = ArrayList()
            var i = 0
            var start = 0
            var match = false
            while (i < len) {
                if (splitMatcher.match(str[i])) {
                    if (match) {
                        list.add(str.substring(start, i))
                        match = false
                    }
                    start = ++i
                    continue
                }
                match = true
                i++
            }
            if (match) {
                list.add(str.substring(start, i))
            }
            return list
        }
    }
}