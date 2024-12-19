package dev.uliana.socks_accounting.service;

import dev.uliana.socks_accounting.dto.SockResponse;

import java.util.List;

public interface SockSearchServiceInterface {
    List<SockResponse> getSocks(
        String hexColor,
        Byte cottonPercentage,
        Byte cottonPercentageFrom,
        Byte cottonPercentageTo,
        String comparisonOperator,
        String sortBy,
        String sortDirection
    );
}