package com.learning.oauth2.security.filter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookUser extends AuthenticatedUser{
	private String id;
	private String name;
	private Picture picture;
	
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Picture getPicture() {
		return picture;
	}


	public void setPicture(Picture picture) {
		this.picture = picture;
	}


	public static class Picture {
		
		private Data data;
		
		
		public Data getData() {
			return data;
		}


		public void setData(Data data) {
			this.data = data;
		}


		public static class Data {
			private boolean is_silhouette;
			private String url;
			
			public Data() {
				
			}

			public boolean isIs_silhouette() {
				return is_silhouette;
			}

			public void setIs_silhouette(boolean is_silhouette) {
				this.is_silhouette = is_silhouette;
			}

			public String getUrl() {
				return url;
			}

			public void setUrl(String url) {
				this.url = url;
			}
			
			
		}
	}
	
	
}