package com.taskforge.service;

import com.taskforge.dto.request.TaskRequest;
import com.taskforge.dto.response.LabelResponse;
import com.taskforge.dto.response.TaskResponse;
import com.taskforge.dto.response.UserSummary;
import com.taskforge.entity.Label;
import com.taskforge.entity.Task;
import com.taskforge.entity.User;
import com.taskforge.enums.TaskPriority;
import com.taskforge.enums.TaskStatus;
import com.taskforge.exception.ResourceNotFoundException;
import com.taskforge.repository.LabelRepository;
import com.taskforge.repository.TaskRepository;
import com.taskforge.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Updated TaskService — now handles User (assignee) and Label relationships.
 *
 * Notice how the service now depends on THREE repositories:
 *   - TaskRepository   → to manage tasks
 *   - UserRepository   → to look up users by ID (for assigning)
 *   - LabelRepository  → to look up labels by ID (for tagging)
 *
 * This is NORMAL in real applications. Services often need
 * multiple repositories to coordinate operations.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;

    // Spring injects all three repositories automatically
    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository,
                       LabelRepository labelRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.labelRepository = labelRepository;
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return mapToResponse(task);
    }

    public TaskResponse createTask(TaskRequest request) {
        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .status(request.status() != null ? request.status() : TaskStatus.TODO)
                .priority(request.priority() != null ? request.priority() : TaskPriority.MEDIUM)
                .build();

        // Set the assignee if provided
        if (request.assigneeId() != null) {
            User user = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "User not found with id: " + request.assigneeId()));
            task.setAssignee(user);
        }

        // Set labels if provided
        if (request.labelIds() != null && !request.labelIds().isEmpty()) {
            Set<Label> labels = new HashSet<>(labelRepository.findAllById(request.labelIds()));
            if (labels.size() != request.labelIds().size()) {
                throw new ResourceNotFoundException("One or more labels not found");
            }
            task.setLabels(labels);
        }

        return mapToResponse(taskRepository.save(task));
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setPriority(request.priority());

        // Update assignee
        if (request.assigneeId() != null) {
            User user = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "User not found with id: " + request.assigneeId()));
            task.setAssignee(user);
        } else {
            task.setAssignee(null);  // Unassign
        }

        // Update labels
        if (request.labelIds() != null) {
            Set<Label> labels = new HashSet<>(labelRepository.findAllById(request.labelIds()));
            task.setLabels(labels);
        }

        return mapToResponse(taskRepository.save(task));
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    /**
     * MAPPER — now maps relationships too.
     * Converts User → UserSummary, Set<Label> → Set<LabelResponse>
     */
    private TaskResponse mapToResponse(Task task) {
        // Map assignee (User → UserSummary) — can be null
        UserSummary assignee = null;
        if (task.getAssignee() != null) {
            User user = task.getAssignee();
            assignee = new UserSummary(user.getId(), user.getUsername(), user.getEmail());
        }

        // Map labels (Set<Label> → Set<LabelResponse>)
        Set<LabelResponse> labels = task.getLabels().stream()
                .map(label -> new LabelResponse(label.getId(), label.getName(), label.getColor()))
                .collect(Collectors.toSet());

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                assignee,
                labels,
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
