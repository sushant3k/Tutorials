/**
 * 
 */
package com.learning.qoeweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.learning.qoe.entities.SessionRecordsEntity;
import com.learning.qoe.model.DeviceSession;
import com.learning.qoe.utils.TopicResolutionService;

/**
 * @author Sushant
 *
 */
@Service
public class WebSocketMessageSenderServiceImpl implements WebSocketMessageSenderSvc{

    @Autowired
    private SimpMessagingTemplate template;
    
    @Autowired
    private JmsTemplate jmsTemplate;
    
    @Autowired
    private GenericService genericService;
    
    public WebSocketMessageSenderServiceImpl() {
        
    }
    
    public synchronized void sendMessageToSubscribedClients(SessionRecordsEntity se) {
        final String sessionId = se.getQoePacket().getSessionId();
        final String deviceId = se.getQoePacket().getDevice().getDeviceIdentifier();
        
        final DeviceSession ds = genericService.findByDeviceAndSession(deviceId, sessionId);
        final String key = "topic-"+sessionId+"-"+deviceId;
        
        String topic = TopicResolutionService.getInstance().getTopic(key);
        
        
        if (topic == null) {
            topic = "/topic/"+sessionId+deviceId;            
        }
        
        template.convertAndSend(topic, ds);
        
        TopicResolutionService.getInstance().addTopic(key, topic);
                       
    }
    

    
    public void test(DeviceSession ds) {
        
        template.convertAndSend("/topic/message",ds);
        return;
//        
//        Topic topic = TopicResolutionService.getInstance().getTopic("Test");
//        if (topic == null) {
//            TopicConnectionFactory tcf = (TopicConnectionFactory)jmsTemplate.getConnectionFactory();
//            try {
//                javax.jms.Session sess = tcf
//                        .createConnection()
//                        .createSession(false,
//                                javax.jms.Session.AUTO_ACKNOWLEDGE);
////                topic = sess.createTopic("topic/" + sessionId + deviceId);                
//                topic = sess.createTopic("topic/message");
//                
//            } catch (JMSException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        
//        TopicResolutionService.getInstance().addTopic("Test", topic);
//        jmsTemplate.send(topic, new MessageCreator() {
//
//            @Override
//            public Message createMessage(Session session)
//                    throws JMSException {
//                ObjectMessage obj = session.createObjectMessage(ds);
//                return obj;
//            }
//            
//        });
//               
    }
    
    
}
