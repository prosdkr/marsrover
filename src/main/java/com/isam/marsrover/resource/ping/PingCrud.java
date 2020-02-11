package com.isam.marsrover.resource.ping;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.isam.marsrover.MarsRoverConstants.PING_ENDPOINT;
import static com.isam.marsrover.MarsRoverConstants.REST_V1;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin
@RestController
@RequestMapping(REST_V1 + PING_ENDPOINT)
public class PingCrud {

    @RequestMapping(method = GET)
    public Ping getPing() {
        return new Ping();
    }
}
