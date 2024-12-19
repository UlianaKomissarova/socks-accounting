package dev.uliana.socks_accounting.service.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import dev.uliana.socks_accounting.dto.SockCsv;
import dev.uliana.socks_accounting.exception.FileProcessingException;
import dev.uliana.socks_accounting.exception.SockBadRequestException;
import dev.uliana.socks_accounting.model.Sock;
import dev.uliana.socks_accounting.repository.SockRepository;
import dev.uliana.socks_accounting.service.SockFileServiceInterface;
import dev.uliana.socks_accounting.service.mapper.SockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dev.uliana.socks_accounting.util.Constants.HEX_REGEX;

@Service
@RequiredArgsConstructor
public class SockFileService implements SockFileServiceInterface {
    private final SockRepository repository;

    private final SockMapper mapper;

    @Override
    @Transactional
    public int registerFileBatch(MultipartFile file) {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<SockCsv> csvToBean = new CsvToBeanBuilder<SockCsv>(reader)
                .withType(SockCsv.class)
                .withIgnoreEmptyLine(true)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

            List<SockCsv> csvSocks = csvToBean.parse();
            List<Sock> socksToSave = getSocksToFromCsv(csvSocks);
            repository.saveAll(socksToSave);

            return socksToSave.size();

        } catch (IOException exception) {
            throw new FileProcessingException("Ошибка при обработке файла" + exception.getMessage());
        }
    }

    private List<Sock> getSocksToFromCsv(List<SockCsv> csvSocks) {
        List<Sock> socksToSave = new ArrayList<>();

        for (SockCsv csvSock : csvSocks) {
            validateCsvSock(csvSock);

            Optional<Sock> existingSock = repository.findByHexColorAndCottonPercentage(
                csvSock.getHexColor(), csvSock.getCottonPercentage());
            Sock newSock;

            if (existingSock.isPresent()) {
                newSock = existingSock.get();
                newSock.setCount(newSock.getCount() + csvSock.getCount());
            } else {
                newSock = mapper.toSockFromCsv(csvSock);
                newSock.setCreatedAt(LocalDateTime.now());
            }

            socksToSave.add(newSock);
        }

        return socksToSave;
    }

    private void validateCsvSock(SockCsv csvSock) {
        if (!csvSock.getHexColor().matches(HEX_REGEX)) {
            throw new SockBadRequestException(
                String.format("Некорректный формат данных цвета: %s.", csvSock.getHexColor())
            );
        }

        if (csvSock.getCottonPercentage() < 0 || csvSock.getCottonPercentage() > 100) {
            throw new SockBadRequestException(
                String.format("Некорректный формат данных процента хлопка: %d.", csvSock.getCottonPercentage())
            );
        }

        if (csvSock.getCount() <= 0) {
            throw new SockBadRequestException(
                String.format("Некорректный формат данных количества носков: %d.", csvSock.getCount())
            );
        }
    }
}