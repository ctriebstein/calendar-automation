package de.ect.home.automation.calendar;

import static org.junit.Assert.*;
import com.google.api.services.calendar.model.Event;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

public class GoogleCalendarEventServiceTest {

    /**
     * NOTE:
     * this test will create a test entry in your actual google calendar and will delete it afterwards
     */
    @Test
    public void canAddCalendarEntry() throws IOException  {
        GoogleCalendarEventService gces = new GoogleCalendarEventService();

        Event event = gces.addCalendarEntry("a test entry", "nothing much - just a test", new Date(), new Date());

        assertNotNull(event);

        gces.removeCalendarEntry(event.getId());
    }
}
