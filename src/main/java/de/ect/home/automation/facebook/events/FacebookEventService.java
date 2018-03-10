package de.ect.home.automation.facebook.events;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Event;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Group;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import de.ect.home.automation.facebook.events.util.FacebookEventList;

@Component
public class FacebookEventService {
	
	private static final String GROUP_SEPARATOR = ",";
	
	/**
	 * comma separated list of facebook groups to fetch events from
	 */
	@Value("${facebook.group.ids}")
	private String facebookGroupIds;
	
	public List<Event> getFacebookEvents(Facebook facebook) {
		if (StringUtils.isEmpty(this.facebookGroupIds)) {
			return new ArrayList<>();
		}
		
		String[] splittedIds = this.facebookGroupIds.split(GROUP_SEPARATOR);
		List<Event> eventList = new ArrayList<>();
		
		for (String groupId : splittedIds) {
			Group group = facebook.groupOperations().getGroup(groupId);
	        FacebookEventList events = facebook.restOperations().getForObject("https://graph.facebook.com/v2.8/" + group.getId() + "/events", FacebookEventList.class);
	        
	        eventList.addAll(events.getEvents());
		}
        
        return eventList;
    }

}
