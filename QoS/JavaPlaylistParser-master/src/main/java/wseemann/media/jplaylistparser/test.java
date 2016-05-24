package wseemann.media.jplaylistparser;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.xml.sax.SAXException;

public class test {
	
	public static void main(String a[]) {
//		
//	    testSplit("10");
//	    testSplit("10,");
//	    testSplit("10, no desc");
	    
	    testParser("C:/sushant/learnings_workspace/m3u8Files/indexa.m3u8", "C:/sushant/learnings_workspace/m3u8Files/index.m3u8");
        
	    testParser("C:/sushant/learnings_workspace/m3u8Files/prog_index.m3u8", "C:/sushant/learnings_workspace/m3u8Files/biobopall.m3u8");
        
	}

	private static void testParser(String mediaPlayListPath, String masterPlayListPath) {
	    
	    try {
            ParseFile parseFile=new ParseFile();
            PlayListAbstractClass pli = parseFile.parseFile(mediaPlayListPath, new String(Files.readAllBytes(Paths.get(mediaPlayListPath))));
            MediaPlayList mpl = (MediaPlayList)pli;
//          parseFile.parseFile(new String(Files.readAllBytes(Paths.get(path)), "UTF-8"),ListType.MEDIA);
            System.out.println("test");
            
        }catch (MalformedURLException | SAXException | JPlaylistParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
	}
	private static void testSplit(String s) {
	    System.out.println(s.split(",")[0]);
	}
}
