package com.example.feeded.client;

import com.example.feeded.element.custom.PreferredFeedSubmission;
import com.example.feeded.element.custom.UserDetails;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Put;
import org.androidannotations.rest.spring.annotations.RequiresAuthentication;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientErrorHandling;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Rest(rootUrl = "http://192.168.0.12:80", converters = {MappingJackson2HttpMessageConverter.class})
public interface UserClient extends RestClientErrorHandling {

    @Get("/users/me")
    @RequiresAuthentication
    UserDetails getPersonalInformation();

    @Put("/users/me/feeds")
    @RequiresAuthentication
    ResponseEntity putPreferredFeeds(@Body final PreferredFeedSubmission preferredFeedSubmission);

    void setBearerAuth(String token);

}
