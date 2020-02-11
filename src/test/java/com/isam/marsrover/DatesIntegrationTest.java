package com.isam.marsrover;

import static com.isam.marsrover.MarsRoverConstants.DATE_ENDPOINT;
import static com.isam.marsrover.MarsRoverConstants.REST_V1;
import static com.isam.marsrover.TestConstants.HOST_URL;
import static com.isam.marsrover.TestConstants.WEBAPP_CONTEXT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DatesIntegrationTest {

    public static String baseUrl;

    @LocalServerPort
    private int port;

    @Inject
    private TestRestTemplate restTemplate;

    @PostConstruct
    public void setup() {
        baseUrl = HOST_URL +  port + WEBAPP_CONTEXT;
    }

    @Test
    public void testGetDates() throws IOException {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + REST_V1 + DATE_ENDPOINT, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response.getBody());

        assertThat(responseJson.isMissingNode(), is(false));
        assertThat(responseJson.toString(), equalTo("[\"2017-02-27\",\"2018-06-02\",\"2016-07-13\"]"));
    }


}