package wseemann.media.jplaylistparser;

import wseemann.media.jplaylistparser.MasterPlayList;
import wseemann.media.jplaylistparser.MediaPlayList;

public class FactoryObject {
	
	public static PlayListAbstractClass buildObject(ListType model) {
		PlayListAbstractClass playListObject = null;
        switch (model) {
        case MEDIA:
        	playListObject = new MediaPlayList();
            break;
 
        case MASTER:
        	playListObject = new MasterPlayList();
            break;
 
        default:
            // throw some exception
            break;
        }
        return playListObject;
    }

}
