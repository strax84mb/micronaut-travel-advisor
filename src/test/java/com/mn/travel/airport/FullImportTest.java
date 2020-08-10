package com.mn.travel.airport;

import com.mn.travel.dto.ImportLineError;
import com.mn.travel.util.MTHelper;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.multipart.MultipartBody;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class FullImportTest {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    public void testImports() {
        // Login as admin
        var token = MTHelper.getAdminToken(client);
        // Import cities
        var result = doImport("cities.txt", "/city/import", token, Map.of(
                "nameIndex", "0",
                "countryIndex", "1"
        ));
        assertTrue(result.isEmpty());
        // Import airports
        result = doImport("airports.txt", "/city/airport/import", token, Map.of(
                "airportIdIndex", "0",
                "nameIndex", "1",
                "cityNameIndex", "2",
                "cityCountryIndex", "3",
                "iataIndex", "4",
                "icaoIndex", "5"
        ));
        assertEquals(44, result.size());
        // Import routes
        result = doImport("routes.txt", "/route/import", token, Map.of(
                "airlineIndex", "0",
                "airlineIdIndex", "1",
                "sourceAirportIdIndex", "3",
                "destinationAirportIdIndex", "5",
                "priceIndex", "9"
        ));
        assertEquals(2051, result.size());
    }

    private List<ImportLineError> doImport(String fileName, String url, String adminToken, Map<String, String> indexesMap) {
        var file = new File(getClass().getClassLoader().getResource(fileName).getFile());
        var payload = MultipartBody.builder()
                .addPart(
                        "file",
                        file.getName(),
                        MediaType.TEXT_PLAIN_TYPE,
                        file)
                .build();
        var uploadRequest = HttpRequest.POST(url, payload);
        uploadRequest.bearerAuth(adminToken);
        uploadRequest.contentType(MediaType.MULTIPART_FORM_DATA_TYPE);
        for (var entry : indexesMap.entrySet()) {
            uploadRequest.header(entry.getKey(), entry.getValue());
        }
        return client.toBlocking().exchange(uploadRequest, Argument.listOf(ImportLineError.class)).body();
    }
}
