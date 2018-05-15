package de.ect.home.automation.stocks.dto;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {
	
	@JsonProperty("1. Information")
	private String information;
	
	@JsonProperty("2. Symbol")
	private String symbol;
	
	@JsonProperty("3. Last Refreshed")
	private Calendar lastRefreshed;
	
	@JsonProperty("4. Output Size")
	private String outputSize;
	
	@JsonProperty("5. Time Zone")
	private String timeZone;

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Calendar getLastRefreshed() {
		return lastRefreshed;
	}

	public void setLastRefreshed(Calendar lastRefreshed) {
		this.lastRefreshed = lastRefreshed;
	}

	public String getOutputSize() {
		return outputSize;
	}

	public void setOutputSize(String outputSize) {
		this.outputSize = outputSize;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
}
