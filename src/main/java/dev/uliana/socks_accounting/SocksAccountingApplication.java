package dev.uliana.socks_accounting;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "REST API для учета носков на складе магазина", version = "1.0"))
public class SocksAccountingApplication {
    public static void main(String[] args) {
        SpringApplication.run(SocksAccountingApplication.class, args);
    }
}