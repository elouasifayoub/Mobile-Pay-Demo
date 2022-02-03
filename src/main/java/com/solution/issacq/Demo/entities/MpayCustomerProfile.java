package com.solution.issacq.Demo.entities;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "mpayCustomerProfile")
@Table(name = "MPAY_CUSTOMER_PROFILE")
public class MpayCustomerProfile extends Identified {

	/**
	* 
	*/
	private static final long serialVersionUID = -2014310550426937203L;

	@Column(name = "external_customer_id")
	private String externalCustomerId;
	
	@Column(name = "service_provider_code")
	private String serviceProvider;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "email")
	private String email;

	@Column(name = "family_status")
	private String familyStatus;

	@Column(name = "gender")
	private String gender;

	@Column(name = "IS_GEO_LOCATION_MASKED")
	private Boolean isGeoLocationMasked;

	@Column(name = "IS_MPA_MASKED_TO_ALL")
	private Boolean isMpaMaskedToAll;

	@Column(name = "IS_FREIND_ALLOWED")
	private Boolean isFreindAllowed;
		
	@Column(name = "title")
	private String title;

	@Column(name = "cvmType")
	private String cvmType;

	@Column(name = "cvm_Value")
	private String cvmValue;

	@Column(name = "date_of_birth")
	private Date dateOfBirth;

	/* To Do: Define Language List */
	@Column(name = "prefered_language")
	private String preferedLanguage;

	@Column(name = "nationality_code")
	private String nationalityCode;

	@Column(name = "legal_id1_type")
	private String legalId1Type;

	@Column(name = "legal_id1")
	private String legalId1;

	@Column(name = "legal_id2_type")
	private String legalId2Type;

	@Column(name = "legal_id2")
	private String legalId2;

	@Column(name = "registration_date")
	private Timestamp registrationDate;

	@Column(name = "activation_date")
	private Timestamp activationDate;

	@Column(name = "customer_status")
	private String status;

	@Column(name = "customer_status_date")
	private Timestamp statusDate;

	@Column(name = "status_reason_code")
	private String statusReason;

	@Column(name = "corporate_id")
	private String corporateId;

	@Column(name = "share_location_status")
	private Boolean shareLocationStatus;
	
	@Column(name = "LASTLONGITUDE")
	private Double lastlongitude;
	
	@Column(name = "LASTLATITUDE")
	private Double lastlatitude;
	
	@Column(name = "iban")
	private String iban;
	
	@Column(name = "swift_code")
	private String swiftCode;
	
	@Column(name = "SECRET_QUESTIONS")
	private String secretQuestions;

	@Column(name = "additional_data")
	String additionalData;
	
	@Column(name = "phone_code")
	private String phoneCode;
	
	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "customer_type")
	private String customerType;

	@Column(name = "is_document_verified")
	private String isDocumentVerified;
	
	@Column(name = "account_verification_state")
	private String accountVerificationState;

	@Column(name = "account_verification_state_ds")
	private String accountVerificationStateDs;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "image_format")
	private String imageFormat;
	
	@Column(name="first_login_code")
	private String firstLoginCode;
	
	@Column(name="authentication_date")
	private	Timestamp authenticationDate;
	
	@Column(name="expiry_date")
    private Date expiryDate;
	
	@Column(name="is_authenticated")
	private Boolean isAuthenticated;

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getExternalCustomerId() {
		return externalCustomerId;
	}

	public void setExternalCustomerId(String externalCustomerId) {
		this.externalCustomerId = externalCustomerId;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFamilyStatus() {
		return familyStatus;
	}

	public void setFamilyStatus(String familyStatus) {
		this.familyStatus = familyStatus;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Boolean getIsGeoLocationMasked() {
		return isGeoLocationMasked;
	}

	public void setIsGeoLocationMasked(Boolean isGeoLocationMasked) {
		this.isGeoLocationMasked = isGeoLocationMasked;
	}

	public Boolean getIsMpaMaskedToAll() {
		return isMpaMaskedToAll;
	}

	public void setIsMpaMaskedToAll(Boolean isMpaMaskedToAll) {
		this.isMpaMaskedToAll = isMpaMaskedToAll;
	}

	public Boolean getIsFreindAllowed() {
		return isFreindAllowed;
	}

	public void setIsFreindAllowed(Boolean isFreindAllowed) {
		this.isFreindAllowed = isFreindAllowed;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCvmType() {
		return cvmType;
	}

	public void setCvmType(String cvmType) {
		this.cvmType = cvmType;
	}

	public String getCvmValue() {
		return cvmValue;
	}

	public void setCvmValue(String cvmValue) {
		this.cvmValue = cvmValue;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPreferedLanguage() {
		return preferedLanguage;
	}

	public void setPreferedLanguage(String preferedLanguage) {
		this.preferedLanguage = preferedLanguage;
	}

	public String getNationalityCode() {
		return nationalityCode;
	}

	public void setNationalityCode(String nationalityCode) {
		this.nationalityCode = nationalityCode;
	}

	public String getLegalId1() {
		return legalId1;
	}

	public void setLegalId1(String legalId1) {
		this.legalId1 = legalId1;
	}

//	public String getLegalId1Type() {
//		return legalId1Type;
//	}
//
//	public void setLegalId1Type(String legalId1Type) {
//		this.legalId1Type = legalId1Type;
//	}

//	public String getLegalId2Type() {
//		return legalId2Type;
//	}
//
//	public void setLegalId2Type(String legalId2Type) {
//		this.legalId2Type = legalId2Type;
//	}

	public void setStatusDate(Timestamp statusDate) {
		this.statusDate = statusDate;
	}

	public String getLegalId2() {
		return legalId2;
	}

	public void setLegalId2(String legalId2) {
		this.legalId2 = legalId2;
	}

	public Timestamp getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Timestamp registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Timestamp getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Timestamp activationDate) {
		this.activationDate = activationDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getStatusDate() {
		return statusDate;
	}

	public void setCustomerStatusDate(Timestamp statusDate) {
		this.statusDate = statusDate;
	}

	public String getStatusReason() {
		return statusReason;
	}

	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}

	public String getCorporateId() {
		return corporateId;
	}

	public void setCorporateId(String corporateId) {
		this.corporateId = corporateId;
	}

	public Boolean getShareLocationStatus() {
		return shareLocationStatus;
	}

	public void setShareLocationStatus(Boolean shareLocationStatus) {
		this.shareLocationStatus = shareLocationStatus;
	}

	public Double getLastlongitude() {
		return lastlongitude;
	}

	public void setLastlongitude(Double lastlongitude) {
		this.lastlongitude = lastlongitude;
	}

	public Double getLastlatitude() {
		return lastlatitude;
	}

	public void setLastlatitude(Double lastlatitude) {
		this.lastlatitude = lastlatitude;
	}

	public String getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}

	public String getSecretQuestions() {
		return secretQuestions;
	}

	public void setSecretQuestions(String secretQuestions) {
		this.secretQuestions = secretQuestions;
	}
	
	public String getPhoneCode() {
		return phoneCode;
	}
	
	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}
	
	public String getMobileNumber() {
		return mobileNumber;
	}
	
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	
	public String getIsDocumentVerified() {
		return isDocumentVerified;
	}

	public void setIsDocumentVerified(String isDocumentVerified) {
		this.isDocumentVerified = isDocumentVerified; 
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getLegalId1Type() {
		return legalId1Type;
	}

	public void setLegalId1Type(String legalId1Type) {
		this.legalId1Type = legalId1Type;
	}

	public String getLegalId2Type() {
		return legalId2Type;
	}

	public void setLegalId2Type(String legalId2Type) {
		this.legalId2Type = legalId2Type;
	}

	public String getAccountVerificationState() {
		return accountVerificationState;
	}

	public void setAccountVerificationState(String accountVerificationState) {
		this.accountVerificationState = accountVerificationState;
	}

	public String getAccountVerificationStateDs() {
		return this.accountVerificationStateDs;
	}
	
	public void setAccountVerificationStateDs(String accountVerificationStateDs) {
		this.accountVerificationStateDs = accountVerificationStateDs;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getImageFormat() {
		return imageFormat;
	}

	public void setImageFormat(String profileImageFormat) {
		this.imageFormat = profileImageFormat;
	}
	
	public String getFirstLoginCode() {
		return firstLoginCode;
	}
	
	public void setFirstLoginCode(String firstLoginCode) {
		this.firstLoginCode = firstLoginCode;
	}
	
	public Timestamp getAuthenticationDate() {
		return authenticationDate;
	}
	
	public void setAuthenticationDate(Timestamp authenticationDate) {
		this.authenticationDate = authenticationDate;
	}
	
	public Date getExpiryDate() {
		return expiryDate;
	}
	
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public Boolean IsAuthenticated() {
		return this.isAuthenticated;
	}
	
	public void setIsAuthenticated(Boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

}
