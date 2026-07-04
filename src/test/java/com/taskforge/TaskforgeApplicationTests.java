package com.taskforge;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This is a basic "smoke test" — it just checks that the application
 * can START without crashing. If Spring can't find a configuration,
 * can't connect to the database, or has any setup error, this test fails.
 */
@SpringBootTest
class TaskforgeApplicationTests {

    @Test
    void contextLoads() {
        // If this test passes, it means:
        // ✅ Spring Boot started successfully
        // ✅ All beans were created
        // ✅ Database connection works
        // ✅ No configuration errors
    }
}
