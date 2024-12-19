package dev.uliana.socks_accounting.data;

import dev.uliana.socks_accounting.dto.SockRequest;
import dev.uliana.socks_accounting.dto.SockResponse;
import dev.uliana.socks_accounting.model.Sock;

import java.time.LocalDateTime;

public class SockData {
    public static SockRequest getSockRequest() {return new SockRequest("#FFFFFF", (byte) 50, 100);}

    public static SockRequest getSockRequestForUpdate() {
        return new SockRequest("#FFFFFA", (byte) 70, 10);
    }

    public static Sock getSock() {
        return new Sock(1L, "#FFFFFF", (byte) 50, 100, LocalDateTime.now());
    }

    public static SockResponse getSockResponse() {
        return new SockResponse(1L, "#FFFFFF", (byte) 50, 100);
    }

    public static SockResponse getSockResponseForUpdate() {
        return new SockResponse(1L, "#FFFFFA", (byte) 70, 10);
    }
}