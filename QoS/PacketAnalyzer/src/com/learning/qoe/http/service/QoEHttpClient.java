
package com.learning.qoe.http.service;

import java.util.concurrent.TimeUnit;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;

/**
 * This class is responsible to provide HttpClient instance.
 */
final class QoEHttpClient extends DefaultHttpClient {

	private static final Logger LOGGER = Logger.getLogger(QoEHttpClient.class);

	private static final int MAX_HTTP_CONNECTIONS = 100;
	private static PoolingClientConnectionManager connMgr = new PoolingClientConnectionManager();
	private static final int HTTP_SO_TIME_OUT = 5000;
	private static final int CONNECTION_TIME_OUT = 5000;

	/**
	 * constructor.
	 */
	private QoEHttpClient() {
		super(connMgr);
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIME_OUT);
		HttpConnectionParams.setSoTimeout(params, HTTP_SO_TIME_OUT);
		this.setParams(params);
		attachShutdownHook();
	}

	private void attachShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			/**
			 * To shutdown ThreadPoolExecutor.
			 */
			public void run() {
				try {
					releaseClient();
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}
			}
		});
	}

	static {
		
		connMgr.setDefaultMaxPerRoute(MAX_HTTP_CONNECTIONS);
		LOGGER.debug("Max HTTP Connection Limit Configured = "
				+ MAX_HTTP_CONNECTIONS);
	}

	/**
	 * To instantiate SMAHttpClient.
	 */
	private static class SMAHttpClientInstance {
		public static final QoEHttpClient INSTANCE = new QoEHttpClient();
	}

	/**
	 * Lazily load and get the instance of the SMAHttpClient.
	 * 
	 * @return
	 */
	public static QoEHttpClient getInstance() {
		return SMAHttpClientInstance.INSTANCE;
	}


	/**
	 * This method must be called after each invocation of the the Httpclient.
	 * If this method is not invoked, the connection closing responsibility
	 * would be handed over to the default implementation of the client
	 * connection manager provided by the Apache HttpComponents and this is not
	 * desirable.
	 */
	public void releaseClient() {
		this.getConnectionManager().shutdown();

	}

	public void cleanUnusedConnections()
	{
		connMgr.closeExpiredConnections();
		connMgr.closeIdleConnections(3600000l, TimeUnit.MILLISECONDS);
	}	
	
}

