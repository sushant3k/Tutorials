/**
 * 
 */
package com.learning.oauth2.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.learning.oauth2.error.objects.FacebookOAuthError;
import com.learning.oauth2.security.filter.AuthenticatedUser;
import com.learning.oauth2.security.filter.FacebookUser;

/**
 * @author Sushant
 *
 */
@Component(value="facebookOAuthService")
public class FacebookOAuthServiceImpl implements FacebookOAuthService {

	
	
	/* (non-Javadoc)
	 * @see com.hsc.security.oauth2.service.LinkedinOAuthService#getURIForAuthorizationCode(java.lang.String)
	 */
	@Override
	public String getURIForAuthorizationCode(String redirectURI) {
		String url = UriComponentsBuilder.fromHttpUrl(FB_AUTHORIZATION_URI).	    		
				queryParam("client_id", API_KEY).				
				queryParam("redirect_uri", redirectURI).
				queryParam("response_type", "code").
				queryParam("scope", SCOPE).
				queryParam("state",STATE).
				build().
				encode().toUriString();
		return url;
	}

	@Override
	public String getAccessToken(String code, String redirectURI)  throws Exception{
		
		/**
		 * How this method works?
		 * - This is basically the  Step 3 of the OAuth process. 
		 * After user logs in successfully to linkedin via the application, 
		 * User is redirected to the application's registered URL. 
		 * This URL should have the 'code' as one of the parameters.
		 * Based on this code, and the initial redirect URI, you post on the linked In URI
		 * and then you are authenticated.
		 */
		if (code == null || code.isEmpty())
		{
			throw new Exception("Invalid redirect from Facebook. Code not found.");
		}

		String url = UriComponentsBuilder.fromHttpUrl(FB_ACCESS_TOKEN_URI).
				queryParam("client_id", API_KEY).
				queryParam("redirect_uri", redirectURI).
				queryParam("client_secret", API_SECRET).
				queryParam("code",code).
				// queryParam("grant_type", GRANT_TYPE). Facebook API didn't mention use of Grant Type
				build().toUriString();
	    String token;
	    String json = null;
	    try {
			json = get(url);		
			if (json != null && !json.isEmpty())
			{
				CharSequence ch = json.subSequence(json.indexOf("=")+1, json.indexOf("&"));
				token = ch.toString();
				
			}
			else
			{
				return null;
			}
			
		} catch (Exception e) {
			// A possibility that there was an error
			ObjectMapper mapper = new ObjectMapper();
			FacebookOAuthError fe = mapper.readValue(json, FacebookOAuthError.class);
			throw new Exception(fe.getError().getMessage());
			
		}
	    return token;	    
	}

 // HTTP GET request
	private String get(String url) throws Exception {
 
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
 
		// add request header
		//request.addHeader("User-Agent", USER_AGENT);
		HttpResponse response = client.execute(request);
 	
		BufferedReader rd = new BufferedReader(
                       new InputStreamReader(response.getEntity().getContent()));
 
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
 
		System.out.println(result.toString());
		return(result.toString());
	}

	@Override
	public AuthenticatedUser getUserFullProfile(String authCode) throws Exception {
		if (authCode == null || authCode.isEmpty())
		{
			throw new Exception("LinkedIn Authentication code cannot be blank");
		}
		
		String url = UriComponentsBuilder.fromHttpUrl(FB_USER_PROFILE_FETCH_URI).
	    		queryParam("access_token", authCode).
				build().
				encode().toUriString();
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		// add this to return data in json, otherwise its xml
		request.addHeader("x-li-format", "json");
		HttpResponse response;
		String text = "";
		try {
			response = client.execute(request);
			BufferedReader rd = new BufferedReader(
	                       new InputStreamReader(response.getEntity().getContent()));
	 
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			text = result.toString();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new Exception(e);
		}
 
		if (text == null || text.isEmpty())
		{
			throw new Exception("User Profile not found");			
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			FacebookOAuthError fe = mapper.readValue(text, FacebookOAuthError.class);
			if (fe != null && fe.getError() !=null && fe.getError().getMessage() !=null )
			{
				throw new Exception(fe.getError().getMessage());
			}
		}
		catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}
		return getFacebookUser(text);		
	}
	
	private FacebookUser getFacebookUser(final String profileJson)
	{
		ObjectMapper m = new ObjectMapper();
		FacebookUser user = null;
		try {
			 user = m.readValue(profileJson, FacebookUser.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		// { "firstName": "Ken", "headline": "Member of Technical Staff at InterDigital Communications", "lastName": "Lynch", 
		// "siteStandardProfileRequest": {"url": "http://www.linkedin.com/profile/view?id=25672599&authType=name&authToken=86kH&trk=api*a3452311*s3525131*"}}
		return user;
	}
}
