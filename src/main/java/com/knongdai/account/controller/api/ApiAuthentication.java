package com.knongdai.account.controller.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.knongdai.account.entities.SendMail;
import com.knongdai.account.entities.User;
import com.knongdai.account.entities.forms.FrmSocailUser;
import com.knongdai.account.entities.forms.FrmUpdateUser;
import com.knongdai.account.entities.forms.FrmUserRegister;
import com.knongdai.account.entities.forms.UserInfo;
import com.knongdai.account.entities.forms.UserLogin;
import com.knongdai.account.entities.forms.UserMobileLogin;
import com.knongdai.account.services.UserService;
import com.knongdai.account.utilities.StringRandom;
import com.knongdai.account.utilities.UtilSendMail;

@Controller
@RequestMapping("/knongdai/v1/authentication")
public class ApiAuthentication {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/login_with_scoial" , method = RequestMethod.POST , headers = "Accept=application/json")
	public ResponseEntity<Map<String , Object>> loginWithScoial(
			@RequestBody FrmSocailUser scoialUser , HttpServletResponse  response 
		){
		Map<String, Object> map = new HashMap<String , Object>();
		try{
			
			// TODO : If user doesn't have email it sets social id value to email. 
			if(scoialUser.getEmail() == null){
				System.out.println("User doesn't provide email, or user doesn't have email!");
				scoialUser.setEmail(scoialUser.getSocialId());
			}
			
			// TODO : Find user by email. If user existed It will login else Sign up and Login.
			UserLogin userLogin = new UserLogin();
			userLogin.setEmail(scoialUser.getEmail());
			UserInfo user = userService.findUserByUserEmailMobile(userLogin);

			if(user != null){
				// Login
				System.out.println("Login");
				map.put("MESSAGE", "Logined success!");
				map.put("STATUS_LOGIN", true);
				map.put("DATA", user);
			}else{
				// Sign up
				System.out.println("Login & Sign up");
				String password = new StringRandom().generateRandomString();
				scoialUser.setPassword(password);
				if(userService.insertUserWithScoial(scoialUser)){
					// Login
					map.put("MESSAGE", "You have been registered successfully.");
					map.put("STATUS_SIGNUP", true);
					map.put("DATA", userService.findUserByUserEmailMobile(userLogin));
				}else{
					map.put("MESSAGE", "Login & Sign up unsuccess!");
					map.put("STATUS_SIGNUP", false);
				}
				
			}
		}catch(Exception e){
			map.put("MESSAGE", "OPERATION FAIL");
			map.put("STATUS", false);
			e.printStackTrace();
		}
		return new ResponseEntity<Map<String , Object>>(map , HttpStatus.OK);
	} 
	
	
	@RequestMapping(value="/mobile/login" , method = RequestMethod.POST , headers = "Accept=application/json")
	public ResponseEntity<Map<String , Object>>  findUserByEmail(@RequestBody UserMobileLogin userLogin){
		Map<String, Object> map = new HashMap<String , Object>();
		UserInfo user= userService.findUserByUserEmailAndPassword(userLogin);
		try{
			if(user == null){
				map.put("MESSAGE", "Incorrect username or passwrd!");
				map.put("STATUS", false);
			}else{
				// 0: Inactive, 1: Active, 2: Deleted, 3: Locked
				if(user.getStatus().equalsIgnoreCase("0")){
					map.put("MESSAGE", "Your account is inactive! Please go to your email to active your account!");
					map.put("STATUS", false);
				}else if(user.getStatus().equalsIgnoreCase("1")){
					map.put("MESSAGE", "Login success!");
					map.put("STATUS", true);
					map.put("DATA", user);
				}else if(user.getStatus().equalsIgnoreCase("2")){
					map.put("MESSAGE", "Your account has been deleted.");
					map.put("STATUS", false);
				}else if(user.getStatus().equalsIgnoreCase
						("3")){
					map.put("MESSAGE", "Your account is locked.");
					map.put("STATUS", false);
				}
			}
		}catch(Exception e){
			map.put("MESSAGE", "OPERATION FAIL");
			map.put("STATUS", false);
			e.printStackTrace();
		}
		return new ResponseEntity<Map<String , Object>>(map , HttpStatus.OK);
	}
	
	@RequestMapping(value="/mobile/register", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> insertUserRegister(@RequestBody FrmUserRegister user){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			if(userService.isIntEmailExists(user.getEmail()) == 0){ //i.e this email does not exists yet
				
				String verification_code = UUID.randomUUID().toString();
				user.setVerification_code( verification_code );
				user.setUserImageUrl("http://api2.khmeracademy.org/resources/upload/file/user/avatar.jpg");
				if(userService.insertUserMobile(user)){
					
					UserInfo userInfo= userService.findUserByUserIdMobile(user.getUserId());
					map.put("USER_DATA", userInfo) ; 
					
					String title = "Knongdai - Email Confirmation";
					String kh_msg = "សូមស្វាគមន៍, \r\n"
							+ "គណនីរបស់អ្នកត្រូវបានបង្កើត⁣។ ដើម្បីដំនើរការ⁣ សូមចុចលើតំណរភ្អាប់ខាងក្រោម៖\r\n"
							+ "http://login.knongdai.com/welcome/confirm/"
							+ verification_code
							+ "\r\n"				
							+ "រីករាយក្នុងការប្រើប្រាស់សេវាកម្មរបស់យើងខ្ញំុ\r\n\r\n";

					String en_msg = "Welcome,\r\n"
							+ "Your account "
							+ user.getEmail()
							+ " has been created. To activate it, please confirm your email address: "
							+ "http://login.knongdai.com/welcome/confirm/"
							+ verification_code
							+ "\r\n" + "Have a great day.";
					String msg = kh_msg + en_msg;
					
					SendMail mail = new SendMail();
					mail.setTo(user.getEmail());
					mail.setSubject(title);
					mail.setBody(msg);
					
					if(new UtilSendMail().sendEmailToUser(mail)){
						map.put("MESSAGE", "You have been registered successfully. Please go to your email to active your account");
						map.put("STATUS", true);
					}else{
						map.put("MESSAGE", "Something went wrong! Please try to register again!");
						map.put("STATUS", false);
					}
					
				} else {
					map.put("STATUS", false);
					map.put("MESSAGE", "Something went wrong! Please try to register again!");
				}
			}else{
				map.put("STATUS", false);
				map.put("MESSAGE", "You’ve already registered with that email address. Please login");
			}
		} catch(Exception e){
			map.put("STATUS", false);
			map.put("MESSAGE", "OPERATION FAIL ! Something went wrong! Please try to register again!");
			e.printStackTrace();
			
		}
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	@RequestMapping(value="/mobile/update-user" , method = RequestMethod.POST , headers = "Accept=application/json")
	public ResponseEntity<Map<String , Object>>  updateUser(@RequestBody FrmUpdateUser user){
		Map<String, Object> map = new HashMap<String , Object>();
		try{
			if(userService.updateUser(user)){
				map.put("MESSAGE", "User has been updated!");
				map.put("STATUS", true);
			}else{
				map.put("MESSAGE", "User has not been updated!");
				map.put("STATUS", false);
			}
		}catch(Exception e){
			map.put("MESSAGE", "OPERATION FAIL");
			map.put("STATUS", false);
			e.printStackTrace();
		}
		return new ResponseEntity<Map<String , Object>>(map , HttpStatus.OK);
	}

}
