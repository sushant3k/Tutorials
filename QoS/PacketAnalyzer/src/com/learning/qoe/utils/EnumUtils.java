/**
 * 
 */
package com.learning.qoe.utils;

/**
 * @author Sushant
 *
 */
public class EnumUtils {

    public enum FileTypeEnum {
        M3U8("m3u8"),
        TS("ts"),
        TXT("txt");
        
        String fileType;
        FileTypeEnum(String fileType) {
            this.fileType = fileType;
        }
        public String getFileType() {
            return fileType;
        }
        public void setFileType(String fileType) {
            this.fileType = fileType;
        }
        
    }
}
