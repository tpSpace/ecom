package com.example.ecommerce.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.dto.RatingRequest;
import com.example.ecommerce.dto.RatingResponse;
import com.example.ecommerce.service.RatingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/ratings")
public class RatingController {
    private static final Logger log = LoggerFactory.getLogger(RatingController.class);
    private final RatingService service;

    public RatingController(RatingService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RatingResponse>> listAll() {
        log.info("Fetching all ratings");
        List<RatingResponse> list = service.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingResponse> getById(@PathVariable UUID id) {
        log.info("Fetching rating with id={}", id);
        RatingResponse resp = service.getById(id);
        return ResponseEntity.ok(resp);
    }

    @PostMapping
    public ResponseEntity<RatingResponse> create(@Valid @RequestBody RatingRequest req) {
        log.info("Creating new rating for product {}", req.getProductId());
        RatingResponse created = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RatingResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody RatingRequest req) {
        log.info("Updating rating id={}", id);
        RatingResponse updated = service.update(id, req);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        log.info("Deleting rating id={}", id);
        service.delete(id);
    }
}