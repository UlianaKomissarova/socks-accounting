package dev.uliana.socks_accounting.service;

import dev.uliana.socks_accounting.dto.SockRequest;
import dev.uliana.socks_accounting.dto.SockResponse;

public interface SockModificationServiceInterface {
    SockResponse update(Long id, SockRequest request);
}