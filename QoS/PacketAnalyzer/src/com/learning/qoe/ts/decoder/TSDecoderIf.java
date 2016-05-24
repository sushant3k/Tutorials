
package com.learning.qoe.ts.decoder;

import com.learning.qoe.model.TSParams;

/**
 * @author Sushant
 *
 */
public interface TSDecoderIf {

    final String SDP_DATA = "SdpData";
    final String CONTAINER_FORMAT_LONG_NAME ="containerFormatLongName";
    final String CONTAINER_FORMAT_SHORT_NAME ="containerFormatShortName";
    final String NUMBER_OF_STREAMS ="numberOfStreams";
    final String DURATION_IN_MS = "durationInMilliSecs";
    final String START_TIME_IN_MS = "startTimeInMilliSecs";
    final String FILE_SIZE_IN_BYTES = "fileSize";
    final String BIT_RATE = "bitRate";        
    final String STREAM_LANGUAGE="language";
    
    final String TIMEBASE_NR = "timebaseNr";
    final String TIMEBASE_DR = "timebaseDr";
    final String DIRECTION ="direction";
    final String FRAMERATE_NR = "framerateNr";
    final String FRAMERATE_DR = "framerateDr";
    final String NUMBER_OF_FRAMES = "numberOfFrames";
    final String NUMBER_OF_DROPPEDFRAMES = "numberOfDroppedFrames";
    final String CODEC_TYPE = "codecType";
    final String CODEC_ID = "codecID";
    final String CODER_TIMEBASE_NR = "coderTimebaseNr";
    final String CODER_TIMEBASE_DR = "coderTimebaseDr";
    final String AUDIO_SAMPLE_RATE = "audioSampleRate";
    final String AUDIO_CHANNELS = "audioChannels";
    final String AUDIO_FORMAT = "audioFormat";
    
    final String VIDEO_WIDTH = "videoWidth";
    final String VIDEO_HEIGHT = "videoHeight";
    final String VIDEO_FORMAT = "videoFormat";
    final String VIDEO_FRAME_RATE = "videoFrameRate";
    
    final String SYNC_ERROR_COUNT="syncErrorCount";
    final String PAT_ERROR_COUNT="patErrorCount";
    final String PMT_ERROR_COUNT = "pmtErrorCount";
    final String PMT_UNSUPPORTED_TYPE ="pmtUnsupportedType";
    final String MALFORMED_PACKET ="malFormedPacket";
    final String SCRAMBLED_PAYLOAD ="scrambledPayload";
    final String NULL_PACKET = "nullPacket";
//    final String TEI ="tei";
//    final String PSI = "psi";
//    final String tp = "tp";
//    final String pid = "pid";
    
   TSParams getTSInformation(byte[] payload, final String filepath);
    
    
    
}
