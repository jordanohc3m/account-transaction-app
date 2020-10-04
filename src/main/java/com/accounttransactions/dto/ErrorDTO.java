package com.accounttransactions.dto;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public final class ErrorDTO {

	private final String message;
	private final HttpStatus status;
	private final LocalDateTime timestamp;

	public ErrorDTO(HttpStatus status, Throwable ex) {
		this(status, ex.getMessage());
	}

	public ErrorDTO(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

}