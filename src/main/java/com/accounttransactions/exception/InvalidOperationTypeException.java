package com.accounttransactions.exception;

public class InvalidOperationTypeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidOperationTypeException() {
		super("Invalid Operation Type");
	}

}