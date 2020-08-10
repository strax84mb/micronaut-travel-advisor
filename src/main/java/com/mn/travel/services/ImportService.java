package com.mn.travel.services;

import com.mn.travel.dto.ImportLineError;
import com.mn.travel.util.Mapping;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ImportService {

    List<ImportLineError> parseDataset(InputStream inputStream, Mapping mapping) throws IOException, CsvValidationException;
}
