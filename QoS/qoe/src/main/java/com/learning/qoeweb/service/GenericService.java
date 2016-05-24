/**
 * 
 */
package com.learning.qoeweb.service;

import com.learning.qoe.entities.SessionRecordsEntity;
import com.learning.qoe.model.DeviceSession;
import com.learning.qoe.model.Devices;
import com.learning.qoe.model.QoEPacket;
import com.learning.qoe.model.Session;
import com.learning.qoe.model.Sessions;

/**
 * @author Sushant
 *
 */
public interface GenericService {

    Sessions getAllSessions();
    void save(QoEPacket packet);
    Sessions findBySessionId(String sessionId);
    Session findBySessionIdAndStreamId(String sessionId, long streamIndex);
    Devices findAllDevices();
    SessionRecordsEntity findDetailsByDeviceId(String deviceId);
    DeviceSession findByDeviceAndSession(String deviceId, String sessionId);
}
