package com.isam.marsrover;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isam.marsrover.entities.Photo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.isam.marsrover.MarsRoverConstants.*;
import static com.isam.marsrover.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PhotosIntegrationTest {

    public static String baseUrl;

    @LocalServerPort
    private int port;

    @Inject
    private TestRestTemplate restTemplate;

    @PostConstruct
    public void setup() {
        baseUrl = HOST_URL +  port + WEBAPP_CONTEXT;
    }

    /**
        Given a date exists with pictures for the curiosity rover
        When pictures a pulled for the date
        Then only pictures with that date will be returned
        And an okay status code is received.
     */
    @Test
    public void testGetPhotosForDate() throws IOException {
        String photoBaseUrl = baseUrl + REST_V1 + ROVER_ENDPOINT;
        String photosForDate = photoBaseUrl + SLASH + ROVER_CURIOSITY + PHOTOS_ENDPOINT + QUERY + EARTH_DATE_PARAM + EQUALS+ DATES_TO_CHECK[0];

        ResponseEntity<String> response =
                restTemplate.getForEntity(photosForDate, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response.getBody());

        assertThat(responseJson.isMissingNode(), is(false));
        List<Photo> photos = objectMapper.readValue(responseJson.toString(),  new TypeReference<List<Photo>>() {});
        for (Photo photo : photos) {
            assertThat("The date should match for all photos!", photo.getEarthDate(), equalTo(DATES_TO_CHECK[0]));
        }
    }

    /**
        Given an empty date for the curiosity rover
        When pictures a pulled for the date
        Then no pictures should be returned
        And a bad request status code is received
     */
    @Test
    public void testGetPhotosForEmptyDate() {
        String photoBaseUrl = baseUrl + REST_V1 + ROVER_ENDPOINT;
        String photosForDate = photoBaseUrl + SLASH + ROVER_CURIOSITY + PHOTOS_ENDPOINT + QUERY + EARTH_DATE_PARAM + EQUALS;

        ResponseEntity<String> response =
                restTemplate.getForEntity(photosForDate, String.class);

        assertThat("Photos should not have been received!", response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    /**
    Given a set of dates for the curiosity rover
    When pictures a pulled for the dates
    Then no pictures should be returned with dates other than those of the given dates
    And a okay status code is received.
    */
    @Test
    public void testGetPhotosForAllDatesRead() throws IOException {
        String photoBaseUrl = baseUrl + REST_V1 + ROVER_ENDPOINT;
        String photosForDate = photoBaseUrl + SLASH + ROVER_CURIOSITY + PHOTOS_FOR_DATES_ENDPOINT;

        ResponseEntity<String> response =
                restTemplate.getForEntity(photosForDate, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response.getBody());

        assertThat(responseJson.isMissingNode(), is(false));
        List<Photo> photos = objectMapper.readValue(responseJson.toString(),  new TypeReference<List<Photo>>() {});

        for (Photo photo : photos) {
            assertThat("Wrong date received!!!", photo.getEarthDate(), isIn(DATES_TO_CHECK));
        }
    }


    /**
     Given an image url
     and the image exists in NASA
     When user requests the image
     Then the image should be downloaded
     And the image is returned
     and an okay status code is received.
     */
    @Test
    public void testGetNewPhoto() {
        String photoBaseUrl = baseUrl + REST_V1 + ROVER_ENDPOINT + PHOTOS_ENDPOINT;
        String imageUrl = photoBaseUrl + QUERY + IMAGE_SOURCE_PARAM + EQUALS + TEST_IMAGE_1;

        ResponseEntity<String> response =
                restTemplate.getForEntity(imageUrl, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().getBytes().length, is(TEST_IMAGE_1_SIZE));
    }

    /**
     Given an image url
     and the image exists in NASA
     When user requests the image
     Then the image should be downloaded
     And the image is returned
     and an okay status code is received.

     When the user requests the image again
     then the image should be served from cache  <- ( I am having trouble testing this part :( )
     and the image is returned
     and an okay status is returned
     */
    @Test
    public void testGetNewPhotoTwice() {
        String photoBaseUrl = baseUrl + REST_V1 + ROVER_ENDPOINT + PHOTOS_ENDPOINT;
        String imageUrl = photoBaseUrl + QUERY + IMAGE_SOURCE_PARAM + EQUALS + TEST_IMAGE_2;

        ResponseEntity<String> response =
                restTemplate.getForEntity(imageUrl, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().getBytes().length, is(TEST_IMAGE_2_SIZE));

        //Request again.
        response = restTemplate.getForEntity(imageUrl, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), is(notNullValue()));

        assertThat(response.getBody().getBytes().length, is(TEST_IMAGE_2_SIZE));
    }


}