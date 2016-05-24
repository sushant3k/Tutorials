/**
 * 
 */
package com.learning.qoe.message.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.learning.qoe.constants.QoeConstants;
import com.learning.qoe.entities.SessionRecordsEntity;
import com.learning.qoe.model.QoEPacket;
import com.learning.qoe.repositories.SessionRecordsEntityRepository;
import com.learning.qoeweb.service.WebSocketMessageSenderSvc;

/**
 * @author Sushant
 *
 */
@Component
public class PacketListener {

    @Autowired
    private SessionRecordsEntityRepository qoeRecordsRepository;
    
    @Autowired
    private WebSocketMessageSenderSvc webSocketMessageSenderSvc;
    
    @JmsListener(destination = QoeConstants.PACKET_QUEUE)
    public void receiveQoePacket(QoEPacket packet) {
        SessionRecordsEntity se = qoeRecordsRepository.save(new SessionRecordsEntity(packet));
        webSocketMessageSenderSvc.sendMessageToSubscribedClients(se);
    }
}
