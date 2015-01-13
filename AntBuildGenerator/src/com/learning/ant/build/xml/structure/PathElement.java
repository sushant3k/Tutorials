package com.learning.ant.build.xml.structure;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="pathelement")
@XmlAccessorType(XmlAccessType.FIELD)
public  class PathElement
{
	@XmlAttribute(name="location")
	String location;
	public PathElement()
	{
		
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
}