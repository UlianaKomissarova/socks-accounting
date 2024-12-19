package dev.uliana.socks_accounting.service;

import dev.uliana.socks_accounting.dto.SockResponse;
import dev.uliana.socks_accounting.exception.SockBadRequestException;
import dev.uliana.socks_accounting.model.Sock;
import dev.uliana.socks_accounting.repository.SockRepository;
import dev.uliana.socks_accounting.service.impl.SockSearchService;
import dev.uliana.socks_accounting.service.mapper.SockMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SockSearchServiceTest {
    @InjectMocks
    private SockSearchService service;

    @Mock
    private SockRepository repository;

    @Mock
    private SockMapper mapper;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void getSocksWithCottonRangeSuccess() {
        Sock sock1 = new Sock(1L, "#FFFFFF", (byte) 50, 100, LocalDateTime.now());
        Sock sock2 = new Sock(2L, "#FFFFFF", (byte) 80, 50, LocalDateTime.now());
        List<Sock> socks = List.of(sock1, sock2);

        SockResponse response1 = new SockResponse(1L, "#FFFFFF", (byte) 50, 100);
        SockResponse response2 = new SockResponse(2L, "#FFFFFF", (byte) 80, 50);
        List<SockResponse> expectedResponses = List.of(response1, response2);

        when(repository.findAll(any(Specification.class), any(Sort.class))).thenReturn(socks);
        when(mapper.toSockResponse(sock1)).thenReturn(response1);
        when(mapper.toSockResponse(sock2)).thenReturn(response2);

        List<SockResponse> result = service.getSocks(
            "#FFFFFF",
            (byte) 50,
            (byte) 40,
            (byte) 90,
            "equal",
            "cottonPercentage",
            "asc");

        assertNotNull(result);
        assertEquals(expectedResponses, result);

        verify(repository).findAll(any(Specification.class), any(Sort.class));
        verify(mapper).toSockResponse(sock1);
        verify(mapper).toSockResponse(sock2);
    }

    @Test
    public void getSocksWithExactCottonPercentageSuccess() {
        Sock sock1 = new Sock(1L, "#FFFFFF", (byte) 60, 100, LocalDateTime.now());
        Sock sock2 = new Sock(2L, "#FFFFFF", (byte) 70, 50, LocalDateTime.now());
        List<Sock> socks = List.of(sock1, sock2);

        SockResponse response1 = new SockResponse(1L, "#FFFFFF", (byte) 60, 100);
        SockResponse response2 = new SockResponse(2L, "#FFFFFF", (byte) 70, 50);
        List<SockResponse> expectedResponses = List.of(response1, response2);

        when(repository.findAll(any(Specification.class), any(Sort.class))).thenReturn(socks);
        when(mapper.toSockResponse(sock1)).thenReturn(response1);
        when(mapper.toSockResponse(sock2)).thenReturn(response2);

        List<SockResponse> result = service.getSocks(
            "#FFFFFF",
            (byte) 50,
            null,
            null,
            "moreThan",
            "cottonPercentage",
            "asc");

        assertNotNull(result);
        assertEquals(expectedResponses, result);

        verify(repository).findAll(any(Specification.class), any(Sort.class));
        verify(mapper).toSockResponse(sock1);
        verify(mapper).toSockResponse(sock2);
    }

    @Test
    public void getSocksSortByBadRequest() {
        assertThrows(SockBadRequestException.class, () -> service.getSocks(
            "#FFFFFF",
            (byte) 50,
            (byte) 40,
            (byte) 90,
            "equal",
            "badRequest",
            "asc")
        );
    }
}