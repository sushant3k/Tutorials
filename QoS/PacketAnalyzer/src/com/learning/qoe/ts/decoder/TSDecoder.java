/**
 * A class that decodes that ts packets
 */
package com.learning.qoe.ts.decoder;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.learning.qoe.model.TSParams;
import com.xuggle.xuggler.Configuration;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

/**
 * @author Sushant
 *
 */
public class TSDecoder implements TSDecoderIf{

    private int pmtPid;

    
    private TSParams tsParams = new TSParams();
    
    public TSDecoder() {           
    }
    
    private void getPacketInformation(byte[] payload) {
        byte[] buff = new byte[188]; // 188 chunks array
        int i = 0;
        while ( i < payload.length && (payload.length - i ) >= 188) {

            System.arraycopy(payload, i, buff, 0, 188);
            i= i + 188;
            ByteBuffer bf = ByteBuffer.wrap(buff);
            updateData(parsePacket(bf));
            
        }
        
    }
    public TSParams getTSInformation(byte[] payload, final String filePath){
      

        getPacketInformation(payload);
        getContainerInformation(filePath);
        return tsParams;
        
    }

    private void getContainerInformation(final String filePath) {
        // If the user passes -Dxuggle.options, then we print
        // out all possible options as well.
        String optionString = System.getProperty("xuggle.options");
        if (optionString != null)
        {
          Configuration.printHelp(System.out);
        }
       
        try {
            Thread.sleep(2000l);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Create a Xuggler container object
        IContainer container = IContainer.make();
        
        // Open up the container
        if (container.open(filePath, IContainer.Type.READ, null) < 0)
          throw new IllegalArgumentException("could not open file: " + filePath);
        

        // Get Format Details.
        Map<String, String> mp = new HashMap<String, String>();

//        System.out.println("SDP data " + container.createSDPData());
        IContainerFormat ic = container.getContainerFormat();
        
        mp.put(CONTAINER_FORMAT_LONG_NAME, ic.getInputFormatLongName());
        mp.put(CONTAINER_FORMAT_SHORT_NAME, ic.getInputFormatShortName());
        
        // Format details are not exhaustive here. 
        
        
        
        // query how many streams the call to open found
        int numStreams = container.getNumStreams();
        mp.put(NUMBER_OF_STREAMS, numStreams+"");
        mp.put(DURATION_IN_MS, container.getDuration() == Global.NO_PTS ? "unknown" : "" + container.getDuration()/1000);
        mp.put(START_TIME_IN_MS, container.getStartTime() == Global.NO_PTS ? "unknown" : "" + container.getStartTime()/1000);
        mp.put(FILE_SIZE_IN_BYTES, container.getFileSize() + "");
        mp.put(BIT_RATE, container.getBitRate()+"");
        
        
//        parsePacket(ip.getByteBuffer());
        // and iterate through the streams to print their meta data
        for(int i = 0; i < numStreams; i++)
        {
            TSParams.Stream s = new TSParams.Stream(i);
            
           // Find the stream object
          IStream stream = container.getStream(i);
          // Get the pre-configured decoder that can decode this stream;
          IStreamCoder coder = stream.getStreamCoder();
//          String key = "STREAM_"+ i;
//          String delimiter = "|";
          s.addStreamParameter(DURATION_IN_MS, stream.getDuration() == Global.NO_PTS ? "unknown" : "" + stream.getDuration());
          
          s.addStreamParameter(STREAM_LANGUAGE, stream.getLanguage() == null ? "unknown" : stream.getLanguage());
          s.addStreamParameter(TIMEBASE_NR, stream.getTimeBase().getNumerator()+"");
          s.addStreamParameter(TIMEBASE_DR, stream.getTimeBase().getDenominator()+"");
          s.addStreamParameter(DIRECTION, stream.getDirection()+"");
          s.addStreamParameter(FRAMERATE_NR, stream.getFrameRate().getNumerator()+"");
          s.addStreamParameter(FRAMERATE_DR, stream.getFrameRate().getDenominator()+"");
          s.addStreamParameter(NUMBER_OF_FRAMES, stream.getNumFrames()+"");
          s.addStreamParameter(NUMBER_OF_DROPPEDFRAMES, coder.getNumDroppedFrames()+"");
          s.addStreamParameter(CODEC_TYPE, coder.getCodecType()+"");
          s.addStreamParameter(CODEC_ID, coder.getCodecID()+"");
          s.addStreamParameter(START_TIME_IN_MS,container.getStartTime() == Global.NO_PTS ? "unknown" : "" + stream.getStartTime() );
          
          s.addStreamParameter(TIMEBASE_NR, coder.getTimeBase().getNumerator()+"");
          s.addStreamParameter(TIMEBASE_DR, coder.getTimeBase().getDenominator()+"");
          
          
          if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO)
          {
              s.addStreamParameter(AUDIO_SAMPLE_RATE, coder.getSampleRate()+"");
              s.addStreamParameter(AUDIO_CHANNELS, coder.getChannels()+"");
              s.addStreamParameter(AUDIO_FORMAT, coder.getSampleFormat()+"");
            
          } else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO)
          {
              s.addStreamParameter(VIDEO_WIDTH, coder.getWidth()+"");
              s.addStreamParameter(VIDEO_HEIGHT, coder.getHeight()+"");
              s.addStreamParameter(VIDEO_FORMAT, coder.getPixelType()+"");
              s.addStreamParameter(VIDEO_FRAME_RATE, coder.getFrameRate().getDouble()+"");
              
          }
          System.out.printf("\n");
          tsParams.addStream(s);
        }
        
        convert(mp);
    }
    
    private TSParams convert(Map<String, String> mp) {
        
        for (Map.Entry<String, String> e : mp.entrySet()) {
            tsParams.addData(e.getKey(), e.getValue());
        }
        return tsParams;
    }
    @SuppressWarnings("unused")
    public Map<String, String> parsePacket(ByteBuffer in) {
        
        Map<String, String> mp = new HashMap<String, String>();
                
        //System.out.printf("parsePacket: {}", in);
        //used to return the packet type
        Message.Type type = Message.Type.DATA;
        in.mark();
        // Sync byte #0 is always 0x47
        byte sync = in.get();
        //System.out.printf("Packet sync: " + (sync == 0x47));
        if (sync == 0x47) {
            
//            int flagsAndPID = readUnsignedShort(in);                        
//            int tei = ((flagsAndPID & 0x80) == 0 ? 0 : 1);            
//            int pusi = ( ((flagsAndPID << 1) & 0x80) == 0  ? 0 : 1);            
//            int tp = (flagsAndPID << 2) & 0x80;
            
            byte flags = in.get();
            // Transport error indicator (TEI)
            int tei = flags & 0x80;
            // Payload unit start indicator - 1 = start of PES data or PSI, 0 otherwise
            int pusi = ( ((flags << 1) & 0x80) == 0  ? 0 : 1);
            
            // Transport priority - 1 = higher priority than packets with same PID
            int tp = (flags << 2) & 0x80;
            
            // Packet id (PID) - 13 bits
//            int pid = (flags & 0x1f) | (flags2 & 0xFF);

//            
//            int flags2 = readUnsignedByte(in);
//            
//            // Packet id (PID) - 13 bits
////            int pid = (flags & 0x1f) | (flags2 & 0xFF);
//            int pid = ((flags << 8) | (flags2 & 0xff)) & 0x1fff;
////            int pid = flagsAndPID & 0x1fff;
//          
//            int flags2 = readUnsignedShort(in);
//            int pid = flags2 & 0x1FFF;
            
            byte flags2 = in.get();
            int pid = ((flags << 8) | (flags2 & 0xff)) & 0x1fff;
            
            System.out.printf("Packet id: %s", Integer.toHexString(pid));
            
            
            // additional flags
            byte flags3 = in.get();
            if (flags3 > 0) {
//                System.out.println("Here you go");
            }
            int scramblingControl = (flags3 >> 6) & 0x03;
            if (scramblingControl > 0) {
                if (scramblingControl == 1) {
                    mp.put(MALFORMED_PACKET, 1+"");
                }
                else {
                    mp.put(SCRAMBLED_PAYLOAD, 1+"");
                }
            }
//            System.out.println("Scrambing Control="+scramblingControl);
            
            boolean hasAdaptationField = ((flags3  & 0x20) > 0 ? true: false); //(flags3 & 0x20) == 1;
            boolean hasPayloadData = ((flags3 & 0x10) > 0 ? true : false);
            // Continuity counter - 4 bits      
            int continuityCount = flags3 & 0x0f;
            
            switch (pid) {
                case 0x0100: //256
                    //fairly sure this is an h.264 video packet                    
                    //assume they are all key frames for now
                    type = Message.Type.VIDEO;
                    break;
                case 0x0101: //257
                    
                    type = Message.Type.AUDIO;
                    break;
                case 0x0000:
                    
                    type = Message.Type.CONFIG_PAT;
                    processPAT(in);
                    break;
                case 0x1000: // 4096:1000 is default for ffmpeg source data
                    
                    type = Message.Type.CONFIG_PMT;
                    processPMT(in);
                    break;
                case 0x0001:
                    
                    type = Message.Type.CONFIG;
                    break;
                case 0x0002:                    
                    type = Message.Type.CONFIG;
                    break;
                case 0x1fff: //8191
                    
                    type = Message.Type.NULL;
                    mp.put(NULL_PACKET, 1+"");
                    break;
                case 0x0010: //16
                    
                    break;
                case 0x0011:
                    // service information
                    
                    break;
                default:
                    // if the PMT's PID is not default
                    if (pid == pmtPid) {
                        // found PMT
                        type = Message.Type.CONFIG_PMT;
                        processPMT(in);
                    } else {
                        if (pid >= 0x0003 && pid <= 0x000f) {
//                            System.out.println("PID in reserved range");
                        } else if (pid >= 0x0010 && pid <= 0x1ffe) {
//                            System.out.println(" PID from other range");
                        } else {
//                            System.out.println("Got unknown PID");
                        }
                    }
                    type = Message.Type.OTHER;
            }
    
            
        }
        else {
            mp.put(SYNC_ERROR_COUNT, 1+"");
        }
        return mp;
    }

    private static int readUnsignedByte(ByteBuffer in) {
        byte b = in.get();
        return b >=0 ? b : 0xFF & b ;
//        return b >= 0 ? b : 0x10000 + b;
    }
    
    private Map<String, Serializable> processPAT(ByteBuffer in) {
        Map<String, Serializable> mp = new HashMap<String, Serializable>();
        System.out.printf("processPAT: {}", in);
        
        byte pointer = in.get(); // Get the pointer table.
        byte tableId = in.get();
        
        int sectionLength = readUnsignedShort(in) & 0x0fff; // ignoring misc and reserved bits
//        int sectionLength = in.get() & 0x0fff; // ignoring misc and reserved bits
        System.out.printf("PAT pointer: {} table id: {} section length: {}", pointer, tableId, sectionLength);
        int remaining = sectionLength;
        // skip tsid + version/cni + sec# + last sec#
        in.position(in.position() + 5);
        remaining -= 5;
        while (remaining > 4) {
//            System.out.printf("Program number: {}", readUnsignedShort(in)); // program number
            System.out.printf("Program number: {%d}", in.getShort()); // program number
            
         // pmtPid can be either Network PID or Program Map PID. REfer to the spec.
            // FIXME
            pmtPid = readUnsignedShort(in) & 0x1fff; // 13 bits
            System.out.printf("PMT pid: {}", pmtPid);
            
            remaining -= 4;
            //return; // immediately after reading the first pmt ID, if we don't we get the LAST one
        }
        // ignore the CRC (4 bytes)
        
        long crc = in.getInt();
        System.out.printf("\nCRC %d", crc);
        
        mp.put(PAT_ERROR_COUNT, 1);
        return mp;
    }

    private Map<String, Serializable> processPMT(ByteBuffer in) {
        
        Map<String, Serializable> mp = new HashMap<String, Serializable>();
        
        byte pointer = in.get();
        byte tableId = in.get();
        
        // Refer here for details: http://www.etherguidesystems.com/help/sdos/mpeg/semantics/mpeg-2/table_id.aspx

        if (tableId != 0x02) {
            System.out.println("PAT pointed to PMT that isn't PMT");
            mp.put(PMT_ERROR_COUNT, 1);
        }
        
        //int sectionLength = readUnsignedShort(in) & 0x03ff; // ignoring section syntax and reserved
        
        int sectionLength = readUnsignedShort(in) & 0x03ff; // ignoring section syntax and reserved
        
//        System.out.printf("PMT pointer: {%d} table id: {%d} section length: {%d}", pointer, tableId, sectionLength);
        
        int remaining = sectionLength;
        // skip program num, reserved, version, cni, section num, last section num, reserved, PCR PID
        in.position(in.position() + 7);
        remaining -= 7;
        // program info length
//        int piLength = readUnsignedShort(in) & 0x0fff;
        int piLength = readUnsignedShort(in) & 0x0fff;
        
        remaining -= 2;
        // prog info
        in.position(in.position() + piLength);
        remaining -= piLength;
        
        while (remaining > 4) {
            byte type = in.get();
//            int pid = readUnsignedShort(in) & 0x1fff;
            int pid = in.getShort() & 0x1fff;
//            int esiLen = readUnsignedShort(in) & 0x0fff;
            int esiLen = in.getShort() & 0x0fff;
            remaining -= 5;
            
            in.position(in.position() + esiLen);
            remaining -= esiLen;
            System.out.printf("data type 0x{} in PMT", Integer.toHexString(type));
            switch (type) {
                case 0x1b: // H.264 video
                    System.out.printf("Video pid: {}", Integer.toHexString(pid));
                    
                    break;
                case 0x0f: // AAC Audio / ADTS
                    System.out.printf("Audio pid: {}", Integer.toHexString(pid));
                    break;
                // need to add MP3 Audio  (3 & 4)
                default:
                    System.out.printf("Unsupported type 0x{} in PMT", Integer.toHexString(type));
                    mp.put(PMT_UNSUPPORTED_TYPE, Integer.toHexString(type));
                    break;
            }
        }
        mp.put(PMT_ERROR_COUNT, 0);
        return mp;
        // and ignore CRC
    }

    private static int readUnsignedShort(ByteBuffer in) {
        short val = in.getShort();
        int unsignedShort = val >= 0 ? val : 0x10000 + val;
        return unsignedShort;
    }

    @SuppressWarnings("unused")
    private static boolean isBitSet(byte b, int bit) {
        return (b & (1 << bit)) != 0;
    }

    public boolean isStreamed(String url, int flags) {
        boolean retval = true;
        System.out.printf("isStreamed({}, {}); {}", new Object[] { url, flags, retval });
        return retval;
    }
    
    
    private void updateData(Map<String, String> mp) {
        
        if (mp != null) {
            for (Map.Entry<String, String> m : mp.entrySet()) {
                String ss = tsParams.getDataValue(m.getKey());
                if (ss != null) {
                    try {
                        Long l = Long.parseLong(ss.trim());
                        tsParams.addData(m.getKey(), ++l+"");
                    }
                    catch(Exception e) {
                        // Eat up.
                    }
//                    if (ss instanceof Integer) {
//                        Integer i = (Integer)ss;
//                        tsParams.addData(m.getKey(), ++i );
//                    }
//                    if (ss instanceof Short) {
//                        Short i = (Short)ss;
//                        tsParams.addData(m.getKey(), ++i );
//                    }
//                    
//                    if (ss instanceof Long) {
//                        Long i = (Long)ss;
//                        tsParams.addData(m.getKey(), ++i );
//                    }
//                    
//                    if (ss instanceof Float) {
//                        Float i = (Float)ss;
//                        tsParams.addData(m.getKey(), ++i );
//                    }
//                    
//                    if (ss instanceof Double) {
//                        Double i = (Double)ss;
//                        tsParams.addData(m.getKey(), ++i );
//                    }
                }
                else {
                    tsParams.addData(m.getKey(), m.getValue());
                }
            }
        }
    }

}
