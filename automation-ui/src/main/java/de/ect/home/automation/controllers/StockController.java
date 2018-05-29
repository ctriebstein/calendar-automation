package de.ect.home.automation.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import de.ect.home.automation.stocks.AlphavantageStockService;

@Controller
public class StockController {
	
	@Autowired
	private AlphavantageStockService stockService;

	@GetMapping("/stock")
	public String stockPage(Model model) throws IOException {
		model.addAttribute("stockData", stockService.getEquityValues());
		
		return "stock";
	}
}
