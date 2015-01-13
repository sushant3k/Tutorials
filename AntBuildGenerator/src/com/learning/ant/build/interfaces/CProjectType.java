/**
 * This class represents an enumeration for the type of project.
 * Currently, only EAR, EJB, JAVA, Dynamic Web, SAR projects are supported. 
 * 
 */
package com.learning.ant.build.interfaces;

/**
 * @author Sushant jain
 * @since 1.0
 * @Version 1.0
 *
 */
public class CProjectType {

	public enum ProjectType
	{
		EAR ("EAR"),
		WEB ("WEB"),
		JAVA ("JAVA"),
		EJB ("EJB"),
		SAR ("SAR");
		
		String type;
		ProjectType(String type)
		{
			this.type = type;
		}
		public String getProjectType()
		{
			return this.type;
		}
	}
}
