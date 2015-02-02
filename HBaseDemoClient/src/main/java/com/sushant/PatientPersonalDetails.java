/**
 * 
 */
package com.sushant;

import com.sushant.annotations.HBaseColumnQualifier;

/**
 * @author Sushant
 *
 */
public class PatientPersonalDetails {

	@HBaseColumnQualifier(columnName="firstName")
	private String firstName;
	
	@HBaseColumnQualifier(columnName="lastName")
	private String lastName;
	
	public PatientPersonalDetails(String firstName, String lastName)
	{
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public PatientPersonalDetails()
	{
		
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Override
	public String toString()
	{
		return "FirstName="+firstName+", LastName="+lastName;
	}
}
