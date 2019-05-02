package com.example.feeded.client;

import com.example.feeded.element.Credentials;
import com.example.feeded.element.UserSession;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientErrorHandling;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Rest(rootUrl = "http://192.168.0.12:80", converters = {MappingJackson2HttpMessageConverter.class})
public interface AuthenticationClient extends RestClientErrorHandling {

    @Post("/auth/register")
    ResponseEntity register(@Body final Credentials credentials);

    @Post("/auth/signin")
    ResponseEntity<UserSession> signin(@Body final Credentials credentials);

}