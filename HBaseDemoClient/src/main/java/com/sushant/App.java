package com.sushant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.sushant.common.ApplicationConstants;
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
    	Set<String> columnFamilies = null;
    	try {
    		System.out.println("Creating Patient Table");
    		columnFamilies = new HashSet<String>();
    		columnFamilies.add(ApplicationConstants.DatabaseConstants.CF_PERSONAL);
    		columnFamilies.add(ApplicationConstants.DatabaseConstants.CF_LABRESULTS);    		
			nss.createAnyTable(ApplicationConstants.DatabaseConstants.TABLE_NAME, columnFamilies);
			System.out.println("Patient Table Created");
		} catch (ApplicationException e) {			
			e.printStackTrace();
		}
    	
    	try
    	{
    		System.out.println("Creating Lab Results Table");
    		columnFamilies = new HashSet<String>();
    		columnFamilies.add(ApplicationConstants.DatabaseConstants.CF_RESULTS);
    		nss.createAnyTable(ApplicationConstants.DatabaseConstants.LABRESULTS_TABLE_NAME, columnFamilies);
    	}
    	catch(ApplicationException e) {
    		e.printStackTrace();
    	}
    	
    	try {
			System.out.println("Fetching all patients' personal records");
    		nss.fetchAllPersonalRecord();
    		System.out.println("Fetched all patients' personal records");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
    	
    	String rowKey = UUID.randomUUID().toString() + "#Customer1";
		
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
    	
    	
    	try {
			System.out.println("Generating Bulk Data");
    		nss.generateBulkData();
    		System.out.println("Generated Bulk Data");
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}
    	
    	
    	try {
    		System.out.println("Getting all patients with the first name starting with first");
			nss.getAllPatientswithARegexFilteronFirstName("First.");
			System.out.println("Got all patients with the first name starting with first");
		} catch (ApplicationException e1) {			
			e1.printStackTrace();
		}
    	try {
			System.out.println("Running Map Reduce Task");
    		nss.runMapReduce();
    		System.out.println("Map Reduce Task Complete");
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}
    	
    	
    	rowKey = UUID.randomUUID().toString() + "#Customer1";
    	
    	List<LabResults> lpd = new ArrayList<LabResults>();
    	lpd.add(new LabResults("TMT", "Pass"));
    	lpd.add(new LabResults("HLD/LLD", "In-Range"));
    	lpd.add(new LabResults("BP", "85/130"));
    	
    	try {
    		System.out.println("Storing lab results for Patient="+rowKey);
			nss.insertLabResultsForPatient(rowKey, new PatientPersonalDetails("Sushant-1", "Jain-11"), lpd);
			System.out.println("Stored lab results for Patient="+rowKey);
		} catch (ApplicationException e) {
			 
			e.printStackTrace();
		}
    	
    	try {
    		
    		System.out.println("-------------------");
    		System.out.println("displaying Lab Result for Patient="+rowKey);
    		long curTime = System.currentTimeMillis();
    		nss.displayLabResultsForPaient(rowKey);
    		System.out.println((System.currentTimeMillis()-curTime));
    		System.out.println("-------------------");
    		System.out.println("Displayed lab results for Patient="+rowKey);
    	}
    	catch(ApplicationException e)
    	{
    		e.printStackTrace();
    	}
    	
    }
   
    
}
