/**
 * 
 */
package com.learning.ant.build.xml.structure;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author sushant
 *
 */
@XmlRootElement(name="delete")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteTask {

	@XmlAttribute
	private String dir;
	
	@XmlAttribute
	private String file;
	@XmlElement(name="fileset")
	private Collection<FileSet> fileSet;
	public DeleteTask()
	{
		
	}

	@XmlAttribute(name="includeemptydirs")
	private boolean includeEmptyDirectories;
	
	
	
	public boolean isIncludeEmptyDirectories() {
		return includeEmptyDirectories;
	}


	public void setIncludeEmptyDirectories(boolean includeEmptyDirectories) {
		this.includeEmptyDirectories = includeEmptyDirectories;
	}


	public Collection<FileSet> getFileSet() {
		return fileSet;
	}


	public void setFileSet(Collection<FileSet> fileSet) {
		this.fileSet = fileSet;
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
	
}
