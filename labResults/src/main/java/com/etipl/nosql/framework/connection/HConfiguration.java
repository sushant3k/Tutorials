package com.etipl.nosql.framework.connection;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

public class HConfiguration {

//	private Lock lock = new ReentrantLock();
	private Configuration config;
	public static final String HBASE_CONFIGURATION_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";
	public static final String HBASE_CONFIGURATION_ZOOKEEPER_CLIENTPORT = "hbase.zookeeper.property.clientPort";
	private HConfiguration()
	{
		config = HBaseConfiguration.create();
		loadDefaultProperties();
	}
	
	private void loadDefaultProperties()
	{
		config.set(HBASE_CONFIGURATION_ZOOKEEPER_QUORUM, "sushant-wireless-box");
    	config.set(HBASE_CONFIGURATION_ZOOKEEPER_CLIENTPORT, "2181"); //Default zookeeper port
	}
//	public void addConfigurationProperties(Properties props)
//	{
//		
//		if (config == null)
//		{
//			lock.tryLock();
//			config = HBaseConfiguration.create();
//			if (props != null)
//			{
//				for (Map.Entry<Object, Object> map : props.entrySet())
//				{
//					config.set(map.getKey().toString(), map.getValue().toString());
//				}
//			}
//			lock.unlock();
//		}
//		
////    	conf.set(HBASE_CONFIGURATION_ZOOKEEPER_QUORUM, "sushant-wireless-box");
////    	conf.set(HBASE_CONFIGURATION_ZOOKEEPER_CLIENTPORT, "2181"); //Default zookeeper port
//	}
	private static class Instance
	{
		private static final HConfiguration configuration = new HConfiguration();
	}
	
	public static HConfiguration getConfiguration()
	{
		return Instance.configuration;
	}
	public Configuration getHBaseConfiguration()
    {
    	 
    	return config;
    }
}
