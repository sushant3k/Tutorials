package com.learning.oauth2.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
/**
 * 
 * @author Sushant
 * This class is just to build the grants for the user. 
 * Note that this doesn't use any DB for fetching the capabilities/grants for a user.
 */
public class AppUserDetailsService implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		List<GrantedAuthority> authorities = buildUserAuthority("ROLE_ADMIN","ROLE_USER");
		ApplicationUser auser = new ApplicationUser("username", "password", true, true, true, true, authorities, "Sushant Jain");
		return auser;
	}

	private List<GrantedAuthority> buildUserAuthority(String... roles) {

		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

		// Build user's authorities
		for (String userRole : roles) {
			setAuths.add(new SimpleGrantedAuthority(userRole));
		}

		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);

		return Result;
	}
	
}
