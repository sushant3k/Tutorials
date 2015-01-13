/**
 * 
 */
package com.learning.ant.build.xml.structure;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ggne0084
 *
 */
@XmlRootElement(name="src")
@XmlAccessorType(XmlAccessType.FIELD)
public class JavaSource {

	@XmlAttribute
	private String path;

	public JavaSource()
	{
		
	}
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
	
}
