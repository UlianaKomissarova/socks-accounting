package dev.uliana.socks_accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "DTO носка для вовзращения ответа")
public class SockResponse {
    @Schema(description = "Уникальный идентификатор носка", example = "1")
    private Long id;

    @Schema(description = "Цвет носка в шестнадцатеричном формате", example = "#FFFFFF")
    private String hexColor;

    @Schema(description = "Процентное содержание хлопка в носке", example = "50")
    private byte cottonPercentage;

    @Schema(description = "Количество носков в партии", example = "20")
    private int count;
}