package dev.uliana.socks_accounting.controller;

import dev.uliana.socks_accounting.dto.SockRequest;
import dev.uliana.socks_accounting.dto.SockResponse;
import dev.uliana.socks_accounting.exception.ErrorResponse;
import dev.uliana.socks_accounting.service.SockModificationServiceInterface;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Контроллер обновления носков", description = "Эндпоинт для обновления данных носков")
@RestController
@RequestMapping("/api/socks/{id}")
@RequiredArgsConstructor
public class SockModificationController {
    private final SockModificationServiceInterface service;

    @Operation(summary = "Обновление данных носков")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Данные о носках успешно обновлены",
            content = {@Content(schema = @Schema(implementation = SockResponse.class))}
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Носки с заданным идентификатором не найдены",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}
        )
    })
    @PutMapping
    public ResponseEntity<SockResponse> update(
        @Parameter(description = "Идентификатор носков", required = true)
        @PathVariable Long id,

        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные носков для обновления", required = true)
        @Valid @RequestBody SockRequest updateRequest
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(service.update(id, updateRequest));
    }
}