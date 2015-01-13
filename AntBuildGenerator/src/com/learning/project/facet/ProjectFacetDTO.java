/**
 * This class represents the details of one Facet of a Project.
 * e.g. If a Project has multiple facets i.e. say an EJB Project,
 * then this project has multiple facets. 
 * One instance of this class can hold details of only one facet. 
 *   
 */
package com.learning.project.facet;

/**
 * @author Sushant
 *
 */
public class ProjectFacetDTO {
	
	private String facetName;
	
	private String facetVersion;
	
	public ProjectFacetDTO()
	{
		
	}

	public String getFacetName() {
		return facetName;
	}

	public void setFacetName(String facetName) {
		this.facetName = facetName;
	}

	public String getFacetVersion() {
		return facetVersion;
	}

	public void setFacetVersion(String facetVersion) {
		this.facetVersion = facetVersion;
	}
	
}
