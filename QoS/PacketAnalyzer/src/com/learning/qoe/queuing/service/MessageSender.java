/**
 * Enqueue a Packet
 */
package com.learning.qoe.queuing.service;


/**
 * @author Sushant
 *
 */
class MessageSender<T> {

    private MessageSender() {
        
    }
    private static class MessageSenderInstance {
        private static MessageSender sender = new MessageSender();
    }
    
    public static MessageSender getInstance() {
        return MessageSenderInstance.sender;
    }
    
    public void enqueueMessage(T qp) {
        MessagingQueue<T> mq = MessagingQueue.getInstance();
        mq.enqueuePacket(qp);
    }
}
