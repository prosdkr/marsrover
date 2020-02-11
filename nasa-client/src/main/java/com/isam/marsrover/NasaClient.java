package com.isam.marsrover;

import com.isam.marsrover.entities.Photo;
import com.isam.marsrover.entities.PhotosList;
import com.isam.marsrover.entities.Rover;
import com.isam.marsrover.entities.RoversList;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.logging.LoggingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Feature;
import java.io.InputStream;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.isam.marsrover.NasaClientConstants.*;
import static java.util.logging.Logger.getLogger;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.glassfish.jersey.logging.LoggingFeature.Verbosity.PAYLOAD_ANY;

@Component
public class NasaClient {

    private final Logger logger = LoggerFactory.getLogger(NasaClient.class.getName());
    private final Marker marker = MarkerFactory.getMarker(NasaClient.class.getName());

    private final Feature feature = new LoggingFeature(getLogger(getClass().getName()),
                                                       PAYLOAD_ANY);
    private final JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider().configure(FAIL_ON_UNKNOWN_PROPERTIES,
                                                                                              false);

    private Client client = ClientBuilder.newClient(new ClientConfig(jacksonJsonProvider)).register(feature);

    public List<Rover> getRovers() {
        try {
            return client.target(REST_URL + NASA_ROVERS_ENDPOINT)
                    .queryParam(API_KEY_PARAM_NAME, NASA_API_KEY)
                    .request(APPLICATION_JSON)
                    .get(RoversList.class).getRovers();
        }
        catch (Exception e) {
            logger.error(marker, "Error occurred while trying to get rover data: {}", e.getMessage());
        }
        return null;
    }

    public List<Photo> getPhotoList(String name, String date) {
        String url = REST_URL + NASA_ROVERS_ENDPOINT + name + NASA_ROVER_PHOTOS_ENDPOINT;
        try {
            return client.target(url)
                    .queryParam(EARTH_DATE_PARAM_NAME, date)
                    .queryParam(API_KEY_PARAM_NAME, NASA_API_KEY)
                    .request(APPLICATION_JSON)
                    .get(PhotosList.class).getPhotos();
        } catch (Exception e) {
            logger.error(marker, "Error occurred while trying to get photo list data: {}", e.getMessage());
        }
        return null;
    }

    public InputStream getPhoto(String url) {
        try {
            return client.target(url)
                         .queryParam(API_KEY_PARAM_NAME, NASA_API_KEY)
                         .request()
                         .get(InputStream.class);
        } catch (Exception e) {
            logger.error(marker, "Error occurred while trying to retrieve photo \"{}\" from NASA: {}", url, e.getMessage());
        }
        return null;
    }

}
