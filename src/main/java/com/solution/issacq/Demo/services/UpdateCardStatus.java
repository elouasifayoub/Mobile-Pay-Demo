package com.solution.issacq.Demo.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.issacq.issacqbusinessservices.dao.CustomerLinkDao;
import com.solution.issacq.issacqbusinessservices.dao.ExternalRequestPropertyDao;
import com.solution.issacq.issacqbusinessservices.dao.MpayCustomerProfileDao;
import com.solution.issacq.issacqbusinessservices.entities.CustomerLink;
import com.solution.issacq.issacqbusinessservices.entities.ExternalRequestProperty;
import com.solution.issacq.issacqbusinessservices.entities.IssuerServiceProvider;
import com.solution.issacq.issacqbusinessservices.entities.MpayCustomerProfile;
import com.solution.issacq.issacqbusinessservices.entities.SolutionDictionaryData;
import com.solution.issacq.issacqbusinessservices.entities.StatusCodeTranslation;
import com.solution.issacq.issacqbusinessservices.payloaddeclaration.CardInfo;
import com.solution.issacq.issacqbusinessservices.payloaddeclaration.InboundRequest;
import com.solution.issacq.issacqbusinessservices.payloaddeclaration.InboundResponse;
import com.solution.issacq.issacqbusinessservices.payloaddeclaration.InboundResponseData;
import com.solution.issacq.issacqbusinessservices.services.IBasicService;
import com.solution.issacq.issacqbusinessservices.utils.CashSettingGetterUtils;
import com.solution.issacq.issacqbusinessservices.utils.Constants;
import com.solution.issacq.issacqbusinessservices.utils.ExternalJsonRequestMapper;
import com.solution.issacq.issacqbusinessservices.utils.IDynamicInvoke;
import com.solution.issacq.issacqbusinessservices.utils.InvokeHttpWebService;
import com.solution.issacq.issacqbusinessservices.utils.SolutionDictionaryListUtils;
import com.solution.issacq.issacqbusinessservices.utils.SolutionErrors;

@Component
public class UpdateCardStatus implements IDynamicInvoke {

	@Autowired
	IBasicService cashSettingService;

	@Autowired
	MpayCustomerProfileDao mpayCustomerProfileDao;

	@Autowired
	CustomerLinkDao customerLinkDao;

	@Autowired
	ExternalRequestPropertyDao externalRequestPropertyDao;

	@Override
	@Transactional
	public void processPaylaod(InboundRequest inboundRequest, InboundResponse inboundResponse) {

		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		SolutionDictionaryListUtils solutionDictionaryListUtils = new SolutionDictionaryListUtils();
		String issuerServiceProviderCode = inboundRequest.getInboundRequestData().getIssuerServiceProviderCode();
		String customerCode = inboundRequest.getInboundRequestData().getMpayCustomerCode();
		String customerLinkCode = inboundRequest.getInboundRequestData().getCardInfoCode();
		String cardStatus = inboundRequest.getInboundRequestData().getCardStatus();

		CashSettingGetterUtils cashSettingGetterUtils = new CashSettingGetterUtils();
		// Validate the Issuer service provider code
		IssuerServiceProvider issuerServiceProvider = cashSettingGetterUtils
				.getIssuerServiceProvider(issuerServiceProviderCode, cashSettingService);

		if (issuerServiceProvider == null) {
			inboundResponse.setProcessingStatus(SolutionErrors.INVALID_SERVICE_PROVIDER_CODE);
			inboundResponse.setProcessingStatusLabel("Invalid Issuer Service Provider "
					+ inboundRequest.getInboundRequestData().getIssuerServiceProviderCode());
			return;
		}

		// Validate the mpayCustomerProfile
		MpayCustomerProfile mpayCustomerProfile = mpayCustomerProfileDao.findOneByCode(customerCode);
		if (mpayCustomerProfile == null) {
			inboundResponse.setProcessingStatus(SolutionErrors.CUSTOMER_NOT_EXIST);
			inboundResponse.setProcessingStatusLabel("Invalid Customer Code : ");
			return;
		}

		// Check that the mpayCustomerProfile belongs to the issuer service provider
		if (!mpayCustomerProfile.getServiceProvider().equals(issuerServiceProvider.getCode())) {
			inboundResponse.setProcessingStatus(SolutionErrors.CUSTOMER_NOT_EXIST);
			inboundResponse.setProcessingStatusLabel("Customer doesn't belong to the issuer  ");
			return;
		}

		CustomerLink customerLink = customerLinkDao.findOneByCode(customerLinkCode);
		if (customerLink == null) {
			inboundResponse.setProcessingStatus(SolutionErrors.CUSTOMER_NOT_EXIST);
			inboundResponse.setProcessingStatusLabel("Invalid CustomerLink Code : ");
			return;
		}

		String externalEntityRequestCode = customerLink.getExternalCardProviderCode() + customerLink.getPackageCode()
				+ inboundRequest.getInboundMethod();

		ExternalRequestProperty externalRequestProperty = externalRequestPropertyDao
				.findOneByCode(externalEntityRequestCode);

		if (externalRequestProperty == null) {
			inboundResponse.setProcessingStatus(SolutionErrors.EXTERNAL_CARD_PROVIDER_NOT_CONFIGURED);
			inboundResponse.setProcessingStatusLabel("Bad Setting (externalCardProvider) " + externalEntityRequestCode);
			return;
		}

		if (StringUtils.isEmpty(externalRequestProperty.getAdditionnalData())) {
			inboundResponse.setProcessingStatus(SolutionErrors.EXTERNAL_CARD_PROVIDER_NOT_CONFIGURED);
			inboundResponse.setProcessingStatusLabel("Bad Setting (externalCardProvider) " + externalEntityRequestCode);
			return;

		}

		StatusCodeTranslation statusCodeTranslation = cashSettingGetterUtils.getStatusCodeTranslation(
				Constants.ISSACQ_COMPONENT, Constants.THIRD_PARTIES, cardStatus, Constants.CARD_STATUS,
				cashSettingService);
		if (statusCodeTranslation == null) {
			inboundResponse.setProcessingStatus(SolutionErrors.INVALID_SOLUTION_SETTING);
			inboundResponse.setProcessingStatusLabel("Missing Setting Translation Status ");
			return;
		}

		inboundRequest.getInboundRequestData().setClientCode(customerLink.getClientId());
		inboundRequest.getInboundRequestData().setStatusCode(statusCodeTranslation.getTranslation());

		JsonNode inboundJsonRequestNode = mapper.convertValue(inboundRequest, JsonNode.class);
		
		ExternalJsonRequestMapper externalJsonRequestMapper = new ExternalJsonRequestMapper();
		Map<String, String> externalRquestMapping = externalJsonRequestMapper.parseRequestMapping(externalRequestProperty.getAdditionnalData(),
				"#", "=");

		if (MapUtils.isEmpty(externalRquestMapping) || externalRquestMapping.size() == 0) {
			inboundResponse.setProcessingStatus(SolutionErrors.EXTERNAL_CARD_PROVIDER_NOT_CONFIGURED);
			inboundResponse.setProcessingStatusLabel("Bad Setting (externalCardProvider) ");
			return;
		}

		JsonNode jsonExternalRequestNode = externalJsonRequestMapper.generateOutgoingJsonRquest(externalRquestMapping,
				inboundJsonRequestNode);

		System.out.println(jsonExternalRequestNode);
		InvokeHttpWebService invokeHttpWebService = new InvokeHttpWebService();

		JsonNode jsonExternalResponsetNode = invokeHttpWebService.submitJsonRequest(externalRequestProperty,
				jsonExternalRequestNode);

		if (!jsonExternalResponsetNode.has("serviceResponseCode")) {
			inboundResponse.setProcessingStatus(SolutionErrors.EXTERNAL_CARD_PROVIDER_ERROR);
			inboundResponse.setProcessingStatusLabel("No Response From externalCardProvider ");
			return;

		}
		System.out.println(jsonExternalResponsetNode);

		String serviceResponseCode = jsonExternalResponsetNode.findPath("serviceResponseCode").asText();
		String serviceResponseLabel = jsonExternalResponsetNode.findPath("serviceResponseLabel").asText();

		if (!Constants.PROCESSING_DONE.equals(serviceResponseCode)) {
			inboundResponse.setProcessingStatus(SolutionErrors.EXTERNAL_CARD_PROVIDER_ERROR);
			inboundResponse.setProcessingStatusLabel("Rejected  " + serviceResponseCode + " " + serviceResponseLabel);
			return;
		}
		// Set status active to Customer
		SolutionDictionaryData activeStatusDictionary = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(
				Constants.CARD_STATUS, cardStatus, cashSettingService.getSolutionDictionaryDataList(),
				cashSettingService.getSolutionDictionaryList());
		if (activeStatusDictionary == null) {
			inboundResponse.setProcessingStatus(SolutionErrors.INVALID_SOLUTION_SETTING);
			inboundResponse.setProcessingStatusLabel("Missing Setting A Status ");
			return;
		}
		customerLink.setCardStatus(activeStatusDictionary.getCode());
		Date date = new Date();
		customerLink.setCardStatusDate(new Timestamp(date.getTime()));

		customerLinkDao.save(customerLink);

		CardInfo cardInfo = new CardInfo();
		cardInfo.setCode(customerLink.getCode());
		cardInfo.setClientId(customerLink.getClientId());
		cardInfo.setCardStatus(customerLink.getCardStatus());
		cardInfo.setCvv(customerLink.getCvv());
		cardInfo.setExpiryDate(customerLink.getExpiryDate());
		cardInfo.setLastFourDigits(customerLink.getLastFourDigits());
		cardInfo.setCardArtType(customerLink.getCardType());

		InboundResponseData inboundResponseData = new InboundResponseData();
		inboundResponseData.setCardInfo(cardInfo);
		inboundResponse.setInboundResponseData(inboundResponseData);
		inboundResponse.setProcessingStatus(Constants.PROCESSING_DONE);
		inboundResponse.setProcessingStatusLabel("Done");

	}

}
