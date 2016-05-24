/**
 *  Object of this class is used for maintainig he steam inde number.
 */
package com.learning.qoe.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Sushant
 *
 */
public class StreamIndexManager {

    private ConcurrentMap<String, Long> streamIndexRepo = new ConcurrentHashMap<String, Long>();
    private volatile long streamIndex;
    private StreamIndexManager() {
        
    }
    
    private static class StreamIndexManagerInstance {
        private static StreamIndexManager sm = new StreamIndexManager();
    }
    
    public static StreamIndexManager getInstance() {
        return StreamIndexManagerInstance.sm;
    }
    
    public synchronized long addStreamIndex(final String srcIp, final String destIp, final int srcPort, final int destPort) {
        long a = streamIndex++;
        streamIndexRepo.put(srcIp+"_"+srcPort+":" + destIp+"_"+destPort, a);
        streamIndexRepo.put(destIp+"_"+destPort+":" + srcIp+"_"+srcPort, a);
        return a;
    }
    public synchronized long addAndGetStreamIndex(final String srcIp, final String destIp, final int srcPort, final int destPort) {
        
        Long a = streamIndexRepo.get(srcIp+"_"+srcPort+":" + destIp+"_"+destPort);
        if (a == null) {
            a = streamIndexRepo.get(destIp+"_"+destPort+":" + srcIp+"_"+srcPort);
        }
        if (a == null) {
            return addStreamIndex(srcIp, destIp, srcPort, destPort);
        }
        
        return a;
    }
    public synchronized void clear(final String srcIp, final String destIp, final int srcPort, final int destPort) {
        streamIndexRepo.remove(srcIp+"_"+srcPort+":" + destIp+"_"+destPort);
        streamIndexRepo.remove(destIp+"_"+destPort+":" + srcIp+"_"+srcPort);
    }
}
