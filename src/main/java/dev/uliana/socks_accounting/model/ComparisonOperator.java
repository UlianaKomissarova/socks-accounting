package dev.uliana.socks_accounting.model;

import dev.uliana.socks_accounting.exception.SockBadRequestException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(description = "Перечисление операторов сравнения процентного содержания хлопка в носках.")
public enum ComparisonOperator {
    @Schema(description = "Оператор сравнения на большее значение", example = "moreThan")
    MORE_THAN("moreThan"),

    @Schema(description = "Оператор сравнения на меньшее значение", example = "lessThan")
    LESS_THAN("lessThan"),

    @Schema(description = "Оператор сравнения на равенство", example = "equal")
    EQUAL("equal");

    private final String value;

    @Schema(hidden = true)
    public static ComparisonOperator fromString(String value) {
        for (ComparisonOperator operator : ComparisonOperator.values()) {
            if (operator.getValue().equals(value)) {
                return operator;
            }
        }

        throw new SockBadRequestException("Некорректный формат данных. Допустимые значения: moreThan, lessThan, equal");
    }
}