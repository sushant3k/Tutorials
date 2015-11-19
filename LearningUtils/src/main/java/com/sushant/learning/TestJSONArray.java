/**
 * 
 */
package com.sushant.learning;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author Sushant
 *
 */
public class TestJSONArray {

	public static void main(String args[]) throws Exception
	{
		ObjectMapper om = new ObjectMapper();
		StringWriter sw = new StringWriter();
		List<JSONObject> lst = new ArrayList<>();
		System.out.println ("you can use single \' or double \" quote");
		List<String> l = new ArrayList<>();
		JSONObject j = new JSONObject();
		j.setName("fName");
		j.setAddress("fAddress");
		lst.add(j);
		l.add(j.toString());
		j = new JSONObject();
		j.setName("SName");
		j.setAddress("SAddress");
		lst.add(j);
		l.add(j.toString());
		
		
		om.writeValue(sw, lst);
		System.out.println(sw);
		sw.close();
		sw = new StringWriter();
		om.writeValue(sw, l);
		System.out.println(sw);
		
		sw.close();
		
		
	}
	
	public static <T> T generateObjectFromJSON(Class<T> cls, String json) throws Exception
	{
		if (json == null || json.isEmpty())
			return null;
		ObjectMapper mp = new ObjectMapper();
		return mp.readValue(json, cls);
	}
	public static String generateJSON(Object obj)
	{
		if (obj == null)
			return null;
		
		ObjectMapper om = new ObjectMapper();
		StringWriter sw = new StringWriter();
		try {
			om.writeValue(sw, obj);			
			sw.close();
			return sw.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				sw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}


class JSONObject
{
	private String name;
	
	private String address;
	
	@JSONField
	private List<JSONObjectInner> jsonObjectInner;
	
	@JSONField
	private JSONObjectInner singleField;
	
	
	public JSONObjectInner getSingleField() {
		return singleField;
	}



	public void setSingleField(JSONObjectInner singleField) {
		this.singleField = singleField;
	}



	public JSONObject()
	{
		
	}
	
	
	
	public List<JSONObjectInner> getJsonObjectInner() {
		return jsonObjectInner;
	}



	public void setJsonObjectInner(List<JSONObjectInner> jsonObjectInner) {
		this.jsonObjectInner = jsonObjectInner;
	}



	public String getName() {
		return name;
	}


	public static class JSONObjectInner
	{
		private String name1;
		private String abc;
		
		public JSONObjectInner()
		{
			
		}

		public String getName1() {
			return name1;
		}

		public void setName1(String name1) {
			this.name1 = name1;
		}

		public String getAbc() {
			return abc;
		}

		public void setAbc(String abc) {
			this.abc = abc;
		}
		
		
	}

	public void setName(String name) {
		this.name = name;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
			sb.append("\"name\": ").append(name).append(",")
			.append("\"address\": ").append(address);
		
		
		return sb.toString();
	}
}