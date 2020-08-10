package com.mn.travel.controllers;

import com.mn.travel.dto.ImportLineError;
import com.mn.travel.services.ImportService;
import com.mn.travel.services.qualifiers.ImportRoutes;
import com.mn.travel.util.RouteMapping;
import com.opencsv.exceptions.CsvValidationException;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.security.annotation.Secured;

import java.io.IOException;
import java.util.List;

/**
 * Controller for /route/import endpoint
 */
@Controller("/route/import")
public class ImportRoutesController {

    private ImportService importService;

    /**
     * Controller used to instantiate this class
     * @param importService @{@link ImportRoutes} qualifier is used to connect {@link ImportService} interface with
     *                      class {@link com.mn.travel.services.impl.ImportRoutesService}
     */
    public ImportRoutesController(@ImportRoutes ImportService importService) {
        this.importService = importService;
    }

    /**
     * POST request handler for endpoint /route/import
     * This is how we import bulk city data by uploading a CSV file
     * @param file Value of request header file
     * @param airlineIndex Value of request header airlineIndex
     * @param airlineIdIndex Value of request header airlineIdIndex
     * @param sourceAirportIdIndex Value of request header sourceAirportIdIndex
     * @param destinationAirportIdIndex Value of request header destinationAirportIdIndex
     * @param priceIndex Value of request header priceIndex
     * @return List of lines of data that could not be saved and its causes
     * @throws IOException
     * @throws CsvValidationException
     */
    @Post
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured("ADMIN")
    public List<ImportLineError> upload(CompletedFileUpload file,
                                        @Header("airlineIndex") Integer airlineIndex,
                                        @Header("airlineIdIndex") Integer airlineIdIndex,
                                        @Header("sourceAirportIdIndex") Integer sourceAirportIdIndex,
                                        @Header("destinationAirportIdIndex") Integer destinationAirportIdIndex,
                                        @Header("priceIndex") Integer priceIndex) throws IOException, CsvValidationException {
        var mapping = RouteMapping.builder()
                .airline(airlineIndex)
                .airlineId(airlineIdIndex)
                .sourceAirportId(sourceAirportIdIndex)
                .destinationAirportId(destinationAirportIdIndex)
                .price(priceIndex)
                .build();
        return importService.parseDataset(file.getInputStream(), mapping);

    }
}
