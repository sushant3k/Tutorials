/**
 * 
 */
package com.learning.qoe.handlers;

import com.learning.qoe.model.Device;

/**
 * @author Sushant
 *
 */
public class DeviceServiceImpl implements DeviceService{

    final static Device device = new Device();
    
    /**
     * This is just a mock implementation.
     */
    @Override
    public Device getDeviceInformation() {
       return device;
    }

}
