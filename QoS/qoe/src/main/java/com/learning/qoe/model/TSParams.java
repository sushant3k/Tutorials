/**
 * 
 */
package com.learning.qoe.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sushant
 *
 */
public class TSParams implements Serializable{

    
    private PacketInformation packetInformation;
    private PAT pat;
    private PMT pmt;
    private String filename;
    
    private List<Stream> streams;
    
    
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public void addStream(Stream s) {
        if (streams == null) {
            streams = new ArrayList<TSParams.Stream>();
        }
        streams.add(s);
    }
    public List<Stream> getStreams() {
        return streams;
    }

    public void setStreams(List<Stream> streams) {
        this.streams = streams;
    }


    private Map<String, String> data = new HashMap<String, String>();
    
    public void addData(final String key, final String value) {
        data.put(key, value);
    }
    
    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getDataValue(final String key) {
        return data.get(key);
    }
    public static class PMT implements Serializable{
        
        private Map<String, String> pmt = new HashMap<String, String>();
        
        public PMT() {
            
        }

        public Map<String, String> getPmt() {
            return pmt;
        }

        public void setPmt(Map<String, String> pmt) {
            this.pmt = pmt;
        }
        
        public void addPMTDetails(final String key, final String value) {
            pmt.put(key, value);
            
        }
    }
    public static class PacketInformation implements Serializable{
        
        private Map<String, String> packet = new HashMap<String, String>();
           
        public PacketInformation() {
            
        }

        public Map<String, String> getPacket() {
            return packet;
        }

        public void setPacket(Map<String, String> packet) {
            this.packet = packet;
        }
        
        public void addPacket(final String key, final String value) {
            packet.put(key, value);
        }
        
        
    }
    
    public static class PAT implements Serializable{
        
        private Map<String, String> patPacket = new HashMap<String, String>();
        
        public PAT() {
            
        }

        public Map<String, String> getPatPacket() {
            return patPacket;
        }

        public void setPatPacket(Map<String, String> patPacket) {
            this.patPacket = patPacket;
        }
        
        public void addPatInformation(final String key, final String value) {
            patPacket.put(key, value);
        }
     }

   
    public PacketInformation getPacketInformation() {
        return packetInformation;
    }

    public void setPacketInformation(PacketInformation packetInformation) {
        this.packetInformation = packetInformation;
    }

    public PAT getPat() {
        return pat;
    }

    public void setPat(PAT pat) {
        this.pat = pat;
    }

    public PMT getPmt() {
        return pmt;
    }

    public void setPmt(PMT pmt) {
        this.pmt = pmt;
    }
    
    
    public static class Stream implements Serializable
    {
        private Integer streamIndex;
        
        private Map<String,String> streams ;
        
        public Stream() {}
        public Stream (Integer index) {
            this.streamIndex = index;
        }
        /**
         * The method is used to add a parameters. 
         * The method is not threadsafe and should be handled with care.
         * @param key
         * @param value
         */
        public void addStreamParameter(final String key, final String value) {
            if (streams == null ) {
                streams = new HashMap<String, String>();
            }
            
            
            streams.put(key, value);
            
            
        }
        public Integer getStreamIndex() {
            return streamIndex;
        }
        public void setStreamIndex(Integer streamIndex) {
            this.streamIndex = streamIndex;
        }
        public Map<String, String> getStreams() {
            return streams;
        }
        public void setStreams(Map<String, String> streams) {
            this.streams = streams;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Stream Index : ").append(streamIndex);
            for (Map.Entry<String, String> entry : streams.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue());
            }
            
            return sb.toString();
        }
        
    }
}
