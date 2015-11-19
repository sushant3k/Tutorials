/**
 * 
 */
package com.etipl.hbase.spi.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author Sushant
 *
 */
class ReflectionUtils {

	public static <T> String getFieldValue(T t, String fieldName)
	{
		String value = null;
		
		try {
			Method m = t.getClass().getMethod(
					"get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length()),
					new Class<?>[] {});
			Object obj = m.invoke(t, new Object[] {});
			if (obj != null) {
				value = obj.toString();
			}
			return value;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException se) {
			se.printStackTrace();
		}
		return null;
	}
	
	
	public static <T> Object getFieldValueAsObject(T t, String fieldName)
	{
		Object value = null;
		
		try {
			Method m = t.getClass().getMethod(
					"get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length()),
					new Class<?>[] {});
			Object obj = m.invoke(t, new Object[] {});
			if (obj != null) {
				value = obj;
			}
			return value;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException se) {
			se.printStackTrace();
		}
		return null;
	}
	
	public static <T> void setFieldValueForCollections(T t, String fieldName, Object value)
	{
		// TODO: This method currently works for String datatypes. Needs to be changed to work for other Primitive types as well.
		try {
			Method m = t.getClass().getMethod(
					"set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length()),
					new Class<?>[] {ArrayList.class});
			m.invoke(t, new Object[] {value});
			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException se) {
			se.printStackTrace();
		}
		
	}
	
	
	public static <T> void setFieldValue(T t, String fieldName, Object value)
	{
		// TODO: This method currently works for String datatypes. Needs to be changed to work for other Primitive types as well.
		try {
			Method m = t.getClass().getMethod(
					"set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length()),
					new Class<?>[] {String.class});
			m.invoke(t, new Object[] {value});
			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException se) {
			se.printStackTrace();
		}
		
	}
	
}
