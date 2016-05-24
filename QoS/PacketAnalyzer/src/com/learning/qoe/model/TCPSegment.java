/**
 *  This bean is for storing data for each TCPSegment
 */
package com.learning.qoe.model;

import java.io.Serializable;

/**
 * @author Sushant
 *
 */
public class TCPSegment implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * The time at which the TCP Packet was received
     */
    private long tcpTime;
    
    /**
     * Actual time at which the tcp packet is received at the application.
     */
    private long packetReceivedTimeAtApplication;
    
    public TCPSegment() {
        
    }

    public long getTcpTime() {
        return tcpTime;
    }

    public void setTcpTime(long tcpTime) {
        this.tcpTime = tcpTime;
    }

    public long getPacketReceivedTimeAtApplication() {
        return packetReceivedTimeAtApplication;
    }

    public void setPacketReceivedTimeAtApplication(
            long packetReceivedTimeAtApplication) {
        this.packetReceivedTimeAtApplication = packetReceivedTimeAtApplication;
    }
    
    
}
