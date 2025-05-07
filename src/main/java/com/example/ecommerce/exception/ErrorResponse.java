package com.example.ecommerce.exception;

import java.util.Date;

public class ErrorResponse {
    private int status;
    private Date timestamp;
    private String message;
    private String details;

    public ErrorResponse(int status, Date timestamp, String message, String details) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public int getStatus() {
        return status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}