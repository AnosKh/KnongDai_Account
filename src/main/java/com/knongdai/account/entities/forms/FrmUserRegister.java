package com.knongdai.account.entities.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class FrmUserRegister {

    @ApiModelProperty(hidden = true)
	@JsonProperty("USER_ID")
	private int userId;
	
	@JsonProperty("EMAIL")
	private String email;

	@JsonProperty("USERNAME")
	private String username;

	@JsonProperty("PASSWORD")
	private String password;

	@JsonProperty("GENDER")
	private String gender;

	@JsonProperty("PHONENUMBER")
	private String phonenumber;

	@JsonProperty("SIGN_UP_WITH")
	private String signUpWith;

	@JsonProperty("USER_IAMGE_URL")
	private String userImageUrl;

	// new field store verification_code ((Ean Sokchomrern, 15/09/2016)
	@ApiModelProperty(hidden = true)
	@JsonProperty("VERIFICATION_CODE")
	private String verification_code;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getSignUpWith() {
		return signUpWith;
	}

	public void setSignUpWith(String signUpWith) {
		this.signUpWith = signUpWith;
	}

	public String getUserImageUrl() {
		return userImageUrl;
	}

	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}

	public String getVerification_code() {
		return verification_code;
	}

	public void setVerification_code(String verification_code) {
		this.verification_code = verification_code;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	
}
