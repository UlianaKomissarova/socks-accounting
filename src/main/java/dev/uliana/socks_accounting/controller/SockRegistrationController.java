package dev.uliana.socks_accounting.controller;

import dev.uliana.socks_accounting.dto.SockRequest;
import dev.uliana.socks_accounting.dto.SockResponse;
import dev.uliana.socks_accounting.exception.ErrorResponse;
import dev.uliana.socks_accounting.service.SockRegistrationServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Контроллер сохранения носков", description = "Эндпоинты для сохранения прихода/отпуска носков")
@RestController
@RequestMapping("/api/socks")
@RequiredArgsConstructor
public class SockRegistrationController {
    private final SockRegistrationServiceInterface service;

    @Operation(summary = "Регистрация прихода носков")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Носки успешно зарегистрированы",
            content = {@Content(schema = @Schema(implementation = SockResponse.class))}
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Ошибка валидации",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}
        )
    })
    @PostMapping("/income")
    public ResponseEntity<SockResponse> registerIncome(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные о носках для регистрации прихода",
            required = true)
        @Valid @RequestBody SockRequest incomeRequest
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(service.registerIncome(incomeRequest));
    }

    @Operation(summary = "Регистрация отпуска носков")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Отпуск носков успешно зарегистрирован",
            content = {@Content(schema = @Schema(implementation = SockResponse.class))}
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Нехватка носков на складе",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Носки с заданным идентификатором не найдены",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}
        )
    })
    @PostMapping("/outcome")
    public ResponseEntity<SockResponse> registerOutcome(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные о носках для регистрации отпуска",
            required = true)
        @Valid @RequestBody SockRequest outcomeRequest
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(service.registerOutcome(outcomeRequest));
    }
}