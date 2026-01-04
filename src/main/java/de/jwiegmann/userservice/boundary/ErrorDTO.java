package de.jwiegmann.userservice.boundary;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDTO {

    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;

    public ErrorDTO(int status, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.path = path;
    }
}
