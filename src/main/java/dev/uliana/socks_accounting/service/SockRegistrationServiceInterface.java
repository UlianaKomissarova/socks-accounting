package dev.uliana.socks_accounting.service;

import dev.uliana.socks_accounting.dto.SockRequest;
import dev.uliana.socks_accounting.dto.SockResponse;

public interface SockRegistrationServiceInterface {
    SockResponse registerIncome(SockRequest incomeRequest);

    SockResponse registerOutcome(SockRequest outcomeRequest);
}