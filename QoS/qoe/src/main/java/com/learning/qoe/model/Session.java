/**
 * 
 */
package com.learning.qoe.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Sushant
 *
 */
public class Session implements Serializable, Comparable<Session>{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String id;
    
    private String srcIp;
    
    private String destIp;
    
    private int srcPort;
    
    private int destPort;
    
    
    public Session() {
        
    }

    public Session(final String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

    public String getDestIp() {
        return destIp;
    }

    public void setDestIp(String destIp) {
        this.destIp = destIp;
    }

    public int getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(int srcPort) {
        this.srcPort = srcPort;
    }

    public int getDestPort() {
        return destPort;
    }

    public void setDestPort(int destPort) {
        this.destPort = destPort;
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                // if deriving: appendSuper(super.hashCode()).
                append(this.id).
                toHashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Session) {
            Session s = (Session)o;
            return new EqualsBuilder().
                    // if deriving: appendSuper(super.equals(obj)).
                    append(id, s.id).
                    isEquals();
            
        }
        return false;
    }

    @Override
    public int compareTo(Session o) {
        if (this.getId().equals(o.getId())) {
            return 0;
        }
        return 1;

    }
}
