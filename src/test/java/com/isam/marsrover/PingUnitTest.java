package com.isam.marsrover;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.isam.marsrover.resource.ping.Ping;
import com.isam.marsrover.resource.ping.PingCrud;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class PingUnitTest {

    @InjectMocks
    private PingCrud pingCrud;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetPing() {
        Ping ping = pingCrud.getPing();
        assertThat(ping, is(notNullValue()));
    }
}