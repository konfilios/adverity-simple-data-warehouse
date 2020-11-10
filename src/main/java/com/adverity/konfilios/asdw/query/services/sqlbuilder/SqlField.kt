package com.adverity.konfilios.asdw.query.services.sqlbuilder

import java.sql.ResultSet

/**
 * Model of an SQL field that can be queried and retrieved
 * from a JDBC [ResultSet]
 */
class SqlField(
    /**
     * Name of the field in the result.
     */
    val name: String,
    /**
     * Expression of the field used in the query.
     *
     * For simple fields this will probably be
     * the same as the field name. For computed
     * fields it will differ.
     */
    val expression: String,
    /**
     * Maps a [ResultSet] to a properly typed value
     * corresponding to this field.
     *
     * Usually this will be a call to [ResultSet]'s
     * getString(), getInt(), etc. methods.
     */
    val mapper: (ResultSet)->Any
)