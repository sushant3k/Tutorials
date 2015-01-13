/**
 * 
 */
package com.learning.ant.build.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.JavaCore;

/**
 * @author Sushant Jain
 *
 */
public class GenericUtilitiesImpl implements IGenericUtilities{

	private static List<String> variableList;
	static
	{
		
		if (variableList == null)
		{
			variableList = new ArrayList<String>();
			variableList.add("JRE_SRC");
			variableList.add("ECLIPSE_HOME");
			variableList.add("M2_REPO");
			variableList.add("JUNIT_HOME");
			variableList.add("JRE_LIB");
			variableList.add("JUNIT_SRC_HOME");			
			variableList.add("JRE_SRCROOT");
			
		}
	}
	@Override
	public Map<String, IPath> getRelevantClassPathVariables() {
		Map<String, IPath> mp = null;
		String cp [] = JavaCore.getClasspathVariableNames();
		for(String s : cp)
		{
			if (mp == null)
				mp = new HashMap<String, IPath>();
			if (!variableList.contains(s))
				mp.put(s,JavaCore.getClasspathVariable(s));
		}
		return mp;
	}

	@Override
	public IPath getClassPathVariableValue(String classpathVariable) {
		if (classpathVariable == null)
		{
			throw new IllegalArgumentException("Classpath variable cannot be null");
		}
		IPath i = JavaCore.getClasspathVariable(classpathVariable);
		return i;
	}

	@Override
	public Map<String,IPath> getAllClassPathVariables() {
		Map<String, IPath> mp = null;
		String cp [] = JavaCore.getClasspathVariableNames();
		for(String s : cp)
		{
			if (mp == null)
				mp = new HashMap<String, IPath>();
			mp.put(s,JavaCore.getClasspathVariable(s));
		}
		return mp;
	}
	


}
