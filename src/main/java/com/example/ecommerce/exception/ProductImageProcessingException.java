package com.example.ecommerce.exception;

public class ProductImageProcessingException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProductImageProcessingException(String message) {
        super(message);
    }

    public ProductImageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}