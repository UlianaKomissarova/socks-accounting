package dev.uliana.socks_accounting.repository;

import dev.uliana.socks_accounting.model.Sock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SockRepository extends JpaRepository<Sock, Long>, JpaSpecificationExecutor<Sock> {
    Optional<Sock> findByHexColorAndCottonPercentage(String color, byte cottonPercentage);
}