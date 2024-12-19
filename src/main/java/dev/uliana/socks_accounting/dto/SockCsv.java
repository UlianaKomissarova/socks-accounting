package dev.uliana.socks_accounting.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SockCsv {
    @CsvBindByName(required = true)
    private String hexColor;

    @CsvBindByName(required = true)
    private Byte cottonPercentage;

    @CsvBindByName(required = true)
    private int count;
}