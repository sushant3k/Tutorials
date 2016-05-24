/**
 * HTTP Message Sender implementation.
 */
package com.learning.qoe.http.service;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.learning.qoe.exception.QoEHttpException;
import com.learning.qoe.model.QoEPacket;
import com.learning.qoe.utils.JSONUtils;
import com.learning.qoe.utils.QoEConstants;
import com.learning.qos.configuration.QoEConfiguration;

/**
 * This is an implementation of the SoapRequestMessageCreatorIf. 
 * It creates the desired type of SOAP message.
 */
public class HttpMessageSenderImpl<T> implements HttpMessageSenderIf<T> {

	private static final Logger LOGGER = Logger
			.getLogger(HttpMessageSenderImpl.class);

	private static final QoEHttpClient HTTP_CLIENT = QoEHttpClient
			.getInstance();

	private static String serverURI = QoEConfiguration.getInstance().getConfigProperty(QoEConstants.SERVER_URL);

	/**
	 * Default Constructor
	 */
	public HttpMessageSenderImpl() {

	}

	
	@Override
	public String sendMsg(T qoePacket) throws QoEHttpException {
	    System.out.println("Sending Message");
		HttpPost post = null;
		try {
			
			post = new HttpPost(serverURI);
			
			HttpEntity se = new StringEntity(JSONUtils.objectToJson(qoePacket),
					ContentType.APPLICATION_JSON);
			post.setEntity(se);
			HttpResponse response = HTTP_CLIENT.execute(post);
			
			LOGGER.info("HTTP response recieved :: " + response);
			final int responseStatus = response.getStatusLine().getStatusCode();
			String responseEntityString = EntityUtils.toString(response.getEntity());
			LOGGER.debug("HTTP Response entity: " + responseEntityString);
			if (responseStatus == HttpURLConnection.HTTP_OK) {
				return responseEntityString;
			}
		} catch (ClientProtocolException e) {
			LOGGER.error(e);
			throw new QoEHttpException(
					"Connection to server failed", e);
		} catch (IOException e) {
			LOGGER.error(e);
			throw new QoEHttpException(
					"Some IO Error", e);
		} finally {
			if ( post != null){
				post.releaseConnection();
			}
			LOGGER.info("Http Connection released...");
		}
		return null;
	}

	
}
