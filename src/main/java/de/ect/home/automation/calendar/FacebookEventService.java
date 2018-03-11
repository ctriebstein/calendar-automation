package de.ect.home.automation.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Event;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Group;
import org.springframework.stereotype.Component;

import de.ect.home.automation.calendar.util.FacebookEventList;

/**
 * component responsible for fetching event data from facebook groups. For API Documentation
 * see <a href="https://docs.spring.io/spring-social-facebook/docs/current/reference/htmlsingle/#introduction">https://docs.spring.io/spring-social-facebook</a>
 * and <a href="https://developers.facebook.com/docs/graph-api/reference/page/events/">https://developers.facebook.com/docs/graph-api/</a>
 * 
 * @author ctr
 */
@Component
public class FacebookEventService {
	
	/**
	 * this is ugly but the facebook api for group events does not contain a decent
	 * filter mechanism for until/since (unlike other facebook endpoints). Therefore
	 * this service will retrieve the MAX_EVENTS latest entries and reduce the list to the
	 * requested month (and year) later on
	 */
	private static final int MAX_EVENTS = 1000;
	
	/**
	 * comma separated list of facebook groups to fetch events from
	 */
	@Value("${facebook.group.ids}")
	private String[] facebookGroupIds;
	
	/**
	 * retrieves all events for all given groups in the month of the year provided with the calendar instance
	 * @param facebook the facebook connection object
	 * @param monthToDisplay the month (and year) to filter events for
	 * @return a list of events from facebook groups
	 */
	public List<Event> getFacebookEvents(Facebook facebook, Calendar monthToDisplay) {
		if (this.facebookGroupIds == null) {
			return new ArrayList<>();
		}
		
		List<Event> eventList = new ArrayList<>();
		
		for (String groupId : this.facebookGroupIds) {
			Group group = facebook.groupOperations().getGroup(groupId);
	        FacebookEventList events = facebook.restOperations().getForObject("https://graph.facebook.com/v2.8/" + group.getId() + 
	        		"/events?limit=" + MAX_EVENTS, FacebookEventList.class);
	        
	        eventList.addAll(events.getEvents());
		}
        
        return reduceAndRevertList(eventList, monthToDisplay);
    }

	/**
	 * reduces the list of all events from facebook groups to the given month (And year)
	 * and sorts the list by event dates ascending
	 * 
	 * @param eventList a list of all events from the given facebook groups regardless their
	 *                  start dates
	 * @param monthToDisplay a calendar object determining the selected month (And year) by
	 * 						 the user
	 * @return a sublist of the given list sorted by event dates ascending and limited
	 * 		   to the selected month (and year). Empty list when nothing found.
	 */
	private List<Event> reduceAndRevertList(List<Event> eventList, Calendar monthToDisplay) {
		List<Event> filteredAndReversedEvents = new ArrayList<>();
		
		if (eventList != null) {
			for (Event event : eventList) {
				Calendar c = Calendar.getInstance();
				c.setTime(event.getStartTime());
				
				if (c.get(Calendar.MONTH) == monthToDisplay.get(Calendar.MONTH) &&
						c.get(Calendar.YEAR) == monthToDisplay.get(Calendar.YEAR)) {
					filteredAndReversedEvents.add(event);
				}
			}
			
			// sort the list
			filteredAndReversedEvents.sort((Event o1, Event o2) -> {
		    	if(o1.getStartTime().equals(o2.getStartTime())) {
		    		return 0;
		    	}
		        return o1.getStartTime().before(o2.getStartTime()) ? -1 : 1;
			});
		}
		
		return filteredAndReversedEvents;
	}
}
