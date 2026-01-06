package io.github.upendramanike.dbguard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlBuilderTest {

    @Test
    void testSelectQuery() {
        SqlBuilder builder = SqlBuilder.select("id", "name")
                .from("users")
                .where("age > ?", 18)
                .orderBy("name")
                .limit(10);

        String sql = builder.build();
        assertTrue(sql.contains("SELECT"));
        assertTrue(sql.contains("FROM users"));
        assertTrue(sql.contains("WHERE"));
        assertTrue(sql.contains("ORDER BY"));
        assertTrue(sql.contains("LIMIT 10"));
        assertEquals(1, builder.getParameters().length);
        assertEquals(18, builder.getParameters()[0]);
    }
}

