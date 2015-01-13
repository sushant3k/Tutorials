/**
 * 
 */
package com.learning.ant.build.xml.structure;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Sushant Jain
 * @version 1.0
 * @since 1.0
 *
 */
@XmlRootElement(name="jar")
@XmlAccessorType(XmlAccessType.FIELD)
public class JarTask extends ArchiveTask{

	
	
	@XmlAttribute
	private String basedir;
	
	
	
	public JarTask()
	{
		
	}

	
	public String getBasedir() {
		return basedir;
	}

	public void setBasedir(String basedir) {
		this.basedir = basedir;
	}

	
	
}
