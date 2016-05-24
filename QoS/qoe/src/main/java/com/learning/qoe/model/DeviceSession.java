/**
 * 
 */
package com.learning.qoe.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sushant
 *
 */
public class DeviceSession implements Serializable{

    private String sessionId;
    private Device deviceProfile;
    
    private List<TCP> tcp;
    private List<HLS> hls;
    private List<TS> ts;
    private List<DeviceNetworkInformation> deviceNetworkInformation;
    
    private List<StreamGraph> streamGraph;
    
    
    public List<DeviceNetworkInformation> getDeviceNetworkInformation() {
        return deviceNetworkInformation;
    }
    public void setDeviceNetworkInformation(
            List<DeviceNetworkInformation> deviceNetworkInformation) {
        this.deviceNetworkInformation = deviceNetworkInformation;
    }
    public List<StreamGraph> getStreamGraph() {
        return streamGraph;
    }
    public void setStreamGraph(List<StreamGraph> streamGraph) {
        this.streamGraph = streamGraph;
    }
    public void addStreamGraph(StreamGraph sg) {
        if (streamGraph == null ) {
            streamGraph = new ArrayList<StreamGraph>();
        }
        streamGraph.add(sg);
    }
    public void addDeviceNetworkInformation(DeviceNetworkInformation din) {
        if (deviceNetworkInformation == null) {
            deviceNetworkInformation = new ArrayList<DeviceSession.DeviceNetworkInformation>();
        }
        deviceNetworkInformation.add(din);
    }
    
    public void addHLS(HLS h) {
        if (hls == null) {
            hls = new ArrayList<DeviceSession.HLS>();
        }
        hls.add(h);
    }
    
    public void addTS(TS t) {
        if (ts == null) {
            ts = new ArrayList<DeviceSession.TS>();
        }
        ts.add(t);
    }
    
    
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Device getDeviceProfile() {
        return deviceProfile;
    }

    public void setDeviceProfile(Device deviceProfile) {
        this.deviceProfile = deviceProfile;
    }

    public List<TCP> getTcp() {
        return tcp;
    }

    public void setTcp(List<TCP> tcp) {
        this.tcp = tcp;
    }

    public List<HLS> getHls() {
        return hls;
    }

    public void setHls(List<HLS> hls) {
        this.hls = hls;
    }

    public List<TS> getTs() {
        return ts;
    }

    public void setTs(List<TS> ts) {
        this.ts = ts;
    }

    public void addTCP(TCP t) {
        if (tcp == null) {
            tcp = new ArrayList<DeviceSession.TCP>();
        }
        tcp.add(t);
    }
    
    public static class StreamGraph implements Serializable{
        
        private Long packetRequestTime;
        private Long bandwidth;
        private String filename;
        private float duration;
        private Long lastPacketReceivedTime;
        
        public StreamGraph() {
            
        }

        
        public Long getLastPacketReceivedTime() {
            return lastPacketReceivedTime;
        }


        public void setLastPacketReceivedTime(Long lastPacketReceivedTime) {
            this.lastPacketReceivedTime = lastPacketReceivedTime;
        }


        public float getDuration() {
            return duration;
        }


        public void setDuration(float duration) {
            this.duration = duration;
        }


        public Long getPacketRequestTime() {
            return packetRequestTime;
        }

        public void setPacketRequestTime(Long packetRequestTime) {
            this.packetRequestTime = packetRequestTime;
        }

        public Long getBandwidth() {
            return bandwidth;
        }

        public void setBandwidth(Long bandwidth) {
            this.bandwidth = bandwidth;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }


       
        
    }
    public static class DeviceNetworkInformation implements Serializable{
        private float latitute;
        private float longitude;
        private String networkType;
        private long streamIndex;
        
        public DeviceNetworkInformation() {
            
        }

        
        public long getStreamIndex() {
            return streamIndex;
        }


        public void setStreamIndex(long streamIndex) {
            this.streamIndex = streamIndex;
        }


        public float getLatitute() {
            return latitute;
        }

        public void setLatitute(float latitute) {
            this.latitute = latitute;
        }

        public float getLongitude() {
            return longitude;
        }

        public void setLongitude(float longitude) {
            this.longitude = longitude;
        }

        public String getNetworkType() {
            return networkType;
        }

        public void setNetworkType(String networkType) {
            this.networkType = networkType;
        }
        
        
    }
    public static class TCP implements Serializable{
        private long streamId;
        
        private  String sourceIP;
        private int sourcePort;
        private String destIP;
        private int destPort;
        private float bandwidth;
        private int retransmission;
        private int droppedPackets;
        private int connectionResets;
        private int dupAcks;
        private int outOfOrder;
        
        private long firstPacketReceivedTime;
        private long lastPacketReceivedTime;
        private long bytesInPayload;
        
        public TCP() {
            
        }

        public long getStreamId() {
            return streamId;
        }

        public void setStreamId(long streamId) {
            this.streamId = streamId;
        }

        

        public String getSourceIP() {
            return sourceIP;
        }

        public void setSourceIP(String sourceIP) {
            this.sourceIP = sourceIP;
        }

        public int getSourcePort() {
            return sourcePort;
        }

        public void setSourcePort(int sourcePort) {
            this.sourcePort = sourcePort;
        }

        public String getDestIP() {
            return destIP;
        }

        public void setDestIP(String destIP) {
            this.destIP = destIP;
        }

        public int getDestPort() {
            return destPort;
        }

        public void setDestPort(int destPort) {
            this.destPort = destPort;
        }

        public float getBandwidth() {
            return bandwidth;
        }

        public void setBandwidth(float bandwidth) {
            this.bandwidth = bandwidth;
        }

        public int getRetransmission() {
            return retransmission;
        }

        public void setRetransmission(int retransmission) {
            this.retransmission = retransmission;
        }

        public int getDroppedPackets() {
            return droppedPackets;
        }

        public void setDroppedPackets(int droppedPackets) {
            this.droppedPackets = droppedPackets;
        }

        public int getConnectionResets() {
            return connectionResets;
        }

        public void setConnectionResets(int connectionResets) {
            this.connectionResets = connectionResets;
        }

        public int getDupAcks() {
            return dupAcks;
        }

        public void setDupAcks(int dupAcks) {
            this.dupAcks = dupAcks;
        }

        public int getOutOfOrder() {
            return outOfOrder;
        }

        public void setOutOfOrder(int outOfOrder) {
            this.outOfOrder = outOfOrder;
        }

        public long getFirstPacketReceivedTime() {
            return firstPacketReceivedTime;
        }

        public void setFirstPacketReceivedTime(long firstPacketReceivedTime) {
            this.firstPacketReceivedTime = firstPacketReceivedTime;
        }

        public long getLastPacketReceivedTime() {
            return lastPacketReceivedTime;
        }

        public void setLastPacketReceivedTime(long lastPacketReceivedTime) {
            this.lastPacketReceivedTime = lastPacketReceivedTime;
        }

        public long getBytesInPayload() {
            return bytesInPayload;
        }

        public void setBytesInPayload(long bytesInPayload) {
            this.bytesInPayload = bytesInPayload;
        }
        
        
       
    }
    
    public static class HLS implements Serializable{
        private String filename;
        
        private String version;
        private boolean masterPlaylist;        
        private List<MasterStreamInfo> masterStreamInfo;
        private List<MediaStreamInfo> mediaStreamInfo;
        
        private int targetDuration;
        private int mediaSequence;
        
        
        public HLS() {
            
        }

        
        
        
        public List<MediaStreamInfo> getMediaStreamInfo() {
            return mediaStreamInfo;
        }




        public void setMediaStreamInfo(List<MediaStreamInfo> mediaStreamInfo) {
            this.mediaStreamInfo = mediaStreamInfo;
        }




        public boolean isMasterPlaylist() {
            return masterPlaylist;
        }

        
        

        public int getTargetDuration() {
            return targetDuration;
        }



        public void setTargetDuration(int targetDuration) {
            this.targetDuration = targetDuration;
        }



        public int getMediaSequence() {
            return mediaSequence;
        }



        public void setMediaSequence(int mediaSequence) {
            this.mediaSequence = mediaSequence;
        }



        public void setMasterPlaylist(boolean masterPlaylist) {
            this.masterPlaylist = masterPlaylist;
        }



        public String getVersion() {
            return version;
        }


        public void setVersion(String version) {
            this.version = version;
        }

        public MediaStreamInfo createMediaStreamInfoObject() {
            return new MediaStreamInfo();
        }
        public MasterStreamInfo createMasterStreamInfoObject() {
            return new MasterStreamInfo();
        }
      
        public void addMediaStreamInfo(MediaStreamInfo msi) {
            if (msi == null) {
                return;
            }
            if (mediaStreamInfo == null) {
                mediaStreamInfo = new ArrayList<DeviceSession.HLS.MediaStreamInfo>();                        
            }
            mediaStreamInfo.add(msi);
        }
        public void addMasterStreamInfo(MasterStreamInfo msi) {
            if (msi == null) {
                return;
            }
            
            if (masterStreamInfo == null) {
                masterStreamInfo = new ArrayList<DeviceSession.HLS.MasterStreamInfo>();
            }
            masterStreamInfo.add(msi);
            
        }

        public List<MasterStreamInfo> getMasterStreamInfo() {
            return masterStreamInfo;
        }


        public void setMasterStreamInfo(List<MasterStreamInfo> masterStreamInfo) {
            this.masterStreamInfo = masterStreamInfo;
        }


        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public static class MediaStreamInfo implements Serializable {
            private Float duration;
            private String uri;
            
            public MediaStreamInfo() {
                
            }

            public Float getDuration() {
                return duration;
            }

            public void setDuration(Float duration) {
                this.duration = duration;
            }

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }
            
            
        }
        
        public static class MasterStreamInfo implements Serializable{
            
            /**
             * 
             */
            private static final long serialVersionUID = 1L;
            
            private Integer programId;
            private Long bandwidth;
            private String uri;
            
            public MasterStreamInfo() {
                
            }

            public Integer getProgramId() {
                return programId;
            }

            public void setProgramId(Integer programId) {
                this.programId = programId;
            }

            public Long getBandwidth() {
                return bandwidth;
            }

            public void setBandwidth(Long bandwidth) {
                this.bandwidth = bandwidth;
            }

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }
            
            
        }
    }
    
    public static class TS implements Serializable{
        private int syncErrorCount;
        private int scrambledPayloadCount;
        private int malformedPacketCount;
        private String containerFormat;
        private int numberOfStreams;
        private long duration;
        private long startTime;
        private long fileSize;
        private String filename;
        private long bitrate;
        private int patErrors;
        private int pmtErrors;
        private int nullPacketCount;
        private int unsupportedPMT;
        private long packetReceivedTime;
        
        public TS() {
            
        }
        
        
        
        public int getUnsupportedPMT() {
            return unsupportedPMT;
        }



        public void setUnsupportedPMT(int unsupportedPMT) {
            this.unsupportedPMT = unsupportedPMT;
        }



        public int getNullPacketCount() {
            return nullPacketCount;
        }



        public void setNullPacketCount(int nullPacketCount) {
            this.nullPacketCount = nullPacketCount;
        }



        public int getPatErrors() {
            return patErrors;
        }



        public void setPatErrors(int patErrors) {
            this.patErrors = patErrors;
        }



        public int getPmtErrors() {
            return pmtErrors;
        }



        public void setPmtErrors(int pmtErrors) {
            this.pmtErrors = pmtErrors;
        }



        public int getSyncErrorCount() {
            return syncErrorCount;
        }
        public void setSyncErrorCount(int syncErrorCount) {
            this.syncErrorCount = syncErrorCount;
        }
        public int getScrambledPayloadCount() {
            return scrambledPayloadCount;
        }
        public void setScrambledPayloadCount(int scrambledPayloadCount) {
            this.scrambledPayloadCount = scrambledPayloadCount;
        }
        public int getMalformedPacketCount() {
            return malformedPacketCount;
        }
        public void setMalformedPacketCount(int malformedPacketCount) {
            this.malformedPacketCount = malformedPacketCount;
        }
        public String getContainerFormat() {
            return containerFormat;
        }
        public void setContainerFormat(String containerFormat) {
            this.containerFormat = containerFormat;
        }
        public int getNumberOfStreams() {
            return numberOfStreams;
        }
        public void setNumberOfStreams(int numberOfStreams) {
            this.numberOfStreams = numberOfStreams;
        }
        public long getDuration() {
            return duration;
        }
        public void setDuration(long duration) {
            this.duration = duration;
        }
        public long getStartTime() {
            return startTime;
        }
        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }
        public long getFileSize() {
            return fileSize;
        }
        public void setFileSize(long fileSize) {
            this.fileSize = fileSize;
        }
        public String getFilename() {
            return filename;
        }
        public void setFilename(String filename) {
            this.filename = filename;
        }
        public long getBitrate() {
            return bitrate;
        }
        public void setBitrate(long bitrate) {
            this.bitrate = bitrate;
        }
        public List<TSStreams> getStreams() {
            return streams;
        }
        public void setStreams(List<TSStreams> streams) {
            this.streams = streams;
        }

        
        public long getPacketReceivedTime() {
            return packetReceivedTime;
        }



        public void setPacketReceivedTime(long packetReceivedTime) {
            this.packetReceivedTime = packetReceivedTime;
        }


        private List<TSStreams> streams;
        
        public void addTSStream(TSStreams s) {
            if (streams == null) {
                streams = new ArrayList<DeviceSession.TS.TSStreams>();
            }
            streams.add(s);
        }
        public static class TSStreams implements Serializable {
            
            private int streamId;
            private String duration;
            private String direction;
            private String droppedFrames;
            private String startTime;
            private String language;
            private String codecType;
            private String codecId;
            private String numberOFrames;
            private String videoHeight;
            private String videoframes;
            private String videoFormat;
            private String audioSampleRate;
            private String audioChannels;
            private String videoWidth;
            
            public TSStreams() {
                
            }

            
          
            public String getVideoWidth() {
                return videoWidth;
            }



            public void setVideoWidth(String videoWidth) {
                this.videoWidth = videoWidth;
            }



            public int getStreamId() {
                return streamId;
            }

            public void setStreamId(int streamId) {
                this.streamId = streamId;
            }

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public String getDirection() {
                return direction;
            }

            public void setDirection(String direction) {
                this.direction = direction;
            }

            public String getDroppedFrames() {
                return droppedFrames;
            }

            public void setDroppedFrames(String droppedFrames) {
                this.droppedFrames = droppedFrames;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }

            public String getCodecType() {
                return codecType;
            }

            public void setCodecType(String codecType) {
                this.codecType = codecType;
            }

            public String getCodecId() {
                return codecId;
            }

            public void setCodecId(String codecId) {
                this.codecId = codecId;
            }

            public String getNumberOFrames() {
                return numberOFrames;
            }

            public void setNumberOFrames(String numberOFrames) {
                this.numberOFrames = numberOFrames;
            }

            public String getVideoHeight() {
                return videoHeight;
            }

            public void setVideoHeight(String videoHeight) {
                this.videoHeight = videoHeight;
            }

            public String getVideoframes() {
                return videoframes;
            }

            public void setVideoframes(String videoframes) {
                this.videoframes = videoframes;
            }

            public String getVideoFormat() {
                return videoFormat;
            }

            public void setVideoFormat(String videoFormat) {
                this.videoFormat = videoFormat;
            }

            public String getAudioSampleRate() {
                return audioSampleRate;
            }

            public void setAudioSampleRate(String audioSampleRate) {
                this.audioSampleRate = audioSampleRate;
            }

            public String getAudioChannels() {
                return audioChannels;
            }

            public void setAudioChannels(String audioChannels) {
                this.audioChannels = audioChannels;
            }
            
            
        }
    }
}
