/*
 * Copyright 2014 William Seemann
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package wseemann.media.jplaylistparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.xml.sax.SAXException;

public class M3U8PlaylistParser extends AbstractParser {
	public final static String EXTENSION = ".m3u8";
	public final static String END_EXTENSION = "#EXT-X-ENDLIST";
	
    private final static String EXTENDED_INFO_TAG = "#EXTM3U";
    private final static String INFO_TAG = "^[#][E|e][X|x][T|t][-][X|x][-].*";
    private final static String RECORD_TAG = "^[#][E|e][X|x][T|t][I|i][N|n][F|f].*";
    
    private final static String PROTOCOL = "^[H|h][T|t][T|t][P|p].*";
    private final static String[] masterTags = new String[]{"#EXT-X-STREAM-INF", "#EXT-X-I-FRAME-STREAM-INF", "#EXT-X-SESSION-DATA", "#EXT-X-SESSION-KEY" };    
    private final static String[] mediaPlayListTags = new String[]{"#EXT-X-TARGETDURATION", "#EXT-X-MEDIA-SEQUENCE", "#EXT-X-DISCONTINUITY-SEQUENCE", "EXT-X-I-FRAMES-ONLY", "#EXT-X-VERSION"};
    private final static String[] mediaSegmentTags = new String[]{"#EXTINF","#EXT-X-BYTERANGE","#EXT-X-DISCONTINUITY","#EXT-X-KEY","#EXT-X-MAP","#EXT-X-PROGRAM-DATE-TIME"};
	
	private static int mNumberOfFiles = 0;
    private boolean processingEntry = false;
    
    private static final Set<MediaType> SUPPORTED_TYPES =
    		Collections.singleton(MediaType.audio("x-mpegurl"));

    public Set<MediaType> getSupportedTypes() {
    	return SUPPORTED_TYPES;
    }
    
	/**
	 * Retrieves the files listed in a .m3u file
	 * @throws IOException 
	 * @throws JPlaylistParserException 
	 */
   private void parsePlaylist(String uri, InputStream stream, Playlist playlist) throws IOException, JPlaylistParserException {
        String line = null;
        BufferedReader reader = null;
        PlaylistEntry playlistEntry = null;
        
        String host = getHost(uri);
        
		// Start the query
		reader = new BufferedReader(new InputStreamReader(stream));
        
		while ((line = reader.readLine()) != null) {
		    
			if (!(line.equalsIgnoreCase(EXTENDED_INFO_TAG) ||
					line.matches(INFO_TAG) ||
					line.trim().equals(""))) {
		    	if (line.matches(RECORD_TAG)) {
		    		playlistEntry = new PlaylistEntry();
		        	playlistEntry.set(PlaylistEntry.PLAYLIST_METADATA, line.replaceAll("^(.*?),", ""));
		    		processingEntry = true;
		    	} else {
		    		if (!processingEntry) {
		    			playlistEntry = new PlaylistEntry();
		    		}
		    		
		    		playlistEntry.set(PlaylistEntry.URI, generateUri(line.trim(), host));
		    		savePlaylistFile(playlistEntry, playlist);
		    	}
		    }           
        }
    }
    
    /**Method is used to parse media files- modified by heena(e01149)
     * @param uri
     * @param stream
     * @param playlist
     * @throws IOException
     * @throws JPlaylistParserException
     */
    private PlayListAbstractClass  parsePlaylists(String uri, InputStream stream, PlayListAbstractClass playlist) throws IOException, JPlaylistParserException {
        String line = null;
        BufferedReader reader = null;
        MasterPlayList masterPlayList=null;
        MediaPlayList mediaplayList=null;
        Map<String,List<MediaPlayList>> mediaMap=new HashMap<String,List<MediaPlayList>>();
        Map<String,List<MasterPlayList>> masterMap=new HashMap<String,List<MasterPlayList>>();
        List<MasterPlayList> listOfMasterPlayList=new ArrayList<MasterPlayList>();
        List<MediaPlayList> listOfMediaPlayList=new ArrayList<MediaPlayList>();
        String key="";
        String keymaster="";
        PlayListAbstractClass obj=null;
        Map<String,String> nouriPlaymap;
        
		// Start the query
		reader = new BufferedReader(new InputStreamReader(stream));
        
		while ((line = reader.readLine()) != null) {
			
			if (!(line.equalsIgnoreCase(EXTENDED_INFO_TAG) &&
					line.matches(INFO_TAG) ||
					line.trim().equals(""))) {
				
			    String[] splitted=line.split(":");
				
				if(Arrays.asList(masterTags).contains(splitted[0])){
					
				    masterPlayList=new MasterPlayList();
				    
					keymaster = splitted[0];
					
					String[] splitedArray=splitted[1].split(",");
					String pgmId=splitedArray[0].substring(splitedArray[0].indexOf("=")+1, splitedArray[0].length());
					String bndWidth=splitedArray[1].substring(splitedArray[1].indexOf("=")+1, splitedArray[1].length());
					
					if(pgmId.isEmpty()){
						masterPlayList.setProgramID(null);
					}
					else{
					masterPlayList.setProgramID(pgmId);
					}
					
					if(bndWidth.isEmpty()){
					    masterPlayList.setBandwidth(null);
					}else{
						masterPlayList.setBandwidth(bndWidth);						
					}
					
				}else if(Arrays.asList(mediaPlayListTags).contains(splitted[0])){
					mediaplayList=new MediaPlayList();
					nouriPlaymap = new HashMap<String,String>();
					nouriPlaymap.put(splitted[0], splitted[1]);
					mediaplayList.setNouriPlayList(nouriPlaymap);
					
				}else if(Arrays.asList(mediaSegmentTags).contains(splitted[0])){
					mediaplayList=new MediaPlayList();					
					key=splitted[0];
					
//					mediaplayList.setDuration(splitted[1].replaceAll(",", ""));
					mediaplayList.setDuration(splitted[1].split(",")[0]);
					
					
				}else if(END_EXTENSION.equalsIgnoreCase(splitted[0])){
					break;
				}
				
				
				
				if (!(line.equalsIgnoreCase(EXTENDED_INFO_TAG) ||
						line.matches(INFO_TAG) ||
						line.trim().equals("") || line.matches(RECORD_TAG)) && !line.endsWith(".ts")) {
					
					masterPlayList.setUri(line);
					 Set<String> keymasterSet = masterMap.keySet();
					 if(keymasterSet.size()==0){
						 listOfMasterPlayList.add(masterPlayList);
						 masterMap.put(keymaster, listOfMasterPlayList);
						}
					 else if(keymasterSet!=null && keymasterSet.contains(keymaster)){
						listOfMasterPlayList.add(masterPlayList);
						}
						else{listOfMasterPlayList.add(masterPlayList);
						masterMap.put(key, listOfMasterPlayList);
					}
			    } 
				
				
				if (!(line.equalsIgnoreCase(EXTENDED_INFO_TAG) ||
						line.matches(INFO_TAG) ||
						line.trim().equals("") || line.matches(RECORD_TAG)) && line.endsWith(".ts")) {
					
					mediaplayList.setUri(line);
					 Set<String> keys = mediaMap.keySet();
					 if(keys.size()==0){
						 listOfMediaPlayList.add(mediaplayList);
							mediaMap.put(key, listOfMediaPlayList);
						}
					 else if(keys!=null && keys.contains(key)){
						listOfMediaPlayList.add(mediaplayList);
						}
						else{listOfMediaPlayList.add(mediaplayList);
					mediaMap.put(key, listOfMediaPlayList);
					}
			    } 
				
				
			}
			
			
			
        }
		
		
		
		
		
		if(playlist instanceof MediaPlayList){
			mediaplayList.setMediaMap(mediaMap);
			 obj=mediaplayList;
		}
		if(playlist instanceof MasterPlayList){
			masterPlayList.setMasterMap(masterMap);
			 obj=masterPlayList;
		}
		return obj;
    }
   
   /**The EXT-X-ENDLIST tag indicates that no more media segments will be
   added to the Media Playlist file.  It MAY occur anywhere in the
   Playlist file; it MUST NOT occur more than once.  Its format is:

   #EXT-X-ENDLIST

   The EXT-X-ENDLIST tag MUST NOT appear in a Master Playlist.
 * @param uri
 * @param stream
 * @return
 * @throws IOException
 * @throws JPlaylistParserException
 */
private PlayListAbstractClass  parseM3U8Playlists(String uri, InputStream stream) throws IOException, JPlaylistParserException {
       String line = null;
       BufferedReader reader = null;
       MasterPlayList masterPlayList=null;
       MediaPlayList mediaplayList=null;
       Map<String,List<MediaPlayList>> mediaMap=new HashMap<String,List<MediaPlayList>>();
       Map<String,List<MasterPlayList>> masterMap=new HashMap<String,List<MasterPlayList>>();
       List<MasterPlayList> listOfMasterPlayList=new ArrayList<MasterPlayList>();
       List<MediaPlayList> listOfMediaPlayList=new ArrayList<MediaPlayList>();
       String key="";
       String keymaster="";
       PlayListAbstractClass obj=null;
       Map<String,String> nouriPlaymap;
       String[] splitted = null;
      List<String> tagList=new ArrayList<String>();
      List<String> tagList1=new ArrayList<String>();
		// Start the query
		reader = new BufferedReader(new InputStreamReader(stream));
       
		while ((line = reader.readLine()) != null) {
			
			if (!(line.equalsIgnoreCase(EXTENDED_INFO_TAG) &&
					line.matches(INFO_TAG) ||
					line.trim().equals(""))) {
				 splitted=line.split(":");
				
				
				
				final String tag = splitted[0];
				if(Arrays.asList(masterTags).contains(tag)){
				    				    
					masterPlayList=new MasterPlayList();					
					keymaster=tag.trim();					
					tagList.add(keymaster);
//					if (tag.equalsIgnoreCase(anotherString))
					
					String[] splitedArray=splitted[1].split(",");
					
					String pgmId=splitedArray[0].substring(splitedArray[0].indexOf("=")+1, splitedArray[0].length());
					String bndWidth=splitedArray[1].substring(splitedArray[1].indexOf("=")+1, splitedArray[1].length());
					if(pgmId.isEmpty()){
						masterPlayList.setProgramID(null);
					}else{
					masterPlayList.setProgramID(pgmId);
					}
					if(bndWidth.isEmpty()){

					masterPlayList.setBandwidth(null);
					}else{
						masterPlayList.setBandwidth(bndWidth);
						
					}
					
				}else if(Arrays.asList(mediaPlayListTags).contains(splitted[0])){
                    mediaplayList=new MediaPlayList();
                    nouriPlaymap = new HashMap<String,String>();
                    nouriPlaymap.put(splitted[0], splitted[1]);
                    mediaplayList.setNouriPlayList(nouriPlaymap);
                    mediaplayList.setDuration(splitted[1]);
                    listOfMediaPlayList=new ArrayList<MediaPlayList>();
                    listOfMediaPlayList.add(mediaplayList);
                    mediaMap.put(splitted[0], listOfMediaPlayList);
                    
                }else if(Arrays.asList(mediaSegmentTags).contains(splitted[0])){
                    mediaplayList=new MediaPlayList();
                    
                    key=splitted[0].trim();
                    tagList1.add(key);
                    mediaplayList.setDuration(splitted[1].replaceAll(",", ""));
					
					
				}else if(END_EXTENSION.equalsIgnoreCase(splitted[0])){
					break;
				}
				
				
				
				if (!(line.equalsIgnoreCase(EXTENDED_INFO_TAG) ||
                        line.matches(INFO_TAG) ||
                        line.trim().equals("") || line.matches(RECORD_TAG)) && !line.endsWith(".ts")) {
                    
                    masterPlayList.setUri(line);
                    
                     Set<String> keymasterSet = masterMap.keySet();
                     if(keymasterSet.size()==0){
                         listOfMasterPlayList.add(masterPlayList);
                         masterMap.put(keymaster, listOfMasterPlayList);
                        }
                     else if ( masterMap.containsKey(keymaster) ) {
                            List<MasterPlayList> masterlist = masterMap.get(keymaster);
                            masterlist.add(masterPlayList);
                            masterMap.put(keymaster, masterlist);
                        }
                        else{
                            listOfMasterPlayList=new ArrayList<MasterPlayList>();
                            listOfMasterPlayList.add(masterPlayList);
                        masterMap.put(keymaster, listOfMasterPlayList);
                    }
                } 
                
                
                if (!(line.equalsIgnoreCase(EXTENDED_INFO_TAG) ||
                        line.matches(INFO_TAG) ||
                        line.trim().equals("") || line.matches(RECORD_TAG)) && line.endsWith(".ts")) {
                    
                    mediaplayList.setUri(line);
                     Set<String> keys = mediaMap.keySet();
                     if(keys.size()==0){
                         listOfMediaPlayList.add(mediaplayList);
                            mediaMap.put(key, listOfMediaPlayList);
                        }
                     else if(keys!=null && mediaMap.containsKey(key)){
                         List<MediaPlayList> mediaList = mediaMap.get(key);
                         mediaList.add(mediaplayList);
                         mediaMap.put(key, mediaList);
                        }
                        else{
                            listOfMediaPlayList=new ArrayList<MediaPlayList>();
                            listOfMediaPlayList.add(mediaplayList);
                    mediaMap.put(key, listOfMediaPlayList);
                    }
                } 
				
				
			}
			
			
			
       }
		Set<String> tagsetOfMasterFile = new HashSet<String>(tagList);
		Set<String> tagsetOfMediaFile = new HashSet<String>(tagList1);
		Set intersect = new TreeSet(Arrays.asList(masterTags));
		Set intersect1 = new TreeSet(Arrays.asList(mediaSegmentTags));
		intersect.retainAll(tagsetOfMasterFile);
		intersect1.retainAll(tagsetOfMediaFile);
		if(intersect.size()>0){
			masterPlayList.setMasterMap(masterMap);
			 obj=masterPlayList;
		}else if(intersect1.size()>0){
			mediaplayList.setMediaMap(mediaMap);
			 obj=mediaplayList;
		}
		
		return obj;
   }
    
    
    

    private void savePlaylistFile(PlaylistEntry playlistEntry, Playlist playlist) {
    	mNumberOfFiles = mNumberOfFiles + 1;
    	playlistEntry.set(PlaylistEntry.TRACK, String.valueOf(mNumberOfFiles));
    	parseEntry(playlistEntry, playlist);
    	processingEntry = false;
    }

    private String getHost(String uri) throws JPlaylistParserException {
		try {
			URI host = new URI(uri);
    	
			String path;
    	
			if ((path = host.getPath()) == null || path.trim().equals("")) {
				return uri + '/';
			}
    	
			int index = path.lastIndexOf('/');
    	
			if (index > -1) {
				host = new URI(host.getScheme(), null, host.getHost(), host.getPort(), path.substring(0, index + 1), null, null);
			}
			
	    	return host.toString();
		} catch (URISyntaxException e) {
			throw new JPlaylistParserException(e.getMessage());
		}
    }
    
    private String generateUri(String uri, String host) {
    	if (uri.matches(PROTOCOL)) {
    		return uri;
    	}
    	
    	return host + uri;
    }
    
	
	public PlayListAbstractClass parseM3U8(String uri, InputStream stream, PlayListAbstractClass playlist)
			throws IOException, SAXException, JPlaylistParserException {
		return parsePlaylists(uri, stream, playlist);
		
	}
	
	public PlayListAbstractClass parseM3U8File(String uri, InputStream stream)
			throws IOException, SAXException, JPlaylistParserException {
		return parseM3U8Playlists(uri, stream);
		
	}
	
	

	
	public void parse(String uri, InputStream stream, Playlist playlist)
			throws IOException, SAXException, JPlaylistParserException {
		// TODO Auto-generated method stub
		parsePlaylist(uri, stream, playlist);
		
	}

	

	
}

