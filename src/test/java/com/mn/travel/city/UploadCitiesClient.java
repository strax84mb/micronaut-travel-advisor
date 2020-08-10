package com.mn.travel.city;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.multipart.MultipartBody;

//@Client("/city")
public interface UploadCitiesClient {

    @Post(value = "/import", produces = MediaType.MULTIPART_FORM_DATA)
    HttpResponse upload(@Body MultipartBody multipartBody);
}
