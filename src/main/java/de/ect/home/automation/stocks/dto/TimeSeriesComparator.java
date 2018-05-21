package de.ect.home.automation.stocks.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import org.springframework.util.StringUtils;

public class TimeSeriesComparator implements Comparator<TimeSeries> {

	@Override
	public int compare(TimeSeries o1, TimeSeries o2) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		if (o1 == null || StringUtils.isEmpty(o1.getDate())) {
			return 1;
		}
		if (o2 == null || StringUtils.isEmpty(o2.getDate())) {
			return -1;
		}

		try {
			Date d1 = df.parse(o1.getDate());
			Date d2 = df.parse(o2.getDate());
			
			return d1.compareTo(d2);
		} catch (ParseException e) {
			return 0;
		}
	}

}
