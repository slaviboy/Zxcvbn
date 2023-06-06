package com.slaviboy.zxcvbn.core

class L33tSubDict(
    table: Map<Char, List<Char>>
) : Iterable<Map<Char, Char>> {

    private val subDicts: List<Map<Char, Char>> = buildSubDicts(table)

    private fun buildSubDicts(table: Map<Char, List<Char>>): List<Map<Char, Char>> {
        val initialSubs: MutableSet<List<String>> = LinkedHashSet()
        initialSubs.add(ArrayList())
        val subs = helper(table, table.keys.iterator(), initialSubs)
        val subDicts: MutableList<Map<Char, Char>> = ArrayList()
        for (sub in subs) {
            val subDict: MutableMap<Char, Char> = HashMap()
            for (ref in sub) {
                subDict[ref[0]] = ref[1]
            }
            subDicts.add(subDict)
        }
        return subDicts
    }

    private fun helper(
        table: Map<Char, List<Char>>,
        keysIterator: Iterator<Char>,
        subs: Set<List<String>>
    ): Set<List<String>> {
        if (!keysIterator.hasNext()) {
            return subs
        }
        val key = keysIterator.next()
        val nextSubs: MutableSet<List<String>> = LinkedHashSet()
        for (l33tChr in table[key]!!) {
            for (sub in subs) {
                var found = false
                for (i in sub.indices) {
                    if (sub[i][0] == l33tChr) {
                        val subAlternative: MutableList<String> = ArrayList(sub)
                        subAlternative.removeAt(i)
                        subAlternative.add(java.lang.String.valueOf(charArrayOf(l33tChr, key)))
                        nextSubs.add(sub)
                        nextSubs.add(subAlternative)
                        found = true
                        break
                    }
                }
                if (!found) {
                    val subExtension: MutableList<String> = ArrayList(sub)
                    subExtension.add(java.lang.String.valueOf(charArrayOf(l33tChr, key)))
                    nextSubs.add(subExtension)
                }
            }
        }
        return helper(table, keysIterator, nextSubs)
    }

    override fun iterator(): Iterator<Map<Char, Char>> {
        return subDicts.iterator()
    }
}