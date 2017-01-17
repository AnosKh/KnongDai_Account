package com.knongdai.account.entities.forms;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FrmSocailUser {
	
	@JsonProperty("EMAIL")
	private String email;
	
	@JsonProperty("PASSWORD")
	private String password;
	
	@JsonProperty("USERNAME")
	private String username;
	
	@JsonProperty("GENDER")
	private String gender;
	
	@JsonProperty("USER_IMAGE_URL")
	private String userImageUrl;
	
	@JsonProperty("POINT")
	private int point;
	
	@JsonProperty("SOCIAL_TYPE")
	private String socialType;
	
	@JsonProperty("SOCIAL_ID")
	private String socialId;
	
	@JsonProperty("SIGN_UP_WITH")
	private String signUpWith;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getUserImageUrl() {
		return userImageUrl;
	}

	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getSocialType() {
		return socialType;
	}

	public void setSocialType(String socialType) {
		this.socialType = socialType;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public String getSignUpWith() {
		return signUpWith;
	}

	public void setSignUpWith(String signUpWith) {
		this.signUpWith = signUpWith;
	}
}
