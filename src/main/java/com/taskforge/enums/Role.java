package com.taskforge.enums;

/**
 * User roles for authorization (used in Module 6 with Spring Security).
 *
 * For now, we just define them. In Module 6, we'll use these to control
 * who can do what:
 *   - USER  → can manage their own tasks
 *   - ADMIN → can manage all tasks and users
 */
public enum Role {
    USER,
    ADMIN
}
