package wseemann.media.jplaylistparser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wseemann.media.jplaylistparser.ListType;
import wseemann.media.jplaylistparser.PlayListAbstractClass;

/**
 * @author e01149
 *
 */
public class MasterPlayList extends PlayListAbstractClass{
	
	public MasterPlayList() {
		super(ListType.MASTER);
		masterMap=new HashMap<String,List<MasterPlayList>>();
	}

	/**
	 * @return the masterMap
	 */
	public Map<String, List<MasterPlayList>> getMasterMap() {
		return masterMap;
	}

	/**
	 * @param masterMap the masterMap to set
	 */
	public void setMasterMap(Map<String, List<MasterPlayList>> masterMap) {
		this.masterMap = masterMap;
	}

	private String programID;
	private String bandwidth;
	Map<String,List<MasterPlayList>> masterMap=null;
	private String uri;

	/**
	 * @return the programID
	 */
	public String getProgramID() {
		return programID;
	}

	/**
	 * @param programID the programID to set
	 */
	public void setProgramID(String programID) {
		this.programID = programID;
	}

	/**
	 * @return the bandwidth
	 */
	public String getBandwidth() {
		return bandwidth;
	}

	/**
	 * @param bandwidth the bandwidth to set
	 */
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	

}
