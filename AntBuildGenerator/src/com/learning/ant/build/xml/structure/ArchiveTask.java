package com.learning.ant.build.xml.structure;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ArchiveTask {

	@XmlAttribute
	private String destfile;
	
	public String getDestfile() {
		return destfile;
	}

	public void setDestfile(String destfile) {
		this.destfile = destfile;
	}

	@XmlElement(name="fileset")
	private Collection<FileSet> fileset;
	
	public void addFileSet(FileSet fs)
	{
		if (this.fileset != null)
		{
			if (fs != null)
			{
				this.fileset.add(fs);
			}
		}
		else
		{
			if (fs != null)
			{
				this.fileset = new ArrayList<FileSet>();
				fileset.add(fs);
			}
		}
	}
	public Collection<FileSet> getFileset() {
		return fileset;
	}

	public void setFileset(Collection<FileSet> fileset) {
		this.fileset = fileset;
	}

	
}
