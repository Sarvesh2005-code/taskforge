package com.taskforge.service;

import com.taskforge.dto.request.LabelRequest;
import com.taskforge.dto.response.LabelResponse;
import com.taskforge.entity.Label;
import com.taskforge.exception.ResourceNotFoundException;
import com.taskforge.repository.LabelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {

    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public List<LabelResponse> getAllLabels() {
        return labelRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public LabelResponse getLabelById(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with id: " + id));
        return mapToResponse(label);
    }

    public LabelResponse createLabel(LabelRequest request) {
        Label label = Label.builder()
                .name(request.name())
                .color(request.color())
                .build();
        return mapToResponse(labelRepository.save(label));
    }

    public LabelResponse updateLabel(Long id, LabelRequest request) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with id: " + id));
        label.setName(request.name());
        label.setColor(request.color());
        return mapToResponse(labelRepository.save(label));
    }

    public void deleteLabel(Long id) {
        if (!labelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Label not found with id: " + id);
        }
        labelRepository.deleteById(id);
    }

    private LabelResponse mapToResponse(Label label) {
        return new LabelResponse(label.getId(), label.getName(), label.getColor());
    }
}
