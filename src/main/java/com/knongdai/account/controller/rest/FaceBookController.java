package com.knongdai.account.controller.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.github.scribejava.apis.FacebookApi;
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
import com.mangofactory.swagger.annotations.ApiIgnore;

@ApiIgnore
@Controller
@RequestMapping(value = "/facebook")
@PropertySource(value = { "classpath:configuration.properties" })
public class FaceBookController {

	@Autowired
	private Environment environment;

	private static Logger logger = LoggerFactory.getLogger(FaceBookController.class);

	@Value("${KD_FACEBOOK_APP_API_KEY}")
	private String YOUR_API_KEY;
	@Value("${KD_FACEBOOK_API_SECRET}")
	private String YOUR_API_SECRET;
	@Value("${KD_HOST}")
	private String HOST;
	private static String CALLBACK_URL = "/facebook/callback";

	private static final Token EMPTY_TOKEN = null;

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

	// permission scope
	private static final List<String> SCOPES = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("public_profile");
			add("user_birthday");
			add("email");
		}
	};

	// API End point
	private static final String USER_PROFILE_API = "https://graph.facebook.com/v2.8/me";
	private static final String QUERY = "?fields=id,name,first_name,last_name,gender,email";

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

		OAuthService service = new ServiceBuilder().provider(FacebookApi.class).apiKey(YOUR_API_KEY)
				.apiSecret(YOUR_API_SECRET).callback(HOST + CALLBACK_URL).scope(String.join(",", SCOPES))
				.state(secretState).grantType("code").connectTimeout(10).build();

		String redirectURL = service.getAuthorizationUrl(EMPTY_TOKEN);

		logger.info("redirectURL:{}", redirectURL);

		response.sendRedirect(redirectURL);
	}

	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public String callback(@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "state", required = false) String state, HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("callback");
		logger.info("code:{}", code);
		logger.info("state:{}", state);

		String secretState = (String) request.getSession().getAttribute("SECRET_STATE");
		String userHash = "";

		System.out.println("callback : " + domain);

		if (secretState.equals(state)) {
			logger.info("State value does match!");
		} else {
			logger.error("Ooops, state value does not match!");
		}

		OAuthService service = new ServiceBuilder().provider(FacebookApi.class).apiKey(YOUR_API_KEY)
				.apiSecret(YOUR_API_SECRET).callback(HOST + CALLBACK_URL).build();

		final String requestUrl = USER_PROFILE_API + QUERY;

		final Verifier verifier = new Verifier(code);
		final Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);

		final OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, requestUrl, service);
		service.signRequest(accessToken, oauthRequest);

		final Response resourceResponse = oauthRequest.send();

		logger.info("code:{}", resourceResponse.getCode());
		logger.info("body:{}", resourceResponse.getBody());
		logger.info("message:{}", resourceResponse.getMessage());

		final JSONObject obj = new JSONObject(resourceResponse.getBody());
		logger.info("json:{}", obj.toString());

		
		FrmSocailUser userScoial = new FrmSocailUser();
		try{
			userScoial.setEmail(obj.getString("email"));
		}catch(Exception e){
			userScoial.setEmail(obj.getString("id"));
			e.printStackTrace();
		}
		
		
		
		
		
		userScoial.setUsername(obj.getString("name"));
		userScoial.setGender(obj.getString("gender"));
		userScoial.setUserImageUrl("http://graph.facebook.com/" + obj.getString("id") + "/picture?type=large");
		userScoial.setSocialId(obj.getString("id"));
		userScoial.setSocialType("2");
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
