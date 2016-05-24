/**
 * 
 */
package com.learning.qoe.queuing.service;


/**
 * @author Sushant
 *
 */
public class EnqueueServiceImpl<T> implements EnqueueService<T>{

    
    public EnqueueServiceImpl() {

    }
    
    public void enqueue(T qoe) {

        MessageSender.getInstance().enqueueMessage(qoe);
        
    }

}
