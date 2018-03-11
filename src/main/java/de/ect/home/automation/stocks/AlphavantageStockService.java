package de.ect.home.automation.stocks;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * this service is fetching stock market data as provided by the alphavantage.co API
 * for details on the API see <a href="https://www.alphavantage.co/documentation/
">https://www.alphavantage.co/documentation/</a>
 * 
 * @author ctr
 */
@Component
public class AlphavantageStockService {

	// you can get the api key from https://www.alphavantage.co/support/#api-key
	@Value("${stock.alphavantage.co.key}")
	private String apiKey;
	
	// default currency is US-Dollar. If you want anything else use this property
	@Value("${stock.alphavantage.co.currency}")
	private String currency;
	
	// all the stocks you're watching
	@Value("${stock.alphavantage.co.equities}")
	private String[] equities;
}
