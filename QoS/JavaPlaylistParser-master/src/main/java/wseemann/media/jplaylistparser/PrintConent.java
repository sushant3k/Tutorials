package wseemann.media.jplaylistparser;

import java.util.List;
import java.util.Map;

import wseemann.media.jplaylistparser.PlayListAbstractClass;
import wseemann.media.jplaylistparser.MasterPlayList;
import wseemann.media.jplaylistparser.MediaPlayList;

public class PrintConent {
	
	public void printContent(PlayListAbstractClass playlist){
		if(playlist instanceof MasterPlayList){
	    	for (Map.Entry<String, List<MasterPlayList>> entry : ((MasterPlayList) playlist).getMasterMap().entrySet()) {
	            String key1 = entry.getKey();
	            List<MasterPlayList> values = entry.getValue();
	            System.out.println("Key = " + key1);
	            for(int i=0;i<values.size();i++){
	            	 System.out.println("bandwidth  = " + values.get(i).getBandwidth() + "\n");
	            	 System.out.println("uri = " + values.get(i).getUri() + "\n");
	            	 System.out.println("pgm id  = " + values.get(i).getProgramID() + "\n");
	    		}
	        }

	    }else if(playlist instanceof MediaPlayList){
	            Map<String, String> mps = ((MediaPlayList) playlist).getNouriPlayList();
	    	for (Map.Entry<String, List<MediaPlayList>> entry : ((MediaPlayList) playlist).getMediaMap().entrySet()) {
	            String key1 = entry.getKey();
	            List<MediaPlayList> values = entry.getValue();
	            System.out.println("\nKey = " + key1);	            
	            for(int i=0;i<values.size();i++){
	            	 System.out.printf("\nduration/URI = %s %s" ,values.get(i).getDuration(), values.get(i).getUri());
	            	 
	    		}
	        }
	    	
	    }
		
		
	}

}
