package com.isam.marsrover.resource.photo;

import com.isam.marsrover.entities.Photo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static com.isam.marsrover.MarsRoverConstants.*;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@CrossOrigin
@RestController
@RequestMapping(REST_V1 + ROVER_ENDPOINT)
public class PhotoCrud {

    private Logger logger = LoggerFactory.getLogger(PhotoCrud.class.getName());
    private Marker marker = MarkerFactory.getMarker(PhotoCrud.class.getName());

    private final PhotoService photoService;

    @Autowired
    public PhotoCrud(PhotoService photoService) {
        this.photoService = photoService;
    }


    @RequestMapping(value = "/{name}" + PHOTOS_ENDPOINT, method = RequestMethod.GET)
    public List<Photo> getRoverPhotos(@PathVariable String name, @RequestParam(EARTH_DATE_PARAM) String date) {
        return photoService.getPhotoList(name, date);
    }

    @RequestMapping(value = "/{name}" + PHOTOS_FOR_DATES_ENDPOINT, method = RequestMethod.GET)
    public List<Photo> getRoverPhotos(@PathVariable String name) {
        return photoService.getAllPhotosForGivenDatesAndName(name);
    }

    @RequestMapping(value = PHOTOS_ENDPOINT, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImgUrl(@RequestParam(IMAGE_SOURCE_PARAM) String imgSrc) {
        if (imgSrc == null || imgSrc.isEmpty()) {
            return ResponseEntity.badRequest().build(); //Bad request, no image specified. Scenario on this?
        }
        File file = photoService.getPhoto(imgSrc);
        if (file == null) {
            //Image URL was provided, but could not be downloaded for some unknown (but logged "hopefully")reason.
            return ResponseEntity.notFound().build();
        }
        try {
            return ResponseEntity.ok()
                                 .contentType(MediaType.IMAGE_JPEG)
                                 .body(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            logger.error(marker, "Error occurred while trying to serve file to client: {}", e.getMessage());
        }
        return ResponseEntity.status(SERVICE_UNAVAILABLE).build();
    }
}
