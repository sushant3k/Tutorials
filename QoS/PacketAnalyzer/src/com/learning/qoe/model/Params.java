/**
 * Licensed to Hughes Systique Corporation
 */
package com.learning.qoe.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstraction of the data.
 * @author Sushant
 *
 */
public class Params{

    /**
     * 
     */
    

    /* Session Id For this Session */
    private String sessionId;
    
    /* TCP Segment Index */
    private String tcpSegmentIndex;
    
    private List<HTTPParams> httpParams = new ArrayList<HTTPParams>();;
    
    private List<TCPParams> tcpParams = new ArrayList<TCPParams>();
    
    private List<TSParams> tsParams = new ArrayList<TSParams>();
    
    public Params() {
        
    }

    
      


    public List<HTTPParams> getHttpParams() {
        return httpParams;
    }





    public void setHttpParams(List<HTTPParams> httpParams) {
        this.httpParams = httpParams;
    }





    public List<TCPParams> getTcpParams() {
        return tcpParams;
    }





    public void setTcpParams(List<TCPParams> tcpParams) {
        this.tcpParams = tcpParams;
    }





    public List<TSParams> getTsParams() {
        return tsParams;
    }





    public void setTsParams(List<TSParams> tsParams) {
        this.tsParams = tsParams;
    }





    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTcpSegmentIndex() {
        return tcpSegmentIndex;
    }

    public void setTcpSegmentIndex(String tcpSegmentIndex) {
        this.tcpSegmentIndex = tcpSegmentIndex;
    }
    
    
    
    
}
