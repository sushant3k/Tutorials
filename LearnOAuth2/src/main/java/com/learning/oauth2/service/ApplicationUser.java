/**
 * 
 */
package com.learning.oauth2.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * @author Sushant
 *
 */
public class ApplicationUser extends User{

	private String fullName;
	public ApplicationUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, String fullName) {
	
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		// TODO Auto-generated constructor stub
		this.fullName = fullName;
	}

	
	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
