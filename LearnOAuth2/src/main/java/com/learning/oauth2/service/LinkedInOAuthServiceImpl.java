/**
 * 
 */
package com.learning.oauth2.service;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.learning.oauth2.security.filter.AuthenticatedUser;
import com.learning.oauth2.security.filter.LinkedinUser;

/**
 * @author Sushant
 *
 */
@Component(value="linkedInOAuthService")
public class LinkedInOAuthServiceImpl implements LinkedinOAuthService {

	
	
	/* (non-Javadoc)
	 * @see com.hsc.security.oauth2.service.LinkedinOAuthService#getURIForAuthorizationCode(java.lang.String)
	 */
	@Override
	public String getURIForAuthorizationCode(String redirectURI) {
		String url = UriComponentsBuilder.fromHttpUrl(LINKEDIN_AUTHORIZATION_URI).
	    		queryParam("response_type", "code").
				queryParam("client_id", API_KEY).
				queryParam("scope", SCOPE).
				queryParam("state",STATE). 
				queryParam("redirect_uri", redirectURI).build().
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
			throw new Exception("Invalid redirect from LinkedIn. Code not found.");
		}

		String url = UriComponentsBuilder.fromHttpUrl(LINKEDIN_ACCESS_TOKEN_URI).
	    		queryParam("grant_type", GRANT_TYPE).
				queryParam("client_id", API_KEY).
				queryParam("client_secret", API_SECRET).
				queryParam("code",code). 
				queryParam("redirect_uri", redirectURI).build().toUriString();
	    String token;
	    try {
	    	LinkedinAccessTokenResponse response = post(url);
//			ObjectMapper m = new ObjectMapper();
//			JsonNode root = m.readTree(json);
//			token = root.path("access_token").getTextValue();
	    	token = response.getAccess_token();
		} catch (Exception e) {
			token = null;
		}
	    return token;	    
	}

	public LinkedinAccessTokenResponse post(String url) throws Exception {
 
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
  
		
		HttpResponse response = client.execute(request);
		try (InputStreamReader is = new InputStreamReader(response.getEntity().getContent()))
		{						
			return (new ObjectMapper()).readValue(is, LinkedinAccessTokenResponse.class);	
		}
		catch(IOException ioe)
		{
			throw new Exception(ioe);
		}
		
//		BufferedReader rd = new BufferedReader(
//                       new InputStreamReader(response.getEntity().getContent()));
// 
//		StringBuffer result = new StringBuffer();
//		String line = "";
//		while ((line = rd.readLine()) != null) {
//			result.append(line);
//		}
// 
//		
//		return(result.toString());
	}

	@Override
	public AuthenticatedUser getUserFullProfile(String authCode) throws Exception {
		if (authCode == null || authCode.isEmpty())
		{
			throw new Exception("LinkedIn Authentication code cannot be blank");
		}
		
		String url = UriComponentsBuilder.fromHttpUrl(LINKEDIN_USER_PROFILE_FETCH_URI).
	    		queryParam("oauth2_access_token", authCode).
				build().
				encode().toUriString();
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		// add this to return data in json, otherwise its xml
		request.addHeader("x-li-format", "json");
		HttpResponse response;
		response = client.execute(request);
//		String text = "";
		try (InputStreamReader is = new InputStreamReader(response.getEntity().getContent())){			
			ObjectMapper mapper = new ObjectMapper();			
			return mapper.readValue(is, LinkedinUser.class);
//			BufferedReader rd = new BufferedReader(
//	                       new InputStreamReader(response.getEntity().getContent()));
//	 
//			StringBuffer result = new StringBuffer();
//			String line = "";
//			while ((line = rd.readLine()) != null) {
//				result.append(line);
//			}
//			text = result.toString();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new Exception(e);
		}
 
//		if (text.isEmpty())
//		{
//			throw new Exception("User Profile not found");			
//		}
//		return getLinkedInUser(text);		
	}
	
	private LinkedinUser getLinkedInUser(final String profileJson)
	{
		ObjectMapper m = new ObjectMapper();
		LinkedinUser user = null;
		try {
			 user = m.readValue(profileJson, LinkedinUser.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		// { "firstName": "Ken", "headline": "Member of Technical Staff at InterDigital Communications", "lastName": "Lynch", 
		// "siteStandardProfileRequest": {"url": "http://www.linkedin.com/profile/view?id=25672599&authType=name&authToken=86kH&trk=api*a3452311*s3525131*"}}
		return user;
	}
}
