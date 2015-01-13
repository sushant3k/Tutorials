/**
 * 
 */
package com.learning.ant.build.xml.structure;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ggne0084
 *
 */
@XmlRootElement(name="copy")
@XmlAccessorType(XmlAccessType.FIELD)
public class CopyTask extends AbstractTask{

	@XmlAttribute(name="todir")
	private String toDir;
	
	@XmlAttribute(name="tofile")
	private String toFile;
	
	@XmlAttribute(name="file")
	private String file;
	
	@XmlAttribute(name="overwrite")
	private boolean overwrite= true;
	
	@XmlAttribute
	private boolean includeEmptyDirs;
	
	@XmlElement
	private FileSet fileset;
	
	public CopyTask()
	{
		
	}

	public String getToDir() {
		return toDir;
	}

	public void setToDir(String toDir) {
		this.toDir = toDir;
	}

	public String getToFile() {
		return toFile;
	}

	public void setToFile(String toFile) {
		this.toFile = toFile;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public boolean isOverwrite() {
		return overwrite;
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	public boolean isIncludeEmptyDirs() {
		return includeEmptyDirs;
	}

	public void setIncludeEmptyDirs(boolean includeEmptyDirs) {
		this.includeEmptyDirs = includeEmptyDirs;
	}

	public FileSet getFileset() {
		return fileset;
	}

	public void setFileset(FileSet fileset) {
		this.fileset = fileset;
	}
	
	
	
	
}
