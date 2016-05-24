package com.learning.qoe.entities;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import com.learning.qoe.model.QoEPacket;



public class SessionRecordsEntity implements Serializable{

    @Id
    private String id;
    
    
    private QoEPacket qoePacket;
    
    public SessionRecordsEntity() {
        
    }
    public SessionRecordsEntity(QoEPacket qoePacket) {
        this.qoePacket = qoePacket;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public QoEPacket getQoePacket() {
        return qoePacket;
    }
    public void setQoePacket(QoEPacket qoePacket) {
        this.qoePacket = qoePacket;
    }
    
    
}
