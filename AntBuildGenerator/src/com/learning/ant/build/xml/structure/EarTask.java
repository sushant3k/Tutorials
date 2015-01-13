package com.learning.ant.build.xml.structure;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ear")
@XmlAccessorType(XmlAccessType.FIELD)
public class EarTask extends ArchiveTask
{

	@XmlAttribute(name="appxml")
	private String applicationXml;
	
	public EarTask()
	{
		super();
	}

	public String getApplicationXml() {
		return applicationXml;
	}

	public void setApplicationXml(String applicationXml) {
		this.applicationXml = applicationXml;
	}
	
	
}
