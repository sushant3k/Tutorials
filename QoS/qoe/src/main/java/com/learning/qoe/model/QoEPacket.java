/**
 * 
 */
package com.learning.qoe.model;

import java.io.Serializable;

/**
 * @author Sushant
 *
 */
public class QoEPacket implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    
    private Long firstPacketReceivedTime;     
    private Long lastPacketReceivedTime;
    private String  sessionId;
    private long streamIndex;
    
    private int clientBandWidth;
    private int retransmissionCount;
    private int packetsDropped;
    
    private int numberOfConnectionResets;
    
    private Params params;
    
    private int dupAcks;
    private int outOfOrder;
    
    private String sourceIp;
    
    private String destIp;
    
    private int sourcePort;
    
    private int destPort;
    
    private Device device;
    
    private long totalNumberOfBytes;
    
    public long getTotalNumberOfBytes() {
        return totalNumberOfBytes;
    }


    public void setTotalNumberOfBytes(long totalNumberOfBytes) {
        this.totalNumberOfBytes = totalNumberOfBytes;
    }
    
    public QoEPacket() {
        
    }

    
    public Params getParams() {
        return params;
    }


    public void setParams(Params params) {
        this.params = params;
    }


    public Device getDevice() {
        return device;
    }


    public void setDevice(Device device) {
        this.device = device;
    }


    public int getOutOfOrder() {
        return outOfOrder;
    }


    public void setOutOfOrder(int outOfOrder) {
        this.outOfOrder = outOfOrder;
    }


    public int getDupAcks() {
        return dupAcks;
    }


    public void setDupAcks(int dupAcks) {
        this.dupAcks = dupAcks;
    }


    public int getRetransmissionCount() {
        return retransmissionCount;
    }


    public void setRetransmissionCount(int retransmissionCount) {
        this.retransmissionCount = retransmissionCount;
    }


    public int getPacketsDropped() {
        return packetsDropped;
    }


    public void setPacketsDropped(int packetsDropped) {
        this.packetsDropped = packetsDropped;
    }


    public int getNumberOfConnectionResets() {
        return numberOfConnectionResets;
    }


    public void setNumberOfConnectionResets(int numberOfConnectionResets) {
        this.numberOfConnectionResets = numberOfConnectionResets;
    }



    public Long getFirstPacketReceivedTime() {
        return firstPacketReceivedTime;
    }

    public void setFirstPacketReceivedTime(Long firstPacketReceivedTime) {
        this.firstPacketReceivedTime = firstPacketReceivedTime;
    }

    public Long getLastPacketReceivedTime() {
        return lastPacketReceivedTime;
    }

    public void setLastPacketReceivedTime(Long lastPacketReceivedTime) {
        this.lastPacketReceivedTime = lastPacketReceivedTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getStreamIndex() {
        return streamIndex;
    }

    public void setStreamIndex(long streamIndex) {
        this.streamIndex = streamIndex;
    }

    public int getClientBandWidth() {
        return clientBandWidth;
    }

    public void setClientBandWidth(int clientBandWidth) {
        this.clientBandWidth = clientBandWidth;
    }
    
  

    public String getSourceIp() {
        return sourceIp;
    }


    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }


    public String getDestIp() {
        return destIp;
    }


    public void setDestIp(String destIp) {
        this.destIp = destIp;
    }


    public int getSourcePort() {
        return sourcePort;
    }


    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }


    public int getDestPort() {
        return destPort;
    }


    public void setDestPort(int destPort) {
        this.destPort = destPort;
    }
    
    
}
