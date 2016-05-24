/**
 * HTTP Message Sender
 */
package com.learning.qoe.http.service;

import com.learning.qoe.exception.QoEHttpException;




public interface HttpMessageSenderIf<T> {

	/**
	 * Send message to the server using HTTP
	 * @param qoePacket
	 * @return
	 * @throws QoEHttpException
	 */
	String sendMsg(final T qoePacket) throws QoEHttpException;
}
