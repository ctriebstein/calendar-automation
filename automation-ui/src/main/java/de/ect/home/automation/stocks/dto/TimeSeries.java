package de.ect.home.automation.stocks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeSeries {

	@JsonProperty("1. open")
	private double openingValue;

	@JsonProperty("2. high")
	private double highValue;

	@JsonProperty("3. low")
	private double lowValue;

	@JsonProperty("4. close")
	private double closingValue;

	@JsonProperty("5. volume")
	private long volume;

	private String date;

	public double getOpeningValue() {
		return openingValue;
	}

	public void setOpeningValue(double openingValue) {
		this.openingValue = openingValue;
	}

	public double getHighValue() {
		return highValue;
	}

	public void setHighValue(double highValue) {
		this.highValue = highValue;
	}

	public double getLowValue() {
		return lowValue;
	}

	public void setLowValue(double lowValue) {
		this.lowValue = lowValue;
	}

	public double getClosingValue() {
		return closingValue;
	}

	public void setClosingValue(double closingValue) {
		this.closingValue = closingValue;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
