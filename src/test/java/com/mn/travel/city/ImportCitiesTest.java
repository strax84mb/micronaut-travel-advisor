package com.mn.travel.city;

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

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class ImportCitiesTest {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    public void importCities() {
        // Login as admin
        var token = MTHelper.getAdminToken(client);
        // Import cities via upload
        var file = new File(getClass().getClassLoader().getResource("cities.txt").getFile());
        var payload = MultipartBody.builder()
                .addPart(
                        "file",
                        file.getName(),
                        MediaType.TEXT_PLAIN_TYPE,
                        file)
                .build();
        var uploadRequest = HttpRequest.POST("/city/import", payload);
        uploadRequest.bearerAuth(token);
        uploadRequest.contentType(MediaType.MULTIPART_FORM_DATA_TYPE);
        uploadRequest.header("nameIndex", "0");
        uploadRequest.header("countryIndex", "1");
        var response = client.toBlocking().exchange(uploadRequest, Argument.listOf(ImportLineError.class)).body();
        assertEquals(0, response.size());
    }
}
