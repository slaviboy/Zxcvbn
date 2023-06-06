package com.slaviboy.zxcvbn.graphBuilders

class SlantedAdjacentGraphBuilder(
    layout: String
) : AdjacentGraphBuilder(layout) {

    override val isSlanted: Boolean
        get() = true

    override fun calcSlant(y: Int): Int {
        return y - 1
    }

    /**
     * Returns the six adjacent coordinates on a standard keyboard, where each row is slanted to the
     * right from the last. adjacencies are clockwise, starting with key to the left, then two keys
     * above, then right key, then two keys below. (that is, only near-diagonal keys are adjacent,
     * so g's coordinate is adjacent to those of t,y,b,v, but not those of r,u,n,c.)
     */
    override fun getAdjacentCoords(position: Position): List<Position> {
        return listOf(
            Position(position.x - 1, position.y),
            Position(position.x, position.y - 1),
            Position(position.x + 1, position.y - 1),
            Position(position.x + 1, position.y),
            Position(position.x, position.y + 1),
            Position(position.x - 1, position.y + 1)
        )
    }
}