/**
 * 
 */
package com.learning.oauth2.service;


/**
 * @author Sushant
 *
 */
public interface FacebookOAuthService extends OAuthService{

	/**
	 * Following is the Facebook Dialog URL
	 */
	final String FB_AUTHORIZATION_URI = "https://www.facebook.com/dialog/oauth";
	
	final String FB_ACCESS_TOKEN_URI = "https://graph.facebook.com/oauth/access_token";
	
	final String FB_USER_PROFILE_FETCH_URI = "https://graph.facebook.com/v2.0/me?fields=id,name,picture";
	
	
	final String API_KEY="<Use your API key>";
	final String API_SECRET="<User your API Secret>"; // Do not share this with anybody
	final String SCOPE="email"; // Fetch email also besides the full public profile
	final String STATE="facebook";  // TODO: What are the possible values for state.
	
	
	final String GRANT_TYPE = "authorization_code";
	 	
}
