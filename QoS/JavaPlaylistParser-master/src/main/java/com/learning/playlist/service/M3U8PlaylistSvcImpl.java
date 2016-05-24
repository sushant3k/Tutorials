/**
 * 
 */
package com.learning.playlist.service;

import java.io.IOException;

import org.xml.sax.SAXException;

import wseemann.media.jplaylistparser.JPlaylistParserException;
import wseemann.media.jplaylistparser.ParseFile;
import wseemann.media.jplaylistparser.PlayListAbstractClass;

/**
 * @author ggne0084
 *
 */

public class M3U8PlaylistSvcImpl implements M3U8PlaylistSvc{

    public PlayListAbstractClass parsePlaylist(String filename, String playlist) {
        ParseFile parseFile=new ParseFile();
        try {
            return parseFile.parseFile(filename, playlist);
            
        } catch (IOException | SAXException | JPlaylistParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        return null;
    }

}
