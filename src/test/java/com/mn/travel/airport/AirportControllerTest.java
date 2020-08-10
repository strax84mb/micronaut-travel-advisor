package com.mn.travel.airport;

import com.mn.travel.dto.AirportResponse;
import com.mn.travel.dto.CityResponse;
import com.mn.travel.dto.WriteAirportRequest;
import com.mn.travel.dto.WriteCityRequest;
import com.mn.travel.util.MTHelper;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class AirportControllerTest {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    public void testAirports() {
        // Login as admin
        var token = MTHelper.getAdminToken(client);
        // Add city
        var addCityPayload = WriteCityRequest.builder()
                .name("Beograd")
                .country("Srbija")
                .build();
        var addCityRequest = HttpRequest.POST("/city", addCityPayload);
        addCityRequest.header("Authorization", "Bearer " + token);
        var cityResponse = client.toBlocking().exchange(addCityRequest, CityResponse.class).body();
        // Add airport
        var writeAirportPayload = WriteAirportRequest.builder()
                .airportId(45L)
                .cityName("Beograd")
                .cityCountry("Srbija")
                .name("Nikola Tesla")
                .iata("12")
                .icao("23")
                .build();
        var writeAirportRequest = HttpRequest.POST("/city/airport", writeAirportPayload);
        writeAirportRequest.header("Authorization", "Bearer " + token);
        var airportResponse = client.toBlocking().exchange(writeAirportRequest, AirportResponse.class).body();
        assertEquals("12", airportResponse.getIata());
        assertEquals("23", airportResponse.getIcao());
        assertEquals("Nikola Tesla", airportResponse.getName());
        assertEquals("Beograd", airportResponse.getCity().getName());
        assertEquals("Srbija", airportResponse.getCity().getCountry());
        // Get airports
        var getAirportRequest = HttpRequest.GET("/city/airport/3");
        getAirportRequest.header("Authorization", "Bearer " + token);
        airportResponse = client.toBlocking().exchange(getAirportRequest, AirportResponse.class).body();
        assertEquals("Nikola Tesla", airportResponse.getName());
        // List airports
        var listAirportsRequest = HttpRequest.GET("city/airport");
        listAirportsRequest.header("Authorization", "Bearer " + token);
        var airportsList = client.toBlocking().exchange(listAirportsRequest, Argument.listOf(AirportResponse.class)).body();
        assertEquals(1, airportsList.size());
        assertEquals("Nikola Tesla", airportsList.get(0).getName());
    }
}
