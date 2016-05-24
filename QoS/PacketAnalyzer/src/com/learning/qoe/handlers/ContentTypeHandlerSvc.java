/**
 * 
 */
package com.learning.qoe.handlers;

import java.util.List;

import com.learning.qoe.model.Params;

/**
 * @author Sushant
 *
 */
public interface ContentTypeHandlerSvc<T> {

    /**
     * Processess the .ts files or .m3u8 files.
     *  
     * @param requestHeaders
     * @param requestPayload
     * @param responseHeaders
     * @param responsePayload
     * @param filePath
     * @param filename
     * @return
     */
    List<T> processFile(byte[] requestHeaders, byte[] requestPayload, byte[] responseHeaders, byte[] responsePayload, final String filePath, final String filename);
}
