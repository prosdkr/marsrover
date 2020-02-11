package com.isam.marsrover;

public class TestConstants {

    public static final String PROTOCOL = "http";
    public static final String HOST = "localhost";
    public static final String HOST_URL = PROTOCOL + "://" + HOST + ":";

    //I chose to create a webapp-context, so I'm using that here.
    public static final String WEBAPP_CONTEXT = "/marsrover";

    public static final String[] DATES_TO_CHECK = {"2017-02-27","2018-06-02","2016-07-13"};
    public static final String ROVER_CURIOSITY = "curiosity";
    public static final String SLASH = "/";
    public static final String QUERY = "?";
    public static final String EQUALS = "=";

    public static final String TEST_IMAGE_1 = "http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01004/opgs/edr/fcam/FLB_486615455EDR_F0481570FHAZ00323M_.JPG";
    public static final String TEST_IMAGE_2 = "http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01004/opgs/edr/rcam/RLB_486615482EDR_F0481570RHAZ00323M_.JPG";
    public static final Integer TEST_IMAGE_1_SIZE = 747138;
    public static final Integer TEST_IMAGE_2_SIZE = 700599;
}
