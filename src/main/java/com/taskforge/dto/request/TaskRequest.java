package com.taskforge.dto.request;

import com.taskforge.enums.TaskPriority;
import com.taskforge.enums.TaskStatus;

/**
 * WHAT IS A DTO (Data Transfer Object)?
 * ======================================
 * A DTO is a simple class that carries data between the client and the server.
 *
 * Why not use the Entity (database model) directly?
 * -------------------------------------------------
 * 1. SECURITY: Your Entity might have fields you don't want the client to set
 *    (like "id" or "createdAt" — those are auto-generated)
 * 2. FLEXIBILITY: What the client sends might be different from what you store
 * 3. VALIDATION: You validate the DTO, not the Entity
 *
 * Think of it like a restaurant order form:
 *   - The customer (client) fills out an ORDER FORM (DTO) — "I want a burger, no pickles"
 *   - The kitchen (server) reads the form and creates the actual DISH (Entity)
 *   - The order form is NOT the dish — it's just a way to communicate what's wanted
 *
 * TaskRequest = what the CLIENT SENDS to us when creating/updating a task
 *
 * NOTE: We're using a Java RECORD here.
 * A "record" is a special class introduced in Java 16 that:
 *   - Automatically generates constructor, getters, equals(), hashCode(), toString()
 *   - Is immutable (fields can't be changed after creation)
 *   - Saves you from writing boilerplate code
 *
 * This one line:
 *     public record TaskRequest(String title, ...)
 * Is equivalent to writing a full class with constructor, getters, toString, etc.
 */
public record TaskRequest(
        String title,           // e.g., "Build the login page"
        String description,     // e.g., "Use JWT for authentication"
        TaskStatus status,      // e.g., TODO, IN_PROGRESS, DONE
        TaskPriority priority   // e.g., LOW, MEDIUM, HIGH
) {
}
