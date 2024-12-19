package dev.uliana.socks_accounting.controller;

import dev.uliana.socks_accounting.exception.ErrorResponse;
import dev.uliana.socks_accounting.service.SockFileServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Контроллер загрузки носков из файла", description = "Эндпоинт для загрузки носков из CSV-файла")
@RestController
@RequestMapping("/api/socks/batch")
@RequiredArgsConstructor
public class SockFileController {
    private final SockFileServiceInterface service;

    @Operation(summary = "Загрузка партий носков из CSV файла")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Носки успешно загружены из файла CSV",
            content = {@Content(schema = @Schema(implementation = String.class))}
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Ошибка валидации/ Ошибка обработки файла",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}
        )
    })
    @PostMapping
    public ResponseEntity<String> registerFileBatch(
        @Parameter(description = "Файл CSV с данными о носках", required = true)
        @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body("Загружено партий носков в количестве " + service.registerFileBatch(file));
    }
}