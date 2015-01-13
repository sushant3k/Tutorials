package com.learning.ant.build.xml.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="project")
@XmlAccessorType(XmlAccessType.FIELD)

public class Project {

	@XmlAttribute
	private String basedir = ".";
	@XmlAttribute(name="default")
	private String defaultTarget ="build";
	@XmlAttribute(name="name")
	private String projectName;
	
	@XmlElement(name="property")
	Collection<Property> properties;
	
	@XmlElement(name="path")
	Collection<PathTask> classpath;
	
	@XmlElement(name="target")
	private Collection<BuildTarget> target;
	@XmlTransient
	private ConcurrentMap<String, Byte> mp = new ConcurrentHashMap<String, Byte>();
	
	public void addClasspath(Collection<PathTask> cp)
	{
		if (cp != null && !cp.isEmpty())
		{
			if (this.classpath != null)
			{
				for(PathTask pt : cp)
				{
					if (!mp.containsKey(pt.getId()))
					{
						mp.put(pt.getId(),(byte)0);
						this.classpath.add(pt);
						
					}
				}
				
			}
			else
			{
				
				this.classpath = cp;
				for(PathTask pt : cp)
				{
					if (!mp.containsKey(pt.getId()))
					{
						mp.put(pt.getId(),(byte)0);
						
						
					}
				}
			}
		}
	}
	
	public  void addProperties(Collection<Property> props)
	{
		if (props != null && !props.isEmpty())
		{
			if (this.properties != null)
			{
				this.properties.addAll(props);
				
			}
			else
				this.properties = props;
		}
	}
	public void addBuildTargets(Collection<BuildTarget> bts)
	{
		if (bts != null && !bts.isEmpty())
		{
			if (this.target != null)
				this.target.addAll(bts);
			else
				this.target = bts;
		}
	}
	public Collection<PathTask> getClasspath() {
		return classpath;
	}

//	public void setClasspath(Collection<PathTask> classpath) {
//		this.classpath = classpath;
//	}

	public Collection<BuildTarget> getTarget() {
		return target;
	}

	public void setTarget(Collection<BuildTarget> target) {
		this.target = target;
	}

	public Collection<Property> getProperties() {
		return properties;
	}

	public void setProperties(Collection<Property> properties) {
		this.properties = properties;
	}

	public Project()
	{
		
	}

	public void addBuildTargetToCollection(BuildTarget bt)
	{
		if (this.target == null)
		{
			this.target = new ArrayList<BuildTarget>();
		}
		this.target.add(bt);
	}
	public String getBasedir() {
		return basedir;
	}

	public void setBasedir(String basedir) {
		this.basedir = basedir;
	}

	public String getDefaultTarget() {
		return defaultTarget;
	}

	public void setDefaultTarget(String defaultTarget) {
		this.defaultTarget = defaultTarget;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	
}
