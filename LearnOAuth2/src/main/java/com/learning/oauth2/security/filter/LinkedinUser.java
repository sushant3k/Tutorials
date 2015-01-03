package com.learning.oauth2.security.filter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedinUser extends AuthenticatedUser{
	public String id;
	public String firstName;
	public String lastName;
	public String pictureUrl;
}