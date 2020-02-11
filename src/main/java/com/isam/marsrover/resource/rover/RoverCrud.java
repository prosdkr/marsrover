package com.isam.marsrover.resource.rover;

import com.isam.marsrover.entities.Rover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

import static com.isam.marsrover.MarsRoverConstants.REST_V1;
import static com.isam.marsrover.MarsRoverConstants.ROVER_ENDPOINT;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin
@RestController
@RequestMapping(REST_V1 + ROVER_ENDPOINT)
public class RoverCrud {

    private final RoverService roverService;

    @Autowired
    public RoverCrud(RoverService roverService) {
        this.roverService = roverService;
    }

    @RequestMapping(method = GET)
    public List<Rover> getRovers() {
        return roverService.getRovers();
    }
}
