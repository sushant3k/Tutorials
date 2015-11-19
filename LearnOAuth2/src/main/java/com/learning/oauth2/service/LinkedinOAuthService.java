/**
 * 
 */
package com.learning.oauth2.service;


/**
 * @author Sushant
 *
 */
public interface LinkedinOAuthService extends OAuthService{

	final String LINKEDIN_AUTHORIZATION_URI = "https://www.linkedin.com/uas/oauth2/authorization";
	final String LINKEDIN_ACCESS_TOKEN_URI = "https://www.linkedin.com/uas/oauth2/accessToken";
	final String LINKEDIN_USER_PROFILE_FETCH_URI = "https://www.linkedin.com/v1/people/~:(id,first-name,last-name,picture-url)";
	
//	final String API_KEY="<User your API Key>";
//	final String API_SECRET="<User Your API Secret>"; // Do not share this with anybody
	
	
	final String API_KEY="754q5002c24qnp";
	final String API_SECRET="xH9Vkoob0SaReHvY"; // Do not share this with anybody
	
	
	final String SCOPE="r_fullprofile";
	final String STATE="linkedin";  // TODO: What are the possible values for state.
	final String REDIRECT_URI_LI_LOGIN = "http://localhost:8080/LearnOAuth2/login/validate";
	
	final String GRANT_TYPE = "authorization_code";
	 

}
