package com.taskforge.config;

import com.taskforge.entity.Label;
import com.taskforge.entity.Task;
import com.taskforge.entity.User;
import com.taskforge.enums.Role;
import com.taskforge.enums.TaskPriority;
import com.taskforge.enums.TaskStatus;
import com.taskforge.repository.LabelRepository;
import com.taskforge.repository.TaskRepository;
import com.taskforge.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Updated DataInitializer — now seeds Users, Labels, AND Tasks with relationships.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;

    public DataInitializer(TaskRepository taskRepository,
                           UserRepository userRepository,
                           LabelRepository labelRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.labelRepository = labelRepository;
    }

    @Override
    public void run(String... args) {
        if (taskRepository.count() > 0) return;

        // ── Create Users ──
        User sarvesh = userRepository.save(User.builder()
                .username("sarvesh")
                .email("sarvesh@taskforge.com")
                .password("password123")    // Will be hashed in Module 6!
                .role(Role.ADMIN)
                .build());

        User john = userRepository.save(User.builder()
                .username("john")
                .email("john@taskforge.com")
                .password("password123")
                .role(Role.USER)
                .build());

        // ── Create Labels ──
        Label bug = labelRepository.save(Label.builder()
                .name("bug").color("#FF0000").build());
        Label feature = labelRepository.save(Label.builder()
                .name("feature").color("#00FF00").build());
        Label backend = labelRepository.save(Label.builder()
                .name("backend").color("#0066FF").build());
        Label frontend = labelRepository.save(Label.builder()
                .name("frontend").color("#FF9900").build());
        Label urgent = labelRepository.save(Label.builder()
                .name("urgent").color("#FF00FF").build());

        // ── Create Tasks with relationships ──
        taskRepository.save(Task.builder()
                .title("Set up Spring Boot project")
                .description("Initialize project with Spring Initializr")
                .status(TaskStatus.DONE).priority(TaskPriority.HIGH)
                .assignee(sarvesh)
                .labels(Set.of(backend))
                .build());

        taskRepository.save(Task.builder()
                .title("Build REST API endpoints")
                .description("Create CRUD operations for tasks")
                .status(TaskStatus.DONE).priority(TaskPriority.HIGH)
                .assignee(sarvesh)
                .labels(Set.of(backend, feature))
                .build());

        taskRepository.save(Task.builder()
                .title("Fix login page CSS")
                .description("Button alignment is broken on mobile")
                .status(TaskStatus.IN_PROGRESS).priority(TaskPriority.MEDIUM)
                .assignee(john)
                .labels(Set.of(frontend, bug))
                .build());

        taskRepository.save(Task.builder()
                .title("Add input validation")
                .description("Use Bean Validation annotations")
                .status(TaskStatus.TODO).priority(TaskPriority.MEDIUM)
                .assignee(sarvesh)
                .labels(Set.of(backend))
                .build());

        taskRepository.save(Task.builder()
                .title("Design dashboard UI")
                .description("Create mockups for the task board")
                .status(TaskStatus.TODO).priority(TaskPriority.LOW)
                .labels(Set.of(frontend))  // No assignee — unassigned task!
                .build());

        System.out.println("✅ Seeded: " + userRepository.count() + " users, "
                + labelRepository.count() + " labels, "
                + taskRepository.count() + " tasks");
    }
}
