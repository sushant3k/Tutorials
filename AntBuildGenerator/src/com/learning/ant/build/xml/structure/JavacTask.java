package com.learning.ant.build.xml.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="javac")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"src","exclude","include","classpath"})
public class JavacTask extends AbstractTask{

	@XmlElement(name="src")
	private JavaSource src;
	
	@XmlAttribute(name="destdir")
	private String destdir;
	
	
	@XmlElement(name="classpath")
	private Collection<ClasspathElement> classpath;
	
	@XmlElement(name="exclude")
	private List<Exclude> exclude;
	
	@XmlElement(name="include")
	private List<Include> include;
	
	
	
	public List<Exclude> getExclude() {
		return exclude;
	}

	public void addToExcludeList(Collection<Exclude> exc)
	{
		if (exc == null || exc.isEmpty())
			return;
		if(exclude == null || exclude.isEmpty())
			exclude = new ArrayList<Exclude>(exc.size());
		exclude.addAll(exc);
	}
	

	public void addToIncludeList(Collection<Include> exc)
	{
		if (exc == null || exc.isEmpty())
			return;
		if(include == null || include.isEmpty())
			include = new ArrayList<Include>(exc.size());
		include.addAll(exc);
	}
	
	public List<Include> getInclude() {
		return include;
	}

	

	public JavacTask()
	{
		
	}

	public JavaSource getSrc() {
		return src;
	}

	public void setSrc(JavaSource src) {
		this.src = src;
	}

	public String getDestdir() {
		return destdir;
	}

	public void setDestdir(String destdir) {
		this.destdir = destdir;
	}

	public Collection<ClasspathElement> getClasspath() {
		return classpath;
	}

	public void setClasspath(Collection<ClasspathElement> classpath) {
		this.classpath = classpath;
	}
	
	
	
}
