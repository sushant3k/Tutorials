/**
 * 
 */
package com.learning.qoe.model;

import java.util.Random;

/**
 * @author ggne0084
 *
 */
public class Device {

    private String name = "dName";
    private String type = "Android";
    private String osVersion = "4.0";
    private String network = "3G";
    
    private float latitude = 28.6f;
    private float longitude = 77.23f ;
    
    private String deviceIdentifier = null;
    public Device() {
        if (deviceIdentifier == null || deviceIdentifier.isEmpty()) {
            deviceIdentifier = "IMEI-1234-STATIC-VALUE" + ((new Random()).nextInt((10 - 1)+1)+1)+""; 
        }
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
    
    
    
}
