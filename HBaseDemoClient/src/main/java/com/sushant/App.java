package com.sushant;

import java.util.UUID;

import com.sushant.db.interfaces.HBaseNativeNoSqlSvcImpl;
import com.sushant.db.interfaces.NoSqlSvc;
import com.sushant.exception.ApplicationException;


/**
 * Main Class
 *
 */
public class App 
{
	
		
    public static void main( String[] args ) throws Exception
    {
    	exerciseSynchronousHBaseClient();
    }
   
    static void exerciseSynchronousHBaseClient(){
    	NoSqlSvc nss = new HBaseNativeNoSqlSvcImpl();
    	
    	try {
    		System.out.println("Creating Table....");
			nss.createTable();
			System.out.println("Table Created");
		} catch (ApplicationException e) {			
			e.printStackTrace();
		}
    	
    	try {
			System.out.println("Fetching all patients' personal records");
    		nss.fetchAllPersonalRecord();
    		System.out.println("Fetched all patients' personal records");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
    	
    	final String rowKey = UUID.randomUUID().toString() + "#Customer1";
		
    	try {
			System.out.println("Inserting Patient Personal details........");
    		nss.insertPersonalRecord(rowKey, new PatientPersonalDetails("John","Doe"));
    		System.out.println("Inserted Patient Personal details........");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
    	
    	try {
			System.out.println("Fetching patient record with key="+rowKey);
    		nss.fetchPersonalRecord(rowKey);
    		System.out.println("Fetched patient record with key="+rowKey);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
    	
    	try {
    		System.out.println("Deleting patient record with key="+rowKey);
			nss.deletePersonalRecord(rowKey);
			System.out.println("Deleted patient record with key="+rowKey);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
    }
   
        
    
    
    
   
   
    
  
}
