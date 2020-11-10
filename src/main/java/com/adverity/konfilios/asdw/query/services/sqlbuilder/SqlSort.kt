package com.adverity.konfilios.asdw.query.services.sqlbuilder

class SqlSort(
    val column : String,
    val direction : String
) {
    companion object {
        /**
         * Parse a string into an SqlSort
         */
        fun parseSort(s: String): SqlSort? {
            val items = s.split(" ")
            return when (items.size) {
                0 -> null
                1 -> SqlSort(
                    column = items[0],
                    direction = "asc"
                )
                else -> SqlSort(
                    column = items.subList(0, items.size - 1).joinToString(" "),
                    direction = items[items.size - 1]
                )
            }
        }
    }
}