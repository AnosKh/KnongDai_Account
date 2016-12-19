package com.knongdai.account.controller.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;
import com.knongdai.account.entities.User;
import com.knongdai.account.entities.forms.FrmSocailUser;
import com.knongdai.account.entities.forms.UserLogin;
import com.knongdai.account.services.UserService;

@Controller
@RequestMapping(value = "/twitter")
public class TwitterContoller {
	
	private static Logger logger = LoggerFactory.getLogger(TwitterContoller.class);

	@Value("${KD_TWITTER_APP_API_KEY}")
	private   String YOUR_API_KEY ;
	
	@Value("${KD_TWITTER_API_SECRET}")
	private   String YOUR_API_SECRET ;

	@Value("${KD_HOST}")
	private   String HOST;
	
	
	private static final String CALLBACK_URL = "/twitter/callback";

	// API End point
	private static final String PROTECTED_RESOURCE_URL = "https://api.twitter.com/1.1/account/verify_credentials.json";
	
	private static String continueSite;
	private static String domain;

	@Autowired
	private HttpHeaders header;

	@Autowired
	private RestTemplate rest;

	@Autowired
	private String WS_URL;

	@Autowired
	private UserService userService;
	

	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public void signin(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("continue-site") String continueSiteParam) throws IOException {
		logger.debug("signin");

		domain = continueSiteParam;
		continueSite = "";
		System.out.println("continueSiteParam =====>>>>> " + continueSiteParam);

		if (!continueSiteParam.equalsIgnoreCase("")) {

			System.out.println("lastIndexOf =====>>>>> " + domain.substring(domain.lastIndexOf(".")));

			if (domain.substring(domain.lastIndexOf("."), domain.lastIndexOf(".") + 4).equalsIgnoreCase(".com")) {
				domain = "knongdai.com";
				HOST = "http://login.knongdai.com";
			} else {
				domain = "khmeracademy.org";
				HOST = "http://login.khmeracademy.org";
			}

			System.out.println("DOMAIN =====>>>>> " + domain + "  |  continueSite " + continueSite + "   |   "
					+ domain.substring(domain.lastIndexOf(".")));

			continueSite = continueSiteParam;

		} else {
			domain = "knongdai.com";
			continueSite = "http://www.knongdai.com";
		}

		System.out.println(YOUR_API_KEY);
		System.out.println(YOUR_API_SECRET);

		String secretState = "secret" + new Random().nextInt(999_999);
		request.getSession().setAttribute("SECRET_STATE", secretState);

		System.out.println("CALLBACK_URL =====>>>>> " + HOST + CALLBACK_URL);
		
		OAuthService service = new ServiceBuilder().provider(TwitterApi.Authenticate.class).apiKey(YOUR_API_KEY)
				.apiSecret(YOUR_API_SECRET).callback(HOST + CALLBACK_URL).build();

		Token requestToken = service.getRequestToken();
		String redirectURL = service.getAuthorizationUrl(requestToken);
		logger.info("redirectURL:{}", redirectURL);

		response.sendRedirect(redirectURL);
	}

	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public String callback(@RequestParam(value = "oauth_token", required = false) String oauth_token,
			@RequestParam(value = "oauth_verifier", required = false) String oauth_verifier, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		logger.debug("callback");
		logger.info("oauth_token:{}", oauth_token);
		logger.info("oauth_verifier:{}", oauth_verifier);

		String userHash = "";
		
		
		
		
		OAuthService service = new ServiceBuilder().provider(TwitterApi.Authenticate.class).apiKey(YOUR_API_KEY)
				.apiSecret(YOUR_API_SECRET).callback(HOST + CALLBACK_URL).build();

		final Verifier verifier = new Verifier(oauth_verifier);
		final Token requestToken = new Token(oauth_token, oauth_verifier);
		final Token accessToken = service.getAccessToken(requestToken, verifier);

		final OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service);
		service.signRequest(accessToken, oauthRequest);

		final Response resourceResponse = oauthRequest.send();

		logger.info("code:{}", resourceResponse.getCode());
		logger.info("body:{}", resourceResponse.getBody());
		logger.info("message:{}", resourceResponse.getMessage());

		final JSONObject obj = new JSONObject(resourceResponse.getBody());
		logger.info("json:{}", obj.toString());

		FrmSocailUser userScoial = new FrmSocailUser();
		
		userScoial.setEmail(obj.getString("id_str"));
		userScoial.setUsername(obj.getString("name"));
		userScoial.setUserImageUrl(obj.getString("profile_image_url"));
		userScoial.setSocialId(obj.getString("id_str"));
		userScoial.setSocialType("3");
		userScoial.setSignUpWith("0"); // 0 = Web; 1 = iOS ; 2 = AOS

		HttpEntity<Object> requestAPI = new HttpEntity<Object>(userScoial, header);
		ResponseEntity<Map> responsAPI = rest.exchange(WS_URL + "/v1/authentication/login_with_scoial", HttpMethod.POST,
				requestAPI, Map.class);

		Map<String, Object> map = (HashMap<String, Object>) responsAPI.getBody();

		if (map.get("USER") != null) {

			Map<String, Object> userMap = (HashMap<String, Object>) map.get("USER");

			UserLogin userLogin = new UserLogin();
			userLogin.setEmail((String) userMap.get("EMAIL"));
			User user = userService.findUserByEmail(userLogin);
			userHash = user.getUserHash();
			System.out.println(userMap);
			System.out.println("1 Email : " + (String) userMap.get("EMAIL"));

			Cookie ck = new Cookie("KD_USER_HASH", null);// deleting value of
															// cookie
			ck.setMaxAge(0);// changing the maximum age to 0 seconds
			ck.setPath("/");
			ck.setDomain(domain);
			response.addCookie(ck);// adding cookie in the response

			if (user != null) {
				System.out.println("2 Email : " + user.getEmail());

				if (!continueSite.equalsIgnoreCase("http://www.knongdai.com")) {
					continueSite += "/auto-login?user-hash=" + user.getUserHash() + "&continue-site=" + continueSite;
				}

				Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
						user.getAuthorities());

				SecurityContext context = new SecurityContextImpl();
				context.setAuthentication(authentication);

				SecurityContextHolder.setContext(context);

				Cookie[] cookies = request.getCookies();

				ck = new Cookie("KD_USER_HASH", user.getUserHash());// deleting
																	// value of
																	// cookie
				ck.setMaxAge( /* Day */ 1 * 24 * 60 * 60 * 1000);// the maximum
																	// age to 1
																	// month
				ck.setPath("/");
				ck.setDomain(domain);
				response.addCookie(ck);// adding cookie in the response

			} else {
				System.out.println("User not found!");
			}
		} else {
			System.out.println("Error");
		}

		request.getSession().setAttribute("FACEBOOK_ACCESS_TOKEN", accessToken);

		System.out.println("redirect:" + continueSite);

		return "redirect:" + continueSite;
	}

	

}