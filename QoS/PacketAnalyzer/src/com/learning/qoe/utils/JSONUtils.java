/**
 * Simple JSOn Utils
 */
package com.learning.qoe.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Sushant
 *
 */
public final class JSONUtils {

    /**
     * Private Constructor ensuring no instance of this class
     */
    private JSONUtils() {
        
    }
    
    public static String objectToJson(Object ob) {
        ObjectMapper mapper = new ObjectMapper();
        String s = null;
        try {
            s = mapper.writeValueAsString(ob);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return s;
        }
        return s;
    }
}
