package com.mn.travel.city;

import com.mn.travel.dto.CityResponse;
import com.mn.travel.dto.WriteCityRequest;
import com.mn.travel.util.MTHelper;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class CityControllerTest {

    @Inject
    @Client("/")
    RxHttpClient client;

    /**
     * Add a city
     * Add another city
     * Get a city
     * Change a city
     * List all cities
     */
    @Test
    public void testCityAsAdmin() {
        // Login as admin
        var token = MTHelper.getAdminToken(client);
        // Add a city
        var addCityPayload = WriteCityRequest.builder()
                .name("Novi Sad")
                .country("Srbija")
                .build();
        var addCityRequest = HttpRequest.POST("/city", addCityPayload);
        addCityRequest.header("Authorization", "Bearer " + token);
        var cityResponse = client.toBlocking().exchange(addCityRequest, CityResponse.class).body();
        assertEquals("Novi Sad", cityResponse.getName());
        assertEquals("Srbija", cityResponse.getCountry());
        assertEquals(2L, cityResponse.getId());
        assertNull(cityResponse.getComments());
        // Add another city
        addCityPayload = WriteCityRequest.builder()
                .name("Szabatka")
                .country("Srbija")
                .build();
        addCityRequest = HttpRequest.POST("/city", addCityPayload);
        addCityRequest.header("Authorization", "Bearer " + token);
        cityResponse = client.toBlocking().exchange(addCityRequest, CityResponse.class).body();
        assertEquals("Szabatka", cityResponse.getName());
        assertEquals("Srbija", cityResponse.getCountry());
        assertEquals(3L, cityResponse.getId());
        assertNull(cityResponse.getComments());
        // Get a city
        var getCityRequest = HttpRequest.GET("/city/3");
        getCityRequest.header("Authorization", "Bearer " + token);
        cityResponse = client.toBlocking().exchange(getCityRequest, CityResponse.class).body();
        assertEquals("Szabatka", cityResponse.getName());
        assertEquals("Srbija", cityResponse.getCountry());
        assertEquals(3L, cityResponse.getId());
        assertNull(cityResponse.getComments());
        // Change a city
        var changeCityPayload = WriteCityRequest.builder()
                .name("Subotica")
                .country("Srbija")
                .build();
        var changeCityRequest = HttpRequest.PUT("/city/3", changeCityPayload);
        changeCityRequest.header("Authorization", "Bearer " + token);
        cityResponse = client.toBlocking().exchange(changeCityRequest, CityResponse.class).body();
        assertEquals("Subotica", cityResponse.getName());
        assertEquals("Srbija", cityResponse.getCountry());
        assertEquals(3L, cityResponse.getId());
        assertNull(cityResponse.getComments());
        // List all cities
        var listCitiesRequest = HttpRequest.GET("/city");
        listCitiesRequest.header("Authorization", "Bearer " + token);
        var citiesListResponse = client.toBlocking().exchange(listCitiesRequest, Argument.listOf(CityResponse.class)).body();
        assertEquals(2, citiesListResponse.size());
        assertTrue(cityIsInList(citiesListResponse, "Novi Sad"));
        assertFalse(cityIsInList(citiesListResponse, "Szabatka"));
        assertTrue(cityIsInList(citiesListResponse, "Subotica"));
    }

    private boolean cityIsInList(List<CityResponse> list, String name) {
        for (var city : list) {
            if (city.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
