package com.isam.marsrover.resource.dates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.isam.marsrover.MarsRoverConstants.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin
@RestController
@RequestMapping(REST_V1 + DATE_ENDPOINT)
public class DateCrud {

    private final DateService dateService;

    @Autowired
    public DateCrud(DateService dateService) {
        this.dateService = dateService;
    }

    @RequestMapping(method = GET)
    public List<String> getDates() {
        return dateService.getFormattedDates();
    }
}
