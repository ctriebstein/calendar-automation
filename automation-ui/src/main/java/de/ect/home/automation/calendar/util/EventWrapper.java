package de.ect.home.automation.calendar.util;

import java.util.Date;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

/**
 * wrapper class containing event information
 * 
 * @author ctr
 */
public class EventWrapper {

	private String id;
	private String summary;
	private String description;
	private Date startDate;
	private Date endDate;
	private boolean fullDayEvent = false;

	public EventWrapper(Event googleCalendarEvent) {
		if (googleCalendarEvent == null) {
			return;
		}

		this.id = googleCalendarEvent.getId();
		this.summary = googleCalendarEvent.getSummary();
		this.description = googleCalendarEvent.getDescription();

		EventDateTime start = googleCalendarEvent.getStart();
		EventDateTime end = googleCalendarEvent.getEnd();

		// parse google event structure
		if (start != null) {
			if (start.getDateTime() != null) {
				this.startDate = new Date(start.getDateTime().getValue());
			} else {
				this.startDate = new Date(start.getDate().getValue());
				this.fullDayEvent = true;
			}
		}

		if (end != null) {
			if (end.getDateTime() != null) {
				this.endDate = new Date(end.getDateTime().getValue());
			} else {
				this.endDate = new Date(end.getDate().getValue());
				this.fullDayEvent = true;
			}
			if (this.startDate == null) {
				this.startDate = endDate;
			}
		}
	}

	public EventWrapper(String summary, long startDate) {
		this(summary, new Date(startDate));
	}

	public EventWrapper(String summary, Date startDate) {
		this(summary, null, startDate, null);
	}

	public EventWrapper(String summary, String description, long startDate,
			long endDate) {
		this(summary, description, new Date(startDate), new Date(endDate));
	}

	public EventWrapper(String summary, String description, Date startDate,
			Date endDate) {
		this.summary = summary;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getId() {
		return id;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isFullDayEvent() {
		return fullDayEvent;
	}

	public void setFullDayEvent(boolean fullDayEvent) {
		this.fullDayEvent = fullDayEvent;
	}
}
