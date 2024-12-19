package dev.uliana.socks_accounting.exception;

public class SockNotFoundException extends RuntimeException {
    public SockNotFoundException(String message) {
        super(message);
    }
}
