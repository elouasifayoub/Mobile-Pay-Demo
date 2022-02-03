package com.solution.issacq.Demo.entities;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "mobileTransaction")
@Table(name = "MOBILE_TRANSACTION")
public class MobileTransaction extends Identified {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7269087916051132901L;
	@Column(name = "transaction_type_ds")
	String transactionTypeDs;
	@Column(name = "transaction_type")
	String transactionType;
	@Column(name = "payment_method_ds")
	String paymentMethodDs;
	@Column(name = "payment_method")
	String paymentMethod;
	@Column(name = "mpa_src_type_ds")
	String mpaSrcTypeDs;
	@Column(name = "mpa_src_type")
	String mpaSrcType;
	@Column(name = "mpa_src_account_id")
	String mpaSrcAccountId;
	@Column(name = "mpa_src_profile_id")
	String mpaSrcProfileId;
	@Column(name = "mpa_dst_type_ds")
	String mpaDstTypeDs;
	@Column(name = "mpa_dst_type")
	String mpaDstType;
	@Column(name = "mpa_dst_account_id")
	String mpaDstAccountId;
	@Column(name = "mpa_dst_profile_id")
	String mpaDstProfileId;
	@Column(name = "mpos_device_code")
	String mposDeviceCode;
	@Column(name = "mpos_merchant_user_code")
	String mposMerchantUserCode;
	@Column(name = "transaction_currency")
	String transactionCurrency;
	@Column(name = "transaction_amount")
	Double transactionAmount;
	@Column(name = "received_cash_amount")
	Double receivedCashAmount;
	@Column(name = "cash_back_amount")
	Double cashBackAmount;
	@Column(name = "src_billing_amount")
	Double srcBillingAmount;
	@Column(name = "src_billing_currency")
	String srcBillingCurrency;
	@Column(name = "src_conversion_rate")
	Double srcConversionRate;
	@Column(name = "src_latitude")
	Double srcLatitude;
	@Column(name = "src_longitude")
	Double srcLongitude;
	@Column(name = "src_posting_date")
	Date srcPostingDate;
	@Column(name = "src_transaction_id")
	String srcTransactionId;
	@Column(name = "dst_billing_amount")
	Double dstBillingAmount;
	@Column(name = "dst_billing_currency")
	String dstBillingCurrency;
	@Column(name = "dst_conversion_rate")
	Double dstConversionRate;
	@Column(name = "dst_latitude")
	Double dstLatitude;
	@Column(name = "dst_longitude")
	Double dstLongitude;
	@Column(name = "dst_posting_date")
	Date dstPostingDate;
	@Column(name = "dst_transaction_id")
	String dstTransactionId;
	@Column(name = "tax_rate")
	Double taxRate;
	@Column(name = "tax_amount")
	Double taxAmount;
	@Column(name = "discount_amount")
	Double discountAmount;
	@Column(name = "fee_amount")
	Double feeAmount;
	@Column(name = "tip_amount")
	Double tipAmount;
	@Column(name = "refund_flag_ds")
	String refundFlagDs;
	@Column(name = "refund_flag")
	String refundFlag;
	@Column(name = "src_transaction_sign_ds")
	String srcTransactionSignDs;
	@Column(name = "src_transaction_sign")
	String srcTransactionSign;
	@Column(name = "dst_transaction_sign_ds")
	String dstTransactionSignDs;
	@Column(name = "dst_transaction_sign")
	String dstTransactionSign;
	@Column(name = "transaction_status_ds")
	String transactionStatusDs;
	@Column(name = "transaction_status")
	String transactionStatus;
	@Column(name = "status_date")
	Timestamp statusDate;
	@Column(name = "external_transaction_reference")
	String externalTransactionReference;
	@Column(name = "merchant_name")
	String merchantName;
	@Column(name = "src_running_available_balance")
	private Double srcRunningAvailableBalance;
	@Column(name = "src_running_settled_balance")
	private Double srcRunningSettledBalance;
	@Column(name = "src_running_pending_balance")
	private Double srcRunningPendingBalance;
	@Column(name = "dst_running_available_balance")
	private Double dstRunningAvailableBalance;
	@Column(name = "dst_running_settled_balance")
	private Double dstRunningSettledBalance;
	@Column(name = "dst_running_pending_balance")
	private Double dstRunningPendingBalance;
	@Column(name="SRC_WORDING")
	private String srcWording;
	@Column(name="DST_WORDING")
	private String dstWording;
	@Column(name="SRC_FORMATTED_AMOUNT")
	private String srcFormattedAmount;
	@Column(name="DST_FORMATTED_AMOUNT")
	private String dstFormattedAmount;
	@Column(name="MEMO")
	private String memo;
	@Column(name="order_status")
	private String orderStatus;
	@Column(name="dealer_code")
	private String dealerCode;
	@Column(name="authorisation_code")
    private String authorisationCode;
	@Column(name="additional_data")
    private String additionalData;
	@Column(name="network_additional_data")
    private String networkAdditionalData;
	@Column(name="geolocation")
    private String geolocation;

	public String getTransactionTypeDs() {
		return transactionTypeDs;
	}

	public void setTransactionTypeDs(String transactionTypeDs) {
		this.transactionTypeDs = transactionTypeDs;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getPaymentMethodDs() {
		return paymentMethodDs;
	}

	public void setPaymentMethodDs(String paymentMethodDs) {
		this.paymentMethodDs = paymentMethodDs;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getMpaSrcTypeDs() {
		return mpaSrcTypeDs;
	}

	public void setMpaSrcTypeDs(String mpaSrcTypeDs) {
		this.mpaSrcTypeDs = mpaSrcTypeDs;
	}

	public String getMpaSrcType() {
		return mpaSrcType;
	}

	public void setMpaSrcType(String mpaSrcType) {
		this.mpaSrcType = mpaSrcType;
	}

	public String getMpaSrcAccountId() {
		return mpaSrcAccountId;
	}

	public void setMpaSrcAccountId(String mpaSrcAccountId) {
		this.mpaSrcAccountId = mpaSrcAccountId;
	}

	public String getMpaSrcProfileId() {
		return mpaSrcProfileId;
	}

	public void setMpaSrcProfileId(String mpaSrcProfileId) {
		this.mpaSrcProfileId = mpaSrcProfileId;
	}

	public String getMpaDstTypeDs() {
		return mpaDstTypeDs;
	}

	public void setMpaDstTypeDs(String mpaDstTypeDs) {
		this.mpaDstTypeDs = mpaDstTypeDs;
	}

	public String getMpaDstType() {
		return mpaDstType;
	}

	public void setMpaDstType(String mpaDstType) {
		this.mpaDstType = mpaDstType;
	}

	public String getMpaDstAccountId() {
		return mpaDstAccountId;
	}

	public void setMpaDstAccountId(String mpaDstAccountId) {
		this.mpaDstAccountId = mpaDstAccountId;
	}

	public String getMpaDstProfileId() {
		return mpaDstProfileId;
	}

	public void setMpaDstProfileId(String mpaDstProfileId) {
		this.mpaDstProfileId = mpaDstProfileId;
	}

	public String getMposDeviceCode() {
		return mposDeviceCode;
	}

	public void setMposDeviceCode(String mposDeviceCode) {
		this.mposDeviceCode = mposDeviceCode;
	}

	public String getMposMerchantUserCode() {
		return mposMerchantUserCode;
	}

	public void setMposMerchantUserCode(String mposMerchantUserCode) {
		this.mposMerchantUserCode = mposMerchantUserCode;
	}

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Double getReceivedCashAmount() {
		return receivedCashAmount;
	}

	public void setReceivedCashAmount(Double receivedCashAmount) {
		this.receivedCashAmount = receivedCashAmount;
	}

	public Double getCashBackAmount() {
		return cashBackAmount;
	}

	public void setCashBackAmount(Double cashBackAmount) {
		this.cashBackAmount = cashBackAmount;
	}

	public Double getSrcBillingAmount() {
		return srcBillingAmount;
	}

	public void setSrcBillingAmount(Double srcBillingAmount) {
		this.srcBillingAmount = srcBillingAmount;
	}

	public String getSrcBillingCurrency() {
		return srcBillingCurrency;
	}

	public void setSrcBillingCurrency(String srcBillingCurrency) {
		this.srcBillingCurrency = srcBillingCurrency;
	}

	public Double getSrcConversionRate() {
		return srcConversionRate;
	}

	public void setSrcConversionRate(Double srcConversionRate) {
		this.srcConversionRate = srcConversionRate;
	}

	public Double getSrcLatitude() {
		return srcLatitude;
	}

	public void setSrcLatitude(Double srcLatitude) {
		this.srcLatitude = srcLatitude;
	}

	public Double getSrcLongitude() {
		return srcLongitude;
	}

	public void setSrcLongitude(Double srcLongitude) {
		this.srcLongitude = srcLongitude;
	}

	public Date getSrcPostingDate() {
		return srcPostingDate;
	}

	public void setSrcPostingDate(Date srcPostingDate) {
		this.srcPostingDate = srcPostingDate;
	}

	public String getSrcTransactionId() {
		return srcTransactionId;
	}

	public void setSrcTransactionId(String srcTransactionId) {
		this.srcTransactionId = srcTransactionId;
	}

	public Double getDstBillingAmount() {
		return dstBillingAmount;
	}

	public void setDstBillingAmount(Double dstBillingAmount) {
		this.dstBillingAmount = dstBillingAmount;
	}

	public String getDstBillingCurrency() {
		return dstBillingCurrency;
	}

	public void setDstBillingCurrency(String dstBillingCurrency) {
		this.dstBillingCurrency = dstBillingCurrency;
	}

	public Double getDstConversionRate() {
		return dstConversionRate;
	}

	public void setDstConversionRate(Double dstConversionRate) {
		this.dstConversionRate = dstConversionRate;
	}

	public Double getDstLatitude() {
		return dstLatitude;
	}

	public void setDstLatitude(Double dstLatitude) {
		this.dstLatitude = dstLatitude;
	}

	public Double getDstLongitude() {
		return dstLongitude;
	}

	public void setDstLongitude(Double dstLongitude) {
		this.dstLongitude = dstLongitude;
	}

	public Date getDstPostingDate() {
		return dstPostingDate;
	}

	public void setDstPostingDate(Date dstPostingDate) {
		this.dstPostingDate = dstPostingDate;
	}

	public String getDstTransactionId() {
		return dstTransactionId;
	}

	public void setDstTransactionId(String dstTransactionId) {
		this.dstTransactionId = dstTransactionId;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(Double feeAmount) {
		this.feeAmount = feeAmount;
	}

	public Double getTipAmount() {
		return tipAmount;
	}

	public void setTipAmount(Double tipAmount) {
		this.tipAmount = tipAmount;
	}

	public String getRefundFlagDs() {
		return refundFlagDs;
	}

	public void setRefundFlagDs(String refundFlagDs) {
		this.refundFlagDs = refundFlagDs;
	}

	public String getSrcTransactionSignDs() {
		return srcTransactionSignDs;
	}

	public void setSrcTransactionSignDs(String srcTransactionSignDs) {
		this.srcTransactionSignDs = srcTransactionSignDs;
	}

	public String getSrcTransactionSign() {
		return srcTransactionSign;
	}

	public void setSrcTransactionSign(String srcTransactionSign) {
		this.srcTransactionSign = srcTransactionSign;
	}

	public String getDstTransactionSignDs() {
		return dstTransactionSignDs;
	}

	public void setDstTransactionSignDs(String dstTransactionSignDs) {
		this.dstTransactionSignDs = dstTransactionSignDs;
	}

	public String getDstTransactionSign() {
		return dstTransactionSign;
	}

	public void setDstTransactionSign(String dstTransactionSign) {
		this.dstTransactionSign = dstTransactionSign;
	}

	public String getTransactionStatusDs() {
		return transactionStatusDs;
	}

	public void setTransactionStatusDs(String transactionStatusDs) {
		this.transactionStatusDs = transactionStatusDs;
	}

	public Timestamp getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Timestamp statusDate) {
		this.statusDate = statusDate;
	}

	public String getExternalTransactionReference() {
		return externalTransactionReference;
	}

	public void setExternalTransactionReference(String externalTransactionReference) {
		this.externalTransactionReference = externalTransactionReference;
	}

	public String getRefundFlag() {
		return refundFlag;
	}

	public void setRefundFlag(String refundFlag) {
		this.refundFlag = refundFlag;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Double getSrcRunningAvailableBalance() {
		return srcRunningAvailableBalance;
	}

	public void setSrcRunningAvailableBalance(Double srcRunningAvailableBalance) {
		this.srcRunningAvailableBalance = srcRunningAvailableBalance;
	}

	public Double getSrcRunningSettledBalance() {
		return srcRunningSettledBalance;
	}

	public void setSrcRunningSettledBalance(Double srcRunningSettledBalance) {
		this.srcRunningSettledBalance = srcRunningSettledBalance;
	}

	public Double getSrcRunningPendingBalance() {
		return srcRunningPendingBalance;
	}

	public void setSrcRunningPendingBalance(Double srcRunningPendingBalance) {
		this.srcRunningPendingBalance = srcRunningPendingBalance;
	}

	public Double getDstRunningAvailableBalance() {
		return dstRunningAvailableBalance;
	}

	public void setDstRunningAvailableBalance(Double dstRunningAvailableBalance) {
		this.dstRunningAvailableBalance = dstRunningAvailableBalance;
	}

	public Double getDstRunningSettledBalance() {
		return dstRunningSettledBalance;
	}

	public void setDstRunningSettledBalance(Double dstRunningSettledBalance) {
		this.dstRunningSettledBalance = dstRunningSettledBalance;
	}

	public Double getDstRunningPendingBalance() {
		return dstRunningPendingBalance;
	}

	public void setDstRunningPendingBalance(Double dstRunningPendingBalance) {
		this.dstRunningPendingBalance = dstRunningPendingBalance;
	}

	public String getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}

	public String getSrcWording() {
		return srcWording;
	}

	public void setSrcWording(String srcWording) {
		this.srcWording = srcWording;
	}

	public String getDstWording() {
		return dstWording;
	}

	public void setDstWording(String dstWording) {
		this.dstWording = dstWording;
	}

	public String getSrcFormattedAmount() {
		return srcFormattedAmount;
	}

	public void setSrcFormattedAmount(String srcFormattedAmount) {
		this.srcFormattedAmount = srcFormattedAmount;
	}

	public String getDstFormattedAmount() {
		return dstFormattedAmount;
	}

	public void setDstFormattedAmount(String dstFormattedAmount) {
		this.dstFormattedAmount = dstFormattedAmount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getNetworkAdditionalData() {
		return networkAdditionalData;
	}

	public void setNetworkAdditionalData(String networkAdditionalData) {
		this.networkAdditionalData = networkAdditionalData;
	}

	public String getAuthorisationCode() {
		return authorisationCode;
	}

	public void setAuthorisationCode(String authorisationCode) {
		this.authorisationCode = authorisationCode;
	}
	
	public String getGeolocation() {
		return geolocation;
	}
	
	public void setGeolocation(String geolocation) {
		this.geolocation = geolocation;
	}

}
