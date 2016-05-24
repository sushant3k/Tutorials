/**
 * 
 */
package com.learning.qoe.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import com.google.common.io.Files;

/**
 * @author Sushant
 *
 */
public class FileManagementUtils {

    private FileManagementUtils() {
        
    }
    
    public static void writeToFile(byte[] payload, String filePath) throws IOException{
        if (payload == null || payload.length == 0 ) {
            System.out.println("**************** Payload length is zero****************");
            return;
        }
        File f = new File(filePath);
        Files.write(payload, f); 
        
        
    }
    
    public static void deleteFile(final String filePath) throws IOException{
        
        java.nio.file.Files.delete(Paths.get(filePath));
        
    }
}
