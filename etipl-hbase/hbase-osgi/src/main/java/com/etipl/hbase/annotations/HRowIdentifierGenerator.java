/**
 * 
 */
package com.etipl.hbase.annotations;

/**
 * @author Sushant
 *
 */
public enum HRowIdentifierGenerator {

	AUTO("UUID"),
	CUSTOM("");
	
	private String identifier;
	private HRowIdentifierGenerator(String identifier)
	{
		this.identifier = identifier;
	}
	
	public String getIdentifier()
	{
		return identifier;
	}
}
