/**
 * 
 */
package com.learning.qoeweb.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import wseemann.media.jplaylistparser.MasterPlayList;
import wseemann.media.jplaylistparser.MediaPlayList;
import wseemann.media.jplaylistparser.PlayListAbstractClass;

import com.learning.playlist.service.M3U8PlaylistSvc;
import com.learning.qoe.constants.QoeConstants;
import com.learning.qoe.entities.SessionRecordsEntity;
import com.learning.qoe.model.Device;
import com.learning.qoe.model.DeviceSession;
import com.learning.qoe.model.DeviceSession.HLS.MasterStreamInfo;
import com.learning.qoe.model.DeviceSession.HLS.MediaStreamInfo;
import com.learning.qoe.model.Devices;
import com.learning.qoe.model.HTTPParams;
import com.learning.qoe.model.Params;
import com.learning.qoe.model.QoEPacket;
import com.learning.qoe.model.Session;
import com.learning.qoe.model.Sessions;
import com.learning.qoe.model.TSParams;
import com.learning.qoe.model.TSParams.Stream;
import com.learning.qoe.repositories.SessionRecordsEntityRepository;
import com.learning.qoe.utils.QoeStringUtils;

/**
 * @author Sushant
 *
 */
@Service
public class GenericServiceImpl implements GenericService{

    private Map<String, Long> mapURIBandwidth;
    private Map<String, String> mapTSURI;
    private Map<String, Float> mapTSDuration;
    
    @Autowired
    private JmsTemplate jmsTemplate;
    
    @Autowired
    private WebSocketMessageSenderSvc webSocketMessageSenderSvc;
    
    @Autowired
    private SessionRecordsEntityRepository qoeRecordsRepository;
    
    @Autowired
    private HLSParserSvc hlsParserSvc;
    
    
    private M3U8PlaylistSvc m3u8PlaylistSvc;
    
    @Autowired
    public void setM3u8PlaylistSvc(M3U8PlaylistSvc m3u8PlaylistSvc) {
        this.m3u8PlaylistSvc = m3u8PlaylistSvc;
//        TopicConnectionFactory tcf = (TopicConnectionFactory)jmsTemplate.getConnectionFactory();
        
    }

    @Override
    public Sessions getAllSessions() {
        List<SessionRecordsEntity> lst = qoeRecordsRepository.findAll();
        if (lst == null) {
            return null;
        }
        Sessions s = new Sessions();
        for (SessionRecordsEntity ss : lst) {
            QoEPacket q = ss.getQoePacket();
            Session s1 = new Session();
            s1.setDestIp(q.getDestIp());
            s1.setDestPort(q.getDestPort());
            s1.setSrcIp(q.getSourceIp());
            s1.setSrcPort(q.getSourcePort());
            s.addSession(s1);
        }
        return s;
        
    }

    @Override
    public void save(QoEPacket packet) {
        jmsTemplate.convertAndSend(QoeConstants.PACKET_QUEUE, packet);
//       qoeRecordsRepository.save(new SessionRecordsEntity(packet));
        
    }

    @Override
    public Sessions findBySessionId(String sessionId) {
        List<SessionRecordsEntity> lst =  qoeRecordsRepository.findBySessionId(sessionId);
        if (lst == null) {
            return null;
        }
        Sessions s = new Sessions();
        for (SessionRecordsEntity ss : lst) {
            QoEPacket q = ss.getQoePacket();
            Session s1 = new Session();
            s1.setDestIp(q.getDestIp());
            s1.setDestPort(q.getDestPort());
            s1.setSrcIp(q.getSourceIp());
            s1.setSrcPort(q.getSourcePort());
            s.addSession(s1);
        }
        return s;
    }

    @Override
    public Session findBySessionIdAndStreamId(String sessionId, long streamIndex) {
        SessionRecordsEntity se = qoeRecordsRepository.findBySessionIdAndStreamIndex(sessionId, streamIndex);
        QoEPacket qe = se.getQoePacket();
        // TODO: Get Data
        return new Session();
    }

    @Override
    public Devices findAllDevices() {
        List<SessionRecordsEntity> ls = qoeRecordsRepository.findAll();
        
        if (ls== null) {
            return null;
        }
        Devices d = new Devices();
        Map<String, Device> map = new HashMap<String, Device>();
        for (SessionRecordsEntity se : ls) {
            
            Device d1 = se.getQoePacket().getDevice();
            Device temp = map.get(d1.getDeviceIdentifier());
            if (temp != null) {
                temp.addSession(new Session(se.getQoePacket().getSessionId()));
                map.put(d1.getDeviceIdentifier(), temp);
            }
            else {
                d1.addSession(new Session(se.getQoePacket().getSessionId()));
                map.put(d1.getDeviceIdentifier(), d1);
            }            
        }
        d.populateDevices(map);
        return d;
    }

    @Override
    public SessionRecordsEntity findDetailsByDeviceId(String deviceId) {
       List<SessionRecordsEntity> ls = qoeRecordsRepository.findByQoePacketDeviceDeviceIdentifier(deviceId);
       if (ls == null) {
           return null;
       }
       return ls.get(0);
    }

    @Override
    public DeviceSession findByDeviceAndSession(String deviceId, String sessionId) {
        List<SessionRecordsEntity> lst = qoeRecordsRepository.findByQoePacketSessionIdAndQoePacketDeviceDeviceIdentifierOrderByQoePacketFirstPacketReceivedTimeAsc(sessionId, deviceId);
        if (lst == null || lst.isEmpty()) {
            return null;
        }
        
        mapURIBandwidth = new HashMap<String, Long>();
        mapTSURI = new HashMap<String, String>();
        mapTSDuration = new HashMap<String, Float>();
        DeviceSession ds = new DeviceSession();
        
        ds.setSessionId(sessionId);
        for (SessionRecordsEntity se : lst) {
            QoEPacket qp = se.getQoePacket();
            ds.setDeviceProfile(qp.getDevice());
            populateDeviceDetails(ds,qp.getDevice(), qp.getStreamIndex());
            
            /**
             * Do that Top down
             */
            populateHTTPDetails(ds, qp);
            populateTCPDetails(ds,qp);            
            populateTSDetails(ds,qp);
            
        }
       
//        jmsTemplate.convertAndSend("topic/message", ds);
//        webSocketMessageSenderSvc.test(ds);
        return ds;
        
    }

    private void populateHTTPDetails(final DeviceSession ds, final QoEPacket qp) {
        
        Params params = qp.getParams();
        
        if (params == null) {
            return;
        }
        
        List<HTTPParams> ls = params.getHttpParams();
        if (ls != null && !ls.isEmpty()) {
            for (HTTPParams htps : ls) {
                if (htps.isRequest()) {
                    continue;
                }
                else {
                    
                    getHLSContent(ds, htps.getFilename(), htps.getPayload());
                }
                
            }
        }
    }
    private void getHLSContent (DeviceSession ds, final String filename, final String payload) {
        if (payload == null || payload.isEmpty()) {
            return;
        }
//        System.out.println(filename);
        PlayListAbstractClass cls = m3u8PlaylistSvc.parsePlaylist(filename, payload);
        if (cls == null) {
            System.err.println("Why is it null?");
            return;
        }
        
        
        
        if (cls instanceof MasterPlayList) {
//            ds.addHLS(getMasterListContent(filename, cls));
            getMasterListContent(filename, cls);
        }
        else if (cls instanceof MediaPlayList) {
            getMediaPlayListContent(filename, cls);
            ds.addHLS(getMediaPlayListContent(filename, cls));
        }
        else {
            System.err.println("Invalid playlist....");
        }
//        hlsParserSvc
//        ds.addHLS(h);
        // TODO: Get HLS Content
        
    }
    
    private DeviceSession.HLS getMediaPlayListContent(final String filename, PlayListAbstractClass playlist) {
        MediaPlayList mpl = (MediaPlayList) playlist;
        Map<String, List<MediaPlayList>> mpl1 = mpl.getMediaMap();
        
        if (mpl1 == null || mpl1.isEmpty()) {
            return null;
        }
        
        List<MediaPlayList> lst = mpl1.get("#EXTINF");//mpl1.get("#EXT-X-VERSION");
        
        if (lst == null || lst.isEmpty()) {
            return null;
        }
        
        DeviceSession.HLS hls = new DeviceSession.HLS();
        
        hls.setMasterPlaylist(false);
        hls.setFilename(filename);
        
        for (MediaPlayList m1 : lst) {
            MediaStreamInfo msi = hls.createMediaStreamInfoObject();
            String str = m1.getDuration();
            Float duration = 0.0f;
            if (str != null && !str.isEmpty()) {
                duration = Float.parseFloat(str.trim());
                msi.setDuration(duration);
            }
            
            String uri = m1.getUri();
            msi.setUri(uri);
            int li = uri.lastIndexOf("/");
           
            if (li == -1) {
                mapTSURI.put(uri, filename);
                mapTSDuration.put(uri, duration);
            }
            else {
                mapTSURI.put(uri.substring(li+1), filename);
                mapTSDuration.put(uri.substring(li+1), duration);
            }
            hls.addMediaStreamInfo(msi);

            
        }
                
        lst = mpl1.get("#EXT-X-TARGETDURATION");
        if (lst == null || lst.isEmpty()) {
            hls.setTargetDuration(0);
        }
        else {
            MediaPlayList mpl22 = lst.get(0);
            String str = mpl22.getNouriPlayList().get("#EXT-X-TARGETDURATION");
            if (str != null && !str.isEmpty()) {
                hls.setTargetDuration(Integer.parseInt(str));
            }
            
        }
        
        lst = mpl1.get("#EXT-X-MEDIA-SEQUENCE");
        if (lst == null || lst.isEmpty()) {
            hls.setMediaSequence(0);
        }
        else {
            MediaPlayList mpl22 = lst.get(0);
            String str = mpl22.getNouriPlayList().get("#EXT-X-MEDIA-SEQUENCE");
            if (str != null && !str.isEmpty()) {
                hls.setMediaSequence(Integer.parseInt(str));
            }
            
        }
        
        return hls;
        
    }
    
    
    private DeviceSession.HLS getMasterListContent(final String filename, PlayListAbstractClass playlist) {
        
        MasterPlayList mpl = (MasterPlayList) playlist;
       
        Map<String, List<MasterPlayList>> mpl1 = mpl.getMasterMap();
        if (mpl1 == null || mpl1.isEmpty()) {
            return null;
        }
        
        
        List<MasterPlayList> lst = mpl1.get("#EXT-X-STREAM-INF");//mpl1.get("#EXT-X-VERSION");
        
        if (lst == null || lst.isEmpty()) {
            return null;
        }
        
        
        DeviceSession.HLS hls = new DeviceSession.HLS();
        
        hls.setMasterPlaylist(true);
        hls.setFilename(filename);
        
        for (MasterPlayList m1 : lst) {
            
            MasterStreamInfo msi = hls.createMasterStreamInfoObject();
            String str = m1.getBandwidth();
            if (str != null && !str.isEmpty()) {
                Long bw = Long.parseLong(str.trim());
                mapURIBandwidth.put(m1.getUri(),bw );
                msi.setBandwidth(bw);
            }
            str = m1.getProgramID();
            if (str != null && !str.isEmpty()) {
                msi.setProgramId(Integer.parseInt(str.trim()));
            }
            msi.setUri(m1.getUri());
            hls.addMasterStreamInfo(msi);
        }
                
        lst = mpl1.get("#EXT-X-VERSION");
        if (lst == null || lst.isEmpty()) {
            hls.setVersion("");
        }
        
        return hls;
        
    }
    private void populateTSDetails(final DeviceSession ds, QoEPacket qp) {
        Params params = qp.getParams();
        if (params == null) {
            return;
        }
        List<TSParams> ls = params.getTsParams();
        
        if (ls !=null && !ls.isEmpty()) {
            for (TSParams tps : ls) {
                DeviceSession.TS t = new DeviceSession.TS();
                populateTSParams(t, tps);
                final String filename = tps.getFilename();
                t.setFilename(filename);
                
                /**
                 * Add Stream Graph.
                 */
                DeviceSession.StreamGraph sg = getStreamGraph(filename);
                sg.setPacketRequestTime(qp.getFirstPacketReceivedTime());
                sg.setLastPacketReceivedTime(qp.getLastPacketReceivedTime());
                
                ds.addStreamGraph(sg);
                
                t.setPacketReceivedTime(qp.getFirstPacketReceivedTime());
                
                List<Stream> lst = tps.getStreams();
                if (lst != null && !lst.isEmpty()) {
                    for (Stream s : lst) {
                        DeviceSession.TS.TSStreams tss = new DeviceSession.TS.TSStreams();
                        
                        Map<String, String> mp = s.getStreams();                        
                        populateTSStreamsParams(tss, mp);
                        tss.setStreamId(s.getStreamIndex());
                        t.addTSStream(tss);
                    }
                }
                ds.addTS(t);
                
            }
        }
        
     
        
        // TODO: HTTP Params
    }
    
    private DeviceSession.StreamGraph  getStreamGraph(final String filename) {
        System.out.println("filename: " + filename);
        if (filename == null) {
            return null;
        }
        final String uri = mapTSURI.get(filename);
        if (uri == null || uri.isEmpty()) {
            System.out.println("URI is null for file : " + filename);
            return null;
        }
        
        String key = getURIKeyFromM3U8Filename(uri);
        if (key == null) {
            System.out.println("Key is null for filename: " + filename);
            return null;
        }
        
        Long bw = mapURIBandwidth.get(key);
        if (bw == null ) {
            System.out.println("bandwidth is null for key: " + key);
            return null;
        }
        Float duration = mapTSDuration.get(filename);
        
        DeviceSession.StreamGraph sg = new DeviceSession.StreamGraph();
        sg.setBandwidth(bw);
        sg.setFilename(filename);
        if (duration != null) {
            sg.setDuration(duration);
        }
        
        return sg;
    }
    
    private String getURIKeyFromM3U8Filename(final String key) {
        Set<String> s = mapURIBandwidth.keySet();
        for (String s1 : s) {
            if (s1.endsWith(key)) {
                return s1;
            }
        }
        return null;
    }
    private void populateTSStreamsParams(DeviceSession.TS.TSStreams tss, Map<String, String> mp) {
        if (null == mp) {
            return;
        }
        
        tss.setAudioChannels(mp.get(QoeConstants.AUDIO_CHANNELS));
        tss.setAudioSampleRate(mp.get(QoeConstants.AUDIO_SAMPLE_RATE));
        tss.setCodecId(mp.get(QoeConstants.CODEC_ID));
        tss.setCodecType(mp.get(QoeConstants.CODEC_TYPE));
        tss.setDirection(mp.get(QoeConstants.DIRECTION));
        tss.setDroppedFrames(mp.get(QoeConstants.NUMBER_OF_DROPPEDFRAMES));
        tss.setDuration(mp.get(QoeConstants.DURATION_IN_MS));
        tss.setLanguage(mp.get(QoeConstants.STREAM_LANGUAGE));
        tss.setNumberOFrames(mp.get(QoeConstants.NUMBER_OF_FRAMES));
        tss.setStartTime(mp.get(QoeConstants.START_TIME_IN_MS));
        
        tss.setVideoFormat(mp.get(QoeConstants.VIDEO_FORMAT));
        tss.setVideoframes(mp.get(QoeConstants.VIDEO_FRAME_RATE));
        tss.setVideoHeight(mp.get(QoeConstants.VIDEO_HEIGHT));
        tss.setVideoWidth(mp.get(QoeConstants.VIDEO_WIDTH));
        
    }
    private void populateTSParams(DeviceSession.TS ts, TSParams tsp) {
        if (tsp != null) {
            
            
            ts.setBitrate(QoeStringUtils.getLongFromString(tsp.getDataValue(QoeConstants.BIT_RATE)));
            ts.setContainerFormat(tsp.getDataValue(QoeConstants.CONTAINER_FORMAT_SHORT_NAME));
            ts.setDuration(QoeStringUtils.getLongFromString(tsp.getDataValue(QoeConstants.DURATION_IN_MS)));
            ts.setFileSize(QoeStringUtils.getLongFromString(tsp.getDataValue(QoeConstants.FILE_SIZE_IN_BYTES)));
            ts.setMalformedPacketCount(QoeStringUtils.getIntfromString(tsp.getDataValue(QoeConstants.MALFORMED_PACKET)));
            ts.setNumberOfStreams(QoeStringUtils.getIntfromString(tsp.getDataValue(QoeConstants.NUMBER_OF_FRAMES)));
            ts.setPatErrors(QoeStringUtils.getIntfromString(tsp.getDataValue(QoeConstants.PAT_ERROR_COUNT)));
            ts.setPmtErrors(QoeStringUtils.getIntfromString(tsp.getDataValue(QoeConstants.PMT_ERROR_COUNT)));
            ts.setScrambledPayloadCount(QoeStringUtils.getIntfromString(tsp.getDataValue(QoeConstants.SCRAMBLED_PAYLOAD)));
            ts.setStartTime(QoeStringUtils.getLongFromString(tsp.getDataValue(QoeConstants.START_TIME_IN_MS)));
            ts.setSyncErrorCount(QoeStringUtils.getIntfromString(tsp.getDataValue(QoeConstants.SYNC_ERROR_COUNT)));
            ts.setNullPacketCount(QoeStringUtils.getIntfromString(tsp.getDataValue(QoeConstants.NULL_PACKET)));
            ts.setUnsupportedPMT(QoeStringUtils.getIntfromString(tsp.getDataValue(QoeConstants.NULL_PACKET)));
        }
    }
    private void populateTCPDetails(DeviceSession ds, QoEPacket qp) {
        DeviceSession.TCP tcp = new DeviceSession.TCP();
        tcp.setBandwidth(qp.getClientBandWidth());
        tcp.setBytesInPayload(qp.getTotalNumberOfBytes());
        tcp.setConnectionResets(qp.getNumberOfConnectionResets());
        tcp.setDestIP(qp.getDestIp());
        tcp.setDestPort(qp.getDestPort());
        tcp.setDroppedPackets(qp.getPacketsDropped());
        tcp.setDupAcks(qp.getDupAcks());
        
        tcp.setFirstPacketReceivedTime(qp.getFirstPacketReceivedTime());
        tcp.setLastPacketReceivedTime(qp.getLastPacketReceivedTime());
        tcp.setOutOfOrder(qp.getOutOfOrder());
        tcp.setRetransmission(qp.getRetransmissionCount());
        tcp.setSourceIP(qp.getSourceIp());
        tcp.setSourcePort(qp.getSourcePort());
        tcp.setStreamId(qp.getStreamIndex());
        ds.addTCP(tcp);
    }
    private void populateDeviceDetails(DeviceSession se, Device d, long streamIndex) {
        if (null == d) {
            return ;
        }
        DeviceSession.DeviceNetworkInformation dni = new DeviceSession.DeviceNetworkInformation();
        dni.setLatitute(d.getLatitude());
        dni.setLongitude(d.getLongitude());
        dni.setNetworkType(d.getType());        
        dni.setStreamIndex(streamIndex);
        se.addDeviceNetworkInformation(dni);
        
        
    }
}
