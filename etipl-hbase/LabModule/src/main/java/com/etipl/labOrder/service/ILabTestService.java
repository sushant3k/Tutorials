/**
 * 
 */
package com.etipl.labOrder.service;

import java.util.List;

import com.etipl.labOrder.model.LabTest;

/**
 * @author Sushant
 *
 */
public interface ILabTestService  {

	List<LabTest> getAllTests();
	LabTest getTestById(final String testId);
	
}