package com.learning.qoeweb.service;

import com.learning.qoe.entities.SessionRecordsEntity;
import com.learning.qoe.model.DeviceSession;

public interface WebSocketMessageSenderSvc {

    void sendMessageToSubscribedClients(SessionRecordsEntity se);
    void test(DeviceSession ds);
}
