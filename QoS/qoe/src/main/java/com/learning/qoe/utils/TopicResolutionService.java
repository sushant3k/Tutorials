/**
 * 
 */
package com.learning.qoe.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Sushant
 *
 */
public class TopicResolutionService {

    private ConcurrentMap<String, String> topicMap = new ConcurrentHashMap<String, String>();
    private TopicResolutionService() {
        
    }
    
    private static class TopicResolutionServiceInstance {
        private static final TopicResolutionService trs = new TopicResolutionService();
    }
    
    public static TopicResolutionService getInstance() {
        return TopicResolutionServiceInstance.trs;
    }
    
    public  String getTopic(final String key) {
        return topicMap.get(key);
       
    }
    public synchronized void addTopic(final String key, final String topic) {
        System.out.printf("\nKey and Value = %s %s", key, topic);
        if (topicMap.containsKey(key)) {
            System.out.println("contains key");
            return;
        }
        System.out.println("does not contain key");
        topicMap.put(key, topic);
    }
}
