package dev.uliana.socks_accounting.service;

import dev.uliana.socks_accounting.data.SockData;
import dev.uliana.socks_accounting.dto.SockRequest;
import dev.uliana.socks_accounting.dto.SockResponse;
import dev.uliana.socks_accounting.exception.SockNotFoundException;
import dev.uliana.socks_accounting.model.Sock;
import dev.uliana.socks_accounting.repository.SockRepository;
import dev.uliana.socks_accounting.service.impl.SockModificationService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SockModificationServiceTest {
    @InjectMocks
    private SockModificationService service;

    @Mock
    private SockRepository repository;

    @Mock
    private SockMapper mapper;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void updateSuccess() {
        SockRequest request = SockData.getSockRequestForUpdate();
        Sock sock = SockData.getSock();
        SockResponse expectedResponse = SockData.getSockResponseForUpdate();

        when(repository.findById(sock.getId())).thenReturn(Optional.of(sock));
        doNothing().when(mapper).update(request, sock);
        when(repository.save(sock)).thenReturn(sock);
        when(mapper.toSockResponse(sock)).thenReturn(expectedResponse);

        SockResponse result = service.update(sock.getId(), request);

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(repository).findById(sock.getId());
        verify(mapper).update(request, sock);
        verify(repository).save(sock);
        verify(mapper).toSockResponse(sock);
    }

    @Test
    public void updateNotFound() {
        SockRequest request = SockData.getSockRequestForUpdate();

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SockNotFoundException.class, () -> service.update(1L, request));
        verify(repository).findById(1L);
    }
}