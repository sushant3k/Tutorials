/**
 * 
 */
package com.learning.oauth2.error.objects;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author Sushant
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookOAuthError {

	
	private Error error;
	
	
	public Error getError() {
		return error;
	}


	public void setError(Error error) {
		this.error = error;
	}


	public static class Error
	{
		private String message;
		
		private String type;
		
		private Integer code;
		
		public Error() {
			
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}
		
		
		
	}
}
