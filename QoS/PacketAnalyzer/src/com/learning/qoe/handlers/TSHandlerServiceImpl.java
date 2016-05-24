/**
 * Handler for the .ts Packets. 
 */
package com.learning.qoe.handlers;

import java.util.ArrayList;
import java.util.List;

import com.learning.qoe.model.TSParams;
import com.learning.qoe.ts.decoder.TSDecoder;
import com.learning.qoe.ts.decoder.TSDecoderIf;

/**
 * @author Sushant
 *
 */
public class TSHandlerServiceImpl implements ContentTypeHandlerSvc<TSParams> {

    /* (non-Javadoc)
     * @see com.learn.ContentTypeHandlerSvc#processFile(byte[], byte[], byte[], byte[])
     */
    @Override
    public List<TSParams> processFile(byte[] requestHeaders, byte[] requestPayload,
            byte[] responseHeaders, byte[] responsePayload, final String path, final String filename) {
        
        
        List<TSParams> abps = new ArrayList<TSParams>();
        
//        while (i < responsePayload.length) {
//            
//            byte [] buffer = new byte[188];
//            System.arraycopy(responsePayload, i, buffer, 0, 188);
//            
//            
//            ByteBuffer bi = ByteBuffer.wrap(buffer);
//            bi.flip();
//            
//            TSDecoderIf tsd = new TSDecoder();
//            
//            Map<String, Serializable> m = tsd.parsePacket(bi);
//        
//            updateData(tsParams, m);
//            i = i + 188;
//            
//        }
        String filePath = path + filename;
        TSDecoderIf tsd = new TSDecoder();
//        Map<String, Serializable> p = tsd.getTSInformation(responsePayload, filePath);
//        if (p != null) {
//            abps.add(convert(tsParams, p));
//        }
//        return abps;
        
        TSParams tsp = tsd.getTSInformation(responsePayload, filePath);
        tsp.setFilename(filename);
        abps.add(tsp);
        return abps;
    }

//    private void updateData(TSParams tsParams, Map<String, Serializable> mp) {
//        
//        if (mp != null) {
//            for (Map.Entry<String, Serializable> m : mp.entrySet()) {
//                Serializable ss = tsParams.getDataValue(m.getKey());
//                if (ss != null) {
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
//                }
//            }
//        }
//    }
//    private TSParams convert(TSParams tsParams, Map<String, Serializable> mp) {
//        
//        for (Map.Entry<String, Serializable> e : mp.entrySet()) {
//            tsParams.addData(e.getKey(), e.getValue());
//        }
//        return tsParams;
//    }
}
