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

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import wseemann.media.jplaylistparser.JPlaylistParserException;
import wseemann.media.jplaylistparser.MediaType;
import wseemann.media.jplaylistparser.PlayListAbstractClass;
import wseemann.media.jplaylistparser.Playlist;

import org.xml.sax.SAXException;

public interface Parser {

    Set<MediaType> getSupportedTypes();

    void parse(
            String uri, InputStream stream, Playlist playlist)
            throws IOException, SAXException, JPlaylistParserException;
    
    PlayListAbstractClass parseM3U8(
            String uri, InputStream stream, PlayListAbstractClass playlist)
            throws IOException, SAXException, JPlaylistParserException;
    
  
	PlayListAbstractClass parseM3U8File(String uri, InputStream stream) throws IOException, SAXException, JPlaylistParserException;


}
