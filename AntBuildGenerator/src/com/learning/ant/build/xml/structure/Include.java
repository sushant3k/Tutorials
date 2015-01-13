package com.learning.ant.build.xml.structure;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="include")
@XmlAccessorType(XmlAccessType.FIELD)
public class Include extends Base
{
	public Include()
	{
		super();
	}
}