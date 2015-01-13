package com.learning.ant.build.xml.structure;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="target")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"mkdir", "copyTask","compilationTask", "jt","wt","et","deleteTask"})
public class BuildTarget {

	@XmlAttribute
	private String name;
	@XmlAttribute
	private String depends;
	@XmlAttribute(name="if")
	private String If;
	@XmlAttribute
	private String unless;
	
	@XmlElement(name="mkdir")
	private Collection<MakeDir> mkdir;
	
	@XmlElement(name="copy")
	private Collection<CopyTask> copyTask;
	
	@XmlElement(name="javac")
	private Collection<JavacTask> compilationTask;
	
	@XmlElement(name="jar")
	private JarTask jt; 
	
	@XmlElement(name="war")
	private WarTask wt;
	
	@XmlElement(name="ear")
	private EarTask et;
	
	
	
	@XmlElement(name="delete")
	private Collection<DeleteTask> deleteTask;
	
	

	public Collection<DeleteTask> getDeleteTask() {
		return deleteTask;
	}
	public void setDeleteTask(Collection<DeleteTask> deleteTask) {
		this.deleteTask = deleteTask;
	}
	public void addDeleteTaskToCollection(DeleteTask dt)
	{
		if (this.deleteTask == null)
			this.deleteTask = new ArrayList<DeleteTask>();
		this.deleteTask.add(dt);
	}
	public JarTask getJt() {
		return jt;
	}

	public void setJt(JarTask jt) {
		this.jt = jt;
	}

	public WarTask getWt() {
		return wt;
	}

	public void setWt(WarTask wt) {
		this.wt = wt;
	}

	public EarTask getEt() {
		return et;
	}

	public void setEt(EarTask et) {
		this.et = et;
	}

	public Collection<JavacTask> getCompilationTask() {
		return compilationTask;
	}

	public void addToCompilationTask(Collection<JavacTask> ct)
	{
		if (this.compilationTask == null)
		{
			this.compilationTask = new ArrayList<JavacTask>();
		}
		this.compilationTask.addAll(ct);
	}
	public void setCompilationTask(Collection<JavacTask> compilationTask) {
		this.compilationTask = compilationTask;
	}

	public Collection<MakeDir> getMkdir() {
		return mkdir;
	}

	public void setMkdir(Collection<MakeDir> mkdir) {
		this.mkdir = mkdir;
	}

	public Collection<CopyTask> getCopyTask() {
		return copyTask;
	}

	public void setCopyTask(Collection<CopyTask> copyTask) {
		this.copyTask = copyTask;
	}

	public BuildTarget()
	{
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepends() {
		return depends;
	}

	public void setDepends(String depends) {
		this.depends = depends;
	}

	public String getIf() {
		return If;
	}

	public void setIf(String if1) {
		If = if1;
	}

	public String getUnless() {
		return unless;
	}

	public void setUnless(String unless) {
		this.unless = unless;
	}
	
	
	
}
