package de.ect.home.automation.calendar;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import de.ect.home.automation.calendar.util.EventWrapper;

/**
 * provides all operations required to fetch data from a google calendar
 * 
 * @author ctr
 */
public class GoogleCalendarEventService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(GoogleCalendarEventService.class);

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(
			System.getProperty("user.home"),
			".credentials/calendar-java-quickstart");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory
			.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/calendar-java-quickstart
	 */
	private static final List<String> SCOPES = Arrays
			.asList(CalendarScopes.CALENDAR_READONLY);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Exception e) {
			LOGGER.error("Unable to create datastore", e);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	private Credential authorize() throws IOException {
		// Load client secrets.
		InputStream in = new FileInputStream(new ClassPathResource(
				"/google-calendar/client_secret.json").getFile());

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
				JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(DATA_STORE_FACTORY)
				.setAccessType("offline").build();
		return new AuthorizationCodeInstalledApp(flow,
				new LocalServerReceiver()).authorize("user");
	}

	/**
	 * Build and return an authorized Calendar client service.
	 * 
	 * @return an authorized Calendar client service
	 * @throws IOException
	 */
	private com.google.api.services.calendar.Calendar getCalendarService()
			throws IOException {
		Credential credential = authorize();
		return new com.google.api.services.calendar.Calendar.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, credential).build();
	}

	/**
	 * return all events in the given month.
	 * 
	 * @param month
	 *            the (0 based) month to retrieve all events for
	 * @param year
	 *            the year all the events should be retrieved for
	 * @return a list of all events in the given month and year
	 * @throws IOException
	 */
	public List<EventWrapper> getCalendarEntries(int month, int year)
			throws IOException {
		com.google.api.services.calendar.Calendar service = getCalendarService();
		List<EventWrapper> eventList = new ArrayList<>();

		// List the next 10 events from the primary calendar.
		Calendar startOfMonth = Calendar.getInstance();
		Calendar endOfMonth = Calendar.getInstance();

		startOfMonth.set(Calendar.YEAR, year);
		startOfMonth.set(Calendar.MONTH, month);
		startOfMonth.set(Calendar.DAY_OF_MONTH, 1);

		endOfMonth.set(Calendar.YEAR, year);
		endOfMonth.set(Calendar.MONTH, month);
		endOfMonth.set(Calendar.DAY_OF_MONTH,
				endOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
		Events events = service.events().list("primary")
				.setTimeMin(new DateTime(startOfMonth.getTime()))
				.setTimeMax(new DateTime(endOfMonth.getTime()))
				.setOrderBy("startTime").setSingleEvents(true).execute();
		List<Event> items = events.getItems();

		if (items != null) {
			for (Event item : items) {
				eventList.add(new EventWrapper(item));
			}
		}

		return eventList;
	}
}
