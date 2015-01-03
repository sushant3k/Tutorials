/**
 * 
 */
package com.learning.oauth2.security.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.learning.oauth2.service.FacebookOAuthService;
import com.learning.oauth2.service.LinkedinOAuthService;

/**
 * A username and password based authentication filter to authenticate users.
 * @author Sushant
 * 
 */
public class UsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter{

	@Autowired
	private LinkedinOAuthService linkedInOAuthService;
	
	@Autowired
	private FacebookOAuthService facebookOAuthService;
	
	
	@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		
		//get username and password based on source, i.e. form/linkedin/facebook		
		String state = request.getParameter("state");
		if (state == null)
			state = "";
		String username = null;
		String password = null;

		/**
		 * You could use better value for state parameter. This should really be the one that cannot be guessed easily. 
		 * 
		 */
		if (state.equalsIgnoreCase("linkedin")) {
			String code = request.getParameter("code");
			try
			{
				String token = linkedInOAuthService.getAccessToken(code, LinkedinOAuthService.REDIRECT_URI_LI_LOGIN);
				AuthenticatedUser au = linkedInOAuthService.getUserFullProfile(token);				 
				if (au == null) {
					username = "";
					password = "";
				} else {
					LinkedinUser linkedinUser = (LinkedinUser)au;
					username = linkedinUser.id;
					password = "password";
				}				
			}
			catch(Exception e)
			{
				return null;
			}
		}
		else if (state.equalsIgnoreCase("facebook")) {
			String code = request.getParameter("code");
			try
			{
				String token = facebookOAuthService.getAccessToken(code, LinkedinOAuthService.REDIRECT_URI_LI_LOGIN);
				AuthenticatedUser au = facebookOAuthService.getUserFullProfile(token);				 
				if (au == null) {
					username = "";
					password = "";
				} else {
					FacebookUser fbUser = (FacebookUser)au;
					username = fbUser.getId();
					password = "password";
				}				
			}
			catch(Exception e)
			{
				return null;
			}
		}
		 else {
			System.out.println("Only Facebook and Linkedin Supported right now.");
		}

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
