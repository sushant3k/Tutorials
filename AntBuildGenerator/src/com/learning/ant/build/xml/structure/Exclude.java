package com.learning.ant.build.xml.structure;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="exclude")
@XmlAccessorType(XmlAccessType.FIELD)	
public class Exclude extends Base
{
	
	public Exclude()
	{
		super();
	}
	
}