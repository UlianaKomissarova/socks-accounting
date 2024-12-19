package dev.uliana.socks_accounting.exception;

public class SockOutOfStockException extends RuntimeException {
    public SockOutOfStockException(String message) {
        super(message);
    }
}
