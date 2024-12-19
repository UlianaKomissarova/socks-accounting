package dev.uliana.socks_accounting.exception;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@Tag(name = "Глобальный обработчик ошибок")
@RestControllerAdvice
public class GlobalExceptionHandler {
    @Operation(summary = "Обработка ошибок BadRequest",
        description = "Обрабатывает ошибки BadRequest, такие как некорректные данные или неправильный запрос.")
    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    @ExceptionHandler({SockBadRequestException.class,
        FileProcessingException.class,
        SockOutOfStockException.class,
        DataIntegrityViolationException.class,
        MethodArgumentNotValidException.class,
        MethodArgumentTypeMismatchException.class}
    )
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception exception) {
        return getErrorResponse(HttpStatus.BAD_REQUEST, exception);
    }

    @Operation(summary = "Обработка ошибок NotFound",
        description = "Обрабатывает ошибки NotFound, такие как отсутствие данных в базе")
    @ApiResponse(responseCode = "404", description = "Данные не найдены")
    @ExceptionHandler(SockNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(SockNotFoundException exception) {
        return getErrorResponse(HttpStatus.NOT_FOUND, exception);
    }

    @Operation(summary = "Обработка внутренних ошибок сервера")
    @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(Exception exception) {
        return getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    private ResponseEntity<ErrorResponse> getErrorResponse(HttpStatus httpStatus, Exception exception) {
        return new ResponseEntity<>(
            new ErrorResponse(
                httpStatus,
                LocalDateTime.now(),
                exception.getMessage()
            ),
            httpStatus
        );
    }
}