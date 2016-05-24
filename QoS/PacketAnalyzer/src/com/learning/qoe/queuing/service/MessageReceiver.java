/**
 *  A thread based MessageReceiveer that when receives messages from queue, 
 *  sends the packet to the server
 */
package com.learning.qoe.queuing.service;

import java.util.concurrent.BlockingQueue;

import com.learning.qoe.exception.QoEHttpException;
import com.learning.qoe.handlers.DeviceService;
import com.learning.qoe.handlers.DeviceServiceImpl;
import com.learning.qoe.http.service.HttpMessageSenderIf;
import com.learning.qoe.http.service.HttpMessageSenderImpl;
import com.learning.qoe.model.QoEPacket;

/**
 * @author Sushant
 *
 */
public class MessageReceiver implements Runnable{

    public MessageReceiver() {
        
    }
    
    private static class MessageReceiverInstance {
        private static MessageReceiver receiver = new MessageReceiver();
    }
    public static MessageReceiver getInstance() {
        return MessageReceiverInstance.receiver;
    }
    
    public void recieveMessage() {
     
    }

    @Override
    public void run() {
        MessagingQueue<QoEPacket> mq = MessagingQueue.getInstance();
        BlockingQueue<QoEPacket> qp = mq.getQueue();
        while (true) {
            DeviceService dsif = new DeviceServiceImpl();
            QoEPacket q;
            try {                
                q = qp.take();
                q.setDevice(dsif.getDeviceInformation());
                
                HttpMessageSenderIf<QoEPacket> hsif = new HttpMessageSenderImpl<QoEPacket>();
                hsif.sendMsg(q);
            } catch (InterruptedException | QoEHttpException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
                        
        }
    }
}
