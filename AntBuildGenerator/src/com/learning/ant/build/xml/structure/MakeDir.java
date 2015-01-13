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
@XmlRootElement(name="mkdir")
@XmlAccessorType(XmlAccessType.FIELD)
public class MakeDir extends AbstractTask{

	@XmlAttribute(name="dir")
	private String dirName;
	
	public MakeDir()
	{
		
	}

	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}
	
	
	
}
