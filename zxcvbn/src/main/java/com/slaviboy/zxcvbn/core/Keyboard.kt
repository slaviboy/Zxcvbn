package com.slaviboy.zxcvbn.core

import com.slaviboy.zxcvbn.graphBuilders.AdjacentGraphBuilder

class Keyboard internal constructor(
    val name: String,
    adjacentGraphBuilder: AdjacentGraphBuilder
) {
    val adjacencyGraph: Map<Char, List<String?>>
    val isSlanted: Boolean
    val startingPositions: Int
    val averageDegree: Double

    init {
        adjacencyGraph = adjacentGraphBuilder.build()
        isSlanted = adjacentGraphBuilder.isSlanted
        startingPositions = adjacencyGraph.size
        averageDegree = calcAverageDegree(adjacencyGraph)
    }

    companion object {
        private fun calcAverageDegree(adjacencyGraph: Map<Char, List<String?>>): Double {
            var average = 0.0
            for ((_, neighbors) in adjacencyGraph) {
                val results: MutableList<String> = ArrayList()
                for (neighbor in neighbors) {
                    if (neighbor != null) {
                        results.add(neighbor)
                    }
                }
                average += results.size.toDouble()
            }
            val keys: MutableList<Char> = ArrayList()
            for ((key) in adjacencyGraph) {
                keys.add(key)
            }
            average /= keys.size.toDouble()
            return average
        }
    }
}