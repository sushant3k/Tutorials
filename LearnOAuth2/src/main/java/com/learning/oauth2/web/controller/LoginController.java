/**
 * 
 */
package com.learning.oauth2.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.learning.oauth2.service.FacebookOAuthService;
import com.learning.oauth2.service.LinkedinOAuthService;

/**
 * @author Sushant
 *
 */
@Controller
public class LoginController {

	@Autowired
	private LinkedinOAuthService linkedInOAuthService;
	
	@Autowired
	private FacebookOAuthService facebookOAuthService;
	

	
	public FacebookOAuthService getFacebookOAuthService() {
		return facebookOAuthService;
	}




	public void setFacebookOAuthService(FacebookOAuthService facebookOAuthService) {
		this.facebookOAuthService = facebookOAuthService;
	}




	public LinkedinOAuthService getLinkedInOAuthService() {
		return linkedInOAuthService;
	}




	public void setLinkedInOAuthService(LinkedinOAuthService linkedInOAuthService) {
		this.linkedInOAuthService = linkedInOAuthService;
	}




	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(Model model, @RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, HttpServletRequest request) {
		
		if (error != null) {
			model.addAttribute("error", "Invalid Username/Password");
		}
		if (logout != null) {
			model.addAttribute("msg", "You've been logged out successfully.");
		}
		
		model.addAttribute("linkedin_login_url", linkedInOAuthService.getURIForAuthorizationCode(LinkedinOAuthService.REDIRECT_URI_LI_LOGIN));
		model.addAttribute("facebook_login_url", facebookOAuthService.getURIForAuthorizationCode(LinkedinOAuthService.REDIRECT_URI_LI_LOGIN));
		

		return "login";
	}
	
	@RequestMapping(value="/app", method = RequestMethod.GET)
	public String authenticatedPage(Model model)
	{
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		if (a == null || !a.isAuthenticated() || a instanceof AnonymousAuthenticationToken)
		{
			return "loginError";
		}
		return "index";
	}
	
}
