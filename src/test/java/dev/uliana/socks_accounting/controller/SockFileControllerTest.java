package dev.uliana.socks_accounting.controller;

import dev.uliana.socks_accounting.repository.SockRepository;
import dev.uliana.socks_accounting.service.SockFileServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext
public class SockFileControllerTest extends PostgresContainer {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SockFileServiceInterface service;

    @Autowired
    private SockRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void registerFileBatchSuccess() throws Exception {
        Path path = Paths.get(getClass().getClassLoader().getResource("test-batch.csv").toURI());
        MockMultipartFile file = new MockMultipartFile(
            "file",
            path.getFileName().toString(),
            "text/csv",
            Files.readAllBytes(path)
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/socks/batch")
                .file(file))
            .andExpect(status().isCreated())
            .andExpect(content().string("Загружено партий носков в количестве 3"));
    }
}