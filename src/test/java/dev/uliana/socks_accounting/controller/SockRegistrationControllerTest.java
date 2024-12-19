package dev.uliana.socks_accounting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.uliana.socks_accounting.data.SockData;
import dev.uliana.socks_accounting.dto.SockRequest;
import dev.uliana.socks_accounting.model.Sock;
import dev.uliana.socks_accounting.repository.SockRepository;
import dev.uliana.socks_accounting.service.SockRegistrationServiceInterface;
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
public class SockRegistrationControllerTest extends PostgresContainer {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SockRegistrationServiceInterface service;

    @Autowired
    private SockRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void registerIncomeSuccess() throws Exception {
        SockRequest request = SockData.getSockRequest();

        mockMvc.perform(MockMvcRequestBuilders.post(("/api/socks/income"))
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.hexColor").value("#FFFFFF"))
            .andExpect(jsonPath("$.cottonPercentage").value("50"))
            .andExpect(jsonPath("$.count").value("100"));
    }

    @Test
    public void registerIncomeInvalidBody() throws Exception {
        SockRequest request = new SockRequest("#FFFFFF", (byte) 50, -1);

        mockMvc.perform(MockMvcRequestBuilders.post(("/api/socks/income"))
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void registerOutcomeSuccess() throws Exception {
        Sock sock = new Sock(null, "#FFFFFF", (byte) 50, 300, LocalDateTime.now());
        repository.save(sock);
        SockRequest request = SockData.getSockRequest();

        mockMvc.perform(MockMvcRequestBuilders.post(("/api/socks/outcome"))
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.hexColor").value("#FFFFFF"))
            .andExpect(jsonPath("$.cottonPercentage").value("50"))
            .andExpect(jsonPath("$.count").value("200"));
    }

    @Test
    public void registerOutcomeInvalidBody() throws Exception {
        Sock sock = new Sock(null, "#FFFFFF", (byte) 50, 300, LocalDateTime.now());
        repository.save(sock);
        SockRequest request = new SockRequest("#FFFFFF", (byte) 50, -1);

        mockMvc.perform(MockMvcRequestBuilders.post(("/api/socks/outcome"))
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}