package com.taskforge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * LABEL ENTITY — Maps to the "labels" table.
 *
 * Labels are like tags: "frontend", "bug", "urgent", "backend".
 * A task can have MANY labels, and a label can be on MANY tasks.
 *
 * RELATIONSHIP: Many Tasks ↔ Many Labels (MANY-TO-MANY)
 * ──────────────────────────────────────────────────────
 *
 * In database terms, a ManyToMany needs a JOIN TABLE:
 *
 *   labels table:       id | name      | color
 *                       1  | bug       | #FF0000
 *                       2  | frontend  | #00FF00
 *
 *   tasks table:        id | title     | ...
 *                       1  | Fix login | ...
 *
 *   task_labels table:  task_id | label_id    ← JOIN TABLE (created by @JoinTable)
 *   (auto-created)      1      | 1            ← Task 1 has label "bug"
 *                       1      | 2            ← Task 1 also has label "frontend"
 *
 * This is the INVERSE side. The OWNING side is Task.labels (it has @JoinTable).
 * mappedBy = "labels" → "Task owns this relationship via its 'labels' field"
 *
 * Set vs List:
 *   We use Set<Task> instead of List<Task> for ManyToMany because:
 *   1. A task shouldn't have the same label twice (Set prevents duplicates)
 *   2. Hibernate handles Set more efficiently for ManyToMany
 */
@Entity
@Table(name = "labels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;        // e.g., "bug", "frontend", "urgent"

    @Column(nullable = false, length = 7)
    private String color;       // Hex color code, e.g., "#FF5733"

    /**
     * INVERSE side of the Task ↔ Label ManyToMany relationship.
     * mappedBy = "labels" → Task.java has the @JoinTable definition.
     */
    @ManyToMany(mappedBy = "labels")
    @JsonIgnore   // Safety net: prevent infinite loop
    private Set<Task> tasks = new HashSet<>();
}
