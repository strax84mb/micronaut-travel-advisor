package com.mn.travel.controllers;

import com.mn.travel.dto.ImportLineError;
import com.mn.travel.services.ImportService;
import com.mn.travel.services.qualifiers.ImportAirports;
import com.mn.travel.util.AirportMapping;
import com.opencsv.exceptions.CsvValidationException;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.security.annotation.Secured;

import java.io.IOException;
import java.util.List;

/**
 * Controller for /city/airport/import endpoint
 */
@Controller("/city/airport/import")
public class ImportAirportsController {

    private ImportService importService;

    /**
     * Controller used to instantiate this class
     * @param importService @{@link ImportAirports} qualifier is used to connect {@link ImportService} interface with
     *                      class {@link com.mn.travel.services.impl.ImportAirportsService}
     */
    public ImportAirportsController(@ImportAirports ImportService importService) {
        this.importService = importService;
    }

    /**
     * POST request handler for endpoint /city/airport/import
     * This is how we import bulk airport data by uploading a CSV file
     * @param file Uploaded file
     * @param airportIdIndex Value of request header airportIdIndex
     * @param nameIndex Value of request header nameIndex
     * @param cityNameIndex Value of request header cityNameIndex
     * @param cityCountryIndex Value of request header cityCountryIndex
     * @param iataIndex Value of request header iataIndex
     * @param icaoIndex Value of request header icaoIndex
     * @return List of lines of data that could not be saved and its causes
     * @throws IOException
     * @throws CsvValidationException
     */
    @Post
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured("ADMIN")
    public List<ImportLineError> upload(CompletedFileUpload file,
                                        @Header("airportIdIndex") Integer airportIdIndex,
                                        @Header("nameIndex") Integer nameIndex,
                                        @Header("cityNameIndex") Integer cityNameIndex,
                                        @Header("cityCountryIndex") Integer cityCountryIndex,
                                        @Header("iataIndex") Integer iataIndex,
                                        @Header("icaoIndex") Integer icaoIndex) throws IOException, CsvValidationException {
        var mapping = AirportMapping.builder()
                .airportId(airportIdIndex)
                .name(nameIndex)
                .cityName(cityNameIndex)
                .cityCountry(cityCountryIndex)
                .iata(iataIndex)
                .icao(icaoIndex)
                .build();
        return importService.parseDataset(file.getInputStream(), mapping);
    }
}
