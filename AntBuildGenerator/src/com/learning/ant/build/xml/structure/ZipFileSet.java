package com.learning.ant.build.xml.structure;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="zipfileset")
@XmlAccessorType(XmlAccessType.FIELD)
public class ZipFileSet {

	private String dir;
	
	private String prefix;
	
	private Collection<Include> include;
	
	private Collection<Exclude>  exclude;
	
	public ZipFileSet()
	{
		
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Collection<Include> getInclude() {
		return include;
	}

	public void setInclude(Collection<Include> include) {
		this.include = include;
	}

	public Collection<Exclude> getExclude() {
		return exclude;
	}

	public void setExclude(Collection<Exclude> exclude) {
		this.exclude = exclude;
	}
	
	
}
