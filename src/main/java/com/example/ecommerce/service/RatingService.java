package com.example.ecommerce.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommerce.dto.RatingRequest;
import com.example.ecommerce.dto.RatingResponse;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.mapper.RatingMapper;
import com.example.ecommerce.model.RatingModel;
import com.example.ecommerce.repository.RatingRepository;

@Service
public class RatingService {
    private final RatingRepository repo;
    private final RatingMapper mapper;

    public RatingService(RatingRepository repo, RatingMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<RatingResponse> getAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public RatingResponse getById(UUID id) {
        RatingModel m = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating", "id", id));
        return mapper.toDto(m);
    }

    @Transactional
    public RatingResponse create(RatingRequest req) {
        RatingModel m = mapper.toEntity(req);
        RatingModel saved = repo.save(m);
        return mapper.toDto(saved);
    }

    @Transactional
    public RatingResponse update(UUID id, RatingRequest req) {
        RatingModel existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating", "id", id));
        mapper.updateEntity(existing, req);
        return mapper.toDto(repo.save(existing));
    }

    @Transactional
    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Rating", "id", id);
        }
        repo.deleteById(id);
    }
}