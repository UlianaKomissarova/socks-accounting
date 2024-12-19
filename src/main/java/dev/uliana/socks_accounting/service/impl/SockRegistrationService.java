package dev.uliana.socks_accounting.service.impl;

import dev.uliana.socks_accounting.dto.SockRequest;
import dev.uliana.socks_accounting.dto.SockResponse;
import dev.uliana.socks_accounting.exception.SockNotFoundException;
import dev.uliana.socks_accounting.exception.SockOutOfStockException;
import dev.uliana.socks_accounting.model.Sock;
import dev.uliana.socks_accounting.repository.SockRepository;
import dev.uliana.socks_accounting.service.SockRegistrationServiceInterface;
import dev.uliana.socks_accounting.service.mapper.SockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SockRegistrationService implements SockRegistrationServiceInterface {
    private final SockRepository repository;

    private final SockMapper mapper;

    @Override
    @Transactional
    public SockResponse registerIncome(SockRequest request) {
        Optional<Sock> existingSock = repository.findByHexColorAndCottonPercentage(
            request.getHexColor(), request.getCottonPercentage());
        Sock newSock;

        if (existingSock.isPresent()) {
            newSock = existingSock.get();
            newSock.setCount(newSock.getCount() + request.getCount());
        } else {
            newSock = mapper.toSockFromRequest(request);
            newSock.setCreatedAt(LocalDateTime.now());
        }

        newSock = repository.save(newSock);

        return mapper.toSockResponse(newSock);
    }

    @Override
    @Transactional
    public SockResponse registerOutcome(SockRequest request) {
        Sock sock = repository.findByHexColorAndCottonPercentage(request.getHexColor(), request.getCottonPercentage())
            .orElseThrow(() -> new SockNotFoundException("Носки для отпуска не найдены."));

        if (request.getCount() > sock.getCount()) {
            throw new SockOutOfStockException("Нехватка носков на складе.");
        }

        sock.setCount(sock.getCount() - request.getCount());
        sock = repository.save(sock);

        return mapper.toSockResponse(sock);
    }
}