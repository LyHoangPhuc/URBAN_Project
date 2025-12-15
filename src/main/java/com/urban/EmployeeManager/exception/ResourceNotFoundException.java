// ResourceNotFoundException.java
package com.urban.EmployeeManager.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

