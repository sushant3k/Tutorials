package com.learning.ant.build.xml.structure;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="fileset")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileSet {

	@XmlAttribute
	private String dir;
	
	@XmlAttribute
	private String file;
	
	@XmlAttribute
	private Boolean casesensitive;
	
	@XmlElement(name="include")
	private Collection<Include> include;
	
	@XmlElement(name="exclude")
	private Collection<Exclude> exclude;
	
	@XmlAttribute(name="includes")
	private String includes;
	
	
	
	public String getIncludes() {
		return includes;
	}

	public void setIncludes(String includes) {
		this.includes = includes;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Boolean getCasesensitive() {
		return casesensitive;
	}

	public void setCasesensitive(Boolean casesensitive) {
		this.casesensitive = casesensitive;
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

	public void addIncludeToCollection(Include include)
	{
		if (this.include == null)
			this.include = new ArrayList<Include>();
		this.include.add(include);
	}
	
	public void addExcludeToCollection(Exclude exclude)
	{
		if (this.exclude == null)
			this.exclude = new ArrayList<Exclude>();
		this.exclude.add(exclude);
	}
}
