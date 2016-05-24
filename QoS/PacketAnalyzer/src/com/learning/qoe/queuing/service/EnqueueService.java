/**
 * Enqueue content. Being used for enqueueing the Packets to be sent to the server. 
 */
package com.learning.qoe.queuing.service;

/**
 * @author Sushant
 *
 */
public interface EnqueueService <T> {

    void enqueue(T t);
}

