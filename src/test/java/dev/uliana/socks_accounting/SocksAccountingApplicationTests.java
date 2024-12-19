package dev.uliana.socks_accounting;

import dev.uliana.socks_accounting.controller.PostgresContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class SocksAccountingApplicationTests extends PostgresContainer {
    @Test
    void contextLoads() {
    }
}