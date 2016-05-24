/**
 * Class that reads the WrapperPacket content and delegates to the request and response handlers.
 */
package com.learning.qoe.lc;

import java.util.concurrent.BlockingQueue;

import com.learning.qoe.handlers.TcpPacketHandler;

/**
 * @author Sushant
 *
 */
public class IndividualPacketReceiver implements Runnable{

    /**
     *  Queue of {@link WrapperPcapPacket} 
     */
    private BlockingQueue<WrapperPcapPacket> queue;
    
    /**
     *  {@link TcpPacketHandler}
     */
    private TcpPacketHandler tcpPacketHandler = null;
    
    
    public IndividualPacketReceiver(BlockingQueue<WrapperPcapPacket> q) {
        this.queue = q;
    }
    public void run() {
     
        while(true) {
            try {                
               WrapperPcapPacket wpp =  queue.take();
               if (tcpPacketHandler == null) {
                   tcpPacketHandler = new TcpPacketHandler(wpp.getSessionId());
               }
               if (wpp.isRequest()) {
                   tcpPacketHandler.analyzeRequest(wpp.getStreamIndex(), wpp.getPacket(), wpp.getTcp());
               }
               else {
                   tcpPacketHandler.analyzeResponse(wpp.getStreamIndex(), wpp.getPacket(), wpp.getTcp());
               }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
