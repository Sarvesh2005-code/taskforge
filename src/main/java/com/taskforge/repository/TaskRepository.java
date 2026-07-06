package com.taskforge.repository;

import com.taskforge.entity.Task;
import com.taskforge.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * WHAT IS A REPOSITORY?
 * =====================
 * A Repository is an INTERFACE (not a class!) that gives you database
 * operations FOR FREE. You don't write any implementation code!
 *
 * By extending JpaRepository<Task, Long>, you get ALL of these methods
 * automatically — Spring generates the implementation at runtime:
 *
 *   INHERITED METHODS (you get these for free):
 *   ──────────────────────────────────────────
 *   findAll()              → SELECT * FROM tasks
 *   findById(Long id)      → SELECT * FROM tasks WHERE id = ?
 *   save(Task task)        → INSERT INTO tasks (...) or UPDATE tasks SET ...
 *   deleteById(Long id)    → DELETE FROM tasks WHERE id = ?
 *   count()                → SELECT COUNT(*) FROM tasks
 *   existsById(Long id)    → SELECT COUNT(*) > 0 FROM tasks WHERE id = ?
 *
 * JpaRepository<Task, Long>:
 *   - Task → the Entity class this repository manages
 *   - Long → the type of the primary key (@Id field)
 *
 * HOW DOES THIS WORK WITHOUT CODE?
 * Spring Data JPA uses a technique called "proxy generation":
 *   1. At startup, Spring sees this interface extends JpaRepository
 *   2. It automatically creates a class that implements all the methods
 *   3. Each method generates the correct SQL query
 *   4. You get a fully working database layer with ZERO code!
 *
 * @Repository is optional here (JpaRepository subinterfaces are auto-detected)
 * but it's good practice — it documents intent and enables exception translation.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // ================================================================
    // CUSTOM QUERY METHODS (Spring Data Magic!)
    // ================================================================
    //
    // Spring Data JPA can generate queries from METHOD NAMES.
    // Just name your method in a specific pattern, and Spring writes the SQL!
    //
    // Method name pattern:
    //   findBy + FieldName + Condition
    //
    // Examples:
    //   findByStatus(...)          → WHERE status = ?
    //   findByTitleContaining(...) → WHERE title LIKE '%...%'
    //   findByPriorityAndStatus    → WHERE priority = ? AND status = ?

    /**
     * Find all tasks with a specific status.
     *
     * Spring generates: SELECT * FROM tasks WHERE status = ?
     *
     * Usage: taskRepository.findByStatus(TaskStatus.TODO)
     *        → returns all tasks with status "TODO"
     */
    List<Task> findByStatus(TaskStatus status);

    /**
     * Find tasks whose title contains a keyword (case-insensitive search).
     *
     * "Containing" = SQL LIKE '%keyword%'
     * "IgnoreCase" = case-insensitive comparison
     *
     * Spring generates: SELECT * FROM tasks WHERE LOWER(title) LIKE LOWER('%keyword%')
     */
    List<Task> findByTitleContainingIgnoreCase(String keyword);
}
