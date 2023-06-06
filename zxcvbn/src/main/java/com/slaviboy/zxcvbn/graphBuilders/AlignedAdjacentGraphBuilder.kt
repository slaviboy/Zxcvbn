package com.slaviboy.zxcvbn.graphBuilders

class AlignedAdjacentGraphBuilder(
    layout: String
) : AdjacentGraphBuilder(layout) {

    override val isSlanted: Boolean
        get() = false

    override fun calcSlant(y: Int): Int {
        return 0
    }

    /**
     * Returns the nine clockwise adjacent coordinates on a keypad, where each row is vert aligned.
     */
    override fun getAdjacentCoords(position: Position): List<Position> {
        return listOf(
            Position(position.x - 1, position.y),
            Position(position.x - 1, position.y - 1),
            Position(position.x, position.y - 1),
            Position(position.x + 1, position.y - 1),
            Position(position.x + 1, position.y),
            Position(position.x + 1, position.y + 1),
            Position(position.x, position.y + 1),
            Position(position.x - 1, position.y + 1)
        )
    }
}