package com.mn.travel.controllers;

import com.mn.travel.dto.ImportLineError;
import com.mn.travel.services.ImportService;
import com.mn.travel.services.qualifiers.ImportCities;
import com.mn.travel.util.CityMapping;
import com.opencsv.exceptions.CsvValidationException;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.security.annotation.Secured;

import java.io.IOException;
import java.util.List;

/**
 * Controller for /city/import endpoint
 */
@Controller("/city/import")
public class ImportCitiesController {

    private ImportService importService;

    /**
     * Controller used to instantiate this class
     * @param importService @{@link ImportCities} qualifier is used to connect {@link ImportService} interface with
     *                      class {@link com.mn.travel.services.impl.ImportCitiesService}
     */
    public ImportCitiesController(@ImportCities ImportService importService) {
        this.importService = importService;
    }

    /**
     * POST request handler for endpoint /city/import
     * This is how we import bulk city data by uploading a CSV file
     * @param file Value of request header file
     * @param nameIndex Value of request header nameIndex
     * @param countryIndex Value of request header countryIndex
     * @return List of lines of data that could not be saved and its causes
     * @throws IOException
     * @throws CsvValidationException
     */
    @Post
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured("ADMIN")
    public List<ImportLineError> upload(CompletedFileUpload file,
                                        @Header("nameIndex") Integer nameIndex,
                                        @Header("countryIndex") Integer countryIndex) throws IOException, CsvValidationException {
        var mapping = CityMapping.builder()
                .name(nameIndex)
                .country(countryIndex)
                .build();
        return importService.parseDataset(file.getInputStream(), mapping);
    }
}
