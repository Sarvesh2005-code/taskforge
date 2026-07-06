package com.taskforge.config;

import com.taskforge.entity.Task;
import com.taskforge.enums.TaskPriority;
import com.taskforge.enums.TaskStatus;
import com.taskforge.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * DATA INITIALIZER — Seeds the database with sample data on startup.
 *
 * WHAT IS CommandLineRunner?
 * It's a Spring interface with one method: run().
 * Spring calls run() AFTER the application has fully started.
 * Perfect for:
 *   - Inserting sample/test data
 *   - Running database migrations
 *   - Printing startup info
 *
 * @Component → tells Spring to create a Bean of this class.
 *   @Component is the generic version. Specializations:
 *     @Service     → for service classes
 *     @Repository  → for database classes
 *     @Controller  → for web controllers
 *     @Component   → for everything else
 *
 * Since H2 is in-memory with ddl-auto=create-drop, the database is
 * EMPTY on every restart. This class gives us sample data to test with.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final TaskRepository taskRepository;

    public DataInitializer(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) {
        // Only seed if the database is empty (avoid duplicates on hot-reload)
        if (taskRepository.count() > 0) {
            return;
        }

        // Using the Builder pattern (from Lombok's @Builder):
        //   Task.builder().title("...").status(...).build()
        //
        // This is cleaner than:
        //   new Task(null, "...", "...", TaskStatus.TODO, TaskPriority.HIGH, null, null)

        taskRepository.save(Task.builder()
                .title("Set up Spring Boot project")
                .description("Initialize the project with Spring Initializr and configure dependencies")
                .status(TaskStatus.DONE)
                .priority(TaskPriority.HIGH)
                .build());

        taskRepository.save(Task.builder()
                .title("Build REST API endpoints")
                .description("Create CRUD operations for the task management API")
                .status(TaskStatus.DONE)
                .priority(TaskPriority.HIGH)
                .build());

        taskRepository.save(Task.builder()
                .title("Connect to database with JPA")
                .description("Replace ArrayList with JPA entities and H2 database")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.HIGH)
                .build());

        taskRepository.save(Task.builder()
                .title("Add input validation")
                .description("Use Bean Validation annotations to validate task input")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.MEDIUM)
                .build());

        taskRepository.save(Task.builder()
                .title("Implement JWT authentication")
                .description("Add Spring Security with JWT tokens for login/register")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.LOW)
                .build());

        System.out.println("✅ Sample data loaded: " + taskRepository.count() + " tasks");
    }
}
