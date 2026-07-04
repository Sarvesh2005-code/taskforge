package com.taskforge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * YOUR FIRST REST CONTROLLER!
 *
 * What is a Controller?
 * ---------------------
 * A Controller is a class that HANDLES incoming HTTP requests.
 * When someone visits http://localhost:8080/hello, THIS class decides
 * what to send back.
 *
 * Think of it like a receptionist at a hotel:
 *   - Guest (browser/Postman) walks in and says "I want /hello"
 *   - Receptionist (Controller) finds the right method
 *   - Receptionist sends back the response
 *
 * @RestController = @Controller + @ResponseBody
 *   - @Controller    → "I handle web requests"
 *   - @ResponseBody  → "Return data directly (JSON), not an HTML page"
 */
@RestController
public class HelloController {

    /**
     * ENDPOINT 1: Simple Hello World
     *
     * @GetMapping("/hello") means:
     *   - Listen for HTTP GET requests
     *   - At the URL path "/hello"
     *   - So: GET http://localhost:8080/hello
     *
     * The return value automatically becomes the HTTP response body.
     * Since we return a String, the response is plain text.
     */
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from TaskForge! 🚀 Your Spring Boot app is running!";
    }

    /**
     * ENDPOINT 2: Return JSON data
     *
     * When you return a Map (or any Java object), Spring automatically
     * converts it to JSON using a library called "Jackson".
     *
     * JSON = JavaScript Object Notation
     *   { "key": "value", "number": 42 }
     *
     * This is how ALL modern APIs communicate — the frontend sends JSON,
     * the backend reads it, processes it, and sends JSON back.
     */
    @GetMapping("/api/status")
    public Map<String, Object> getStatus() {
        return Map.of(
            "app", "TaskForge API",
            "status", "UP",
            "version", "1.0.0",
            "timestamp", LocalDateTime.now().toString()
        );
    }

    /**
     * ENDPOINT 3: Path Variables (reading values from the URL)
     *
     * @PathVariable extracts a value from the URL itself.
     *
     * Example:
     *   GET /api/greet/Sarvesh  →  name = "Sarvesh"
     *   GET /api/greet/John     →  name = "John"
     *
     * The {name} in the URL is a PLACEHOLDER — it matches any value.
     */
    @GetMapping("/api/greet/{name}")
    public Map<String, String> greetUser(@PathVariable String name) {
        return Map.of(
            "message", "Welcome to TaskForge, " + name + "!",
            "tip", "You just used a @PathVariable — the '" + name + "' came from the URL!"
        );
    }

    /**
     * ENDPOINT 4: Query Parameters (reading values after the ? in the URL)
     *
     * @RequestParam reads values from the query string.
     *
     * Example:
     *   GET /api/search?keyword=spring         → keyword = "spring", limit = 10
     *   GET /api/search?keyword=java&limit=5   → keyword = "java", limit = 5
     *
     * "defaultValue" means: if the user doesn't provide it, use this value.
     * "required = false" means: this parameter is optional.
     */
    @GetMapping("/api/search")
    public Map<String, Object> searchDemo(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "10", required = false) int limit
    ) {
        return Map.of(
            "searchedFor", keyword,
            "maxResults", limit,
            "tip", "You just used @RequestParam — values came from the ?query=string!"
        );
    }
}
