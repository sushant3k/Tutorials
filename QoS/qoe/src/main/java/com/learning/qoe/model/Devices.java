/**
 * 
 */
package com.learning.qoe.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Sushant
 *
 */
public class Devices implements Serializable{

    private List<Device> devices;
    
   
    public Devices() {
        
    }

    public void populateDevices(Map<String, Device> mp) {
        if (mp == null || mp.isEmpty()) {
            return ;
        }
        for (Map.Entry<String, Device> mp1 : mp.entrySet()) {
            this.addDevice(mp1.getValue());
        }
    }
    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
    
    public void addDevice(Device d) {
        if (devices == null) {
            devices = new ArrayList<Device>();
        }
        devices.add(d);
    }
}
