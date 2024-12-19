package dev.uliana.socks_accounting.service.mapper;

import dev.uliana.socks_accounting.dto.SockCsv;
import dev.uliana.socks_accounting.dto.SockRequest;
import dev.uliana.socks_accounting.dto.SockResponse;
import dev.uliana.socks_accounting.model.Sock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SockMapper {
    SockResponse toSockResponse(Sock sock);

    Sock toSockFromRequest(SockRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void update(SockRequest request, @MappingTarget Sock sock);

    Sock toSockFromCsv(SockCsv csv);
}