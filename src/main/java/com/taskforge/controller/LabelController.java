package com.taskforge.controller;

import com.taskforge.dto.request.LabelRequest;
import com.taskforge.dto.response.LabelResponse;
import com.taskforge.service.LabelService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Label CRUD controller — follows the exact same pattern as TaskController.
 * By now this pattern should feel familiar!
 */
@RestController
@RequestMapping("/api/labels")
public class LabelController {

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping
    public ResponseEntity<List<LabelResponse>> getAllLabels() {
        return ResponseEntity.ok(labelService.getAllLabels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabelResponse> getLabelById(@PathVariable Long id) {
        return ResponseEntity.ok(labelService.getLabelById(id));
    }

    @PostMapping
    public ResponseEntity<LabelResponse> createLabel(@Valid @RequestBody LabelRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labelService.createLabel(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabelResponse> updateLabel(
            @PathVariable Long id,
            @Valid @RequestBody LabelRequest request
    ) {
        return ResponseEntity.ok(labelService.updateLabel(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable Long id) {
        labelService.deleteLabel(id);
        return ResponseEntity.noContent().build();
    }
}
