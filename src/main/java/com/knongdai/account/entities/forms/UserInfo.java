package com.knongdai.account.entities.forms;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.knongdai.account.entities.Role;

public class UserInfo {
	
	@JsonProperty("USER_ID")
	private int userId;
	
	@JsonProperty("USER_HASH")
	private String userHash;
	
	@JsonProperty("EMAIL")
	private String email;
	
	@JsonProperty("USERNAME")
	private String username;
	
	@JsonProperty("GENDER")
	private String gender;
	
	@JsonProperty("REGISTERED_DATE")
	private Date registeredDate;
	
	@JsonProperty("USER_IMAGE_URL")
	private String userImageUrl;
	
	@JsonProperty("PHONENUMBER")
	private String phonenumber;
	
	@JsonProperty("POINT")
	private int point;
	
	
	@JsonProperty("STATUS")
	private String status;
	
	@JsonProperty("ROLES")
	private List<Role> roles;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
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

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getUserHash() {
		return userHash;
	}

	public void setUserHash(String userHash) {
		this.userHash = userHash;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
