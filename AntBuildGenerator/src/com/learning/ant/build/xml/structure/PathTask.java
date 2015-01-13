package com.learning.ant.build.xml.structure;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="path")
@XmlAccessorType(XmlAccessType.FIELD)
public class PathTask {

	@XmlAttribute
	private String id;
	
	@XmlElement(name="path")
	private Collection<PathTask> classpath;
	
	@XmlElement(name="pathelement")
	private Collection<PathElement> pathElement;
	
	@XmlAttribute
	private String refid; 
	
	@XmlElement(name="fileset")
	private Collection<FileSet> fileset;

	
	
	public Collection<FileSet> getFileset() {
		return fileset;
	}


	public void setFileset(Collection<FileSet> fileset) {
		this.fileset = fileset;
	}


	public String getRefid() {
		return refid;
	}


	public void setRefid(String refid) {
		this.refid = refid;
	}


	public Collection<PathElement> getPathElement() {
		return pathElement;
	}


	public void setPathElement(Collection<PathElement> pathElement) {
		this.pathElement = pathElement;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Collection<PathTask> getClasspath() {
		return classpath;
	}


	public void setClasspath(Collection<PathTask> classpath) {
		this.classpath = classpath;
		
	}
	
	public void addPathElement(PathElement pe)
	{
		if (this.pathElement == null)
		{
			pathElement = new ArrayList<PathElement>();
		}
		pathElement.add(pe);
	}
	
	public void addPathTask(PathTask pt)
	{
		if (this.classpath == null)
		{
			classpath = new ArrayList<PathTask>();
		}
		classpath.add(pt);
	}
	
	@Override
	public int hashCode()
	{
		if (this.id != null)
			return this.id.hashCode();
		else
			return super.hashCode();
	}
	
	@Override
	public boolean equals(Object o )
	{
		if (o instanceof PathTask)
		{
			PathTask pt = (PathTask)o;
			if (pt.id.equals(this.id))
				return true;
			else
				return false;
		}
		return false;
	}
	
}
