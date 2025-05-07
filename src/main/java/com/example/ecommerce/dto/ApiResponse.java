package com.example.ecommerce.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private Map<String, String> errors = new HashMap<>();
    private LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponse<T> success(String msg, T data) {
        ApiResponse<T> r = new ApiResponse<>();
        r.success = true;
        r.message = msg;
        r.data = data;
        return r;
    }

    public static ApiResponse<?> error(String msg, Map<String, String> errs) {
        ApiResponse<?> r = new ApiResponse<>();
        r.success = false;
        r.message = msg;
        r.errors = errs;
        return r;
    }
}