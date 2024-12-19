package dev.uliana.socks_accounting.repository;

import dev.uliana.socks_accounting.exception.SockBadRequestException;
import dev.uliana.socks_accounting.model.ComparisonOperator;
import dev.uliana.socks_accounting.model.Sock;
import org.springframework.data.jpa.domain.Specification;

public class SockSpecification {
    public static Specification<Sock> filterByColor(String hexColor) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("hexColor"), hexColor);
    }

    public static Specification<Sock> filterByCottonPercentage(Byte cottonPercentage, String comparisonOperator) {
        ComparisonOperator operator = ComparisonOperator.fromString(comparisonOperator);

        return (root, query, criteriaBuilder) -> switch (operator) {
            case EQUAL -> criteriaBuilder.equal(root.get("cottonPercentage"), cottonPercentage);
            case MORE_THAN -> criteriaBuilder.gt(root.get("cottonPercentage"), cottonPercentage);
            case LESS_THAN -> criteriaBuilder.lt(root.get("cottonPercentage"), cottonPercentage);
            default -> throw new SockBadRequestException("Некорректный формат данных.");
        };
    }

    public static Specification<Sock> filterByCottonPercentageRange(
        Byte cottonPercentageFrom, Byte cottonPercentageTo) {

        return (root, query, criteriaBuilder) ->
            criteriaBuilder.between(root.get("cottonPercentage"), cottonPercentageFrom, cottonPercentageTo);
    }
}