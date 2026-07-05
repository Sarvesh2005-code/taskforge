package com.taskforge.enums;

/**
 * WHAT IS AN ENUM?
 * ================
 * An Enum (Enumeration) is a special class that represents a FIXED SET of constants.
 *
 * Why not just use Strings like "TODO", "IN_PROGRESS", "DONE"?
 * Because with Strings, someone could accidentally type "To Do" or "todo" or "IN PROGRESS"
 * and your code would break. Enums prevent typos — the compiler catches mistakes.
 *
 * Think of it like a dropdown menu:
 *   - You can ONLY select from the given options
 *   - You can't type a random value
 *
 * In the database, this will be stored as a String: "TODO", "IN_PROGRESS", or "DONE"
 * (because of @Enumerated(EnumType.STRING) which we'll add in Module 3)
 */
public enum TaskStatus {
    TODO,           // Task hasn't been started yet
    IN_PROGRESS,    // Task is currently being worked on
    DONE            // Task is completed
}
