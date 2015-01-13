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
 * @author ggne0084
 *
 */
@XmlRootElement(name="war")
@XmlAccessorType(XmlAccessType.FIELD)
public class WarTask extends ArchiveTask{

	@XmlAttribute
	private String webxml;
	
	
	@XmlElement(name="lib")
	private Lib lib;
	
	@XmlElement(name="classes")
	private ClassesDir classes;
	
	private Collection<ZipFileSet> zipfileset;
	

	
	public String getWebxml() {
		return webxml;
	}

	public void setWebxml(String webxml) {
		this.webxml = webxml;
	}

	
	public Lib getLib() {
		return lib;
	}

	public void setLib(Lib lib) {
		this.lib = lib;
	}

	public ClassesDir getClasses() {
		return classes;
	}

	public void setClasses(ClassesDir classes) {
		this.classes = classes;
	}

	public Collection<ZipFileSet> getZipfileset() {
		return zipfileset;
	}

	public void setZipfileset(Collection<ZipFileSet> zipfileset) {
		this.zipfileset = zipfileset;
	}

	@XmlRootElement(name="lib")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Lib
	{
		@XmlAttribute(name="dir")
		private String dir;
		
		@XmlElement
		private Collection<Exclude> exclude;
		
		@XmlElement
		private Collection<Include> include;
		
		@XmlElement
		private Collection <FileSet> fileset;
		
		public Lib()
		{
			
		}

		public String getDir() {
			return dir;
		}

		public void setDir(String dir) {
			this.dir = dir;
		}

		public Collection<Exclude> getExclude() {
			return exclude;
		}

		public void setExclude(Collection<Exclude> exclude) {
			this.exclude = exclude;
		}

		public Collection<Include> getInclude() {
			return include;
		}

		public void setInclude(Collection<Include> include) {
			this.include = include;
		}

		public Collection<FileSet> getFileset() {
			return fileset;
		}

		public void setFileset(Collection<FileSet> fileset) {
			this.fileset = fileset;
		}
		
		
	}
	
	@XmlRootElement(name="classes")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class ClassesDir
	{
		@XmlAttribute
		private String dir;
		
		@XmlElement(name="fileset")
		private Collection<FileSet> fileset;
		
		public ClassesDir()
		{
			
		}

		public String getDir() {
			return dir;
		}

		public void setDir(String dir) {
			this.dir = dir;
		}

		public Collection<FileSet> getFileset() {
			return fileset;
		}

		public void setFileset(Collection<FileSet> fileset) {
			this.fileset = fileset;
		}
		
		
	}
}
