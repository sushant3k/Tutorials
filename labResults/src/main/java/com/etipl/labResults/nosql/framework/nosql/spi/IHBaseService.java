/**
 * 
 */
package com.etipl.labResults.nosql.framework.nosql.spi;

import java.util.List;

import com.etipl.nosql.framework.exception.HBaseException;

/**
 * @author Sushant
 *
 */
public interface IHBaseService <T> {

	/**
	 * Get all the results from the database
	 * @param t
	 * @return
	 */
	List<T> getResultList(Class<T> t);
	
	/**
	 * Get Single Result
	 * @param t
	 * @return
	 */
	T getSingleResult(Class<T> t, String rowKey);
	
	/**
	 * Store Data in the table
	 * @param t
	 * @return
	 */
	T persist(T t) throws HBaseException;
	
	/**
	 * Update data
	 * @param t
	 * @return
	 */
	T update(T t);
	
	/**
	 * Remove data
	 * @param t
	 * @return
	 */
	void remove(Class<T> t, String rowKey) throws HBaseException;
}
