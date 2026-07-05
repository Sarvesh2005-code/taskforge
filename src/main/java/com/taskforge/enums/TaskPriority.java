package com.taskforge.enums;

/**
 * Priority levels for tasks.
 *
 * Why an enum and not an integer (1, 2, 3)?
 * Because "HIGH" is self-documenting — anyone reading the code knows what it means.
 * With integers, you'd need to remember: "wait, is 1 high or low?"
 */
public enum TaskPriority {
    LOW,
    MEDIUM,
    HIGH
}
