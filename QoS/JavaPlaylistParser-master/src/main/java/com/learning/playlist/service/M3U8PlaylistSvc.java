/**
 * 
 */
package com.learning.playlist.service;

import wseemann.media.jplaylistparser.MasterPlayList;
import wseemann.media.jplaylistparser.MediaPlayList;
import wseemann.media.jplaylistparser.PlayListAbstractClass;

/**
 * @author ggne0084
 *
 */
public interface M3U8PlaylistSvc {

    /**
     * This method returns the PlayList Object. 
     * The PlaylistObject can Either be {@link MediaPlayList} or {@link MasterPlayList}.
     * It is the responsibility of the client to manage it by itself.
     * @param filename
     * @param playlist
     * @return
     */
    PlayListAbstractClass parsePlaylist(String filename, String playlist);
}
