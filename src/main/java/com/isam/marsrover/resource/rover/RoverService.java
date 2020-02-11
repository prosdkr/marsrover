package com.isam.marsrover.resource.rover;

import com.isam.marsrover.NasaClient;
import com.isam.marsrover.entities.Rover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoverService {

    private final NasaClient nasaClient;

    @Autowired
    public RoverService(NasaClient nasaClient) {
        this.nasaClient = nasaClient;
    }

    public List<Rover> getRovers() {
        return nasaClient.getRovers();
    }
}
