package com.knongdai.account.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.knongdai.account.entities.User;
import com.knongdai.account.services.UserService;
import com.mangofactory.swagger.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class AutoLoginController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/auto-login" , method= RequestMethod.GET)
	public String autoLogin(@RequestParam("user-hash") String userHash , @RequestParam(name="continue-site", required=false , defaultValue="http://www.knongdai.com") String continueSite , HttpServletResponse  response) {

		try{
		
			Cookie ck=new Cookie("KD_USER_HASH","");//deleting value of cookie  
			ck.setMaxAge(0);//changing the maximum age to 0 seconds  
			response.addCookie(ck);//adding cookie in the response  
			
			User user = userService.findUserByUserHash(userHash);

			if(user != null){
				System.out.println("Username : " + user.getUsername());
				
				Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
	
				SecurityContext context = new SecurityContextImpl();
				context.setAuthentication(authentication);
	
				SecurityContextHolder.setContext(context);
	
				ck=new Cookie("KD_USER_HASH",user.getUserHash());//deleting value of cookie  
				ck.setMaxAge(30 * 24 * 60 * 60 * 1000);//the maximum age to 1 month  
				response.addCookie(ck);//adding cookie in the response  
				
			}else{
				System.out.println("Username not found!" );
				return "login";
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return "redirect:"+continueSite;

	}
	
	/*@RequestMapping(value = "/isLogin", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> isUserLogin(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Cookie[] cookies = request.getCookies();
		String userId = null;
		for(Cookie cookie : cookies){
		    if("KNONG_DAI_USER_ID".equals(cookie.getName())){
		    	userId = cookie.getValue();
		    	map.put("USER_ID", userId);
		    	map.put("STATUS", true);
		    	break;
		    }
		}
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}*/
	
	/*@RequestMapping(value = "/kd_logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request,HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){    
		      new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		handleLogOutResponse(response,request);
		return "redirect:http://login.knongdai.com";
	}
	
	 
	 * This method would edit the cookie information and make JSESSIONID empty
	 * while responding to logout. This would further help in order to. This would help
	 * to avoid same cookie ID each time a person logs in
	 * @param response
	 
	private void handleLogOutResponse(HttpServletResponse response,HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			cookie.setMaxAge(0);
			cookie.setValue(null);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
	}*/
	
	
	
	


}