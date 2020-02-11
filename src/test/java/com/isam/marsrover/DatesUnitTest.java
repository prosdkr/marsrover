package com.isam.marsrover;

import static com.isam.marsrover.TestConstants.DATES_TO_CHECK;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import javax.inject.Inject;

import com.isam.marsrover.resource.dates.DateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DatesUnitTest {

    @Inject
    DateService dateService;

    @Test
    public void getDatesTest() {
        assertThat(dateService.getFormattedDates(), containsInAnyOrder(DATES_TO_CHECK));
    }
}