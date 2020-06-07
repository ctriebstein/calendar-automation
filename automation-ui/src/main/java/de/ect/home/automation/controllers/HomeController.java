package de.ect.home.automation.controllers;

import de.ect.home.automation.calendar.GoogleCalendarEventService;
import de.ect.home.automation.calendar.util.EventWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
public class HomeController {

	private GoogleCalendarEventService ces = new GoogleCalendarEventService();

	@GetMapping("/")
	public String homePage(Model model) throws IOException {
		// get events from google calendar
		Calendar selectedMonth = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		
		model.addAttribute("selectedMonth", df.format(selectedMonth.getTime()));		
		model.addAttribute("events", getCalendarEntries(df.format(selectedMonth.getTime())));

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

			return ces.getCalendarEntries(selectedMonth.get(Calendar.MONTH), selectedMonth.get(Calendar.YEAR));
		}
		catch (IOException | ParseException e) {
			return new ArrayList<>();
		}
	}
}
