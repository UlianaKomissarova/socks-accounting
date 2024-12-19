package dev.uliana.socks_accounting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.uliana.socks_accounting.data.SockData;
import dev.uliana.socks_accounting.dto.SockRequest;
import dev.uliana.socks_accounting.model.Sock;
import dev.uliana.socks_accounting.repository.SockRepository;
import dev.uliana.socks_accounting.service.SockModificationServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext
public class SockModificationControllerTest extends PostgresContainer {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SockModificationServiceInterface service;

    @Autowired
    private SockRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void updateSuccess() throws Exception {
        Sock sock = SockData.getSock();
        sock = repository.save(sock);
        SockRequest request = SockData.getSockRequestForUpdate();

        mockMvc.perform(MockMvcRequestBuilders.put(("/api/socks/") + sock.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.hexColor").value("#FFFFFA"))
            .andExpect(jsonPath("$.cottonPercentage").value("70"))
            .andExpect(jsonPath("$.count").value("10"));
    }

    @Test
    public void updateInvalidId() throws Exception {
        SockRequest request = SockData.getSockRequestForUpdate();

        mockMvc.perform(MockMvcRequestBuilders.put(("/api/socks/") + "Строка")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateInvalidCount() throws Exception {
        Sock sock = SockData.getSock();
        sock = repository.save(sock);
        SockRequest request = new SockRequest("#FFFFFF", (byte) 50, -1);

        mockMvc.perform(MockMvcRequestBuilders.put(("/api/socks/") + sock.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateInvalidCottonPercentage() throws Exception {
        Sock sock = SockData.getSock();
        sock = repository.save(sock);
        SockRequest request = new SockRequest("#FFFFFF", (byte) 120, 10);

        mockMvc.perform(MockMvcRequestBuilders.put(("/api/socks/") + sock.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateInvalidColor() throws Exception {
        Sock sock = SockData.getSock();
        sock = repository.save(sock);
        SockRequest request = new SockRequest("13", (byte) 50, 10);

        mockMvc.perform(MockMvcRequestBuilders.put(("/api/socks/") + sock.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}