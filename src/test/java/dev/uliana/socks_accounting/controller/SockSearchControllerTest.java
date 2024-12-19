package dev.uliana.socks_accounting.controller;

import dev.uliana.socks_accounting.model.Sock;
import dev.uliana.socks_accounting.repository.SockRepository;
import dev.uliana.socks_accounting.service.SockSearchServiceInterface;
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

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext
public class SockSearchControllerTest extends PostgresContainer {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SockSearchServiceInterface service;

    @Autowired
    private SockRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void getSocksSuccess() throws Exception {
        Sock sock1 = new Sock(null, "#FFFFFF", (byte) 50, 100, LocalDateTime.now());
        Sock sock2 = new Sock(null, "#AAAAAA", (byte) 80, 200, LocalDateTime.now());
        repository.save(sock1);
        repository.save(sock2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/socks")
                .param("hexColor", "#FFFFFF")
                .param("cottonPercentage", "50")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].hexColor").value("#FFFFFF"))
            .andExpect(jsonPath("$[0].cottonPercentage").value(50));
    }

    @Test
    public void getSocksSuccessEmptyList() throws Exception {
        Sock sock1 = new Sock(null, "#FFFFFF", (byte) 50, 100, LocalDateTime.now());
        Sock sock2 = new Sock(null, "#AAAAAA", (byte) 80, 200, LocalDateTime.now());
        repository.save(sock1);
        repository.save(sock2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/socks")
                .param("hexColor", "#FFFAAA")
                .param("cottonPercentage", "50")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }
}