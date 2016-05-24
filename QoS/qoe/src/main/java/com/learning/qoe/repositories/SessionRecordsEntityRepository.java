/**
 * 
 */
package com.learning.qoe.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.learning.qoe.entities.SessionRecordsEntity;
/**
 * @author Sushant
 *
 */

public interface SessionRecordsEntityRepository extends MongoRepository<SessionRecordsEntity, String> {

    
//    List<SessionRecordsEntity> findAllOrderByQoePacketDeviceDeviceIdentifierAsc();
    
    @Query(value="{ 'sessionId' : ?0 } ")
    public List<SessionRecordsEntity> findBySessionId(String sessionId);
    
    @Query(value="{ 'sessionId' : ?0, 'streamIndex' : ?1 } ")
    SessionRecordsEntity findBySessionIdAndStreamIndex(String sessionId, long streamIndex);
    
    
    
//    @Query(value="{ 'qoePacket.device.deviceIdentifier' : ?0 } ")
    List<SessionRecordsEntity> findByQoePacketDeviceDeviceIdentifier(String deviceIdentifier);
    
    List<SessionRecordsEntity> findByQoePacketSessionIdAndQoePacketDeviceDeviceIdentifierOrderByQoePacketFirstPacketReceivedTimeAsc(String sessionId, String deviceIdentifier);
    
//    @Query(fields="{ 'device' : 1 }")
//    List<SessionRecordsEntity> findAllByQoePacket_Device();
    
//    @Query(fields="{'sessionId':1, 'streamIndex' : 1, 'sourceIp' : 1, 'sourcePort' : 1 , 'destIp' : 1, 'destPort' : 1}")
//    public List<SessionRecordsEntity> findAll();
}
