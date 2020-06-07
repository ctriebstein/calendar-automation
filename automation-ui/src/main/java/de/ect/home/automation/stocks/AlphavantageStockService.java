package de.ect.home.automation.stocks;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import de.ect.home.automation.stocks.dto.StockData;

/**
 * this service is fetching stock market data as provided by the alphavantage.co API
 * for details on the API see <a href="https://www.alphavantage.co/documentation/">https://www.alphavantage.co/documentation/</a>
 * 
 * @author ctr
 */
@Component
public class AlphavantageStockService {
	
	private static final String API_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s";
	
	@Autowired
	private RestTemplate restTemplate;
	
	// you can get the api key from https://www.alphavantage.co/support/#api-key
	@Value("${stock.alphavantage.co.key}")
	private String apiKey;
	
	// default currency is US-Dollar. If you want anything else use this property
	@Value("${stock.alphavantage.co.currency}")
	private String currency;
	
	// all the stocks you're watching
	@Value("${stock.alphavantage.co.equities}")
	private String[] equities;
	
	/**
	 * get values for all equities defined in stock.alphavantage.co.equities
	 * from 100 days ago to now
	 * 
	 * @return a list of all closing values of all equities defined in the given
	 *         property in the configured currency
	 */
	public List<StockData> getEquityValues() {
		List<StockData> equityList = new ArrayList<>();
		
		if (this.equities != null) {
			for (String equity : equities) {
				equityList.add(getDataPoints(equity));
			}
		}
		
		return equityList;
	}

	private StockData getDataPoints(String equity) {
		return restTemplate.getForObject(String.format(API_URL, equity, this.apiKey), StockData.class);
	}
}
