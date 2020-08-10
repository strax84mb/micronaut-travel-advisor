package com.mn.travel.city;

import com.mn.travel.dto.CityResponse;
import com.mn.travel.dto.CommentResponse;
import com.mn.travel.dto.WriteCityRequest;
import com.mn.travel.dto.WriteCommentRequest;
import com.mn.travel.util.MTHelper;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class CommentsTest {

    @Client("/")
    @Inject
    RxHttpClient client;

    /**
     * Post comment by user Matko
     * Try to delete it by user Sandra
     * Get posted comment
     * Delete it by user Matko
     * Try to get deleted comment
     */
    @Test
    public void postComments() {
        var adminToken = MTHelper.getAdminToken(client);
        var noviSadId = addCityOfNoviSad(adminToken);
        var matkoToken = signupAndLoginUser("matko", "matkopass", client);
        var sandraToken = signupAndLoginUser("sandra", "sandrapass", client);
        // Post comment by user Matko
        var commentId = postComment(noviSadId, "Matkos comment", matkoToken);
        // Try to delete it by user Sandra
        var deleteCommentRequest = HttpRequest.DELETE("/city/comment/" + commentId.toString());
        deleteCommentRequest.bearerAuth(sandraToken);
        var success = client.toBlocking().exchange(deleteCommentRequest, Boolean.class).body();
        assertFalse(success);
        // Get posted comment
        var getCityRequest = HttpRequest.GET("city/" + noviSadId.toString());
        getCityRequest.bearerAuth(sandraToken);
        var city = client.toBlocking().exchange(getCityRequest, CityResponse.class).body();
        assertEquals(1, city.getComments().size());
        assertEquals("Matkos comment", city.getComments().get(0).getText());
        assertEquals("matko", city.getComments().get(0).getPoster());
        // Delete it by user Matko
        deleteCommentRequest = HttpRequest.DELETE("/city/comment/" + commentId.toString());
        deleteCommentRequest.bearerAuth(matkoToken);
        success = client.toBlocking().exchange(deleteCommentRequest, Boolean.class).body();
        assertTrue(success);
        // Try to get deleted comment
        getCityRequest = HttpRequest.GET("city/" + noviSadId.toString());
        getCityRequest.bearerAuth(sandraToken);
        city = client.toBlocking().exchange(getCityRequest, CityResponse.class).body();
        assertNull(city.getComments());
    }

    @Test
    public void testMultipleComments() {
        var adminToken = MTHelper.getAdminToken(client);
        var noviSadId = addCityOfNoviSad(adminToken);
        var userToken = signupAndLoginUser("matko", "matkopass", client);
        // Post 5 comments
        var comment0Id = postComment(noviSadId,"comment 0", userToken);
        var comment1Id = postComment(noviSadId,"comment 1", userToken);
        var comment2Id = postComment(noviSadId,"comment 2", userToken);
        var comment3Id = postComment(noviSadId,"comment 3", userToken);
        var comment4Id = postComment(noviSadId,"comment 4", userToken);
        // Get all comments
        var getCityRequest = HttpRequest.GET("/city/" + noviSadId.toString());
        getCityRequest.bearerAuth(userToken);
        var city = client.toBlocking().exchange(getCityRequest, CityResponse.class).body();
        assertEquals(5, city.getComments().size());
        assertEquals(comment4Id.longValue(), city.getComments().get(0).getId().longValue());
        assertEquals(comment3Id.longValue(), city.getComments().get(1).getId().longValue());
        assertEquals(comment2Id.longValue(), city.getComments().get(2).getId().longValue());
        assertEquals(comment1Id.longValue(), city.getComments().get(3).getId().longValue());
        assertEquals(comment0Id.longValue(), city.getComments().get(4).getId().longValue());
        // Get 3 latest comments
        getCityRequest = HttpRequest.GET("/city/" + noviSadId.toString() + "?max-comments=3");
        getCityRequest.bearerAuth(userToken);
        city = client.toBlocking().exchange(getCityRequest, CityResponse.class).body();
        assertEquals(3, city.getComments().size());
        assertEquals(comment4Id.longValue(), city.getComments().get(0).getId().longValue());
        assertEquals(comment3Id.longValue(), city.getComments().get(1).getId().longValue());
        assertEquals(comment2Id.longValue(), city.getComments().get(2).getId().longValue());
    }

    private String signupAndLoginUser(String username, String password, RxHttpClient client) {
        assertTrue(MTHelper.signup(username, password, client));
        return MTHelper.login(username, password, client);
    }

    private Long addCityOfNoviSad(String adminToken) {
        var addCityPayload = WriteCityRequest.builder()
                .name("Novi Sad")
                .country("Srbija")
                .build();
        var addCityRequest = HttpRequest.POST("/city", addCityPayload);
        addCityRequest.bearerAuth(adminToken);
        return client.toBlocking().exchange(addCityRequest, CityResponse.class).body().getId();
    }

    private Long postComment(Long cityId, String comment, String token) {
        var commentPayload = WriteCommentRequest.builder()
                .cityId(cityId)
                .text(comment)
                .build();
        var postCommentRequest = HttpRequest.POST("/city/comment", commentPayload);
        postCommentRequest.bearerAuth(token);
        return client.toBlocking().exchange(postCommentRequest, CommentResponse.class).body().getId();
    }
}
