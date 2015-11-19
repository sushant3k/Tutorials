package com.etipl.labResults.nosql.framework.nosql.spi;

import java.lang.reflect.Field;

import com.etipl.labResults.nosql.framework.annotations.HColumnQualifier;
import com.etipl.labResults.nosql.framework.annotations.HRowIdentifier;
import com.etipl.labResults.nosql.framework.annotations.HRowIdentifierGenerator;
import com.etipl.labResults.nosql.framework.annotations.HTableQualifier;

class HBaseUtils {

	private HBaseUtils()
	{
		
	}
	
	public static <T> String getColumnFamily(Field f)
	{
		String columnFamily = "";
		if (f.isAnnotationPresent(HColumnQualifier.class))
		{
			HColumnQualifier hbcq = f.getAnnotation(HColumnQualifier.class);
			columnFamily = hbcq.columnFamily();
			
		}
		return columnFamily;
	}
	public static <T> String getColumnName(Field f)
	{
		String columnName = f.getName();
		if (f.isAnnotationPresent(HColumnQualifier.class)) {
			HColumnQualifier hbcq = f.getAnnotation(HColumnQualifier.class);
			 columnName = hbcq.columnName();
			if (columnName.isEmpty()) {
				columnName = f.getName();
			}
		}
		
		return columnName; 
	}
	public static <T> String getKeyFieldValue(T t)
	{
		Field [] fs = t.getClass().getDeclaredFields();
		
		for (Field f : fs)
		{
			if (f.isAnnotationPresent(HRowIdentifier.class))
			{
				HRowIdentifier hrif = f.getAnnotation(HRowIdentifier.class);
				if (hrif.identifier() == HRowIdentifierGenerator.AUTO) 
				{
					return java.util.UUID.randomUUID().toString();
				}
				return ReflectionUtils.getFieldValue(t, f.getName());
			}
			
		}
		return null;
	}
	public static <T> String getTableName(Class<T> cls)
	{
		if (cls == null)
			throw new IllegalArgumentException("Invalid input");
		
		if (cls.isAnnotationPresent(HTableQualifier.class))
		{
			HTableQualifier ht = cls.getAnnotation(HTableQualifier.class);
			String tableName = ht.tableName();
			
			if (tableName == null || tableName.isEmpty())
			{
				return cls.getSimpleName();
			}
			return tableName;
		}
		return cls.getSimpleName();
	}
}
