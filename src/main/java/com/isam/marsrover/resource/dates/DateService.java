package com.isam.marsrover.resource.dates;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static com.isam.marsrover.MarsRoverConstants.ALLOWED_DATE_FORMATS;
import static com.isam.marsrover.MarsRoverConstants.DATES_FILE;

@Service
public class DateService {
    private final Logger logger = LoggerFactory.getLogger(DateService.class.getName());
    private final Marker marker = MarkerFactory.getMarker(DateService.class.getName());

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private List<String> dateList = new ArrayList<>();


    public List<String> getFormattedDates() {
        return dateList;
    }

    @PostConstruct
    public void initializeDates() {
        String fileName = DATES_FILE;

        try (InputStream inputStream = getClass().getResourceAsStream(fileName);
             Stream<String> stream = new BufferedReader(new InputStreamReader(inputStream)).lines()) {

            stream.forEach(date -> {
                logger.trace(marker, "Raw date from file: {}", date);
                String formattedDate = formatDate(date);
                if (formattedDate != null) {
                    dateList.add(formattedDate);
                }
            });

        } catch (Exception e) {
            logger.error("Error reading file {}: {}", fileName, e.getMessage());
        }
    }

    public String formatDate(String date) {
        Date parsedDate;
        try {
            parsedDate = DateUtils.parseDateStrictly(date, ALLOWED_DATE_FORMATS);
            return formatter.format(parsedDate);
        } catch (ParseException e) {
            logger.error(marker, "{} is an invalid date.: {}", date, e.getMessage());
            return null;
        }
    }
}
