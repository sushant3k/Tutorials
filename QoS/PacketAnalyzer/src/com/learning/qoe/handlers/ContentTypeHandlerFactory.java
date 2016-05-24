/**
 * Factory for returning different types of Content Type handlers.
 * Handles HLS and TS files only. 
 * 
 */
package com.learning.qoe.handlers;

import com.learning.qoe.utils.EnumUtils.FileTypeEnum;

/**
 * @author Sushant
 *
 */
public class ContentTypeHandlerFactory {

    private ContentTypeHandlerFactory() {
        
    }
    
    public static ContentTypeHandlerSvc getContentTypeHandlerFactory(FileTypeEnum fileType) {
        
        if (fileType == FileTypeEnum.TS) {
            return new TSHandlerServiceImpl();
        }
        else {
            return new HLSHandlerSvcImpl();
        }
        
      
    }
}
