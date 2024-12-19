package dev.uliana.socks_accounting.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static dev.uliana.socks_accounting.util.Constants.DATE_TIME_FORMAT;

@Entity
@Table(name = "sock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sock {
    @Id
    @GeneratedValue(generator = "sock_seq")
    @SequenceGenerator(name = "sock_seq", sequenceName = "sock_sequence", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String hexColor;

    @Column(nullable = false)
    private byte cottonPercentage;

    @Column(nullable = false)
    private int count;

    @Column
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime createdAt;
}