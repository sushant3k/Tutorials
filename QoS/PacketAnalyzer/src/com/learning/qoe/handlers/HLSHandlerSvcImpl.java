/**
 * HLS (.m3u8 ) handlers.
 */
package com.learning.qoe.handlers;

import java.util.ArrayList;
import java.util.List;

import com.learning.qoe.model.HTTPParams;

/**
 * @author Sushant
 *
 */
public class HLSHandlerSvcImpl implements ContentTypeHandlerSvc<HTTPParams>{

    @Override
    public List<HTTPParams> processFile(byte[] requestHeaders, byte[] requestPayload,
            byte[] responseHeaders, byte[] responsePayload, final String filePath, final String filename) {
        
        List<HTTPParams> abstractParams = new ArrayList<HTTPParams>(2);
        HTTPParams abps = new HTTPParams();
        
        if (requestHeaders !=null && requestHeaders.length > 0) {
            abps.setHeader(new String(requestHeaders));
        }
        
        if (requestPayload != null && requestPayload.length > 0) {
            abps.setPayload(new String(requestPayload));
        }
        
        abps.setRequest(true);
        abps.setFilename(filename);
        abstractParams.add(abps);
        
        abps = new HTTPParams();
        if (responseHeaders != null && responseHeaders.length > 0) {
            abps.setHeader(new String(responseHeaders));
        }
        
        if (responsePayload != null && responsePayload.length > 0) {
            abps.setPayload(new String(responsePayload));
        }
        
        abps.setRequest(false);
        
        abps.setFilename(filename);
        abstractParams.add(abps);
        return abstractParams;
    }

}
