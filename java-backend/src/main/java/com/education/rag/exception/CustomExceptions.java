package com.education.rag.exception;

public final class CustomExceptions {
    private CustomExceptions() {}
    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) { super(message); }
    }
    public static class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException(String message) { super(message); }
    }
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) { super(message); }
    }
    public static class PythonServiceException extends RuntimeException {
        private final int status;
        public PythonServiceException(String message, int status) { super(message); this.status = status; }
        public int getStatus() { return status; }
    }
}