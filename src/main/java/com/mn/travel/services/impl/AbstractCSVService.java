package com.mn.travel.services.impl;

import com.mn.travel.dto.ImportLineError;
import com.mn.travel.services.ImportService;
import com.mn.travel.util.Mapping;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCSVService implements ImportService {

    @Override
    public List<ImportLineError> parseDataset(InputStream inputStream, Mapping mapping) throws IOException, CsvValidationException {
        long linesParsed = 0;
        var result = new ArrayList<ImportLineError>();
        try (var reader = makeReader(inputStream)) {
            for (var fields = reader.readNext(); fields != null; fields = reader.readNext()) {
                try {
                    parseAndSaveLine(fields, mapping);
                    linesParsed++;
                } catch (Exception error) {
                    error.printStackTrace();
                    result.add(ImportLineError.builder()
                            .errorMessage(error.getMessage())
                            .fields(fields)
                            .build());
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    private CSVReader makeReader(InputStream inputStream) {
        var parser = new CSVParserBuilder()
                .withEscapeChar('~') // OpenCSV doesn't support \N null value. Will read it as N char
                .withSeparator(',')
                .withQuoteChar('"')
                .build();
        return new CSVReaderBuilder(new InputStreamReader(inputStream))
                .withKeepCarriageReturn(false)
                .withCSVParser(parser)
                .build();
    }

    String textOrNull(String text) {
        return "\\N".equals(text) ? null : text;
    }

    long toLong(String number) {
        return Long.parseLong(number);
    }

    double toDouble(String number) {
        return Double.parseDouble(number);
    }

    public abstract void parseAndSaveLine(String[] fields, Mapping mapping) throws Exception;
}
