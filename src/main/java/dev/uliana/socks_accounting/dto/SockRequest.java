package dev.uliana.socks_accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import static dev.uliana.socks_accounting.exception.ErrorMessage.COLOR_IS_BLANK;
import static dev.uliana.socks_accounting.exception.ErrorMessage.COTTON_IS_NULL;
import static dev.uliana.socks_accounting.exception.ErrorMessage.COTTON_LESS_THAN_ZERO;
import static dev.uliana.socks_accounting.exception.ErrorMessage.COTTON_MORE_THAN_HUNDRED;
import static dev.uliana.socks_accounting.exception.ErrorMessage.COUNT_IS_NEGATIVE;
import static dev.uliana.socks_accounting.exception.ErrorMessage.COUNT_IS_NULL;
import static dev.uliana.socks_accounting.exception.ErrorMessage.INVALID_HEX;
import static dev.uliana.socks_accounting.util.Constants.HEX_REGEX;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Schema(description = "DTO для регистрации/обновления данных о носках")
public class SockRequest {
    @NotBlank(message = COLOR_IS_BLANK)
    @Pattern(regexp = HEX_REGEX, message = INVALID_HEX)
    @Schema(description = "Цвет носка в шестнадцатеричном формате", example = "#FFFFFF",
        pattern = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$")
    private String hexColor;

    @NotNull(message = COTTON_IS_NULL)
    @Min(value = 0, message = COTTON_LESS_THAN_ZERO)
    @Max(value = 100, message = COTTON_MORE_THAN_HUNDRED)
    @Schema(description = "Процентное содержание хлопка в носке", example = "50", minimum = "0", maximum = "100")
    private Byte cottonPercentage;

    @NotNull(message = COUNT_IS_NULL)
    @Positive(message = COUNT_IS_NEGATIVE)
    @Schema(description = "Количество носков в партии", example = "20")
    private Integer count;
}