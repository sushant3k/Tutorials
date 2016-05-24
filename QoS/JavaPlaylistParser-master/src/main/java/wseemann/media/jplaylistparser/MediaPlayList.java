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
public class MediaPlayList extends PlayListAbstractClass{
	
	private String uri;
	private String duration;
	Map<String,String> nouriPlayList;
	Map<String,List<MediaPlayList>> mediaMap=null;
	/**
	 * @return the mediaMap
	 */
	public Map<String, List<MediaPlayList>> getMediaMap() {
		return mediaMap;
	}
	/**
	 * @param mediaMap the mediaMap to set
	 */
	public void setMediaMap(Map<String, List<MediaPlayList>> mediaMap) {
		this.mediaMap = mediaMap;
	}
	public MediaPlayList(){
		super(ListType.MEDIA);
		nouriPlayList=new HashMap<String,String>();
		mediaMap=new HashMap<String,List<MediaPlayList>>();
	}
	/**
	 * @return the nouriPlayList
	 */
	public Map<String, String> getNouriPlayList() {
		return nouriPlayList;
	}
	/**
	 * @param nouriPlayList the nouriPlayList to set
	 */
	public void setNouriPlayList(Map<String, String> nouriPlayList) {
		this.nouriPlayList = nouriPlayList;
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
	/**
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}
	 

}
