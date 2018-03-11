package de.ect.home.automation.controllers;

import java.io.IOException;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import de.ect.home.automation.calendar.FacebookEventService;
import de.ect.home.automation.calendar.GoogleCalendarEventService;

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
		GoogleCalendarEventService ces = new GoogleCalendarEventService();
		Calendar c = Calendar.getInstance();
		model.addAttribute("events", ces.getCalendarEntries(c.get(Calendar.MONTH), c.get(Calendar.YEAR)));

		// only if an app id is set for facebook get those events
		if (!StringUtils.isEmpty(facebookAppId)) {
			// connect to facebook if that hasn't already happened
			if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
				return "redirect:/connect/facebook";
			}

			model.addAttribute("facebookEvents", facebookEventService.getFacebookEvents(facebook, c));
			
			
		}
		return "home";
	}
}
