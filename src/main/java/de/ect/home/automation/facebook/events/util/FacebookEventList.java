package de.ect.home.automation.facebook.events.util;

import java.util.List;

import org.springframework.social.facebook.api.Event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * wrapper class containing facebook events
 * @author ctr
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookEventList {
	
	@JsonProperty(value="data")
	private List<Event> events;
	
	public FacebookEventList() {
		// default constructor for jackson mapping
	}

	/**
	 * @return the events
	 */
	public List<Event> getEvents() {
		return events;
	}

	/**
	 * @param events the events to set
	 */
	public void setEvents(List<Event> events) {
		this.events = events;
	}
}
