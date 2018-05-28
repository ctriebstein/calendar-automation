package de.ect.home.automation.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.ect.home.automation.calendar.FacebookEventService;
import de.ect.home.automation.calendar.GoogleCalendarEventService;
import de.ect.home.automation.calendar.util.EventWrapper;

@Controller
public class HomeController {

	private Facebook facebook;
	private ConnectionRepository connectionRepository;

	@Autowired
	private FacebookEventService facebookEventService;

	@Value("${spring.social.facebook.app-id}")
	private String facebookAppId;

	public HomeController(Facebook facebook, ConnectionRepository connectionRepository) {
		this.facebook = facebook;
		this.connectionRepository = connectionRepository;
	}

	@GetMapping("/")
	public String homePage(Model model) throws IOException {
		// get events from google calendar
		Calendar selectedMonth = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		
		model.addAttribute("selectedMonth", df.format(selectedMonth.getTime()));		
		model.addAttribute("events", getCalendarEntries(df.format(selectedMonth.getTime())));
		
		// only if an app id is set for facebook get those events
		if (!StringUtils.isEmpty(facebookAppId)) {
			// connect to facebook if that hasn't already happened
			if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
				return "redirect:/connect/facebook";
			}

			model.addAttribute("facebookEvents", facebookEventService.getFacebookEvents(facebook, selectedMonth));
		}
	
		return "home";
	}
	
	@RequestMapping(value = "reloadEventData", method = RequestMethod.GET)
	public String reloadEventData(@RequestParam("month") String month,  Model model) {
		model.addAttribute("events", getCalendarEntries(month));
		
		return "home::eventList";
	}
	
	private List<EventWrapper> getCalendarEntries(String month) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		Calendar selectedMonth = Calendar.getInstance();
		try {
			selectedMonth.setTime(df.parse(month));
			GoogleCalendarEventService ces = new GoogleCalendarEventService();
			
			return ces.getCalendarEntries(selectedMonth.get(Calendar.MONTH), selectedMonth.get(Calendar.YEAR));
		}
		catch (IOException | ParseException e) {
			return new ArrayList<>();
		}
	}
}
