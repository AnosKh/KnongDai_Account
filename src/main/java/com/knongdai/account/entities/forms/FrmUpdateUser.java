package com.knongdai.account.entities.forms;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class FrmUpdateUser {
	
	@JsonProperty("USER_ID")
	private int userId;
	
	@JsonProperty("USERNAME")
	private String username;

	@JsonProperty("GENDER")
	private String gender;

	@JsonProperty("PHONENUMBER")
	private String phonenumber;

	@JsonProperty("USER_IAMGE_URL")
	private String userImageUrl;
	
	@JsonProperty("DATE_OF_BIRTH")
	private Date dateOfBirth;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getUserImageUrl() {
		return userImageUrl;
	}

	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	
	
}
