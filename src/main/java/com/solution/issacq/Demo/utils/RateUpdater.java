package com.solution.issacq.Demo.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.solution.issacq.issacqbusinessservices.dao.CentralConversionRatesDao;
import com.solution.issacq.issacqbusinessservices.dao.CentralConversionRatesHistoryDao;
import com.solution.issacq.issacqbusinessservices.dao.ConversionRateDao;
import com.solution.issacq.issacqbusinessservices.entities.CentralConversionRates;
import com.solution.issacq.issacqbusinessservices.entities.CentralConversionRatesHistory;
import com.solution.issacq.issacqbusinessservices.entities.ConversionRate;

@Component
public class RateUpdater {

	private static Logger logger = LoggerFactory.getLogger(RateUpdater.class);
	
	@Autowired
	private ECBClient client;
	
	@Autowired
	CentralConversionRatesDao centralConversionRatesDao;
	
	@Autowired
	CentralConversionRatesHistoryDao centralConversionRatesHistoryDao;
	
	@Autowired
	ConversionRateDao conversionRateDao;

	@Scheduled(cron = "0 1 0 * * *", zone = "GMT+2")
	public void execute() {
		
		
		List<CentralConversionRates> exchanges = client.getDailyRates();
		
		if ( exchanges != null)
		{
			logger.info("------------ start Update rate ----------");
			centralConversionRatesDao.deleteAll();
			conversionRateDao.deleteAll();
			
			List<CentralConversionRatesHistory> centralConversionRatesHistoryList = 
					exchanges.stream().map(emp -> new CentralConversionRatesHistory(Constants.ECB, emp.getSrcCurrencyCode(), emp.getDstCurrencyCode(), emp.getRate()))
				             .collect(Collectors.toList());
			
			
			
			List<ConversionRate> conversionRateList = 
					exchanges.stream().map(emp -> { double random = 0.018 + Math.random() * (0.028 - 0.018);
					return new ConversionRate(Constants.CENTER3GP, emp.getSrcCurrencyCode()
					, emp.getDstCurrencyCode(), emp.getRate().subtract(new BigDecimal(random))); })
				             .collect(Collectors.toList());
			
			centralConversionRatesHistoryList.stream().forEach(centralConversionRatesHistoryDao::save);
			exchanges.stream().forEach(centralConversionRatesDao::save);
			conversionRateList.stream().forEach(conversionRateDao::save);
			logger.info("------------ end Update rate ----------");
		}
		
	}

}
