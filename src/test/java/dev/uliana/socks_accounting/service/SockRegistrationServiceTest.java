package dev.uliana.socks_accounting.service;

import dev.uliana.socks_accounting.data.SockData;
import dev.uliana.socks_accounting.dto.SockRequest;
import dev.uliana.socks_accounting.dto.SockResponse;
import dev.uliana.socks_accounting.exception.SockNotFoundException;
import dev.uliana.socks_accounting.exception.SockOutOfStockException;
import dev.uliana.socks_accounting.model.Sock;
import dev.uliana.socks_accounting.repository.SockRepository;
import dev.uliana.socks_accounting.service.impl.SockRegistrationService;
import dev.uliana.socks_accounting.service.mapper.SockMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SockRegistrationServiceTest {
    @InjectMocks
    private SockRegistrationService service;

    @Mock
    private SockRepository repository;

    @Mock
    private SockMapper mapper;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void registerNewIncomeSuccess() {
        SockRequest request = SockData.getSockRequest();
        Sock newSock = SockData.getSock();
        SockResponse expectedResponse = SockData.getSockResponse();

        when(repository.save(newSock)).thenReturn(newSock);
        when(repository.findByHexColorAndCottonPercentage(newSock.getHexColor(), newSock.getCottonPercentage()))
            .thenReturn(Optional.empty());
        when(mapper.toSockFromRequest(request)).thenReturn(newSock);
        when(mapper.toSockResponse(newSock)).thenReturn(expectedResponse);

        SockResponse result = service.registerIncome(request);

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(repository).findByHexColorAndCottonPercentage(request.getHexColor(), request.getCottonPercentage());
        verify(repository).save(newSock);
        verify(mapper).toSockFromRequest(request);
        verify(mapper).toSockResponse(newSock);
    }

    @Test
    public void registerExistingIncomeSuccess() {
        SockRequest request = SockData.getSockRequest();
        Sock sock = SockData.getSock();
        SockResponse expectedResponse = SockData.getSockResponse();
        expectedResponse.setCount(sock.getCount() + request.getCount());

        when(repository.save(sock)).thenReturn(sock);
        when(repository.findByHexColorAndCottonPercentage(sock.getHexColor(), sock.getCottonPercentage()))
            .thenReturn(Optional.of(sock));
        when(mapper.toSockResponse(sock)).thenReturn(expectedResponse);

        SockResponse result = service.registerIncome(request);

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(repository).findByHexColorAndCottonPercentage(request.getHexColor(), request.getCottonPercentage());
        verify(repository).save(sock);
        verify(mapper).toSockResponse(sock);
    }

    @Test
    public void registerOutcomeSuccess() {
        SockRequest request = SockData.getSockRequest();
        Sock sock = SockData.getSock();
        SockResponse expectedResponse = SockData.getSockResponse();
        expectedResponse.setCount(sock.getCount() - request.getCount());

        when(repository.findByHexColorAndCottonPercentage(request.getHexColor(), request.getCottonPercentage()))
            .thenReturn(Optional.of(sock));
        when(repository.save(sock)).thenReturn(sock);
        when(mapper.toSockResponse(sock)).thenReturn(expectedResponse);

        SockResponse result = service.registerOutcome(request);

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(repository).findByHexColorAndCottonPercentage(request.getHexColor(), request.getCottonPercentage());
        verify(repository).save(sock);
        verify(mapper).toSockResponse(sock);
    }

    @Test
    public void registerOutcomeNotFound() {
        SockRequest request = SockData.getSockRequest();

        when(repository.findByHexColorAndCottonPercentage(request.getHexColor(), request.getCottonPercentage()))
            .thenReturn(Optional.empty());

        assertThrows(SockNotFoundException.class, () -> service.registerOutcome(request));
    }

    @Test
    public void registerOutcomeSockOutOfStock() {
        SockRequest request = SockData.getSockRequest();
        Sock sock = SockData.getSock();
        sock.setCount(sock.getCount() - sock.getCount() / 2);

        when(repository.findByHexColorAndCottonPercentage(request.getHexColor(), request.getCottonPercentage()))
            .thenReturn(Optional.of(sock));

        assertThrows(SockOutOfStockException.class, () -> service.registerOutcome(request));
    }
}