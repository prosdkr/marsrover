package com.isam.marsrover;

public class MarsRoverConstants {
    public static final String IMAGE_CACHE_PATH = "/tmp/";
    public static final String DATES_FILE = "/dates.txt";

    public static final String REST_V1 = "/rest/v1";

    public static final String PING_ENDPOINT = "/ping";
    public static final String DATE_ENDPOINT = "/dates";
    public static final String ROVER_ENDPOINT = "/rovers";
    public static final String PHOTOS_ENDPOINT = "/photos";
    public static final String PHOTOS_FOR_DATES_ENDPOINT = "/photos-for-dates";
    public static final String EARTH_DATE_PARAM = "earth_date";
    public static final String IMAGE_SOURCE_PARAM = "img_src";

    public static final String[] ALLOWED_DATE_FORMATS = {
            "MM/dd/yy",
            "MMM d, yyyy",
            "MMM-d-yyyy"
    };
}
