/**
 * 
 */
package com.etipl.hbase.spi.internal;

import java.io.IOException;
import java.io.StringWriter;

import org.codehaus.jackson.map.ObjectMapper;

import com.etipl.hbase.exception.ApplicationException;

/**
 * @author Sushant
 *
 */
public class JSONUtils {

	private JSONUtils()
	{
		
	}
	
	/**
	 * Generate JSON from the Object.
	 * @param obj
	 * @return
	 */
	public static String getJSONFromObject(Object obj) throws ApplicationException
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
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		finally {
			try {
				
				sw.close();
			} catch (Exception e) {
				// TODO: Skip this exception or log this exception
			}
		}		
	}
	
	public static <T> T generateObjectFromJSON(Class<T> cls, String jsonString) throws Exception
	{
		if (jsonString == null || jsonString.isEmpty())
			return null;
		ObjectMapper mp = new ObjectMapper();
		return mp.readValue(jsonString, cls);
	}
}
