/**
 * A wrapper over the PcapPacket. This contains some more fields that are required for processing.
 */
package com.learning.qoe.lc;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.tcpip.Tcp;

/**
 * @author Sushant
 *
 */
public class WrapperPcapPacket {

    /**
     * PcapPacket instance {@link PcapPacket}
     */
    private PcapPacket packet;
    
    /**
     * Generated Stream Index number
     */
    private long streamIndex;
    
    /**
     * Source IP address
     */
    private String srcIP;
    
    /**
     * Destination address from where the streaming is being done.
     */
    private String destIP;
    
    /**
     * Destination port involved in the TCP socket communication.
     */
    private int destPort;
    
    /**
     * Session ID
     */
    private String sessionId;
    
    /**
     *  {@link Tcp}
     */
    private Tcp tcp;
    /**
     * This variable identifies whether it is request packet from the source machine to the destination machine or not. 
     * A value of true means that the packet is from source machine (i.e. this machine) to destination IP.
     */
    private boolean request = false;
    
    public WrapperPcapPacket(final PcapPacket p, Long si, boolean request, final String sessionId, Tcp tcp) {
        this.packet = p;
        this.streamIndex = si;
        this.sessionId = sessionId;
        this.request = request;
        this.tcp = tcp;
    }
    
    
    public WrapperPcapPacket(final PcapPacket p, Long si, String srcIp, String destIp, int destPort, boolean request) {
        this.packet = p;
        this.streamIndex = si;
        this.srcIP = srcIp;
        this.destIP = destIp;
        this.destPort = destPort;
        this.request = request;
    }
    public WrapperPcapPacket() {
        
    }

    public PcapPacket getPacket() {
        return packet;
    }

    public void setPacket(PcapPacket packet) {
        this.packet = packet;
    }

    public long getStreamIndex() {
        return streamIndex;
    }

    public void setStreamIndex(long streamIndex) {
        this.streamIndex = streamIndex;
    }

    public String getSrcIP() {
        return srcIP;
    }

    public void setSrcIP(String srcIP) {
        this.srcIP = srcIP;
    }

    public String getDestIP() {
        return destIP;
    }

    public void setDestIP(String destIP) {
        this.destIP = destIP;
    }

    public int getDestPort() {
        return destPort;
    }

    public void setDestPort(int destPort) {
        this.destPort = destPort;
    }
    public boolean isRequest() {
        return request;
    }
    public void setRequest(boolean request) {
        this.request = request;
    }


    public Tcp getTcp() {
        return tcp;
    }


    public void setTcp(Tcp tcp) {
        this.tcp = tcp;
    }


    public String getSessionId() {
        return sessionId;
    }


    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    
    
}
