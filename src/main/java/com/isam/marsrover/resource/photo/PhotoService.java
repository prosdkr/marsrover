package com.isam.marsrover.resource.photo;

import com.isam.marsrover.NasaClient;
import com.isam.marsrover.entities.Photo;
import com.isam.marsrover.resource.dates.DateService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.isam.marsrover.MarsRoverConstants.IMAGE_CACHE_PATH;

@Service
public class PhotoService {

    private final Logger logger = LoggerFactory.getLogger(PhotoService.class.getName());
    private final Marker marker = MarkerFactory.getMarker(PhotoService.class.getName());

    private final NasaClient nasaClient;
    private final DateService dateService;

    @Autowired
    public PhotoService(NasaClient nasaClient, DateService dateService) {
        this.nasaClient = nasaClient;
        this.dateService = dateService;
    }

    public List<Photo> getPhotoList(String name, String date) {
        return nasaClient.getPhotoList(name, date);
    }

    public List<Photo> getAllPhotosForGivenDatesAndName(String name) {
        List<Photo> photos = new ArrayList<>();
        List<String> dates = dateService.getFormattedDates();
        if (dates == null) {
            return photos;
        }
        for (String date : dates) {
            List<Photo> datePhotos = getPhotoList(name, date);
            if (datePhotos != null) {
                photos.addAll(datePhotos);
            }
        }
        return photos;
    }

    public File getPhoto(String url) {
        final String sha = DigestUtils.sha512Hex(url);
        final String imageFileName = IMAGE_CACHE_PATH + sha;

        if (fileExists(imageFileName)) {
            logger.debug(marker, "File {} already exists. Pulling from cache.", imageFileName);
            return readExistingImage(imageFileName);
        } else {
            return downloadAndReturnNewImage(url, imageFileName);
        }
    }

    private File readExistingImage(String imageFileName) {
        try {
            return Paths.get(imageFileName).toFile();
        } catch(InvalidPathException ipe) {
            logger.error(marker, "Tried to read a validated file. " +
                                 "Was the file {} deleted immediately upon validation?: {}",
                                 imageFileName, ipe.getMessage());
        }
        return null;
    }

    private File downloadAndReturnNewImage(String url, String imageFileName) {
        InputStream inputStream = nasaClient.getPhoto(url);
        try {
            if (inputStream == null) {
                logger.error(marker, "Image could not be pulled from target, is it the right URL?");
                throw new Exception();
            }
            Path filePath = createFile(imageFileName);
            saveFile(inputStream, filePath);
            return filePath.toFile();
        } catch (Exception e) {
            logger.error(marker, "Exception occurred during new image retrieval process.");
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return null;
    }

    public Path createFile(String fileName) throws IOException {
        try {
           return Files.createFile(Paths.get(fileName));
        } catch (IOException | SecurityException e) {
            logger.error(marker, "Something went wrong creating the file. Do you have read/write access to the cache?"
                                 + e.getMessage());
            throw e;
        }
    }

    public void saveFile(InputStream toSave, Path filePath) throws IOException {
        try {
            Files.copy(toSave, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | SecurityException e) {
            logger.error(marker, "Error occurred when attempting to save data to file in cache at {} : {}",
                                 filePath, e.getMessage());
            throw e;
        }
    }

    private boolean fileExists(String imageFileName) {
        try {
            return Files.exists(Paths.get(imageFileName));
        } catch (SecurityException se) {
            logger.error(marker, "Could not check the cache for file's existence at {} " +
                                 "Do you have read/write access to the cache?: {}", imageFileName, se.getMessage());
        }
        return false;
    }

}
