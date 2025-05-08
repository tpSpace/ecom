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

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String msg, T data) {
        ApiResponse<T> r = new ApiResponse<>(true, msg, data);
        return r;
    }

    public static ApiResponse<?> error(String msg, Map<String, String> errs) {
        ApiResponse<?> r = new ApiResponse<>(false, msg, null);
        r.errors = errs;
        return r;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}