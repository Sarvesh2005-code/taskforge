package com.taskforge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * THIS IS THE ENTRY POINT OF YOUR ENTIRE APPLICATION.
 *
 * Think of this class as the "main gate" of your app.
 * When you run this, Spring Boot:
 *   1. Starts an embedded Tomcat web server (default port: 8080)
 *   2. Scans ALL classes in com.taskforge.* packages for annotations
 *   3. Creates and manages "Beans" (objects) automatically
 *   4. Sets up the database connection
 *   5. Your app is ready to receive HTTP requests!
 *
 * @SpringBootApplication is actually THREE annotations combined:
 *   - @Configuration    → "This class has configuration settings"
 *   - @EnableAutoConfiguration → "Spring, please configure things automatically"
 *   - @ComponentScan    → "Scan com.taskforge and sub-packages for components"
 */
@SpringBootApplication
public class TaskforgeApplication {

    public static void main(String[] args) {
        // This single line boots up your ENTIRE application:
        // - Starts the web server
        // - Connects to the database
        // - Registers all your controllers, services, repositories
        SpringApplication.run(TaskforgeApplication.class, args);
    }
}
