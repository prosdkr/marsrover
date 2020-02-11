package com.isam.marsrover;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isam.marsrover.entities.Photo;
import com.isam.marsrover.entities.Rover;
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
import java.io.IOException;
import java.util.List;

import static com.isam.marsrover.MarsRoverConstants.*;
import static com.isam.marsrover.TestConstants.*;
import static com.isam.marsrover.TestConstants.DATES_TO_CHECK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoverIntegrationTest {

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
     When a user requests a list of rovers
     Then all rovers shall be returned
     And an okay status code is received.
     */
    @Test
    public void testGetRovers() throws IOException {
        String roverBaseUrl = baseUrl + REST_V1 + ROVER_ENDPOINT;

        ResponseEntity<String> response =
                restTemplate.getForEntity(roverBaseUrl, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response.getBody());

        assertThat(responseJson.isMissingNode(), is(false));
        List<Rover> rovers = objectMapper.readValue(responseJson.toString(),  new TypeReference<List<Rover>>() {});
        for (Rover rover : rovers) {
            if (rover.getName().equalsIgnoreCase(ROVER_CURIOSITY)) {
                return;
            }
        }
        fail("The rover data didn't contain an expected value!");
    }


}