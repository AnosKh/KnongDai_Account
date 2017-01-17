package com.knongdai.account.controller.api;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.knongdai.account.SmtpMailSender;
import com.knongdai.account.entities.SendMail;
import com.knongdai.account.entities.User;
import com.knongdai.account.entities.UserRegister;
import com.knongdai.account.entities.forms.UserLogin;
import com.knongdai.account.services.UserService;
import com.knongdai.account.utilities.HttpCode;
import com.knongdai.account.utilities.HttpMessage;
import com.knongdai.account.utilities.ResponseRecord;
import com.knongdai.account.utilities.UtilSendMail;
import com.mangofactory.swagger.annotations.ApiIgnore;

import scala.annotation.meta.setter;

@ApiIgnore
@RestController
@RequestMapping("/knongdai/user")
public class ApiUserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseRecord<User> findUserByEmail(@RequestBody UserLogin userLogin){
		ResponseRecord<User> response = new ResponseRecord<>();
		User user= userService.findUserByEmail(userLogin);
		try{
			if(user == null){
				response.setCode(HttpCode.NOT_FOUND);
				response.setStatus(false);
				response.setMessage(HttpMessage.notFound());
			}else{
				response.setCode(HttpCode.OK);
				response.setStatus(true);
				response.setMessage(HttpMessage.found());
				response.setData(user);
			}
		}catch(Exception e){
			response.setCode(HttpCode.INTERNAL_SERVER_ERROR);
			response.setMessage(HttpMessage.error());
			e.printStackTrace();
		}
		return response;
	}
	
	@RequestMapping(value="/{user-id}" , method = RequestMethod.POST)
	public ResponseRecord<User> findUserByUserId(@PathVariable("user-id") String userId){
		ResponseRecord<User> response = new ResponseRecord<>();
		User user= userService.findUserByUserId(userId);
		try{
			if(user == null){
				response.setCode(HttpCode.NOT_FOUND);
				response.setStatus(false);
				response.setMessage(HttpMessage.notFound());
			}else{
				response.setCode(HttpCode.OK);
				response.setStatus(true);
				response.setMessage(HttpMessage.found());
				response.setData(user);
			}
		}catch(Exception e){
			response.setCode(HttpCode.INTERNAL_SERVER_ERROR);
			response.setMessage(HttpMessage.error());
			e.printStackTrace();
		}
		return response;
	}
	
	@RequestMapping(value="/user-hash/{user-hash}" , method = RequestMethod.POST)
	public ResponseRecord<User> findUserByUserHash(@PathVariable("user-hash") String userHash){
		ResponseRecord<User> response = new ResponseRecord<>();
		User user= userService.findUserByUserHash(userHash);
		try{
			if(user == null){
				response.setCode(HttpCode.NOT_FOUND);
				response.setStatus(false);
				response.setMessage(HttpMessage.notFound());
			}else{
				response.setCode(HttpCode.OK);
				response.setStatus(true);
				response.setMessage(HttpMessage.found());
				response.setData(user);
			}
		}catch(Exception e){
			response.setCode(HttpCode.INTERNAL_SERVER_ERROR);
			response.setMessage(HttpMessage.error());
			e.printStackTrace();
		}
		return response;
	}
	
	// Update User status when verifying email -- Writer: Ean Sokchomrern, Date: 15/09/2016
	@RequestMapping(value="/updateUserVerifyEmail/{verification_code}", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> updateUserVerifyEmail(@PathVariable String verification_code){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			if(userService.updateUserVerifyEmail(verification_code)){
				map.put("STATUS",true);
				map.put("MESSAGE","Update Successfully");
			} else {
				map.put("STATUS", false);
				map.put("MESSAGE", "Update Unsuccessfully");
			} 
		} catch(Exception e){
			map.put("STATUS", false);
			map.put("MESSAGE","Error");
			e.printStackTrace();
		}
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	// Send mail
//	@Autowired
//	private SmtpMailSender smtpMailSender;	
	@RequestMapping(value="/send-mail",method=RequestMethod.POST)	
	public ResponseEntity<Map<String,Object>> sendMail(@RequestBody SendMail sendMail) {
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			if(new UtilSendMail().sendEmailToUser(sendMail)){
				map.put("MESSAGE", "Sending mail successfully");
				map.put("STATUS", true);
			}else{
				map.put("MESSAGE", "Sending mail not successfully");
				map.put("STATUS", false);
			}
			/*smtpMailSender.send(
				sendMail.getTo(), 
				sendMail.getSubject(), 
				sendMail.getBody());*/
				// mail is sent
				
				System.out.println("Send Email");
		} catch(Exception ex){
				map.put("MESSAGE", "Sending mail unsuccessfully");
				map.put("STATUS", false);
				ex.printStackTrace();
		}
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	// Register new user - Ean Sokchomrern, 16/09/2016
	@RequestMapping(value="/insert-user-register", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> insertUserRegister(@RequestBody UserRegister user){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			if(userService.isIntEmailExists(user.getEmail()) == 0){ //i.e this email does not exists yet
				if(userService.insertUserRegister(user)){
					map.put("STATUS",true);
					map.put("MESSAGE","Insert Successfully");
				} else {
					map.put("STATUS", false);
					map.put("MESSAGE", "Insert Unsuccessfully");
				}
			}else{
				map.put("STATUS", false);
				map.put("MESSAGE", "You’ve already registered with that email address.");
			}
		} catch(Exception e){
			map.put("STATUS", false);
			map.put("MESSAGE","Error");
			e.printStackTrace();
			
		}
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	// find email address is existed in database or not. If so, return verification code. Ean Sokchomrern (19/09/2016)
	@RequestMapping(value="/get-verification-code-by-email/{email}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getVerificationCodeByEmail(@PathVariable String email){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			
			if(!userService.getVerificationCodeByEmail(email).isEmpty()){
				
				map.put("STATUS",true);
				map.put("MESSAGE","Yes, User exists in database");
				map.put("DATA",userService.getVerificationCodeByEmail(email) );
			} else {
				map.put("STATUS", false);
				map.put("MESSAGE", "No, User does not exist in database");
			} 
		} catch(Exception e){
			map.put("STATUS", false);
			map.put("MESSAGE","Error");
			e.printStackTrace();
			
		}
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	// Update password by verification_code. Ean Sokchomrern (20/09/2016)
	@RequestMapping(value="/update-user-password", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> updateUserPassword(@RequestBody UserRegister user){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			if(userService.updateUserPassword(user)){
				map.put("STATUS",true);
				map.put("MESSAGE","Update Successfully");
			} else {
				map.put("STATUS", false);
				map.put("MESSAGE", "Update Unsuccessfully");
			} 
		} catch(Exception e){
			map.put("STATUS", false);
			map.put("MESSAGE","Error");
			e.printStackTrace();
		}
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	
}
