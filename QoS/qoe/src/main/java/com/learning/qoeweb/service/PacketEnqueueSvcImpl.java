/**
 * 
 */
package com.learning.qoeweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.qoe.model.QoEPacket;

/**
 * @author ggne0084
 *
 */
@Service
public class PacketEnqueueSvcImpl implements PacketEnqueueSvc{

    @Autowired private GenericService genericService;
    
    @Override
    public void enqueuePacket(QoEPacket packet) {
        // TODO Auto-generated method stub
        genericService.save(packet);
    }

}
