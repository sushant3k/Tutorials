/**
 * 
 */
package com.learning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.learning.qoe.entities.SessionRecordsEntity;
import com.learning.qoe.model.DeviceSession;
import com.learning.qoe.model.Devices;
import com.learning.qoe.model.QoEPacket;
import com.learning.qoe.model.Sessions;
import com.learning.qoe.model.Status;
import com.learning.qoeweb.service.GenericService;
import com.learning.qoeweb.service.PacketEnqueueSvc;

/**
 * @author Sushant
 *
 */
@RestController
public class QoEController {

    @Autowired
    private PacketEnqueueSvc enqueuSvc;
    
    
    @Autowired
    private GenericService genericService;
    
    
    @RequestMapping(value="/api/v1/packets", 
            method = { RequestMethod.POST }, 
            consumes = {"application/json"},
            produces = {"application/json"})
    public Status savePacket(@RequestBody QoEPacket packet) {
        enqueuSvc.enqueuePacket(packet);
        Status st = new Status("Message Received");
        return st;
    }
    
    @RequestMapping(value="/api/v1/sessions", 
            method = { RequestMethod.GET },             
            produces = {"application/json"})
    public Sessions getAllSession() {
        return genericService.getAllSessions();        
    }
    
    @RequestMapping(value="/api/v1/devices", 
            method = { RequestMethod.GET },             
            produces = {"application/json"})
    public Devices getAllDevices() {
        return genericService.findAllDevices();
    }
    
    @RequestMapping(value="/api/v1/devices/{deviceId}", 
            method = { RequestMethod.GET },             
            produces = {"application/json"})
    public SessionRecordsEntity getAllDevicesByDeviceId(@PathVariable String deviceId) {
        return genericService.findDetailsByDeviceId(deviceId);
    }
    
    @RequestMapping(value="/api/v1/devices/{deviceId}/sessions/{sessionId}", 
            method = { RequestMethod.GET },             
            produces = {"application/json"})
    
    public DeviceSession getByDeviceAndSession(@PathVariable String deviceId, @PathVariable String sessionId) {
        return genericService.findByDeviceAndSession(deviceId, sessionId);
    }
}
