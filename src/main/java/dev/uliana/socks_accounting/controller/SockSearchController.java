package dev.uliana.socks_accounting.controller;

import dev.uliana.socks_accounting.dto.SockResponse;
import dev.uliana.socks_accounting.exception.ErrorResponse;
import dev.uliana.socks_accounting.service.SockSearchServiceInterface;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Контроллер поиска носков", description = "Эндпоинт для поиска носков с фильтрацией и сортировкой")
@RestController
@RequestMapping("/api/socks")
@RequiredArgsConstructor
public class SockSearchController {
    private final SockSearchServiceInterface service;

    @Operation(summary = "Получение носков с фильтрацией")
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
        )
    })
    @GetMapping
    public ResponseEntity<List<SockResponse>> getSocks(
        @Parameter(description = "Цвет носков в формате HEX, например, #FFFFFF, для фильтрации")
        @RequestParam(required = false) String hexColor,

        @Parameter(description = "Процент хлопка, для фильтрации")
        @RequestParam(required = false, defaultValue = "50") Byte cottonPercentage,

        @Parameter(description = "Наименьшая граница процента хлопка, не ниже нуля, для фильтрации")
        @RequestParam(required = false) Byte cottonPercentageFrom,

        @Parameter(description = "Наибольшая граница процента хлопка, не больше ста, для фильтрации")
        @RequestParam(required = false) Byte cottonPercentageTo,

        @Parameter(description = "Оператор сравнения (moreThan, lessThan, equal), для фильтрации")
        @RequestParam(required = false, defaultValue = "equal") String comparisonOperator,

        @Parameter(description = "Параметр сортировки (по цвету или проценту хлопка)")
        @RequestParam(required = false, defaultValue = "cottonPercentage") String sortBy,

        @Parameter(description = "Направление сортировки (ASC/DESC)")
        @RequestParam(required = false, defaultValue = "ASC") String sortDirection
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(service.getSocks(
                hexColor,
                cottonPercentage,
                cottonPercentageFrom,
                cottonPercentageTo,
                comparisonOperator,
                sortBy,
                sortDirection)
            );
    }
}