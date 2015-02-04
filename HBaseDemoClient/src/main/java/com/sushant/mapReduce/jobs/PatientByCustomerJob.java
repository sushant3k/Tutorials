/**
 * 
 */
package com.sushant.mapReduce.jobs;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;


/**
 * @author Sushant
 *
 */
public class PatientByCustomerJob {

	public static class PatientToCustomerMapper extends TableMapper<Text, Text> {

		  public void map(ImmutableBytesWritable row, Result value, Context context) throws InterruptedException, IOException {
		    // process data for the row from the Result instance.
			  System.out.println(row);
			  System.out.println(value);
			  System.out.println(context);
		   }
	}
	
	public static class PatientToCustomerReducer extends TableReducer<Text, IntWritable, Text> {
		
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException
		{
			
		}
	}
}
