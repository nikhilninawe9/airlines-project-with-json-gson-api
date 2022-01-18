package com.example.demo.exception;

public class VendorNotFoundException extends RuntimeException {
	public VendorNotFoundException() {
		super();
	}

	public VendorNotFoundException(String message) {
		super(message);
	}
}
