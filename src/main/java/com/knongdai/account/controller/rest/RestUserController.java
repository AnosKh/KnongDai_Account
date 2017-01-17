package com.knongdai.account.controller.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.knongdai.account.entities.SendMail;
import com.knongdai.account.entities.UserRegister;
import com.knongdai.account.entities.forms.UserLogin;
import com.mangofactory.swagger.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class RestUserController {

	@Autowired
	private HttpHeaders header;
	
	@Autowired
	private RestTemplate rest;
	
	@Autowired
	private String WS_URL;
	
	@RequestMapping(value="/rest/user" , method = RequestMethod.POST)
	public ResponseEntity<Map<String , Object>> findUserByEmail(@RequestBody UserLogin userLogin){
		HttpEntity<Object> request = new HttpEntity<Object>(userLogin,header);
		ResponseEntity<Map> response = rest.exchange(WS_URL+"/user", HttpMethod.POST , request , Map.class) ;
		return new ResponseEntity<Map<String , Object>>(response.getBody() , HttpStatus.OK);
	}
	
	// Update User status when verifying email -- Writer: Ean Sokchomrern, Date: 15/09/2016
	@RequestMapping(value="/rest/user/updateUserVerifyEmail/{verification_code}", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> updateUserVerifyEmail(@PathVariable String verification_code){
		HttpEntity<Object> request = new HttpEntity<Object>(header);
		ResponseEntity<Map> response = rest.exchange(WS_URL+"/user/updateUserVerifyEmail/"+verification_code, 
				HttpMethod.PUT, request, Map.class);
		return new ResponseEntity<Map<String, Object>>(response.getBody(), HttpStatus.OK);
	}
	
	
	// send mail to verify email
	@RequestMapping(value="/rest/user/send-mail", method = RequestMethod.POST)
	public ResponseEntity<Map<String , Object>> sendMail(@RequestBody SendMail sendMail){
		HttpEntity<Object> request = new HttpEntity<Object>(sendMail,header);
		ResponseEntity<Map> response = rest.exchange(WS_URL + "/user/send-mail", HttpMethod.POST , request , Map.class) ;
		return new ResponseEntity<Map<String , Object>>(response.getBody() , HttpStatus.OK);
	}
	
	// Register new user - Ean Sokchomrern, 16/09/2016
	@RequestMapping(value="/rest/user/insert-user-register", method = RequestMethod.POST)
	public ResponseEntity<Map<String , Object>> insertUserRegister(@RequestBody UserRegister user){
		HttpEntity<Object> request = new HttpEntity<Object>(user,header);
		ResponseEntity<Map> response = rest.exchange(WS_URL + "/user/insert-user-register", HttpMethod.POST , request , Map.class) ;
		return new ResponseEntity<Map<String , Object>>(response.getBody() , HttpStatus.OK);
	}
	
	// find email address is existed in database or not. Ean Sokchomrern (19/09/2016)
	@RequestMapping(value="/rest/user/get-verification-code-by-email/{email}", method = RequestMethod.GET)
	public ResponseEntity<Map<String , Object>> getVerificationCodeByEmail(@PathVariable String email){
		HttpEntity<Object> request = new HttpEntity<Object>(header);
		ResponseEntity<Map> response = rest.exchange(WS_URL + "/user/get-verification-code-by-email/"+email, HttpMethod.GET , request , Map.class) ;
		return new ResponseEntity<Map<String , Object>>(response.getBody() , HttpStatus.OK);
	}
		
	// Update password by verification_code. Ean Sokchomrern (20/09/2016)
	@RequestMapping(value="/rest/user/update-user-password", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> updateUserPassword(@RequestBody UserRegister user){
		HttpEntity<Object> request = new HttpEntity<Object>(user, header);
		ResponseEntity<Map> response = rest.exchange(WS_URL + "/user/update-user-password", HttpMethod.PUT , request , Map.class) ;
		return new ResponseEntity<Map<String, Object>>(response.getBody(), HttpStatus.OK);
	}	
	
	
		
}
