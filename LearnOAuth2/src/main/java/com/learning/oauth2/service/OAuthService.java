/**
 * 
 */
package com.learning.oauth2.service;

import com.learning.oauth2.security.filter.AuthenticatedUser;

/**
 * @author Sushant
 *
 */
public interface OAuthService {

	final String REDIRECT_URI_LI_LOGIN = "http://localhost:8080/LearnOAuth2/login/validate";
	/**
	 * This API returns the OAuth2 providers API.
	 * @param - redirectURI (Client Application's redirect URI)
	 * @return -OAuth2 providers API's URL.
	 */
	String getURIForAuthorizationCode(final String redirectURI);
	
	String getAccessToken(final String code, final String redirectURI) throws Exception;
	
	AuthenticatedUser getUserFullProfile(final String authCode) throws Exception;
	
}
