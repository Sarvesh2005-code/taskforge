package com.taskforge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taskforge.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * USER ENTITY — Maps to the "users" table.
 *
 * RELATIONSHIP: One User → Many Tasks
 * ─────────────────────────────────────
 * One user can have MANY tasks assigned to them.
 * This is a ONE-TO-MANY relationship.
 *
 * In database terms:
 *   users table:  id | username | email | ...
 *   tasks table:  id | title | ... | assignee_id (FK → users.id)
 *
 * The "tasks" field here is the INVERSE side (read-only view).
 * The OWNING side is Task.assignee (it has the @JoinColumn / FK).
 *
 * @OneToMany(mappedBy = "assignee")
 *   - mappedBy = "assignee" → "The Task entity owns this relationship
 *     via its 'assignee' field. I'm just the mirror."
 *
 * @JsonIgnore on tasks:
 *   - Without this, serializing a User would include all its Tasks,
 *     and each Task includes its User, which includes Tasks again...
 *     INFINITE LOOP! → StackOverflowError
 *   - We avoid this by using DTOs (Module 3 lesson), but @JsonIgnore
 *     is a safety net in case an entity is accidentally serialized.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Password is stored here but will be HASHED in Module 6.
     * NEVER store plain text passwords in production!
     * For now, it's a placeholder.
     */
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * THE INVERSE SIDE of the User ↔ Task relationship.
     *
     * mappedBy = "assignee" means:
     *   "The Task entity has a field called 'assignee' that holds the FK.
     *    I'm just the other side of that relationship."
     *
     * cascade = CascadeType.ALL:
     *   When you save/delete a User, the operation "cascades" to their tasks.
     *   - Save user → also saves their tasks
     *   - Delete user → also deletes their tasks
     *
     * orphanRemoval = true:
     *   If you remove a task from user.getTasks(), it's also deleted from the DB.
     *   "Orphan" = a child entity that no longer has a parent.
     */
    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore   // Safety net: prevent infinite loop if entity is accidentally serialized
    private List<Task> tasks = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
