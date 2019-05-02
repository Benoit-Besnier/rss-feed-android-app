package com.example.feeded.client;

import com.example.feeded.element.UserDetails;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.RequiresAuthentication;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientErrorHandling;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Rest(rootUrl = "http://192.168.0.12:80", converters = {MappingJackson2HttpMessageConverter.class})
public interface UserClient extends RestClientErrorHandling {

    @Get("/users/me")
    @RequiresAuthentication
    UserDetails getPersonalInformation();

    void setBearerAuth(String token);

}
