package com.solution.issacq.Demo.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.solution.issacq.issacqbusinessservices.entities.CentralConversionRates;

@Component
public class ECBClient {

	public final String dailyRatesUrl = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

	private final RestTemplate restTemplate;

	public ECBClient(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	// since we don't have a xsd and the model XML is very simple a custom
	private final ECBUnmarshaller unmarshaller = new ECBUnmarshaller();

	public List<CentralConversionRates> getDailyRates() {

		return getRatesFromUrl(dailyRatesUrl);

	}

	private List<CentralConversionRates> getRatesFromUrl(String url) {

		ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

		try {
			return unmarshaller.apply(responseEntity.getBody());
		} catch (Exception e) {
			return new ArrayList<>();
		}

	}
}
