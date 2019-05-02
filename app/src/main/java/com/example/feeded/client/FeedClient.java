package com.example.feeded.client;

import com.example.feeded.element.Feed;
import com.example.feeded.element.FeedSubmission;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.RequiresAuthentication;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientErrorHandling;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

@Rest(rootUrl = "http://192.168.0.12:80", converters = {MappingJackson2HttpMessageConverter.class})
public interface FeedClient extends RestClientErrorHandling {

    @Get("/feeds")
    List<Feed> getAllFeeds();

    @Get("/feeds/{id}")
    @RequiresAuthentication
    Feed getFeedById(@Path final String id);

    @Post("/feeds")
    @RequiresAuthentication
    ResponseEntity postNewFeed(@Body final FeedSubmission newFeed);

    void setBearerAuth(String token);

}
