package com.learning.ant.build.xml.structure;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="classpath")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClasspathElement {

	@XmlAttribute
	private String refid;
	
	private Collection<PathElement> pathelement;
	
	public ClasspathElement()
	{
		
	}

	public String getRefid() {
		return refid;
	}

	public void setRefid(String refid) {
		this.refid = refid;
	}

	public Collection<PathElement> getPathelement() {
		return pathelement;
	}

	public void setPathelement(Collection<PathElement> pathelement) {
		this.pathelement = pathelement;
	}
	
	
}
