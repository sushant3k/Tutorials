/**
 * 
 */
package com.sushant.learning;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ggne0084
 *
 */
public class TestReflection {

	private List<String>  lst = new ArrayList<>();
	public static void main(String args[])
	{
		TestReflection tr = new TestReflection();
		tr.generateJSON();
	//	tr.display();
	}
	
	private void generateJSON()
	{
		JSONObject j = new JSONObject();
		j.setName("fName");
		j.setAddress("fAddress");
		
		List<JSONObject.JSONObjectInner> li = new ArrayList<>();
		
		JSONObject.JSONObjectInner ji = new JSONObject.JSONObjectInner();
		ji.setAbc("abcd3");
		ji.setName1("name1");
		
		li.add(ji);
		
		ji = new JSONObject.JSONObjectInner();
		ji.setAbc("abcdadfasdfadf");
		ji.setName1("name3");		
		li.add(ji);
		
		j.setJsonObjectInner(li);
		
		
		ji = new JSONObject.JSONObjectInner();
		ji.setAbc("sdafadf2314134");
		ji.setName1("name4");		
//		j.setSingleField(ji);
		
		identifyJSONFieldsAndGenerateJSON(j);
		//TestJSONArray.generateJSON(j);
		
	}
	
	private void identifyJSONFieldsAndGenerateJSON(JSONObject j)
	{
		Class<?> cls = j.getClass();
		
		Field fs[] = cls.getDeclaredFields();
		
		for (Field f : fs)
		{
			if (f.isAnnotationPresent(JSONField.class))
			{
				try {
					f.setAccessible(true);
					String s = TestJSONArray.generateJSON(f.get(j));
					System.out.println(s);
					Object obj= TestJSONArray.generateObjectFromJSON(f.getType(), s);
					System.out.println(obj);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void display()
	{
		Field fs[] = this.getClass().getDeclaredFields();
		
		for (Field f : fs)
		{
			System.out.println("-------- Field name -------------- :" + f.getName());
			System.out.println(f.getDeclaringClass());
			System.out.println("primitive: " + f.getType().isPrimitive());
			System.out.println("Array: " + f.getType().isArray());
			System.out.println("Enum: " + f.getType().isEnum());
			System.out.println("Interface: " + f.getType().isInterface());
			System.out.println("LocalClass: " + f.getType().isLocalClass());
			System.out.println("memberClass: " + f.getType().isMemberClass());
			System.out.println("Synthetic: " + f.getType().isSynthetic());			
		}
	}
}
