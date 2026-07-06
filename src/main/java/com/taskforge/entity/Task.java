package com.taskforge.entity;

import com.taskforge.enums.TaskPriority;
import com.taskforge.enums.TaskStatus;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * WHAT IS AN ENTITY?
 * ===================
 * An Entity is a Java class that maps to a DATABASE TABLE.
 * Each INSTANCE of this class = one ROW in the table.
 * Each FIELD = one COLUMN in the table.
 *
 *   Java World              Database World
 *   ──────────              ──────────────
 *   Task class      →      "tasks" table
 *   Task object     →      one row in the table
 *   String title    →      "title" column (VARCHAR)
 *   Long id         →      "id" column (BIGINT)
 *
 * This mapping is done by JPA (Java Persistence API) and the actual
 * work is performed by Hibernate (the JPA implementation).
 *
 * You write Java. Hibernate writes SQL for you.
 *
 * ───────────────────────────────────────────────────────
 * LOMBOK ANNOTATIONS (reduce boilerplate code):
 * ───────────────────────────────────────────────────────
 * @Getter           → generates getTitle(), getDescription(), etc.
 * @Setter           → generates setTitle(), setDescription(), etc.
 * @NoArgsConstructor → generates Task() { } (empty constructor — JPA requires this!)
 * @AllArgsConstructor → generates Task(id, title, desc, ...) (all fields constructor)
 * @Builder          → generates Task.builder().title("...").build() (builder pattern)
 *
 * WITHOUT Lombok, you'd have to write ~80 lines of getters, setters, constructors.
 * WITH Lombok, you write 5 annotations.
 */
@Entity                          // "This class maps to a database table"
@Table(name = "tasks")           // The table will be called "tasks" (optional: defaults to class name)
@Getter                          // Auto-generate all getter methods
@Setter                          // Auto-generate all setter methods
@NoArgsConstructor               // Auto-generate empty constructor (JPA REQUIRES this)
@AllArgsConstructor              // Auto-generate constructor with all fields
@Builder                         // Auto-generate builder pattern (Task.builder().title("x").build())
public class Task {

    /**
     * PRIMARY KEY — Every database table needs a unique identifier for each row.
     *
     * @Id → "This field is the primary key"
     * @GeneratedValue(strategy = GenerationType.IDENTITY)
     *   → "Let the DATABASE auto-generate this value (1, 2, 3, 4...)"
     *
     * GenerationType options:
     *   IDENTITY → Database auto-increments (most common, works with H2, MySQL, Postgres)
     *   SEQUENCE → Uses a database sequence (preferred for Postgres in production)
     *   AUTO     → Let Hibernate decide (not recommended — behavior varies)
     *   TABLE    → Uses a separate table to track IDs (rarely used)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column lets you customize the database column.
     *
     * nullable = false → this column CANNOT be null (NOT NULL constraint)
     * length = 200     → maximum 200 characters (VARCHAR(200))
     *
     * If you don't use @Column, Hibernate uses defaults:
     *   - Column name = field name
     *   - nullable = true
     *   - length = 255
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * columnDefinition = "TEXT" → use TEXT type instead of VARCHAR
     * TEXT can store much longer strings than VARCHAR(255)
     * Good for descriptions, notes, or any free-form content.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * @Enumerated tells Hibernate HOW to store an enum in the database.
     *
     * EnumType.STRING  → stores "TODO", "IN_PROGRESS", "DONE" (readable!)
     * EnumType.ORDINAL → stores 0, 1, 2 (BAD! If you reorder the enum, data breaks)
     *
     * ALWAYS use EnumType.STRING. This is an interview question!
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    /**
     * @CreationTimestamp → Hibernate automatically sets this to NOW
     *                      when the entity is FIRST saved to the database.
     *                      You never need to set this manually!
     *
     * updatable = false → this column can't be changed after insertion
     */
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * @UpdateTimestamp → Hibernate automatically updates this to NOW
     *                    every time the entity is modified and saved.
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
