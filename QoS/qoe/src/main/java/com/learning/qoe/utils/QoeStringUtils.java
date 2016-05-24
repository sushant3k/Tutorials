/**
 * 
 */
package com.learning.qoe.utils;

/**
 * @author Sushant
 *
 */
public class QoeStringUtils {

    private QoeStringUtils() {
        
    }
    
    /**
     * The method returns null if String is not an integer 
     * @param s
     * @return
     */
    public static Integer getIntfromString(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(s);
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public static Long getLongFromString(String s) {
        if (s == null || s.isEmpty()) {
            return 0l;
        }
        try {
            return Long.parseLong(s);
        }
        catch(Exception e) {
            return null;
        }
    }
}
