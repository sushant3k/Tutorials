package wseemann.media.jplaylistparser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.SAXException;

public class ParseFile {
	
	public PlayListAbstractClass parseFile(String filename, String playList, ListType type) throws IOException, SAXException, JPlaylistParserException{
		

		String contentType = "text/html";
//		InputStream is = new FileInputStream(filename);
		InputStream is = new ByteArrayInputStream(playList.getBytes("UTF-8"));
		AutoDetectParser parser = new AutoDetectParser(); 
	    PlayListAbstractClass obj=FactoryObject.buildObject(type);
	    PlayListAbstractClass playlist=parser.parse(filename, contentType, is, obj);
//	    PrintConent printConent=new PrintConent();
//	    printConent.printContent(playlist);
	    
	    return playlist;
		
	}
	
	public PlayListAbstractClass parseFile(String filename, String content) throws IOException, SAXException, JPlaylistParserException{
        

        String contentType = "text/html";
        InputStream is = new ByteArrayInputStream(content.getBytes("UTF-8"));
        AutoDetectParser parser = new AutoDetectParser();       
        PlayListAbstractClass playlist=parser.parse(filename, contentType, is);     
        
//        PrintConent printConent=new PrintConent();
//        printConent.printContent(playlist);
        return playlist;
    }

}
