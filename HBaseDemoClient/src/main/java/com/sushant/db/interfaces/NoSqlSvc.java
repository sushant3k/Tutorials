/**
 * 
 */
package com.sushant.db.interfaces;

import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

import com.sushant.PatientPersonalDetails;
import com.sushant.exception.ApplicationException;

/**
 * @author Sushant
 *
 */
public abstract class NoSqlSvc {

	public static final String HBASE_CONFIGURATION_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";
	public static final String HBASE_CONFIGURATION_ZOOKEEPER_CLIENTPORT = "hbase.zookeeper.property.clientPort";
	protected Configuration getHBaseConfiguration()
    {
    	 
    	Configuration conf = HBaseConfiguration.create();
    	conf.set(HBASE_CONFIGURATION_ZOOKEEPER_QUORUM, "sushant-wireless-box");
    	conf.set(HBASE_CONFIGURATION_ZOOKEEPER_CLIENTPORT, "2181"); //Default zookeeper port
    	
    	// Some other configuration properties commented for reference.
 //    	conf.set("hbase.client.retries.number", Integer.toString(1));
//        conf.set("zookeeper.session.timeout", Integer.toString(60000));
//        conf.set("zookeeper.recovery.retry", Integer.toString(1));
    	return conf;
    }
	
	public abstract void createTable() throws ApplicationException;
	public abstract List<PatientPersonalDetails> fetchAllPersonalRecord() throws ApplicationException;
	public abstract void insertPersonalRecord(final String rowKey,  PatientPersonalDetails ppd) throws ApplicationException;
	public abstract PatientPersonalDetails fetchPersonalRecord(final String rowKey) throws ApplicationException;
	public abstract void deletePersonalRecord(final String rowKey) throws ApplicationException;
	protected  void displayPatientsPersonalDetails(List<PatientPersonalDetails> lpd)
    {
    	if (lpd !=null && !lpd.isEmpty())
    	{
    		for (PatientPersonalDetails ppd : lpd)
    		{
    			System.out.println(ppd);
    		}
    	}
    }
}
