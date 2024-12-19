package dev.uliana.socks_accounting.service.impl;

import dev.uliana.socks_accounting.dto.SockResponse;
import dev.uliana.socks_accounting.exception.SockBadRequestException;
import dev.uliana.socks_accounting.model.Sock;
import dev.uliana.socks_accounting.repository.SockRepository;
import dev.uliana.socks_accounting.repository.SockSpecification;
import dev.uliana.socks_accounting.service.SockSearchServiceInterface;
import dev.uliana.socks_accounting.service.mapper.SockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SockSearchService implements SockSearchServiceInterface {
    private final SockRepository repository;

    private final SockMapper mapper;

    private static final List<String> ALLOWED_SORT_FIELDS = List.of("hexColor", "cottonPercentage");

    @Override
    @Transactional(readOnly = true)
    public List<SockResponse> getSocks(String hexColor, Byte cottonPercentage, Byte cottonPercentageFrom,
        Byte cottonPercentageTo, String comparisonOperator, String sortBy, String sortDirection
    ) {
        Specification<Sock> specification = Specification.where(null);

        if (hexColor != null && !hexColor.isBlank()) {
            hexColor = URLDecoder.decode(hexColor, StandardCharsets.UTF_8);
            specification = specification.and(SockSpecification.filterByColor(hexColor));
        }

        if (cottonPercentageFrom != null && cottonPercentageTo != null) {
            specification = specification.and(
                SockSpecification.filterByCottonPercentageRange(cottonPercentageFrom, cottonPercentageTo)
            );
        } else if (cottonPercentage != null) {
            specification = specification.and(
                SockSpecification.filterByCottonPercentage(cottonPercentage, comparisonOperator)
            );
        }

        Sort sort = getSort(sortBy, sortDirection);

        return repository.findAll(specification, sort)
            .stream()
            .map(mapper::toSockResponse)
            .toList();
    }

    private Sort getSort(String sortBy, String sortDirection) {
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new SockBadRequestException("Невалидный формат сортировки.");
        }

        return Sort.by(Sort.Order.by(sortBy)
            .with(Sort.Direction.fromString(sortDirection)));
    }
}