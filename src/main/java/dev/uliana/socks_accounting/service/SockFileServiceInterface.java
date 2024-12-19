package dev.uliana.socks_accounting.service;

import org.springframework.web.multipart.MultipartFile;

public interface SockFileServiceInterface {
    int registerFileBatch(MultipartFile file);
}