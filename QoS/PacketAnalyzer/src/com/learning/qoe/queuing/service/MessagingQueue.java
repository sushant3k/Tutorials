/**
 *  Messaging Queue wrapper over a blocking Queue.
 *  This is used for enqueuing message to a queue;
 */
package com.learning.qoe.queuing.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Sushant
 *
 */
public class MessagingQueue<E> {

    private BlockingQueue<E> queue = new ArrayBlockingQueue<E>(1000);
    private MessagingQueue() {
        
    }
    
    private static class MessagingQueueInstance {
        private static final MessagingQueue q = new MessagingQueue();
    }
    public static MessagingQueue getInstance() {
        return MessagingQueueInstance.q;
    }
    
    public BlockingQueue<E> getQueue() {
        return queue;
    }
    
    public void enqueuePacket(E e) {
        queue.offer(e);
    }
}
