package dev.uliana.socks_accounting.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static dev.uliana.socks_accounting.util.Constants.DATE_TIME_FORMAT;

@AllArgsConstructor
@Getter
@Schema(description = "Ответ с информацией об ошибке, возвращаемый в случае исключений")
public class ErrorResponse {
    @Schema(description = "Статус-код ошибки", example = "BAD_REQUEST")
    private HttpStatus statusCode;

    @Schema(description = "Время возникновения ошибки", example = "2024-12-19T00:00:00")
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime timestamp;

    @Schema(description = "Сообщение ошибки", example = "Некорректный запрос")
    private String message;
}