package dev.uliana.socks_accounting.service;

import dev.uliana.socks_accounting.dto.SockCsv;
import dev.uliana.socks_accounting.exception.FileProcessingException;
import dev.uliana.socks_accounting.exception.SockBadRequestException;
import dev.uliana.socks_accounting.model.Sock;
import dev.uliana.socks_accounting.repository.SockRepository;
import dev.uliana.socks_accounting.service.impl.SockFileService;
import dev.uliana.socks_accounting.service.mapper.SockMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SockFileServiceTest {
    @InjectMocks
    private SockFileService service;

    @Mock
    private SockRepository repository;

    @Mock
    private SockMapper mapper;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void registerFileBatchSuccess() throws IOException {
        MultipartFile file = new MockMultipartFile(
            "file", "test-batch.csv", "text/csv", Files.readAllBytes(
            Path.of("src/test/resources/test-batch.csv")
        ));

        when(repository.findByHexColorAndCottonPercentage(anyString(), any(Byte.class)))
            .thenReturn(Optional.empty());
        when(mapper.toSockFromCsv(any(SockCsv.class)))
            .thenAnswer(invocation -> {
                SockCsv sockCsv = invocation.getArgument(0);
                return new Sock(null, sockCsv.getHexColor(), sockCsv.getCottonPercentage(), sockCsv.getCount(), LocalDateTime.now());
            });

        int count = service.registerFileBatch(file);

        assertEquals(3, count);
    }

    @Test
    public void registerFileBatchColorNotValid() {
        String invalidCsvContent = """
            hexColor,cottonPercentage,count
            INVALID_COLOR,50,100
            """;
        MultipartFile file = new MockMultipartFile(
            "file",
            "invalid-batch.csv",
            "text/csv",
            invalidCsvContent.getBytes()
        );

        SockBadRequestException exception = assertThrows(SockBadRequestException.class,
            () -> service.registerFileBatch(file));
        assertEquals("Некорректный формат данных цвета: INVALID_COLOR.", exception.getMessage());
    }

    @Test
    public void registerFileBatchCottonPercentageNotValid() {
        String invalidCsvContent = """
            hexColor,cottonPercentage,count
            #FFFFFF,-10,100
            """;
        MultipartFile file = new MockMultipartFile(
            "file",
            "invalid-batch.csv",
            "text/csv",
            invalidCsvContent.getBytes()
        );

        SockBadRequestException exception = assertThrows(SockBadRequestException.class,
            () -> service.registerFileBatch(file));
        assertEquals("Некорректный формат данных процента хлопка: -10.", exception.getMessage());
    }

    @Test
    public void registerFileBatchCountNotValid() {
        String invalidCsvContent = """
            hexColor,cottonPercentage,count
            #FFFFFF,50,-1
            """;
        MultipartFile file = new MockMultipartFile(
            "file",
            "invalid-batch.csv",
            "text/csv",
            invalidCsvContent.getBytes()
        );

        SockBadRequestException exception = assertThrows(SockBadRequestException.class,
            () -> service.registerFileBatch(file));
        assertEquals("Некорректный формат данных количества носков: -1.", exception.getMessage());
    }

    @Test
    public void registerFileBatchFileException() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenThrow(new IOException("Ошибка чтения файла"));

        FileProcessingException exception = assertThrows(FileProcessingException.class,
            () -> service.registerFileBatch(file));
        assertTrue(exception.getMessage().contains("Ошибка при обработке файла"));
        assertTrue(exception.getMessage().contains("Ошибка чтения файла"));
    }
}