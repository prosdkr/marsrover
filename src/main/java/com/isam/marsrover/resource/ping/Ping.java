package com.isam.marsrover.resource.ping;

import java.time.LocalDateTime;

import static java.lang.System.currentTimeMillis;

public class Ping {

    private long timestamp;
    private LocalDateTime dateTime;

    public Ping() {
        this.timestamp = currentTimeMillis();
        this.dateTime = LocalDateTime.now();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
