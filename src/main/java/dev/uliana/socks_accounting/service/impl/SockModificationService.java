package dev.uliana.socks_accounting.service.impl;

import dev.uliana.socks_accounting.dto.SockRequest;
import dev.uliana.socks_accounting.dto.SockResponse;
import dev.uliana.socks_accounting.exception.SockNotFoundException;
import dev.uliana.socks_accounting.model.Sock;
import dev.uliana.socks_accounting.repository.SockRepository;
import dev.uliana.socks_accounting.service.SockModificationServiceInterface;
import dev.uliana.socks_accounting.service.mapper.SockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SockModificationService implements SockModificationServiceInterface {
    private final SockRepository repository;

    private final SockMapper mapper;

    @Override
    @Transactional
    public SockResponse update(Long id, SockRequest request) {
        Sock sock = repository.findById(id)
            .orElseThrow(() -> new SockNotFoundException(String.format("Носки с id %d не найдены.", id)));

        mapper.update(request, sock);
        Sock updated = repository.save(sock);

        return mapper.toSockResponse(updated);
    }
}