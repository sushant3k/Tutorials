/**
 * 
 */
package com.learning.qoeweb.service;

import com.learning.qoe.model.QoEPacket;

/**
 * @author Sushant
 *
 */
public interface PacketEnqueueSvc {

    void enqueuePacket(QoEPacket packet);
}
