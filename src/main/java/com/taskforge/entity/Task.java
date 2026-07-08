package com.taskforge.entity;

import com.taskforge.enums.TaskPriority;
import com.taskforge.enums.TaskStatus;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    // =================================================================
    // RELATIONSHIP 1: Many Tasks → One User (MANY-TO-ONE)
    // =================================================================
    /**
     * @ManyToOne — "Many tasks can belong to ONE user"
     *
     * This is the OWNING SIDE of the User ↔ Task relationship.
     * "Owning side" means THIS table has the foreign key column.
     *
     * Database result:
     *   tasks table: ... | assignee_id (FK → users.id)
     *
     * FetchType.LAZY vs EAGER:
     *   LAZY  → Load the user ONLY when you call task.getAssignee()
     *           (saves memory — don't load data you might not need)
     *   EAGER → Load the user IMMEDIATELY when you load the task
     *           (convenient but can cause performance issues)
     *
     *   ALWAYS use LAZY for @ManyToOne. This is an interview question!
     *   Default: @ManyToOne is EAGER by default, so we explicitly set LAZY.
     *
     * @JoinColumn(name = "assignee_id")
     *   → The foreign key column in the tasks table will be called "assignee_id"
     *   → It references users.id
     *
     * nullable: an assignee is optional — tasks can be unassigned.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    // =================================================================
    // RELATIONSHIP 2: Many Tasks ↔ Many Labels (MANY-TO-MANY)
    // =================================================================
    /**
     * @ManyToMany — "A task can have many labels, a label can be on many tasks"
     *
     * This is the OWNING SIDE (it has @JoinTable).
     *
     * ManyToMany requires a JOIN TABLE — a separate table that links the two:
     *
     *   tasks table:       id | title
     *                      1  | Fix login
     *                      2  | Add tests
     *
     *   labels table:      id | name
     *                      1  | bug
     *                      2  | frontend
     *
     *   task_labels table:  task_id | label_id   ← JOIN TABLE
     *                       1      | 1           ← Task 1 has "bug"
     *                       1      | 2           ← Task 1 has "frontend"
     *                       2      | 1           ← Task 2 has "bug"
     *
     * @JoinTable defines this join table:
     *   name             → table name: "task_labels"
     *   joinColumns      → FK pointing to THIS entity: task_id
     *   inverseJoinColumns → FK pointing to the OTHER entity: label_id
     *
     * We use Set (not List) because:
     *   1. No duplicate labels on a task
     *   2. Hibernate handles Set better for ManyToMany
     *
     * @Builder.Default → tells Lombok's @Builder to use this default value
     *   (new HashSet<>()) instead of null when building.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "task_labels",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    @Builder.Default
    private Set<Label> labels = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
