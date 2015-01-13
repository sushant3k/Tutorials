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
@XmlRootElement(name="property")
@XmlAccessorType(XmlAccessType.FIELD)
public class Property {

	@XmlAttribute(name="environment")
	private String environment;
	
	@XmlAttribute
	private String name;
	
	@XmlAttribute
	private String value;

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
