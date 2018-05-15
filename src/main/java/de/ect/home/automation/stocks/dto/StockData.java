package de.ect.home.automation.stocks.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for single stock values
 * @author ctr
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockData {
	
	@JsonProperty("Meta Data")
	private Metadata metadata;

	@JsonProperty("Time Series (Daily)")
    private Map<String, TimeSeries> timeSeries;

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public Map<String, TimeSeries> getTimeSeries() {
		return timeSeries;
	}

	public void setTimeSeries(Map<String, TimeSeries> timeSeries) {
		this.timeSeries = timeSeries;
	}
	
	
}
