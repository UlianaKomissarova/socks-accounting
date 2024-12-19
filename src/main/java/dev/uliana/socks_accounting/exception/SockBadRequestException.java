package dev.uliana.socks_accounting.exception;

public class SockBadRequestException extends RuntimeException {
    public SockBadRequestException(String message) {
        super(message);
    }
}