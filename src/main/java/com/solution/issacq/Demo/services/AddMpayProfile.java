package com.solution.issacq.Demo.services;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.solution.issacq.issacqbusinessservices.dao.MpayCustomerProfileDao;
import com.solution.issacq.issacqbusinessservices.dao.MpayCustomerSigninDao;
import com.solution.issacq.issacqbusinessservices.dao.business.AddressDao;
import com.solution.issacq.issacqbusinessservices.entities.Address;
import com.solution.issacq.issacqbusinessservices.entities.Country;
import com.solution.issacq.issacqbusinessservices.entities.IssuerServiceProvider;
import com.solution.issacq.issacqbusinessservices.entities.MpayCustomerProfile;
import com.solution.issacq.issacqbusinessservices.entities.MpayCustomerSignin;
import com.solution.issacq.issacqbusinessservices.entities.SolutionDictionaryData;
import com.solution.issacq.issacqbusinessservices.payloaddeclaration.InboundRequest;
import com.solution.issacq.issacqbusinessservices.payloaddeclaration.InboundResponse;
import com.solution.issacq.issacqbusinessservices.payloaddeclaration.MpayProfile;
import com.solution.issacq.issacqbusinessservices.payloaddeclaration.PayLoadAddress;
import com.solution.issacq.issacqbusinessservices.services.IBasicService;
import com.solution.issacq.issacqbusinessservices.utils.CashSettingGetterUtils;
import com.solution.issacq.issacqbusinessservices.utils.Constants;
import com.solution.issacq.issacqbusinessservices.utils.IDynamicInvoke;
import com.solution.issacq.issacqbusinessservices.utils.SolutionDictionaryListUtils;
import com.solution.issacq.issacqbusinessservices.utils.SolutionErrors;

@Component
public class AddMpayProfile implements IDynamicInvoke {

	@Autowired
	MpayCustomerProfileDao mpayCustomerProfileDao;

	@Autowired
	MpayCustomerSigninDao mpayCustomerSigninDao;

	@Autowired
	IBasicService cashSettingService;

	@Autowired
	AddressDao addressDao;
	
	@Override
	@Transactional
	public void processPaylaod(InboundRequest inboundRequest, InboundResponse inboundResponse) {
			
			CashSettingGetterUtils cashSettingGetterUtils = new CashSettingGetterUtils();
			SolutionDictionaryListUtils solutionDictionaryListUtils = new SolutionDictionaryListUtils();
			
			// Validate the Issuer service provider code
			IssuerServiceProvider issuerServiceProvider = cashSettingGetterUtils.getIssuerServiceProvider(
					inboundRequest.getInboundRequestData().getMpayProfile().getServiceProvider(), cashSettingService);
			
			if (issuerServiceProvider == null) {
				inboundResponse.setProcessingStatus(SolutionErrors.INVALID_SERVICE_PROVIDER_CODE);
				inboundResponse.setProcessingStatusLabel("Invalid Issuer Service Provider ");
				return;
			}
			// Validate the CustomerSignin code
			MpayCustomerSignin mpayCustomerSignin = mpayCustomerSigninDao
					.findOneByCode(inboundRequest.getInboundRequestData().getMpayProfile().getCode());
			if (mpayCustomerSignin == null) {
				inboundResponse.setProcessingStatus(SolutionErrors.CUSTOMER_NOT_EXIST);
				inboundResponse.setProcessingStatusLabel("this user does not exist" );
				return;
			}

			if (mpayCustomerSignin.isEnabled().equals(Boolean.FALSE)) {
				inboundResponse.setProcessingStatus(SolutionErrors.EMAIL_ADDRESS_NOT_ACTIVATED);
				inboundResponse.setProcessingStatusLabel("Sign In Not Activated ");
				return;
			}
			// Create new CustomerProfile 
			MpayCustomerProfile mpayCustomerProfileAE = mpayCustomerProfileDao.findOneByCode(inboundRequest.getInboundRequestData().getMpayProfile().getCode());
			if (mpayCustomerProfileAE != null)
			{
				inboundResponse.setProcessingStatus(SolutionErrors.CUSTOMER_CODE_ALREADY_EXIST);
				inboundResponse.setProcessingStatusLabel("Customer Code Already Exists");
				return;
			}
		 
		    //Perform Some Operation Here
		    MpayCustomerProfile mpayCustomerProfile = new MpayCustomerProfile();
			//  Added new Customer to the MpayCustomerProfile table.
			MpayProfile mpayProfile = inboundRequest.getInboundRequestData().getMpayProfile();
			mpayCustomerProfile.setServiceProvider(issuerServiceProvider.getCode());
			mpayCustomerProfile.setCode(mpayProfile.getCode());
			mpayCustomerProfile.setEmail(mpayCustomerSignin.getEmail());
			mpayCustomerProfile.setFirstName(mpayProfile.getFirstName());
			mpayCustomerProfile.setLastName(mpayProfile.getLastName());
			mpayCustomerProfile.setMiddleName(mpayProfile.getMiddleName());
			mpayCustomerProfile.setCvmValue(mpayProfile.getCvmValue());
			mpayCustomerProfile.setCorporateId(mpayProfile.getCorporateId());
			mpayCustomerProfile.setActivationDate(mpayCustomerSignin.getActivationDate());
			mpayCustomerProfile.setIsGeoLocationMasked(mpayProfile.getGeoLocationMasked());
			mpayCustomerProfile.setIsMpaMaskedToAll(mpayProfile.getMpaMaskedToAll());
			mpayCustomerProfile.setIsFreindAllowed(mpayProfile.getFreindAllowed());
			mpayCustomerProfile.setShareLocationStatus(mpayProfile.getShareLocationStatus());
			mpayCustomerProfile.setPhoneCode(mpayProfile.getPhoneCode());
			mpayCustomerProfile.setMobileNumber(mpayProfile.getMobileNumber());
			
			mpayCustomerProfile.setLastlongitude(mpayProfile.getLastlongitude());
			mpayCustomerProfile.setLastlatitude(mpayProfile.getLastlatitude());

			Date dateofB;
			try {
				dateofB = new SimpleDateFormat("yyyy-MM-dd").parse(mpayProfile.getDateOfBirth());
				mpayCustomerProfile.setDateOfBirth(dateofB);
			} catch (ParseException e) {
				inboundResponse.setProcessingStatus(SolutionErrors.INVALID_JSON_ATTRIBUTE);
				inboundResponse.setProcessingStatusLabel("Invalid Date (Format is dd/MM/yyyy)" + mpayProfile.getDateOfBirth());
				return;
			}

			String nationalityCode = mpayProfile.getNationalityCode();
			if (!StringUtils.isBlank(nationalityCode)) {
				Country nationality = cashSettingGetterUtils.getCountryIso3Alpha(nationalityCode, cashSettingService);
	
				if (nationality == null) {
					inboundResponse.setProcessingStatus(SolutionErrors.INVALID_JSON_ATTRIBUTE);
					inboundResponse.setProcessingStatusLabel("Invalid Data " + nationalityCode);
					return;
				}
				mpayCustomerProfile.setNationalityCode(nationality.getCode());
			}
			
			List<PayLoadAddress> addressesList = inboundRequest.getInboundRequestData().getMpayProfile().getAddresses();
			if (addressesList != null) {
				//Set OWNER ADDRESS CATEGORY to CUSTOMER
				SolutionDictionaryData ownerCategoryDictionary = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(Constants.OWNER_ADDRESS_CATEGORY,
						Constants.CUSTOMER, cashSettingService.getSolutionDictionaryDataList(),
						cashSettingService.getSolutionDictionaryList());
				if (ownerCategoryDictionary == null) {
					inboundResponse.setProcessingStatus(SolutionErrors.INVALID_SOLUTION_SETTING);
					inboundResponse.setProcessingStatusLabel("Missing Setting owner Address Category ");
					return;
				}
				
				List<Address> addresses = new ArrayList<>();
				
				//Loop through List of address and finally add them 
				for(PayLoadAddress payLoadAddress : addressesList)
				{
					Address address = new Address();
					address.setOwnerCategory(ownerCategoryDictionary.getCode());
					address.setOwnerCode(inboundRequest.getInboundRequestData().getMpayProfile().getCode());
					address.setCode(RandomStringUtils.randomAlphanumeric(8));
					
					 SolutionDictionaryData addressCategoryDictionary = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(Constants.ADDRESS_CATEGORY,
							payLoadAddress.getAddressCategory(), cashSettingService.getSolutionDictionaryDataList(),
							cashSettingService.getSolutionDictionaryList());
					 
					if (addressCategoryDictionary == null) {
						inboundResponse.setProcessingStatus(SolutionErrors.INVALID_JSON_ATTRIBUTE);
						inboundResponse.setProcessingStatusLabel("Invalid Address Category " + payLoadAddress.getAddressCategory());
						return;
					}
					address.setAddressCategory (addressCategoryDictionary.getCode());
					
					String receivedCountry = payLoadAddress.getCountry();
					// Validate the Country code
					Country country = cashSettingGetterUtils.getCountryIso3Alpha(receivedCountry, cashSettingService);
					if (country == null) {
						inboundResponse.setProcessingStatus(SolutionErrors.INVALID_JSON_ATTRIBUTE);
						inboundResponse.setProcessingStatusLabel("Invalid Country Code " + receivedCountry);
						return;
					}
					address.setCountry(country.getCode());
					address.setStateOrProvince(payLoadAddress.getStateOrProvince());
					address.setTown(payLoadAddress.getTown());
					address.setPostalCode(payLoadAddress.getPostalCode());
					address.setStreet(payLoadAddress.getStreet());
					address.setLine1(payLoadAddress.getLine1());
					address.setLine2(payLoadAddress.getLine2());
					address.setLine3(payLoadAddress.getLine3());
					address.setLine4(payLoadAddress.getLine4());
					address.setEmail1(payLoadAddress.getEmail1());
					address.setEmail2(payLoadAddress.getEmail2());
					address.setPhone1(payLoadAddress.getPhone1());
					address.setPhone2(payLoadAddress.getPhone2());
					addresses.add(address);
				}
				
				addressDao.saveAll(addresses);
			}

			// Set status active to Customer
			SolutionDictionaryData activeStatusDictionary = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(Constants.CUSTOMER_STATUS,
					Constants.ACTIVE, cashSettingService.getSolutionDictionaryDataList(),
					cashSettingService.getSolutionDictionaryList());
			if (activeStatusDictionary == null) {
				inboundResponse.setProcessingStatus(SolutionErrors.INVALID_SOLUTION_SETTING);
				inboundResponse.setProcessingStatusLabel("Missing Setting A Status ");
				return;
			}
			
			mpayCustomerProfile.setStatus(activeStatusDictionary.getCode());
			
			// Set Family Status (MARIED, SINGLE ..)
			String familyStatus = inboundRequest.getInboundRequestData().getMpayProfile().getFamilyStatus();
			if (!StringUtils.isBlank(familyStatus)) {
				SolutionDictionaryData familyStatusDictionary = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(Constants.FAMILY_STATUS,
						familyStatus, cashSettingService.getSolutionDictionaryDataList(),
						cashSettingService.getSolutionDictionaryList());
	
				if (familyStatusDictionary.getId() == null) {
					inboundResponse.setProcessingStatus(SolutionErrors.INVALID_SOLUTION_SETTING );
					inboundResponse.setProcessingStatusLabel("Missing Setting Family Status " + familyStatus);
					return;
				}
				mpayCustomerProfile.setFamilyStatus(familyStatusDictionary.getCode());
			}
			
			// Set gender (Male, Female ..)
			String gender = inboundRequest.getInboundRequestData().getMpayProfile().getGender();
			if (!StringUtils.isBlank(gender)) {
				SolutionDictionaryData genderSolutionDictionary= solutionDictionaryListUtils.getSolutionDictionaryDataByCode(Constants.GENDER, gender,
						cashSettingService.getSolutionDictionaryDataList(), cashSettingService.getSolutionDictionaryList());
				if (genderSolutionDictionary == null) {
					inboundResponse.setProcessingStatus(SolutionErrors.INVALID_SOLUTION_SETTING);
					inboundResponse.setProcessingStatusLabel("Missing Setting Gender " +  gender);
					return;
				}
				mpayCustomerProfile.setGender(genderSolutionDictionary.getCode());
			}
			
			// Set cvmProperties (for pin, finger Print, faceId)
			String cvmProperties = inboundRequest.getInboundRequestData().getMpayProfile().getCvmProperties();
			if (!StringUtils.isBlank(cvmProperties)) {
				SolutionDictionaryData cvmTypeSolutionDictionary = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(Constants.CVM_PROPERTIES,
						cvmProperties, cashSettingService.getSolutionDictionaryDataList(),
						cashSettingService.getSolutionDictionaryList());
				if (cvmTypeSolutionDictionary == null) {
					inboundResponse.setProcessingStatus(SolutionErrors.INVALID_SOLUTION_SETTING);
					inboundResponse.setProcessingStatusLabel("Missing Setting CvmType " + cvmProperties);
					return;
				}
				mpayCustomerProfile.setCvmType(cvmTypeSolutionDictionary.getCode());
			}

			
			Date date = new Date();
			mpayCustomerProfile.setRegistrationDate(new Timestamp(date.getTime()));

			mpayCustomerProfile.setActivationDate(mpayCustomerSignin.getActivationDate());
			date = new Date();
			mpayCustomerProfile.setCustomerStatusDate(new Timestamp(date.getTime()));
			
			String legalId1Type = inboundRequest.getInboundRequestData().getMpayProfile().getLegalId1Type();
			if (!StringUtils.isBlank(legalId1Type)) {
				SolutionDictionaryData legalId1Dictionary = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(Constants.LEGAL_ID_TYPE,
						legalId1Type, cashSettingService.getSolutionDictionaryDataList(),
						cashSettingService.getSolutionDictionaryList());
	
				if (legalId1Dictionary == null) {
					inboundResponse.setProcessingStatus(SolutionErrors.INVALID_SOLUTION_SETTING );
					inboundResponse.setProcessingStatusLabel("Missing Setting Legal ID 1 Type :" + legalId1Type);
					return;
				}
				mpayCustomerProfile.setLegalId1(inboundRequest.getInboundRequestData().getLegalId1());
			}
			
			String legalId2Type = inboundRequest.getInboundRequestData().getMpayProfile().getLegalId2Type();
			if (!StringUtils.isBlank(legalId1Type)) {
				SolutionDictionaryData legalId2Dictionary = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(Constants.LEGAL_ID_TYPE,
						legalId2Type, cashSettingService.getSolutionDictionaryDataList(),
						cashSettingService.getSolutionDictionaryList());
	
				if (legalId2Dictionary == null) {
					inboundResponse.setProcessingStatus(SolutionErrors.INVALID_SOLUTION_SETTING );
					inboundResponse.setProcessingStatusLabel("Missing Setting Legal ID 2 Type :" + legalId2Type);
					return;
				}
				mpayCustomerProfile.setLegalId2(inboundRequest.getInboundRequestData().getLegalId2());
			}
			
			SolutionDictionaryData customerTypeDictionary = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(Constants.CUSTOMER_TYPE,
					"S", cashSettingService.getSolutionDictionaryDataList(),
					cashSettingService.getSolutionDictionaryList());
			
			if (customerTypeDictionary == null) {
				inboundResponse.setProcessingStatus(SolutionErrors.INVALID_SOLUTION_SETTING );
				inboundResponse.setProcessingStatusLabel("Missing Setting Customer Type :" + "S");
				return;
			}
			mpayCustomerProfile.setCustomerType(customerTypeDictionary.getCode());
			mpayCustomerProfile.setIsDocumentVerified(Constants.IS_INPUT_NO);
			
			Gson gson = new Gson();
			String secretQuestions = gson.toJson(inboundRequest.getInboundRequestData().getMpayProfile().getSecretQuestionsList());
			mpayCustomerProfile.setSecretQuestions(secretQuestions);
			
			//Set Account Verification State to CUSTOMER
			SolutionDictionaryData accountVerificationDictionary = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(Constants.ACCOUNT_VERIFICATION_STATE,
					Constants.UNVERIFIED, cashSettingService.getSolutionDictionaryDataList(),
					cashSettingService.getSolutionDictionaryList());
			if (accountVerificationDictionary == null) {
				inboundResponse.setProcessingStatus(SolutionErrors.INVALID_SOLUTION_SETTING);
				inboundResponse.setProcessingStatusLabel("Missing Setting account verification state !");
				return;
			}
			mpayCustomerProfile.setAccountVerificationState(accountVerificationDictionary.getCode());
			
			mpayCustomerProfile.setIsAuthenticated(true);
			
			mpayCustomerProfileDao.save(mpayCustomerProfile);
			
			inboundRequest.getInboundRequestData().setMpayCustomerProfile(mpayCustomerProfile);
			inboundResponse.setProcessingStatus(Constants.PROCESSING_DONE);
			inboundResponse.setProcessingStatusLabel("Sucessfully Processed");
		
	}

}
