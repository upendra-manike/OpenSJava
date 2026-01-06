package io.github.upendramanike.dbguard;

import java.util.ArrayList;
import java.util.List;

/**
 * Safe SQL query builder that helps prevent SQL injection.
 */
public final class SqlBuilder {

    private final StringBuilder sql = new StringBuilder();
    private final List<Object> parameters = new ArrayList<>();

    private SqlBuilder(String base) {
        sql.append(base);
    }

    /**
     * Creates a new SQL builder starting with a SELECT statement.
     *
     * @param columns the columns to select
     * @return a new builder
     */
    public static SqlBuilder select(String... columns) {
        return new SqlBuilder("SELECT " + String.join(", ", columns));
    }

    /**
     * Adds a FROM clause.
     *
     * @param table the table name
     * @return this builder
     */
    public SqlBuilder from(String table) {
        sql.append(" FROM ").append(table);
        return this;
    }

    /**
     * Adds a WHERE clause with a parameterized condition.
     *
     * @param condition the condition (use ? for parameters)
     * @param values the parameter values
     * @return this builder
     */
    public SqlBuilder where(String condition, Object... values) {
        sql.append(" WHERE ").append(condition);
        for (Object value : values) {
            parameters.add(value);
        }
        return this;
    }

    /**
     * Adds an AND condition.
     *
     * @param condition the condition (use ? for parameters)
     * @param values the parameter values
     * @return this builder
     */
    public SqlBuilder and(String condition, Object... values) {
        sql.append(" AND ").append(condition);
        for (Object value : values) {
            parameters.add(value);
        }
        return this;
    }

    /**
     * Adds an ORDER BY clause.
     *
     * @param columns the columns to order by
     * @return this builder
     */
    public SqlBuilder orderBy(String... columns) {
        sql.append(" ORDER BY ").append(String.join(", ", columns));
        return this;
    }

    /**
     * Adds a LIMIT clause.
     *
     * @param limit the limit value
     * @return this builder
     */
    public SqlBuilder limit(int limit) {
        sql.append(" LIMIT ").append(limit);
        return this;
    }

    /**
     * Builds the SQL query string.
     *
     * @return the SQL query
     */
    public String build() {
        return sql.toString();
    }

    /**
     * Gets the parameter values.
     *
     * @return the parameters array
     */
    public Object[] getParameters() {
        return parameters.toArray();
    }
}


