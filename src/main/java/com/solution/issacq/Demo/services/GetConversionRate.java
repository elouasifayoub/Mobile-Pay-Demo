package com.solution.issacq.Demo.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.solution.issacq.issacqbusinessservices.dao.CentralConversionRatesDao;
import com.solution.issacq.issacqbusinessservices.dao.ConversionRateDao;
import com.solution.issacq.issacqbusinessservices.dao.MpaCustomerAccountDao;
import com.solution.issacq.issacqbusinessservices.dao.MpayCustomerProfileDao;
import com.solution.issacq.issacqbusinessservices.entities.CentralConversionRates;
import com.solution.issacq.issacqbusinessservices.entities.ConversionRate;
import com.solution.issacq.issacqbusinessservices.entities.Currency;
import com.solution.issacq.issacqbusinessservices.entities.MpaCustomerAccount;
import com.solution.issacq.issacqbusinessservices.entities.MpayCustomerProfile;
import com.solution.issacq.issacqbusinessservices.payloaddeclaration.CurrencyRate;
import com.solution.issacq.issacqbusinessservices.payloaddeclaration.InboundRequest;
import com.solution.issacq.issacqbusinessservices.payloaddeclaration.InboundResponse;
import com.solution.issacq.issacqbusinessservices.payloaddeclaration.InboundResponseData;
import com.solution.issacq.issacqbusinessservices.payloaddeclaration.Rate;
import com.solution.issacq.issacqbusinessservices.utils.CashSettingGetterUtils;
import com.solution.issacq.issacqbusinessservices.utils.Constants;
import com.solution.issacq.issacqbusinessservices.utils.IDynamicInvoke;
import com.solution.issacq.issacqbusinessservices.utils.SolutionErrors;
import com.solution.issacq.issacqbusinessservices.services.IBasicService;

@Component
public class GetConversionRate implements IDynamicInvoke {

	@Autowired
	ConversionRateDao conversionRateDao;

	@Autowired
	MpayCustomerProfileDao mpayCustomerProfileDao;

	@Autowired
	MpaCustomerAccountDao mpaCustomerAccountDao;

	@Autowired
	IBasicService cashSettingService;

	@Autowired
	CentralConversionRatesDao centralConversionRatesDao;

	@Override
	@Transactional
	public void processPaylaod(InboundRequest inboundRequest, InboundResponse inboundResponse) {

		CurrencyRate currencyRate;
		currencyRate = inboundRequest.getInboundRequestData().getCurrencyRate();
		String mpayCustomerCode = inboundRequest.getInboundRequestData().getMpayCustomerCode();
		
		if (currencyRate.getDstCurrenyCode() == null) {
			inboundResponse.setProcessingStatus("ERR-GCR-0001");
			inboundResponse.setProcessingStatusLabel("A minimum of 2 different accounts is required to be able to convert!");
			return;
		}

		ConversionRate conversionRate = conversionRateDao.findOneByCenterCodeAndSrcCurrencyCodeAndDstCurrencyCode(
				currencyRate.getCenterCode(), currencyRate.getCurrenyCode(), currencyRate.getDstCurrenyCode());

		if (conversionRate == null) {
			inboundResponse.setProcessingStatus("ERR-GCR-0002");
			inboundResponse.setProcessingStatusLabel("Rate Not Defined Yet Between " + currencyRate.getCurrenyCode()
					+ " and " + currencyRate.getDstCurrenyCode());
			return;
		}
		ConversionRate reverseRate = conversionRateDao.findOneByCenterCodeAndSrcCurrencyCodeAndDstCurrencyCode(
				currencyRate.getCenterCode(), currencyRate.getDstCurrenyCode(), currencyRate.getCurrenyCode());

		if (reverseRate == null) {
			inboundResponse.setProcessingStatus("ERR-GCR-0003");
			inboundResponse.setProcessingStatusLabel("Rate Not Defined Yet Between " + currencyRate.getDstCurrenyCode()
					+ " and " + currencyRate.getCurrenyCode() );
			return;
		}

		InboundResponseData inboundResponseData = new InboundResponseData();
		Rate rate = new Rate();

		CentralConversionRates centralConversionRates = centralConversionRatesDao
				.findOneBySrcCurrencyCodeAndDstCurrencyCode(currencyRate.getCurrenyCode(),
						currencyRate.getDstCurrenyCode());

		if (centralConversionRates != null) {
			BigDecimal differenceImpayVsECB = centralConversionRates.getRate().subtract(conversionRate.getRate()).multiply(new BigDecimal("100"));
			rate.setDifferenceImpayVsECB(differenceImpayVsECB);
		}

		rate.setCurrenyCode(currencyRate.getDstCurrenyCode());
		rate.setRate(conversionRate.getRate());
		rate.setReverseRate(reverseRate.getRate());

		if (currencyRate.getAmountToConvert() == null)
			currencyRate.setAmountToConvert(BigDecimal.ZERO);

		BigDecimal convertedAmount = currencyRate.getAmountToConvert().multiply(conversionRate.getRate());

		CashSettingGetterUtils cashSettingGetterUtils = new CashSettingGetterUtils();

		Currency dstCurrency = cashSettingGetterUtils.getCurrencyIso3Alpha(currencyRate.getDstCurrenyCode(),
				cashSettingService);

		MathContext mc = new MathContext(dstCurrency.getExponent());

		convertedAmount.round(mc);

		rate.setConvertedAmount(convertedAmount);

		if (!StringUtils.isEmpty(mpayCustomerCode)) {
			MpayCustomerProfile mpayCustomerProfile = mpayCustomerProfileDao.findOneByCode(mpayCustomerCode);
			if (mpayCustomerProfile == null) {
				inboundResponse.setProcessingStatus(SolutionErrors.INVALID_EMAIL_OR_CODE);
				inboundResponse.setProcessingStatusLabel("this User does not exist " + mpayCustomerCode);
				return;
			}
			Currency currencySrc = cashSettingGetterUtils.getCurrencyIso3Alpha(currencyRate.getCurrenyCode(),
					cashSettingService);
			MpaCustomerAccount mpaCustomerAccountSrc = mpaCustomerAccountDao
					.findOneByMpayCustomerProfileAndCurrency(mpayCustomerCode, currencySrc.getIso3Numeric());
			if (mpaCustomerAccountSrc == null) {
				inboundResponse.setProcessingStatus(SolutionErrors.INVALID_EMAIL_OR_CODE);
				inboundResponse.setProcessingStatusLabel(
						"this User does not have an account with currency " + currencyRate.getCurrenyCode());
				return;
			}

			Currency currencyDst = cashSettingGetterUtils.getCurrencyIso3Alpha(currencyRate.getDstCurrenyCode(),
					cashSettingService);
			MpaCustomerAccount mpaCustomerAccountDst = mpaCustomerAccountDao
					.findOneByMpayCustomerProfileAndCurrency(mpayCustomerCode, currencyDst.getIso3Numeric());
			if (mpaCustomerAccountDst == null) {
				inboundResponse.setProcessingStatus(SolutionErrors.INVALID_EMAIL_OR_CODE);
				inboundResponse.setProcessingStatusLabel(
						"this User does not have an account with currency " + currencyRate.getDstCurrenyCode());
				return;
			}

			BigDecimal a = new BigDecimal(mpaCustomerAccountSrc.getBalance())
					.subtract(currencyRate.getAmountToConvert());
			BigDecimal newBalanceSrc = a.setScale(2, RoundingMode.HALF_UP);
			BigDecimal b = new BigDecimal(mpaCustomerAccountDst.getBalance()).add(rate.getConvertedAmount());
			BigDecimal newBalanceDst = b.setScale(2, RoundingMode.HALF_UP);

			inboundResponseData.setNewBalanceSrc(newBalanceSrc.toString());
			inboundResponseData.setNewBalanceDst(newBalanceDst.toString());
		}

		inboundResponseData.setRate(rate);
		inboundResponse.setProcessingStatus(Constants.PROCESSING_DONE);
		inboundResponse.setProcessingStatusLabel("Success process");
		inboundResponse.setInboundResponseData(inboundResponseData);

	}

}
