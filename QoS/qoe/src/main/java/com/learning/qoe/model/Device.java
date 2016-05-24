/**
 * 
 */
package com.learning.qoe.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Sushant
 *
 */
public class Device implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private String type;
    private String osVersion;
    private String network;
    
    private float latitude;
    private float longitude;
    
    private String deviceIdentifier;
    
    private Set<Session> session;
    public Device() {
        
    }

    
    
    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }



    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }



    public Set<Session> getSession() {
        return session;
    }



    public void setSession(Set<Session> session) {
        this.session = session;
    }
    
    public void addSession(Session sess) {
        if (sess == null) {
            return;
        }
        if (session == null) {
            session = new HashSet<Session>();
        }
        session.add(sess);
    }
    
}
