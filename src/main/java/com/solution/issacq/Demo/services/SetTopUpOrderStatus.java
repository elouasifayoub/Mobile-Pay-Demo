package com.solution.issacq.Demo.services;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.solution.issacq.issacqbusinessservices.dao.AvailableTransactionDao;
import com.solution.issacq.issacqbusinessservices.dao.DeviceAccessDao;
import com.solution.issacq.issacqbusinessservices.dao.IssuerAccountProgramDao;
import com.solution.issacq.issacqbusinessservices.dao.IssuerServiceIndexDao;
import com.solution.issacq.issacqbusinessservices.dao.MerchantUserDao;
import com.solution.issacq.issacqbusinessservices.dao.MobileTransactionDao;
import com.solution.issacq.issacqbusinessservices.dao.MpaCustomerAccountDao;
import com.solution.issacq.issacqbusinessservices.dao.MpayCustomerProfileDao;
import com.solution.issacq.issacqbusinessservices.dao.MposDeviceProfileDao;
import com.solution.issacq.issacqbusinessservices.dao.MposMerchantAccountDao;
import com.solution.issacq.issacqbusinessservices.dao.MposMerchantProfileDao;
import com.solution.issacq.issacqbusinessservices.dao.NetworkCommunityDao;
import com.solution.issacq.issacqbusinessservices.dao.PresalesDao;
import com.solution.issacq.issacqbusinessservices.dao.SolutionDictionaryDataDao;
import com.solution.issacq.issacqbusinessservices.entities.AvailableTransaction;
import com.solution.issacq.issacqbusinessservices.entities.Currency;
import com.solution.issacq.issacqbusinessservices.entities.DeviceAccess;
import com.solution.issacq.issacqbusinessservices.entities.IssuerAccountProgram;
import com.solution.issacq.issacqbusinessservices.entities.IssuerServiceIndex;
import com.solution.issacq.issacqbusinessservices.entities.IssuerVelocityActivity;
import com.solution.issacq.issacqbusinessservices.entities.MerchantUser;
import com.solution.issacq.issacqbusinessservices.entities.MobileTransaction;
import com.solution.issacq.issacqbusinessservices.entities.MpaCustomerAccount;
import com.solution.issacq.issacqbusinessservices.entities.MpayCustomerProfile;
import com.solution.issacq.issacqbusinessservices.entities.MposDeviceProfile;
import com.solution.issacq.issacqbusinessservices.entities.MposMerchantAccount;
import com.solution.issacq.issacqbusinessservices.entities.MposMerchantProfile;
import com.solution.issacq.issacqbusinessservices.entities.NetworkCommunity;
import com.solution.issacq.issacqbusinessservices.entities.Presales;
import com.solution.issacq.issacqbusinessservices.entities.SolutionDictionaryData;
import com.solution.issacq.issacqbusinessservices.payloaddeclaration.CustomerAccount;
import com.solution.issacq.issacqbusinessservices.payloaddeclaration.InboundRequest;
import com.solution.issacq.issacqbusinessservices.payloaddeclaration.InboundResponse;
import com.solution.issacq.issacqbusinessservices.payloaddeclaration.InboundResponseData;
import com.solution.issacq.issacqbusinessservices.payloaddeclaration.MpayMobileTransaction;
import com.solution.issacq.issacqbusinessservices.services.IBasicService;
import com.solution.issacq.issacqbusinessservices.services.business.issuer.CheckIssuerProgramVelocity;
import com.solution.issacq.issacqbusinessservices.services.business.issuer.UpdateIssuerProgramVelocity;
import com.solution.issacq.issacqbusinessservices.utils.CashSettingGetterUtils;
import com.solution.issacq.issacqbusinessservices.utils.Constants;
import com.solution.issacq.issacqbusinessservices.utils.IDynamicInvoke;
import com.solution.issacq.issacqbusinessservices.utils.LockAccount;
import com.solution.issacq.issacqbusinessservices.utils.SolutionDictionaryListUtils;
import com.solution.issacq.issacqbusinessservices.utils.SolutionErrors;

@Component
public class SetTopUpOrderStatus implements IDynamicInvoke {
	
	@Autowired
	LockAccount lockAccount;
	
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	MobileTransactionDao mobileTransactionDao;

	@Autowired
	SolutionDictionaryDataDao solutionDictionaryDataDao;

	@Autowired
	MposDeviceProfileDao mposDeviceProfileDao;

	@Autowired
	MerchantUserDao merchantUserDao;

	@Autowired
	MpayCustomerProfileDao mpayCustomerProfileDao;

	@Autowired
	NetworkCommunityDao networkCommunityDao;

	@Autowired
	MpaCustomerAccountDao mpaCustomerAccountDao;

	@Autowired
	MposMerchantProfileDao mposMerchantProfileDao;

	@Autowired
	MposMerchantAccountDao mposMerchantAccountDao;

	@Autowired
	IssuerAccountProgramDao issuerAccountProgramDao;

	@Autowired
	IssuerServiceIndexDao issuerServiceIndexDao;

	@Autowired
	AvailableTransactionDao availableTransactionDao;

	@Autowired
	IBasicService cashSettingService;

	@Autowired
	DeviceAccessDao deviceAccessDao;

	@Autowired
	PresalesDao presalesDao;

	@Autowired
	CheckIssuerProgramVelocity checkIssuerProgramVelocity;

	@Autowired
	UpdateIssuerProgramVelocity updateIssuerProgramVelocity;

	// Function call Setter
	public void invokeSetter(Object obj, String propertyName, Object variableValue) {
		PropertyDescriptor pd;
		try {
			pd = new PropertyDescriptor(propertyName, obj.getClass());
			Method setter = pd.getWriteMethod();
			setter.invoke(obj, variableValue);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	// Function CREDIT and DEBIT Balance
	private void withdraw(Object object, String transactionSign, Double amount, Double balance) {
		if (transactionSign.equals(Constants.CREDIT)) {
			invokeSetter(object, "Balance", balance + amount);
		} else if (transactionSign.equals(Constants.DEBIT)) {
			invokeSetter(object, "Balance", balance - amount);
		}
	}

	private void setStatus(String statusReasonCode, String statusReason, Presales presales, String status) {
		SolutionDictionaryListUtils solutionDictionaryListUtils = new SolutionDictionaryListUtils();

		// Set status
		SolutionDictionaryData activeStatusDictionary = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(
				Constants.TRANSACTION_STATUS, status, cashSettingService.getSolutionDictionaryDataList(),
				cashSettingService.getSolutionDictionaryList());
		if (activeStatusDictionary == null) {
			return;
		}
		presales.setStatus(activeStatusDictionary.getCode());
		Date dateN = new Date();
		presales.setStatusDate(new Timestamp(dateN.getTime()));
		presales.setStatusReasonCode(statusReasonCode);
		presales.setStatusReason(statusReason);
		presalesDao.save(presales);
	}

	@Override
	@Transactional
	public void processPaylaod(InboundRequest inboundRequest, InboundResponse inboundResponse) {
		CashSettingGetterUtils cashSettingGetterUtils = new CashSettingGetterUtils();
		SolutionDictionaryListUtils solutionDictionaryListUtils = new SolutionDictionaryListUtils();
		SolutionDictionaryData solutionDictionaryData;
		MposMerchantProfile mposMerchantProfile;
		MerchantUser merchantUser = null;
		MposDeviceProfile mposDeviceProfile = null;

		// Validate the presale
		String uid = inboundRequest.getInboundRequestData().getUid();
		Presales presales = presalesDao.findOneByCode(uid);
		if (presales == null) {
			inboundResponse.setProcessingStatus(SolutionErrors.INVALID_JSON_ATTRIBUTE);
			inboundResponse.setProcessingStatusLabel("this presales does not exist");
			setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
					Constants.UNSUCCESS);
			return;
		}
		if (!presales.getStatus().equals(Constants.INPROGRESS)) {
			inboundResponse.setProcessingStatus(SolutionErrors.INVALID_TRANSACTION_STATUS);
			inboundResponse.setProcessingStatusLabel("Invalid code !");
			setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
					Constants.UNSUCCESS);
			return;
		}

		if (StringUtils.isEmpty(presales.getMerchantUserCode())) {
			// Validate the merchant code
			mposMerchantProfile = mposMerchantProfileDao.findOneByCode(presales.getMerchantCode());
			if (mposMerchantProfile == null) {
				inboundResponse.setProcessingStatus(SolutionErrors.MERCHANT_NOT_EXIST);
				inboundResponse.setProcessingStatusLabel("this Merchant does not exist");
				setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
						Constants.UNSUCCESS);
				return;
			}
			// Check status is active
			solutionDictionaryData = solutionDictionaryListUtils.getSolutionDictionaryDataByCode("merchantStatus",
					mposMerchantProfile.getStatusCode(), cashSettingService.getSolutionDictionaryDataList(),
					cashSettingService.getSolutionDictionaryList());
			if (!solutionDictionaryData.getCode().equals(Constants.ACTIVE)) {
				inboundResponse.setProcessingStatus(SolutionErrors.STATUS_NOT_ACTIVE);
				inboundResponse.setProcessingStatusLabel("Status not active");
				setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
						Constants.UNSUCCESS);
				return;
			}
		} else {
			// Validate the merchantUser code
			merchantUser = merchantUserDao.findOneByCode(presales.getMerchantUserCode());
			if (merchantUser == null) {
				inboundResponse.setProcessingStatus(SolutionErrors.MERCHANT_USER_NOT_EXIST);
				inboundResponse.setProcessingStatusLabel("this Merchant does not exist");
				setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
						Constants.UNSUCCESS);
				return;
			}
			// Check status is active
			solutionDictionaryData = solutionDictionaryListUtils.getSolutionDictionaryDataByCode("merchantStatus",
					merchantUser.getStatus(), cashSettingService.getSolutionDictionaryDataList(),
					cashSettingService.getSolutionDictionaryList());
			if (!solutionDictionaryData.getCode().equals(Constants.ACTIVE)) {
				inboundResponse.setProcessingStatus(SolutionErrors.STATUS_NOT_ACTIVE);
				inboundResponse.setProcessingStatusLabel("Status not active");
				setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
						Constants.UNSUCCESS);
				return;
			}
			mposMerchantProfile = mposMerchantProfileDao.findOneByCode(merchantUser.getMposMerchantProfile());
			if (mposMerchantProfile == null) {
				inboundResponse.setProcessingStatus(SolutionErrors.MERCHANT_NOT_EXIST);
				inboundResponse.setProcessingStatusLabel("this Merchant does not exist");
				setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
						Constants.UNSUCCESS);
				return;
			}
			// Check status is active
			solutionDictionaryData = solutionDictionaryListUtils.getSolutionDictionaryDataByCode("merchantStatus",
					mposMerchantProfile.getStatusCode(), cashSettingService.getSolutionDictionaryDataList(),
					cashSettingService.getSolutionDictionaryList());
			if (!solutionDictionaryData.getCode().equals(Constants.ACTIVE)) {
				inboundResponse.setProcessingStatus(SolutionErrors.STATUS_NOT_ACTIVE);
				inboundResponse.setProcessingStatusLabel("Status not active");
				setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
						Constants.UNSUCCESS);
				return;
			}
			if (!StringUtils.isEmpty(presales.getMposDeviceCode())) {
				// Validate the DeviceProfile code
				mposDeviceProfile = mposDeviceProfileDao.findOneByCode(presales.getMposDeviceCode());
				if (mposDeviceProfile == null) {
					inboundResponse.setProcessingStatus(SolutionErrors.DEVICE_NOT_EXIST);
					inboundResponse.setProcessingStatusLabel(
							"this device does not exist " + inboundRequest.getInboundRequestData().getmPosDeviceCode());
					setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(),
							presales, Constants.UNSUCCESS);
					return;
				}
				// Check status is active
				if (!mposDeviceProfile.getStatus().equals(Constants.ACTIVE)) {
					inboundResponse.setProcessingStatus(SolutionErrors.STATUS_NOT_ACTIVE);
					inboundResponse.setProcessingStatusLabel("Status of device not active");
					setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(),
							presales, Constants.UNSUCCESS);
					return;
				}
				// Validate the deviceAccess code
				DeviceAccess deviceAccess = deviceAccessDao.findOneByMposDeviceProfileCode(mposDeviceProfile.getCode());
				if (deviceAccess == null) {
					inboundResponse.setProcessingStatus(SolutionErrors.DEVICE_ACCESS_NOT_EXIST);
					inboundResponse.setProcessingStatusLabel("Device Access doesn't exist !");
					setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(),
							presales, Constants.UNSUCCESS);
					return;
				}
				if (!deviceAccess.getMerchantUser().equals(merchantUser.getCode())) {
					inboundResponse.setProcessingStatus(SolutionErrors.DEVICE_NOT_ALLOWED);
					inboundResponse.setProcessingStatusLabel("Device not allowed");
					setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(),
							presales, Constants.UNSUCCESS);
					return;
				}
			}
		}

		// Validate the CustomerProfile code
		MpayCustomerProfile mpayCustomerProfile = mpayCustomerProfileDao
				.findOneByCode(inboundRequest.getInboundRequestData().getMpayCustomerCode());
		if (mpayCustomerProfile == null) {
			inboundResponse.setProcessingStatus(SolutionErrors.CUSTOMER_NOT_EXIST);
			inboundResponse.setProcessingStatusLabel("this Customer does not exist");
			setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
					Constants.UNSUCCESS);
			return;
		}
		// Check status is active
		if (!mpayCustomerProfile.getStatus().equals(Constants.ACTIVE)) {
			inboundResponse.setProcessingStatus(SolutionErrors.STATUS_NOT_ACTIVE);
			inboundResponse.setProcessingStatusLabel("Status of customer Src not active");
			setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
					Constants.UNSUCCESS);
			return;
		}
		// Check if the Customer and Merchant belong to the same Network
		NetworkCommunity networkCommunityCustomer = networkCommunityDao
				.findOneByServiceProviderCode(mpayCustomerProfile.getServiceProvider());
		NetworkCommunity networkCommunityMerchant = networkCommunityDao
				.findOneByServiceProviderCode(mposMerchantProfile.getServiceProviderCode());
		if (!networkCommunityCustomer.getNetwork().equals(networkCommunityMerchant.getNetwork())) {
			inboundResponse.setProcessingStatus(SolutionErrors.NETWORK_COMMUNITY_MISMATCH);
			inboundResponse.setProcessingStatusLabel("Is not on the same network");
			setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
					Constants.UNSUCCESS);
			return;
		}

		// Validate the Currency
		Currency currency = cashSettingGetterUtils.getCurrencyIso3Alpha(
				inboundRequest.getInboundRequestData().getTransactionCurrency(), cashSettingService);
		if (currency == null) {
			inboundResponse.setProcessingStatus(SolutionErrors.INVALID_JSON_ATTRIBUTE);
			inboundResponse.setProcessingStatusLabel("Currency not found");
			setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
					Constants.UNSUCCESS);
			return;
		}

		// Get the account of Customer and Merchant by the currency of the transaction
//		String accountType = inboundRequest.getInboundRequestData().getAccountType();
		MpaCustomerAccount mpaCustomerAccount = mpaCustomerAccountDao
				.findOneByMpayCustomerProfileAndCurrencyAndAccountTypeIsNot(
						inboundRequest.getInboundRequestData().getMpayCustomerCode(), currency.getCode(), "CD");
		if (mpaCustomerAccount == null) {
			inboundResponse.setProcessingStatus(SolutionErrors.ACCOUNT_NOT_EXIST);
			inboundResponse.setProcessingStatusLabel("Customer account not found");
			setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
					Constants.UNSUCCESS);
			return;
		}
		lockAccount.lock(mpaCustomerAccount);
		
		MposMerchantAccount mposMerchantAccount = mposMerchantAccountDao
				.findOneByMposMerchantProfileAndCurrency(mposMerchantProfile.getCode(), currency.getCode());
		if (mposMerchantAccount == null) {
			inboundResponse.setProcessingStatus(SolutionErrors.ACCOUNT_NOT_EXIST);
			inboundResponse.setProcessingStatusLabel("Merchant account not found");
			setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
					Constants.UNSUCCESS);
			return;
		}
		lockAccount.lock(mposMerchantAccount);
		
		// Check customer account status is active
		if (!mpaCustomerAccount.getStatus().equals(Constants.ACTIVE)) {
			inboundResponse.setProcessingStatus(SolutionErrors.STATUS_NOT_ACTIVE);
			inboundResponse.setProcessingStatusLabel("Status of customer account not active");
			setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
					Constants.UNSUCCESS);
			return;
		}
		// Check merchant account status is active
		if (!mposMerchantAccount.getStatus().equals(Constants.ACTIVE)) {
			inboundResponse.setProcessingStatus(SolutionErrors.STATUS_NOT_ACTIVE);
			inboundResponse.setProcessingStatusLabel("Status of merchant account not active");
			setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
					Constants.UNSUCCESS);
			return;
		}

		// Ensure that transaction is available

		// Get Service Index from customer account
		IssuerServiceIndex issuerServiceIndex = issuerServiceIndexDao
				.findOneByCode(mpaCustomerAccount.getServiceIndex());
		// If not exist get the default service index from account program
		IssuerAccountProgram mpaCustomerAccountProgram = null;
		if (issuerServiceIndex == null) {
			mpaCustomerAccountProgram = issuerAccountProgramDao.findOneByCode(mpaCustomerAccount.getAccountProgram());
			issuerServiceIndex = issuerServiceIndexDao
					.findOneByCode(mpaCustomerAccountProgram.getDefaultServiceIndex());
		}
		// Test that transaction code exists among available transactions in issuer
		// service index
		AvailableTransaction topUpTransaction = availableTransactionDao.findOneByCode("TU");
		if (topUpTransaction == null || !issuerServiceIndex.getAvailableTransaction().contains(topUpTransaction.getCode())) {
			inboundResponse.setProcessingStatus("ERR-STUOS-0015");
			inboundResponse.setProcessingStatusLabel("Not Available Transaction !");
			setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
					Constants.UNSUCCESS);
			return;
		}

		Double balanceCustomer = mpaCustomerAccount.getBalance();
		Double balanceMerchant = mposMerchantAccount.getBalance();
		// Check that the account is having enough balance (balance > transaction
		// Amount)
		if (balanceMerchant < inboundRequest.getInboundRequestData().getTransactionAmount()) {
			inboundResponse.setProcessingStatus(SolutionErrors.INSUFFICIENT_FUNDS);
			inboundResponse.setProcessingStatusLabel("Insufficient funds !");
			setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
					Constants.UNSUCCESS);
			return;
		}

		Double amount = inboundRequest.getInboundRequestData().getTransactionAmount();
		// save the transaction
		MobileTransaction mobileTransaction = new MobileTransaction();
		mobileTransaction.setCode(RandomStringUtils.randomNumeric(8));
		mobileTransaction.setMpaSrcProfileId(mpayCustomerProfile.getCode());
		mobileTransaction.setMpaSrcAccountId(mpaCustomerAccount.getCode());
		mobileTransaction.setMpaDstProfileId(mposMerchantProfile.getCode());
		mobileTransaction.setMpaDstAccountId(mposMerchantAccount.getCode());
		mobileTransaction.setTransactionAmount(inboundRequest.getInboundRequestData().getTransactionAmount());
		mobileTransaction.setTransactionCurrency(inboundRequest.getInboundRequestData().getTransactionCurrency());
		mobileTransaction.setSrcBillingAmount(amount);
		mobileTransaction.setSrcBillingCurrency(currency.getIso3Alpha());
		mobileTransaction.setDstBillingAmount(amount);
		mobileTransaction.setDstBillingCurrency(currency.getIso3Alpha());
		mobileTransaction.setExternalTransactionReference(
				inboundRequest.getInboundRequestData().getExternalTransactionReference());
		mobileTransaction.setSrcLongitude(mpayCustomerProfile.getLastlongitude());
		mobileTransaction.setSrcLatitude(mpayCustomerProfile.getLastlatitude());

		// Formatting Balance without using apostrophe
		String pattern2 = "##0.00";
		DecimalFormatSymbols decimalFormatSymbols2 = new DecimalFormatSymbols();
		DecimalFormat decimalFormat2 = new DecimalFormat(pattern2, decimalFormatSymbols2);
		decimalFormat2.setDecimalSeparatorAlwaysShown(false);

		mobileTransaction
				.setSrcWording("From " + mposMerchantProfile.getFirstName() + " " + mposMerchantProfile.getLastName());
		if (!StringUtils.isEmpty(currency.getSymbol())) {
			mobileTransaction.setSrcFormattedAmount("+ " + currency.getSymbol() + decimalFormat2.format(amount));
		} else {
			mobileTransaction.setSrcFormattedAmount("+ " + currency.getIso2Alpha() + decimalFormat2.format(amount));
		}
		mobileTransaction
				.setDstWording("To " + mpayCustomerProfile.getFirstName() + " " + mpayCustomerProfile.getLastName());
		if (!StringUtils.isEmpty(currency.getSymbol())) {
			mobileTransaction.setDstFormattedAmount("- " + currency.getSymbol() + decimalFormat2.format(amount));
		} else {
			mobileTransaction.setDstFormattedAmount("- " + currency.getIso2Alpha() + decimalFormat2.format(amount));
		}

		Date date = new Date();
		Timestamp dateNow = new Timestamp(date.getTime());
		mobileTransaction.setSrcPostingDate(dateNow);
		mobileTransaction.setDstPostingDate(dateNow);

		SolutionDictionaryData solutionDictionaryDataRF = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(
				Constants.REFUND_FLAG, "NO", cashSettingService.getSolutionDictionaryDataList(),
				cashSettingService.getSolutionDictionaryList());
		if (solutionDictionaryDataRF != null) {
			mobileTransaction.setRefundFlag(solutionDictionaryDataRF.getCode());
		}
		SolutionDictionaryData solutionDictionaryDataS = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(
				Constants.TRANSACTION_STATUS, "C", cashSettingService.getSolutionDictionaryDataList(),
				cashSettingService.getSolutionDictionaryList());
		if (solutionDictionaryDataS != null) {
			mobileTransaction.setTransactionStatus(solutionDictionaryDataS.getCode());
		}
		mobileTransaction.setStatusDate(dateNow);
		mobileTransaction.setMposDeviceCode(mposDeviceProfile != null ? mposDeviceProfile.getCode() : null);
		mobileTransaction.setMposMerchantUserCode(merchantUser != null ? merchantUser.getCode() : null);
		mobileTransaction.setTransactionType(topUpTransaction.getCode());
		mobileTransaction.setMerchantName(mposMerchantProfile.getStoreName());
		mobileTransaction.setSrcRunningSettledBalance(mpaCustomerAccount.getBalance());
		mobileTransaction.setSrcRunningPendingBalance(mpaCustomerAccount.getPendingAuthorization());
		mobileTransaction.setSrcRunningAvailableBalance(
				mobileTransaction.getSrcRunningSettledBalance() - mobileTransaction.getSrcRunningPendingBalance());
		mobileTransaction.setDstRunningSettledBalance(mposMerchantAccount.getBalance());
		mobileTransaction.setDstRunningPendingBalance(mposMerchantAccount.getPendingAuthorization());
		mobileTransaction.setDstRunningAvailableBalance(
				mobileTransaction.getDstRunningSettledBalance() - mobileTransaction.getDstRunningPendingBalance());

		boolean checkVelocity1 = checkIssuerProgramVelocity.checkVelocity(mpaCustomerAccountProgram,
				mpayCustomerProfile, mpaCustomerAccount, mobileTransaction, inboundResponse, inboundRequest);
		if (!checkVelocity1) {
			setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
					Constants.UNSUCCESS);
			return;
		}

		// Credit the Mpay account (balance := balance + transaction amount)
		withdraw(mpaCustomerAccount, Constants.CREDIT, amount, balanceCustomer);
		// Debit the Merchant account (balance := balance - transaction amount)
		withdraw(mposMerchantAccount, Constants.DEBIT, amount, balanceMerchant);

		IssuerVelocityActivity issuerVelocityActivity = inboundRequest.getInboundRequestData()
				.getIssuerVelocityActivity();
		if (issuerVelocityActivity != null) {
			boolean updateVelocity = updateIssuerProgramVelocity.updateVelocity(issuerVelocityActivity,
					mpaCustomerAccount, mobileTransaction, inboundResponse);
			if (!updateVelocity) {
				setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
						Constants.UNSUCCESS);
				return;
			}
		}

		SolutionDictionaryData solutionDictionaryDataTS = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(
				Constants.SRC_TRANSACTION_SIGN, Constants.CREDIT, cashSettingService.getSolutionDictionaryDataList(),
				cashSettingService.getSolutionDictionaryList());
		if (solutionDictionaryDataTS != null) {
			mobileTransaction.setSrcTransactionSign(solutionDictionaryDataTS.getCode());
		}

		SolutionDictionaryData solutionDictionaryDataTS2 = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(
				Constants.DST_TRANSACTION_SIGN, Constants.DEBIT, cashSettingService.getSolutionDictionaryDataList(),
				cashSettingService.getSolutionDictionaryList());
		if (solutionDictionaryDataTS2 != null) {
			mobileTransaction.setDstTransactionSign(solutionDictionaryDataTS2.getCode());
		}

		String paymentMethod = inboundRequest.getInboundRequestData().getPaymentMethod();
		SolutionDictionaryData solutionDictionaryDataP = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(
				Constants.PAYMENT_METHOD, paymentMethod, cashSettingService.getSolutionDictionaryDataList(),
				cashSettingService.getSolutionDictionaryList());
		if (solutionDictionaryDataP != null) {
			mobileTransaction.setPaymentMethod(solutionDictionaryDataP.getCode());
		}

		SolutionDictionaryData solutionDictionaryDataScr = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(
				Constants.MPASRC_TYPE, Constants.MPAY, cashSettingService.getSolutionDictionaryDataList(),
				cashSettingService.getSolutionDictionaryList());
		if (solutionDictionaryDataScr != null) {
			mobileTransaction.setMpaSrcType(solutionDictionaryDataScr.getCode());
		}

		SolutionDictionaryData solutionDictionaryDataDst = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(
				Constants.MPADST_TYPE, Constants.MPOS, cashSettingService.getSolutionDictionaryDataList(),
				cashSettingService.getSolutionDictionaryList());
		if (solutionDictionaryDataDst != null) {
			mobileTransaction.setMpaDstType(solutionDictionaryDataDst.getCode());
		}

		mobileTransaction.setSrcTransactionId(inboundRequest.getInboundId());
		mobileTransaction.setDstTransactionId(inboundRequest.getInboundId());
		mobileTransaction.setMemo(inboundRequest.getInboundRequestData().getMemo());
		
		String authorizationCode = RandomStringUtils.randomAlphanumeric(6);
		mobileTransaction.setAuthorisationCode(authorizationCode);

		mobileTransactionDao.save(mobileTransaction);
		
		mpaCustomerAccountDao.save(mpaCustomerAccount);
		mposMerchantAccountDao.save(mposMerchantAccount);

		// Formatting Balance using apostrophe
		String pattern = "###,##0.00";
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setGroupingSeparator('\'');
//		ds.setDecimalSeparator(',');
		DecimalFormat decimalFormat = new DecimalFormat(pattern, decimalFormatSymbols);
		decimalFormat.setGroupingUsed(true);

		CustomerAccount customerAccount = new CustomerAccount();
		customerAccount.setCode(mpaCustomerAccount.getCode());
		customerAccount.setCurrencyIson(mpaCustomerAccount.getCurrency());
		customerAccount.setAccountType(mpaCustomerAccount.getAccountType());
		customerAccount.setBalance(decimalFormat2.format(mpaCustomerAccount.getBalance()));
		customerAccount.setFormattedBalance(decimalFormat.format(mpaCustomerAccount.getBalance()));
		customerAccount.setPendingAuthorization(decimalFormat2.format(mpaCustomerAccount.getPendingAuthorization()));
		customerAccount
				.setFormattedPendingAuthorization(decimalFormat.format(mpaCustomerAccount.getPendingAuthorization()));
		customerAccount.setAvailableBalance(
				decimalFormat2.format(mpaCustomerAccount.getBalance() - mpaCustomerAccount.getPendingAuthorization()));
		customerAccount.setFormattedAvailableBalance(
				decimalFormat.format(mpaCustomerAccount.getBalance() - mpaCustomerAccount.getPendingAuthorization()));
		customerAccount.setPendingDeposit(decimalFormat2.format(mpaCustomerAccount.getPendingDeposit()));
		customerAccount.setRewardsBalance(decimalFormat2.format(mpaCustomerAccount.getRewardsBalance()));
		customerAccount.setStatus(mpaCustomerAccount.getStatus());
		customerAccount.setStatusDate(mpaCustomerAccount.getStatusDate().toString());
		customerAccount.setProgramCode(mpaCustomerAccount.getAccountProgram());

		InboundResponseData inboundResponseData = new InboundResponseData();
		try {
			inboundResponseData.setCustomerAccount(customerAccount);

			Pageable sortedByCreatedOnDesc = PageRequest.of(0, 10, Sort.by("createdOn").descending());

			List<MobileTransaction> mobileTransactionList = mobileTransactionDao
					.findAllByMpaSrcProfileIdOrMpaDstProfileId(mpayCustomerProfile.getCode(),
							mpayCustomerProfile.getCode(), sortedByCreatedOnDesc);

			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			Gson gson = new Gson();

			String mobileTransactionListJson = gson.toJson(mobileTransactionList);

			List<MpayMobileTransaction> mpayMobileTransactionList;
			mpayMobileTransactionList = mapper.readValue(mobileTransactionListJson,
					new TypeReference<List<MpayMobileTransaction>>() {
					});

			// Set status active to Customer
			SolutionDictionaryData activeStatusDictionary = solutionDictionaryListUtils.getSolutionDictionaryDataByCode(
					Constants.TRANSACTION_STATUS, Constants.SUCCESS, cashSettingService.getSolutionDictionaryDataList(),
					cashSettingService.getSolutionDictionaryList());
			if (activeStatusDictionary == null) {
				inboundResponse.setProcessingStatus(SolutionErrors.INVALID_SOLUTION_SETTING);
				inboundResponse.setProcessingStatusLabel("Missing Setting A Status ");
				setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
						Constants.UNSUCCESS);
				return;
			}
			setStatus(Constants.PROCESSING_DONE, "Sucessfully Processed", presales, Constants.UNSUCCESS);
			inboundResponseData.setMpayMobileTransactionList(mpayMobileTransactionList);
			inboundResponse.setInboundResponseData(inboundResponseData);
			inboundResponse.setProcessingStatus(Constants.PROCESSING_DONE);
			inboundResponse.setProcessingStatusLabel("Sucessfully Processed");
			setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
					Constants.SUCCESS);
		} catch (IOException e) {
			inboundResponse.setProcessingStatus(SolutionErrors.ERROR_MAPPER);
			inboundResponse.setProcessingStatusLabel("Error Mapper");
			setStatus(inboundResponse.getProcessingStatus(), inboundResponse.getProcessingStatusLabel(), presales,
					Constants.UNSUCCESS);
		}

	}
}
